package com.uti.classbookingservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "class_bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @Column(name = "member_username", nullable = false)
    private String memberUsername;


    @Column(name = "trainer_username", nullable = false)
    private String trainerUsername;

    @Column(name = "class_name", nullable = false)
    private String className;

    @Column(name = "class_date", nullable = false)
    private LocalDateTime classDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status = BookingStatus.RESERVED;

}
