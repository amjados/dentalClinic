package com.dentalclinic.dentalclinicapp.repository;

import com.dentalclinic.dentalclinicapp.entity.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClinicRepository extends JpaRepository<Clinic, Long> {

    Optional<Clinic> findByName(String name);

    List<Clinic> findByCityId(Long cityId);

    @Query("SELECT c FROM Clinic c WHERE c.active = true")
    List<Clinic> findActiveClinics();

    @Query("SELECT c FROM Clinic c WHERE c.city.id = :cityId AND c.active = true")
    List<Clinic> findActiveClinicsByCityId(@Param("cityId") Long cityId);

    @Query("SELECT c FROM Clinic c WHERE c.city.name = :cityName AND c.active = true")
    List<Clinic> findActiveClinicsByCityName(@Param("cityName") String cityName);
}
