package com.uti.classbookingservice.service;

import com.uti.classbookingservice.dto.BookingRequest;
import com.uti.classbookingservice.dto.BookingResponse;

import java.util.List;

public interface ClassBookingService {

    // Crea una reserva a nombre del socio autenticado
    BookingResponse createBooking(BookingRequest request, String memberUsername);

    // Reservas del socio autenticado
    List<BookingResponse> getBookingsByMember(String memberUsername);

    // Reservas de las clases dictadas por el entrenador autenticado
    List<BookingResponse> getBookingsByTrainer(String trainerUsername);

    // Marca una reserva como ATTENDED, solo si estaba en RESERVED
    BookingResponse attendBooking(Long bookingId);

    // Todas las reservas del sistema, sin restriccion de propietario
    List<BookingResponse> getAllBookings();

    // Elimina cualquier reserva del sistema
    void deleteBooking(Long bookingId);
}
