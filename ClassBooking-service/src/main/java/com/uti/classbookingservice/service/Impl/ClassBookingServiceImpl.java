package com.uti.classbookingservice.service.Impl;

import com.uti.classbookingservice.dto.BookingRequest;
import com.uti.classbookingservice.dto.BookingResponse;
import com.uti.classbookingservice.model.BookingStatus;
import com.uti.classbookingservice.model.ClassBooking;
import com.uti.classbookingservice.repository.ClassBookingRepository;
import com.uti.classbookingservice.service.ClassBookingService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ClassBookingServiceImpl implements ClassBookingService {

    private final ClassBookingRepository classBookingRepository;

    public ClassBookingServiceImpl(ClassBookingRepository classBookingRepository) {
        this.classBookingRepository = classBookingRepository;
    }

    @Override
    public BookingResponse createBooking(BookingRequest request, String memberUsername) {
        ClassBooking booking = new ClassBooking();
        booking.setMemberUsername(memberUsername);
        booking.setTrainerUsername(request.trainerUsername());
        booking.setClassName(request.className());
        booking.setClassDate(request.classDate());
        booking.setStatus(BookingStatus.RESERVED);

        ClassBooking saved = classBookingRepository.save(booking);
        return toResponseDto(saved);
    }

    @Override
    public List<BookingResponse> getBookingsByMember(String memberUsername) {
        return classBookingRepository.findByMemberUsername(memberUsername)
                .stream().map(this::toResponseDto)
                .toList();
    }

    @Override
    public List<BookingResponse> getBookingsByTrainer(String trainerUsername) {
        return classBookingRepository.findByTrainerUsername(trainerUsername)
                .stream().map(this::toResponseDto)
                .toList();
    }

    @Override
    public BookingResponse attendBooking(Long bookingId) {
        ClassBooking booking = classBookingRepository.findById(bookingId)
                .orElseThrow(() -> new NoSuchElementException("No se encontro la reserva con id: " + bookingId));

        if (booking.getStatus() != BookingStatus.RESERVED) {
            throw new IllegalStateException(
                    "Solo se pueden marcar como asistidas las reservas en estado RESERVED. Estado actual: " + booking.getStatus());
        }

        booking.setStatus(BookingStatus.ATTENDED);
        ClassBooking updated = classBookingRepository.save(booking);
        return toResponseDto(updated);
    }

    @Override
    public List<BookingResponse> getAllBookings() {
        return classBookingRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public void deleteBooking(Long bookingId) {
        if (!classBookingRepository.existsById(bookingId)) {
            throw new NoSuchElementException("No se encontro la reserva con id: " + bookingId);
        }
        classBookingRepository.deleteById(bookingId);
    }

    // Convierte la entidad a DTO de salida, nunca se expone la entidad directamente
    private BookingResponse toResponseDto(ClassBooking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getMemberUsername(),
                booking.getTrainerUsername(),
                booking.getClassName(),
                booking.getClassDate(),
                booking.getStatus()
        );
    }
}
