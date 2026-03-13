package com.dentist.appointment.controller;

import com.dentist.appointment.model.Appointment;
import com.dentist.appointment.model.AppointmentStatus;
import com.dentist.appointment.service.AppointmentService;
import com.dentist.appointment.service.PatientService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AppointmentService appointmentService;
    private final PatientService patientService;

    public AdminController(AppointmentService appointmentService, PatientService patientService) {
        this.appointmentService = appointmentService;
        this.patientService = patientService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalAppointments", appointmentService.getAllAppointments().size());
        model.addAttribute("todayAppointments", appointmentService.countTodayAppointments());
        model.addAttribute("pendingCount", appointmentService.countByStatus(AppointmentStatus.PENDING));
        model.addAttribute("confirmedCount", appointmentService.countByStatus(AppointmentStatus.CONFIRMED));
        model.addAttribute("cancelledCount", appointmentService.countByStatus(AppointmentStatus.CANCELLED));
        model.addAttribute("completedCount", appointmentService.countByStatus(AppointmentStatus.COMPLETED));
        model.addAttribute("totalPatients", patientService.findAll().size());
        model.addAttribute("recentAppointments", appointmentService.getAllAppointments()
                .stream().limit(5).toList());
        return "admin/dashboard";
    }

    @GetMapping("/appointments")
    public String appointments(@RequestParam(required = false)
                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                               Model model) {
        if (date != null) {
            model.addAttribute("appointments", appointmentService.getAppointmentsForDate(date));
            model.addAttribute("selectedDate", date);
        } else {
            model.addAttribute("appointments", appointmentService.getAllAppointments());
        }
        return "admin/appointments";
    }

    @PostMapping("/appointments/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam AppointmentStatus status,
                               RedirectAttributes redirectAttributes) {
        try {
            appointmentService.updateStatus(id, status);
            redirectAttributes.addFlashAttribute("successMessage", "Appointment status updated.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/appointments";
    }

    @GetMapping("/patients")
    public String patients(Model model) {
        model.addAttribute("patients", patientService.findAll());
        return "admin/patients";
    }

    @GetMapping("/patients/{id}")
    public String patientDetail(@PathVariable Long id, Model model) {
        model.addAttribute("patient", patientService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found")));
        return "admin/patient-detail";
    }

    @GetMapping("/schedules")
    public String schedules(@RequestParam(required = false)
                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                            Model model) {
        LocalDate selectedDate = date != null ? date : LocalDate.now();
        model.addAttribute("appointments", appointmentService.getAppointmentsForDate(selectedDate));
        model.addAttribute("selectedDate", selectedDate);
        model.addAttribute("dentists", appointmentService.getAllDentists());
        return "admin/schedules";
    }
}
