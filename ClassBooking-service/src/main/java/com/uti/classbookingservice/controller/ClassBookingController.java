package com.uti.classbookingservice.controller;

import com.uti.classbookingservice.dto.BookingRequest;
import com.uti.classbookingservice.dto.BookingResponse;
import com.uti.classbookingservice.service.ClassBookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class ClassBookingController {

    private final ClassBookingService classBookingService;

    public ClassBookingController(ClassBookingService classBookingService) {
        this.classBookingService = classBookingService;
    }

    // Socio: crea una reserva a su propio nombre, tomado del JWT
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(
            @Valid @RequestBody BookingRequest request, @AuthenticationPrincipal Jwt jwt) {
        String memberUsername = jwt.getClaimAsString("preferred_username");
        BookingResponse created = classBookingService.createBooking(request, memberUsername);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Socio: consulta unicamente sus propias reservas
    @GetMapping("/my-bookings")
    public ResponseEntity<List<BookingResponse>> getMyBookings(@AuthenticationPrincipal Jwt jwt) {
        String memberUsername = jwt.getClaimAsString("preferred_username");
        return ResponseEntity.ok(classBookingService.getBookingsByMember(memberUsername));
    }

    // Entrenador: consulta las reservas de las clases que dicta
    @GetMapping("/my-classes")
    public ResponseEntity<List<BookingResponse>> getMyClasses(@AuthenticationPrincipal Jwt jwt) {
        String trainerUsername = jwt.getClaimAsString("preferred_username");
        return ResponseEntity.ok(classBookingService.getBookingsByTrainer(trainerUsername));
    }

    // Entrenador: marca una reserva como asistida
    @PatchMapping("/{id}/attend")
    public ResponseEntity<BookingResponse> attendBooking(@PathVariable Long id) {
        return ResponseEntity.ok(classBookingService.attendBooking(id));
    }

    // Admin: todas las reservas del sistema
    @GetMapping
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        return ResponseEntity.ok(classBookingService.getAllBookings());
    }

    // Admin: elimina cualquier reserva
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        classBookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
}
