package com.dentalclinic.dentalclinicapp.repository;

import com.dentalclinic.dentalclinicapp.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

        Optional<Patient> findByPersonEmail(String email);

        List<Patient> findByPersonFirstNameContainingIgnoreCaseOrPersonLastNameContainingIgnoreCase(
                        String firstName, String lastName);

        @Query("SELECT p FROM Patient p WHERE " +
                        "LOWER(p.person.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
                        "LOWER(p.person.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
                        "LOWER(p.person.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
                        "p.person.phone LIKE CONCAT('%', :searchTerm, '%')")
        Page<Patient> searchPatientsByPersonName(@Param("searchTerm") String searchTerm, Pageable pageable);

        List<Patient> findByPersonPhone(String phone);

        @Query("SELECT p FROM Patient p WHERE p.person.dateOfBirth BETWEEN :startDate AND :endDate")
        List<Patient> findByPersonDateOfBirthBetween(@Param("startDate") java.time.LocalDate startDate,
                        @Param("endDate") java.time.LocalDate endDate);
}
