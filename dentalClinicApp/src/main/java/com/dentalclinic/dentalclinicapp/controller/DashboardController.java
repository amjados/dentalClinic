package com.dentalclinic.dentalclinicapp.controller;

import com.dentalclinic.dentalclinicapp.service.AppointmentService;
import com.dentalclinic.dentalclinicapp.service.DentistService;
import com.dentalclinic.dentalclinicapp.service.PatientService;
import com.dentalclinic.dentalclinicapp.service.TreatmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "Dashboard APIs for clinic statistics")
public class DashboardController {

        @Autowired
        private PatientService patientService;

        @Autowired
        private DentistService dentistService;

        @Autowired
        private AppointmentService appointmentService;

        @Autowired
        private TreatmentService treatmentService;

        @GetMapping("/stats")
        @Operation(summary = "Get dashboard statistics", description = "Get overall clinic statistics for dashboard")
        public ResponseEntity<Map<String, Object>> getDashboardStats() {
                Map<String, Object> stats = new HashMap<>();

                // Basic counts
                stats.put("totalPatients", patientService.getTotalPatientCount());
                stats.put("totalDentists", dentistService.getTotalDentistCount());
                stats.put("totalAppointments", appointmentService.getTotalAppointmentCount());
                stats.put("totalTreatments", treatmentService.getTotalTreatmentCount());

                // Appointment statistics
                stats.put("scheduledAppointments", appointmentService.getAppointmentCountByStatus(
                                com.dentalclinic.dentalclinicapp.entity.Appointment.AppointmentStatus.SCHEDULED));
                stats.put("completedAppointments", appointmentService.getAppointmentCountByStatus(
                                com.dentalclinic.dentalclinicapp.entity.Appointment.AppointmentStatus.COMPLETED));
                stats.put("cancelledAppointments", appointmentService.getAppointmentCountByStatus(
                                com.dentalclinic.dentalclinicapp.entity.Appointment.AppointmentStatus.CANCELLED));

                // Treatment statistics
                stats.put("plannedTreatments", treatmentService.getTreatmentCountByStatus(
                                com.dentalclinic.dentalclinicapp.entity.Treatment.TreatmentStatus.PLANNED));
                stats.put("completedTreatments", treatmentService.getTreatmentCountByStatus(
                                com.dentalclinic.dentalclinicapp.entity.Treatment.TreatmentStatus.COMPLETED));
                stats.put("inProgressTreatments", treatmentService.getTreatmentCountByStatus(
                                com.dentalclinic.dentalclinicapp.entity.Treatment.TreatmentStatus.IN_PROGRESS));

                // Upcoming follow-ups
                stats.put("upcomingFollowUps", treatmentService.getUpcomingFollowUps().size());

                return ResponseEntity.ok(stats);
        }
}
