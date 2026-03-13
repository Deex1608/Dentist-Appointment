package com.dentist.appointment.controller;

import com.dentist.appointment.model.Appointment;
import com.dentist.appointment.model.Patient;
import com.dentist.appointment.service.AppointmentService;
import com.dentist.appointment.service.PatientService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;
    private final AppointmentService appointmentService;

    public PatientController(PatientService patientService, AppointmentService appointmentService) {
        this.patientService = patientService;
        this.appointmentService = appointmentService;
    }

    private Patient resolvePatient(Authentication auth) {
        return patientService.findByUsername(auth.getName())
                .orElseThrow(() -> new IllegalStateException("Patient profile not found for user: " + auth.getName()));
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        Patient patient = resolvePatient(auth);
        model.addAttribute("patient", patient);
        model.addAttribute("appointments", appointmentService.getAppointmentsForPatient(patient));
        return "patient/dashboard";
    }

    @GetMapping("/appointments")
    public String appointments(Authentication auth, Model model) {
        Patient patient = resolvePatient(auth);
        model.addAttribute("patient", patient);
        model.addAttribute("appointments", appointmentService.getAppointmentsForPatient(patient));
        return "patient/appointments";
    }

    @GetMapping("/book")
    public String bookPage(Authentication auth, Model model) {
        resolvePatient(auth);
        model.addAttribute("dentists", appointmentService.getAvailableDentists());
        model.addAttribute("today", LocalDate.now().toString());
        return "patient/book";
    }

    @PostMapping("/book")
    public String bookAppointment(
            Authentication auth,
            @RequestParam Long dentistId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate appointmentDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime appointmentTime,
            @RequestParam String treatmentType,
            @RequestParam(required = false) String notes,
            RedirectAttributes redirectAttributes) {
        try {
            Patient patient = resolvePatient(auth);
            appointmentService.bookAppointment(patient, dentistId, appointmentDate,
                    appointmentTime, treatmentType, notes);
            redirectAttributes.addFlashAttribute("successMessage", "Appointment booked successfully!");
            return "redirect:/patient/appointments";
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/patient/book";
        }
    }

    @PostMapping("/appointments/{id}/cancel")
    public String cancelAppointment(@PathVariable Long id, Authentication auth,
                                    RedirectAttributes redirectAttributes) {
        try {
            Patient patient = resolvePatient(auth);
            appointmentService.cancelAppointment(id, patient);
            redirectAttributes.addFlashAttribute("successMessage", "Appointment cancelled.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/patient/appointments";
    }

    @GetMapping("/profile")
    public String profilePage(Authentication auth, Model model) {
        Patient patient = resolvePatient(auth);
        model.addAttribute("patient", patient);
        return "patient/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(
            Authentication auth,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String dateOfBirth,
            @RequestParam(required = false) String medicalHistory,
            RedirectAttributes redirectAttributes) {
        Patient patient = resolvePatient(auth);
        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setPhone(phone);
        patient.setAddress(address);
        patient.setDateOfBirth(dateOfBirth);
        patient.setMedicalHistory(medicalHistory);
        patientService.updateProfile(patient);
        redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
        return "redirect:/patient/profile";
    }
}
