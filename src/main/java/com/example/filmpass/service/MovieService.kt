package com.example.filmpass.service

import com.example.filmpass.dto.DailyBoxOfficeDto
import com.example.filmpass.dto.MovieInfoResponse
import com.example.filmpass.dto.MovieResponseDTO
import com.example.filmpass.entity.Movie
import com.example.filmpass.repository.MovieRepository
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.hibernate.query.sqm.tree.SqmNode.log
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.client.WebClient
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

@Service
@Transactional
class MovieService(
    private val webClient: WebClient,
    private val movieRepository: MovieRepository,
    private val webClientKMDB: WebClient
) {
    // 일일박스오피스 API에서 1위부터 10위까지의 영화를 불러오는 코드
    // 일일박스오피스 API에서 1위부터 10위까지의 영화를 불러오는 코드
    fun getDailyBoxOffice(apiKey: String, targetDate: String): List<DailyBoxOfficeDto> {
        print("INFO: Fetching daily box office data for target date: $targetDate")

        val dailyBoxOfficeDtos = webClient.get()
            .uri { uriBuilder ->
                uriBuilder
                    .path("/boxoffice/searchDailyBoxOfficeList.json")
                    .queryParam("key", apiKey)
                    .queryParam("targetDt", targetDate)
                    .build()
            }
            .retrieve()
            .bodyToMono(MovieResponseDTO::class.java)
            .map { it.boxOfficeResult?.dailyBoxOfficeList }
            .block()

        print("INFO: Fetched daily box office data: $dailyBoxOfficeDtos")

        // 상세 정보 API 호출 및 저장
        dailyBoxOfficeDtos?.forEach { dailyBoxOfficeDto ->
            dailyBoxOfficeDto.movieCd?.let { movieCd ->  // movieCd가 null이 아닐 때만 처리
                print("INFO: Fetching movie details for movieCd: $movieCd")

                val movieDTO = webClient.get()
                    .uri { uriBuilder ->
                        uriBuilder
                            .path("/movie/searchMovieInfo.json")
                            .queryParam("key", apiKey)
                            .queryParam("movieCd", movieCd)
                            .build()
                    }
                    .retrieve()
                    .bodyToMono(MovieInfoResponse::class.java)
                    .block()

                movieDTO?.toEntity()?.let { movie ->
                    if (!movie.movieCd?.let { movieRepository.existsByMovieCd(it) }!!) {
                        movie.directorName?.let { directorName ->
                            updateMovieWithPosterAndPlot(movie, directorName)
                        } ?: print("WARN: Director name is null for movieCd $movieCd")

                        movieRepository.save(movie)
                        print("INFO: Saved movie to DB: $movie")
                    } else {
                        print("WARN: Movie with movieCd ${movie.movieCd} already exists in DB")
                    }
                }
            }
        }
        return dailyBoxOfficeDtos ?: emptyList()
    }


    // 영화 제목으로 줄거리와 포스터 가져오는 코드
    fun updateMovieWithPosterAndPlot(movie: Movie, directorName: String) {
        print("INFO: Starting poster and plot update for movie: ${movie.movieName}")
        try {
            val kmdbApiKey = "P2428ABO640DWR015TI4"
            val urlBuilder = StringBuilder("http://api.koreafilm.or.kr/openapi-data2/wisenut/search_api/search_json2.jsp")
                .append("?${URLEncoder.encode("ServiceKey", "UTF-8")}=$kmdbApiKey")
                .append("&${URLEncoder.encode("collection", "UTF-8")}=kmdb_new2")
                .append("&${URLEncoder.encode("title", "UTF-8")}=${URLEncoder.encode(movie.movieName ?: "", "UTF-8")}") // Null을 빈 문자열로 대체
                .append("&${URLEncoder.encode("detail", "UTF-8")}=Y")
                .append("&${URLEncoder.encode("format", "UTF-8")}=json")

            val url = URL(urlBuilder.toString())
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.setRequestProperty("Content-type", "application/json")

            val rd = if (conn.responseCode in 200..300) {
                BufferedReader(InputStreamReader(conn.inputStream))
            } else {
                BufferedReader(InputStreamReader(conn.errorStream))
            }

            val sb = StringBuilder()
            rd.useLines { lines -> lines.forEach { sb.append(it) } }
            conn.disconnect()

            val movieDetails = sb.toString()
            print("INFO: KMDB API Response: $movieDetails")

            val movieInfo = parseMovieDetailsForDirector(movieDetails, directorName)

            movieInfo?.let {
                val firstPoster = it.poster?.split("|")?.get(0) ?: ""
                movie.poster = firstPoster
                movie.plot = it.plot ?: ""
                print("INFO: Updated movie with poster: ${movie.poster}, plot: ${movie.plot}")

                movieRepository.save(movie)
                print("INFO: Movie updated in DB: ${movie.movieName}")
            } ?: print("WARN: No movie details found in KMDB for: ${movie.movieName}")
        } catch (e: Exception) {
            print("ERROR: Error while updating movie with poster and plot for: ${movie.movieName}, ${e.message}")
        }
    }

    // JSON 파싱 메서드 (특정 감독으로 필터링하여 포스터와 줄거리만 추출)
    private fun parseMovieDetailsForDirector(movieDetails: String, targetDirectorName: String): MovieInfoResponse.MovieInfoResponseKMDB? {
        return try {
            val objectMapper = ObjectMapper()
            val root: JsonNode = objectMapper.readTree(movieDetails)
            val dataArray = root.path("Data")

            if (dataArray.isMissingNode || !dataArray.isArray || dataArray.size() == 0) {
                print("WARN: No 'Data' array found in the response.")
                return null
            }

            for (movieNode in dataArray) {
                val resultArray = movieNode.path("Result")
                for (resultNode in resultArray) {
                    val directorsNode = resultNode.path("directors").path("director")
                    if (!directorsNode.isMissingNode && directorsNode.isArray && directorsNode.size() > 0) {
                        for (directorNode in directorsNode) {
                            val directorFromAPI = directorNode.path("directorNm").asText().trim()
                            print("INFO: Comparing director: '$directorFromAPI' with '$targetDirectorName'")
                            if (directorFromAPI.equals(targetDirectorName.trim(), ignoreCase = true)) {
                                val poster = resultNode.path("posters").asText("")
                                val plot = resultNode.path("plots").path("plot").get(0).path("plotText").asText("")

                                return MovieInfoResponse.MovieInfoResponseKMDB(poster = poster, plot = plot)
                            }
                        }
                    } else {
                        print("WARN: No directors found in the result node.")
                    }
                }
            }
            print("WARN: No matching director found in the results for: $targetDirectorName")
            null
        } catch (e: Exception) {
            print("ERROR: Error while parsing movie details JSON, ${e.message}")
            null
        }
    }

    fun updateDailyBoxOfficeWithDetails(apiKey: String, targetDate: String): List<DailyBoxOfficeDto> {
        val dailyBoxOfficeList = getDailyBoxOffice(apiKey, targetDate)

        dailyBoxOfficeList.forEach { dailyBoxOfficeDto ->
            dailyBoxOfficeDto.movieCd?.let { movieCd ->
                if (!movieRepository.existsByMovieCd(movieCd)) {
                    log.info("Movie with movieCd $movieCd does not exist in DB, fetching details.")
                    val movie = getMovieInfo(movieCd)

                    movie?.let {
                        movieRepository.save(it)
                        log.info("Saved movie to DB: $movie")
                        movie.directorName?.let { directorName ->
                            updateMovieWithPosterAndPlot(movie, directorName)
                        } ?: log.warn("WARN: Director name is null for movieCd $movieCd")
                    }
                } else {
                    log.warn("Movie with movieCd $movieCd already exists in DB")
                }
            }
        }

        log.info("daily box office movies updated with poster and plot")
        return dailyBoxOfficeList
    }

    fun getMovieInfo(movieCd: String): Movie? {
        return movieRepository.findByMovieCd(movieCd)
    }
}
