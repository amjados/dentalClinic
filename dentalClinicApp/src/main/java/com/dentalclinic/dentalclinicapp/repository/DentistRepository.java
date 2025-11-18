package com.dentalclinic.dentalclinicapp.repository;

// @Repository - Deprecated: Use EmployeeRepository instead
@Deprecated
public interface DentistRepository { // extends JpaRepository<Dentist, Long> {

    // Deprecated methods - Use EmployeeRepository instead
    /*
     * Optional<Dentist> findByEmail(String email);
     * 
     * Optional<Dentist> findByLicenseNumber(String licenseNumber);
     * 
     * List<Dentist> findBySpecializationContainingIgnoreCase(String
     * specialization);
     * 
     * @Query("SELECT d FROM Dentist d WHERE " +
     * "LOWER(d.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
     * "LOWER(d.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
     * "LOWER(d.specialization) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
     * List<Dentist> searchDentists(@Param("searchTerm") String searchTerm);
     * 
     * List<Dentist> findByExperienceYearsGreaterThanEqual(Integer experienceYears);
     */
}
