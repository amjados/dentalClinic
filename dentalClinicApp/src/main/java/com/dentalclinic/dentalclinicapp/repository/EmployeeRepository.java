package com.dentalclinic.dentalclinicapp.repository;

import com.dentalclinic.dentalclinicapp.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByEmployeeType(String employeeType);

    Optional<Employee> findByEmployeeNumber(String employeeNumber);

    Optional<Employee> findByLicenseNumber(String licenseNumber);

    List<Employee> findByEmploymentStatus(String employmentStatus);

    List<Employee> findByClinicId(Long clinicId);

    @Query("SELECT e FROM Employee e JOIN FETCH e.person JOIN FETCH e.clinic c JOIN FETCH c.city WHERE e.employeeType = 'DENTIST' AND e.employmentStatus = 'ACTIVE'")
    List<Employee> findActiveDentists();

    @Query("SELECT e FROM Employee e WHERE e.clinic.id = :clinicId AND e.employeeType = :employeeType AND e.employmentStatus = 'ACTIVE'")
    List<Employee> findActiveEmployeesByClinicAndType(@Param("clinicId") Long clinicId,
            @Param("employeeType") String employeeType);

    @Query("SELECT e FROM Employee e WHERE e.person.firstName LIKE %:name% OR e.person.lastName LIKE %:name%")
    List<Employee> findByPersonName(@Param("name") String name);
}
