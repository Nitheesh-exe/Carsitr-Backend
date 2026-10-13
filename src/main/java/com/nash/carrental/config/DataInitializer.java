package com.nash.carrental.config;

import com.nash.carrental.entity.Car;
import com.nash.carrental.entity.CarStatus;
import com.nash.carrental.entity.Role;
import com.nash.carrental.entity.User;
import com.nash.carrental.repository.CarRepository;
import com.nash.carrental.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, CarRepository carRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.carRepository = carRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedUsers();
        seedCars();
    }

    private void seedUsers() {
        if (!userRepository.existsByEmail("admin@nash.com")) {
            User admin = User.builder()
                    .fullName("Nash Executive Admin")
                    .email("admin@nash.com")
                    .password(passwordEncoder.encode("admin123"))
                    .phone("+1 (555) 019-2834")
                    .role(Role.ROLE_ADMIN)
                    .build();
            userRepository.save(admin);
            log.info("Default Admin created: admin@nash.com / admin123");
        }

        if (!userRepository.existsByEmail("customer@nash.com")) {
            User customer = User.builder()
                    .fullName("Alexander Wright")
                    .email("customer@nash.com")
                    .password(passwordEncoder.encode("customer123"))
                    .phone("+1 (555) 749-1102")
                    .role(Role.ROLE_CUSTOMER)
                    .build();
            userRepository.save(customer);
            log.info("Default Customer created: customer@nash.com / customer123");
        }
    }

    private void seedCars() {
        if (carRepository.count() == 0) {
            List<Car> initialFleet = List.of(
                    Car.builder()
                            .brand("Porsche")
                            .model("911 GT3 RS")
                            .year(2024)
                            .category("Sports")
                            .dailyRate(new BigDecimal("850.00"))
                            .status(CarStatus.AVAILABLE)
                            .imageUrl("https://images.unsplash.com/photo-1614162692292-7ac56d7f7f1e?auto=format&fit=crop&w=1200&q=80")
                            .transmission("Automatic (PDK)")
                            .fuelType("Petrol")
                            .seatingCapacity(2)
                            .build(),

                    Car.builder()
                            .brand("Mercedes-Benz")
                            .model("G 63 AMG")
                            .year(2024)
                            .category("Luxury")
                            .dailyRate(new BigDecimal("650.00"))
                            .status(CarStatus.AVAILABLE)
                            .imageUrl("https://images.unsplash.com/photo-1520050206274-a1ae44613e6d?auto=format&fit=crop&w=1200&q=80")
                            .transmission("Automatic")
                            .fuelType("Petrol")
                            .seatingCapacity(5)
                            .build(),

                    Car.builder()
                            .brand("BMW")
                            .model("M8 Competition Gran Coupe")
                            .year(2023)
                            .category("Sports")
                            .dailyRate(new BigDecimal("520.00"))
                            .status(CarStatus.AVAILABLE)
                            .imageUrl("https://images.unsplash.com/photo-1555215695-3004980ad54e?auto=format&fit=crop&w=1200&q=80")
                            .transmission("Automatic")
                            .fuelType("Petrol")
                            .seatingCapacity(5)
                            .build(),

                    Car.builder()
                            .brand("Audi")
                            .model("RS7 Sportback")
                            .year(2024)
                            .category("Luxury")
                            .dailyRate(new BigDecimal("480.00"))
                            .status(CarStatus.AVAILABLE)
                            .imageUrl("https://images.unsplash.com/photo-1603584173870-7f23fdae1b7a?auto=format&fit=crop&w=1200&q=80")
                            .transmission("Automatic")
                            .fuelType("Hybrid")
                            .seatingCapacity(5)
                            .build(),

                    Car.builder()
                            .brand("Range Rover")
                            .model("Autobiography V8")
                            .year(2024)
                            .category("SUV")
                            .dailyRate(new BigDecimal("590.00"))
                            .status(CarStatus.AVAILABLE)
                            .imageUrl("https://images.unsplash.com/photo-1563720223185-11003d516935?auto=format&fit=crop&w=1200&q=80")
                            .transmission("Automatic")
                            .fuelType("Petrol")
                            .seatingCapacity(5)
                            .build(),

                    Car.builder()
                            .brand("Tesla")
                            .model("Model S Plaid")
                            .year(2023)
                            .category("Sedan")
                            .dailyRate(new BigDecimal("380.00"))
                            .status(CarStatus.AVAILABLE)
                            .imageUrl("https://images.unsplash.com/photo-1617788138017-80ad40651399?auto=format&fit=crop&w=1200&q=80")
                            .transmission("Automatic")
                            .fuelType("Electric")
                            .seatingCapacity(5)
                            .build(),

                    Car.builder()
                            .brand("Ferrari")
                            .model("F8 Tributo")
                            .year(2023)
                            .category("Sports")
                            .dailyRate(new BigDecimal("1250.00"))
                            .status(CarStatus.AVAILABLE)
                            .imageUrl("https://images.unsplash.com/photo-1583121274602-3e2820c69888?auto=format&fit=crop&w=1200&q=80")
                            .transmission("Automatic")
                            .fuelType("Petrol")
                            .seatingCapacity(2)
                            .build(),

                    Car.builder()
                            .brand("Lamborghini")
                            .model("Urus Performante")
                            .year(2024)
                            .category("SUV")
                            .dailyRate(new BigDecimal("1100.00"))
                            .status(CarStatus.AVAILABLE)
                            .imageUrl("https://images.unsplash.com/photo-1544829099-b9a0c07fad1a?auto=format&fit=crop&w=1200&q=80")
                            .transmission("Automatic")
                            .fuelType("Petrol")
                            .seatingCapacity(5)
                            .build()
            );

            carRepository.saveAll(initialFleet);
            log.info("Fleet initialized with {} exotic & luxury vehicles", initialFleet.size());
        }
    }
}
