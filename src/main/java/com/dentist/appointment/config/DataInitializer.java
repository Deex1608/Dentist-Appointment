package com.dentist.appointment.config;

import com.dentist.appointment.model.Dentist;
import com.dentist.appointment.model.Role;
import com.dentist.appointment.model.User;
import com.dentist.appointment.repository.DentistRepository;
import com.dentist.appointment.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository,
                                      DentistRepository dentistRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            // Create admin user if not exists
            if (!userRepository.existsByUsername("admin")) {
                User admin = new User("admin", passwordEncoder.encode("admin123"),
                        "admin@dentalclinic.com", Role.ADMIN);
                userRepository.save(admin);
            }

            // Seed dentists if none exist
            if (dentistRepository.count() == 0) {
                dentistRepository.save(new Dentist("Emily", "Carter", "General Dentistry",
                        "555-0101", "ecarter@dentalclinic.com"));
                dentistRepository.save(new Dentist("James", "Nguyen", "Orthodontics",
                        "555-0102", "jnguyen@dentalclinic.com"));
                dentistRepository.save(new Dentist("Sofia", "Patel", "Pediatric Dentistry",
                        "555-0103", "spatel@dentalclinic.com"));
                dentistRepository.save(new Dentist("Michael", "Torres", "Oral Surgery",
                        "555-0104", "mtorres@dentalclinic.com"));
            }
        };
    }
}
