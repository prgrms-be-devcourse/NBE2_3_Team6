package com.example.filmpass.repository

import com.example.filmpass.entity.Cinema
import org.springframework.data.jpa.repository.JpaRepository

interface CinemaRepository : JpaRepository<Cinema?, Long?>
