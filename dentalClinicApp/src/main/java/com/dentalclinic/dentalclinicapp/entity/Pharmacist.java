package com.dentalclinic.dentalclinicapp.entity;

// import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @deprecated This entity has been replaced by Employee entity with employeeType = PHARMACIST.
 * Use EmployeeRepository with employeeType filter instead.
 */
@Deprecated
// @Entity
// @Table(name = "pharmacists")
public class Pharmacist {
    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;
    
    // @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;
    
    // @Column(name = "license_number", unique = true, length = 50)
    private String licenseNumber;
    
    // @Column(length = 20)
    private String phone;
    
    // @Column(length = 100)
    private String email;
    
    // @Column(length = 100)
    private String specialization;
    
    // @Column(name = "years_of_experience")
    private Integer yearsOfExperience;
    
    // @Column(length = 500)
    private String address;
    
    // @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    // @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    // @OneToMany(mappedBy = "pharmacist", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Prescription> prescriptions;
    
    // Constructors
    public Pharmacist() {}
    
    public Pharmacist(String firstName, String lastName, String licenseNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.licenseNumber = licenseNumber;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getLicenseNumber() {
        return licenseNumber;
    }
    
    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getSpecialization() {
        return specialization;
    }
    
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    
    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }
    
    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }
    
    public void setPrescriptions(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }
    
    // Helper method
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    // @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
