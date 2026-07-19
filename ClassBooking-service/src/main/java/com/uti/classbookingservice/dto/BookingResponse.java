package com.uti.classbookingservice.dto;

import com.uti.classbookingservice.model.BookingStatus;

import java.time.LocalDateTime;


public record BookingResponse(
        Long id,
        String memberUsername,
        String trainerUsername,
        String className,
        LocalDateTime classDate,
        BookingStatus status
) {}
