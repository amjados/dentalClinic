package com.dentalclinic.dentalclinicapp.repository;

import com.dentalclinic.dentalclinicapp.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    Optional<City> findByName(String name);

    List<City> findByCountry(String country);

    List<City> findByState(String state);

    @Query("SELECT c FROM City c ORDER BY c.name")
    List<City> findAllCitiesOrdered();

    @Query("SELECT c FROM City c WHERE c.country = ?1 ORDER BY c.name")
    List<City> findCitiesByCountryOrdered(String country);
}
