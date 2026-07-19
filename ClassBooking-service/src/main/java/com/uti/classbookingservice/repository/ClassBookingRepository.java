package com.uti.classbookingservice.repository;

import com.uti.classbookingservice.model.ClassBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassBookingRepository extends JpaRepository<ClassBooking, Long> {


    List<ClassBooking> findByMemberUsername(String memberUsername);


    List<ClassBooking> findByTrainerUsername(String trainerUsername);
}
