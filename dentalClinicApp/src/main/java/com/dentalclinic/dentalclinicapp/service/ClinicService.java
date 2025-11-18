package com.dentalclinic.dentalclinicapp.service;

import com.dentalclinic.dentalclinicapp.entity.Clinic;
import com.dentalclinic.dentalclinicapp.repository.ClinicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClinicService {

    @Autowired
    private ClinicRepository clinicRepository;

    public List<Clinic> getAllClinics() {
        return clinicRepository.findAll();
    }

    public Optional<Clinic> getClinicById(Long id) {
        return clinicRepository.findById(id);
    }

    public Optional<Clinic> getClinicByName(String name) {
        return clinicRepository.findByName(name);
    }

    public Clinic saveClinic(Clinic clinic) {
        return clinicRepository.save(clinic);
    }

    public Clinic updateClinic(Long id, Clinic clinicDetails) {
        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clinic not found with id: " + id));

        clinic.setName(clinicDetails.getName());
        clinic.setAddress(clinicDetails.getAddress());
        clinic.setPhone(clinicDetails.getPhone());
        clinic.setEmail(clinicDetails.getEmail());
        clinic.setLicenseNumber(clinicDetails.getLicenseNumber());
        clinic.setTimezoneId(clinicDetails.getTimezoneId());
        clinic.setActive(clinicDetails.getActive());

        if (clinicDetails.getCity() != null) {
            clinic.setCity(clinicDetails.getCity());
        }

        return clinicRepository.save(clinic);
    }

    public void deleteClinic(Long id) {
        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clinic not found with id: " + id));
        clinicRepository.delete(clinic);
    }

    public List<Clinic> getClinicsByCityId(Long cityId) {
        return clinicRepository.findByCityId(cityId);
    }

    public List<Clinic> getActiveClinics() {
        return clinicRepository.findActiveClinics();
    }

    public List<Clinic> getActiveClinicsByCityId(Long cityId) {
        return clinicRepository.findActiveClinicsByCityId(cityId);
    }

    public List<Clinic> getActiveClinicsByCityName(String cityName) {
        return clinicRepository.findActiveClinicsByCityName(cityName);
    }

    public Clinic activateClinic(Long id) {
        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clinic not found with id: " + id));
        clinic.setActive(true);
        return clinicRepository.save(clinic);
    }

    public Clinic deactivateClinic(Long id) {
        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clinic not found with id: " + id));
        clinic.setActive(false);
        return clinicRepository.save(clinic);
    }

    public boolean existsByName(String name) {
        return clinicRepository.findByName(name).isPresent();
    }

    public long getTotalClinicCount() {
        return clinicRepository.count();
    }

    public long getActiveClinicCount() {
        return clinicRepository.findActiveClinics().size();
    }
}
