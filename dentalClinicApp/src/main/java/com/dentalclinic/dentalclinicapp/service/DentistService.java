package com.dentalclinic.dentalclinicapp.service;

import com.dentalclinic.dentalclinicapp.entity.Employee;
import com.dentalclinic.dentalclinicapp.entity.Employee.EmployeeType;
import com.dentalclinic.dentalclinicapp.entity.Employee.EmploymentStatus;
import com.dentalclinic.dentalclinicapp.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class DentistService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> getAllDentists() {
        return employeeRepository.findActiveDentists();
    }

    public Optional<Employee> getDentistById(Long id) {
        return employeeRepository.findById(id)
                .filter(employee -> employee.getEmployeeType() == EmployeeType.DENTIST);
    }

    public Employee saveDentist(Employee dentist) {
        dentist.setEmployeeType(EmployeeType.DENTIST);
        if (dentist.getEmploymentStatus() == null) {
            dentist.setEmploymentStatus(EmploymentStatus.ACTIVE);
        }
        return employeeRepository.save(dentist);
    }

    public Employee updateDentist(Long id, Employee dentistDetails) {
        Employee dentist = employeeRepository.findById(id)
                .filter(employee -> employee.getEmployeeType() == EmployeeType.DENTIST)
                .orElseThrow(() -> new RuntimeException("Dentist not found with id: " + id));

        // Update person details if they exist
        if (dentistDetails.getPerson() != null) {
            if (dentistDetails.getPerson().getFirstName() != null) {
                dentist.getPerson().setFirstName(dentistDetails.getPerson().getFirstName());
            }
            if (dentistDetails.getPerson().getLastName() != null) {
                dentist.getPerson().setLastName(dentistDetails.getPerson().getLastName());
            }
            if (dentistDetails.getPerson().getEmail() != null) {
                dentist.getPerson().setEmail(dentistDetails.getPerson().getEmail());
            }
            if (dentistDetails.getPerson().getPhone() != null) {
                dentist.getPerson().setPhone(dentistDetails.getPerson().getPhone());
            }
            if (dentistDetails.getPerson().getAddress() != null) {
                dentist.getPerson().setAddress(dentistDetails.getPerson().getAddress());
            }
        }

        // Update employee-specific details
        if (dentistDetails.getLicenseNumber() != null) {
            dentist.setLicenseNumber(dentistDetails.getLicenseNumber());
        }
        if (dentistDetails.getSpecialization() != null) {
            dentist.setSpecialization(dentistDetails.getSpecialization());
        }
        if (dentistDetails.getExperienceYears() != null) {
            dentist.setExperienceYears(dentistDetails.getExperienceYears());
        }

        return employeeRepository.save(dentist);
    }

    public void deleteDentist(Long id) {
        Employee dentist = employeeRepository.findById(id)
                .filter(employee -> employee.getEmployeeType() == EmployeeType.DENTIST)
                .orElseThrow(() -> new RuntimeException("Dentist not found with id: " + id));
        employeeRepository.delete(dentist);
    }

    public Optional<Employee> findByEmail(String email) {
        return employeeRepository.findAll().stream()
                .filter(employee -> employee.getEmployeeType() == EmployeeType.DENTIST)
                .filter(employee -> employee.getPerson() != null && email.equals(employee.getPerson().getEmail()))
                .findFirst();
    }

    public Optional<Employee> findByLicenseNumber(String licenseNumber) {
        return employeeRepository.findByLicenseNumber(licenseNumber)
                .filter(employee -> employee.getEmployeeType() == EmployeeType.DENTIST);
    }

    public List<Employee> findBySpecialization(String specialization) {
        return employeeRepository.findAll().stream()
                .filter(employee -> employee.getEmployeeType() == EmployeeType.DENTIST)
                .filter(employee -> employee.getSpecialization() != null &&
                        employee.getSpecialization().toLowerCase().contains(specialization.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Employee> searchDentists(String searchTerm) {
        return employeeRepository.findAll().stream()
                .filter(employee -> employee.getEmployeeType() == EmployeeType.DENTIST)
                .filter(employee -> {
                    String lowerSearchTerm = searchTerm.toLowerCase();
                    return (employee.getPerson() != null &&
                            (employee.getPerson().getFirstName() != null
                                    && employee.getPerson().getFirstName().toLowerCase().contains(lowerSearchTerm))
                            ||
                            (employee.getPerson().getLastName() != null
                                    && employee.getPerson().getLastName().toLowerCase().contains(lowerSearchTerm))
                            ||
                            (employee.getPerson().getEmail() != null
                                    && employee.getPerson().getEmail().toLowerCase().contains(lowerSearchTerm)))
                            ||
                            (employee.getSpecialization() != null
                                    && employee.getSpecialization().toLowerCase().contains(lowerSearchTerm))
                            ||
                            (employee.getLicenseNumber() != null
                                    && employee.getLicenseNumber().toLowerCase().contains(lowerSearchTerm));
                })
                .collect(Collectors.toList());
    }

    public List<Employee> findByMinimumExperience(Integer experienceYears) {
        return employeeRepository.findAll().stream()
                .filter(employee -> employee.getEmployeeType() == EmployeeType.DENTIST)
                .filter(employee -> employee.getExperienceYears() != null &&
                        employee.getExperienceYears() >= experienceYears)
                .collect(Collectors.toList());
    }

    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }

    public boolean existsByLicenseNumber(String licenseNumber) {
        return findByLicenseNumber(licenseNumber).isPresent();
    }

    public long getTotalDentistCount() {
        return employeeRepository.findAll().stream()
                .filter(employee -> employee.getEmployeeType() == EmployeeType.DENTIST)
                .count();
    }
}
