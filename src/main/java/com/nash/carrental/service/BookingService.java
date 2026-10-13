package com.nash.carrental.service;

import com.nash.carrental.dto.AdminStatsResponse;
import com.nash.carrental.dto.BookingRequest;
import com.nash.carrental.dto.BookingResponse;
import com.nash.carrental.entity.*;
import com.nash.carrental.repository.BookingRepository;
import com.nash.carrental.repository.CarRepository;
import com.nash.carrental.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository, CarRepository carRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.carRepository = carRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public BookingResponse createBooking(String userEmail, BookingRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + userEmail));

        Car car = carRepository.findById(request.getCarId())
                .orElseThrow(() -> new IllegalArgumentException("Car not found with id: " + request.getCarId()));

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be earlier than start date");
        }

        if (car.getStatus() != CarStatus.AVAILABLE) {
            throw new IllegalStateException("Selected car is currently not available for booking");
        }

        long days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate());
        if (days == 0) {
            days = 1; // Minimum 1-day rental charge
        }

        BigDecimal totalAmount = car.getDailyRate().multiply(BigDecimal.valueOf(days));

        Booking booking = Booking.builder()
                .user(user)
                .car(car)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .totalAmount(totalAmount)
                .status(BookingStatus.PENDING)
                .build();

        Booking savedBooking = bookingRepository.save(booking);
        return mapToResponse(savedBooking, days);
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getUserBookings(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + userEmail));

        return bookingRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getAllBookings(BookingStatus status) {
        List<Booking> bookings = (status != null)
                ? bookingRepository.findByStatusOrderByCreatedAtDesc(status)
                : bookingRepository.findAllByOrderByCreatedAtDesc();

        return bookings.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookingResponse updateBookingStatus(Long bookingId, BookingStatus newStatus) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + bookingId));

        booking.setStatus(newStatus);

        Car car = booking.getCar();
        if (newStatus == BookingStatus.CONFIRMED) {
            car.setStatus(CarStatus.RENTED);
            carRepository.save(car);
        } else if (newStatus == BookingStatus.COMPLETED || newStatus == BookingStatus.CANCELLED) {
            car.setStatus(CarStatus.AVAILABLE);
            carRepository.save(car);
        }

        Booking updated = bookingRepository.save(booking);
        return mapToResponse(updated);
    }

    @Transactional(readOnly = true)
    public AdminStatsResponse getAdminStats() {
        long totalCars = carRepository.count();
        long availableCars = carRepository.countByStatus(CarStatus.AVAILABLE);
        long rentedCars = carRepository.countByStatus(CarStatus.RENTED);
        long maintenanceCars = carRepository.countByStatus(CarStatus.MAINTENANCE);

        long totalBookings = bookingRepository.count();
        long pendingBookings = bookingRepository.countByStatus(BookingStatus.PENDING);
        long confirmedBookings = bookingRepository.countByStatus(BookingStatus.CONFIRMED);

        BigDecimal revenue = bookingRepository.calculateTotalRevenue(
                List.of(BookingStatus.CONFIRMED, BookingStatus.COMPLETED)
        );

        return AdminStatsResponse.builder()
                .totalCars(totalCars)
                .availableCars(availableCars)
                .rentedCars(rentedCars)
                .maintenanceCars(maintenanceCars)
                .totalBookings(totalBookings)
                .pendingBookings(pendingBookings)
                .confirmedBookings(confirmedBookings)
                .totalRevenue(revenue != null ? revenue : BigDecimal.ZERO)
                .build();
    }

    private BookingResponse mapToResponse(Booking booking) {
        long days = ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate());
        if (days == 0) {
            days = 1;
        }
        return mapToResponse(booking, days);
    }

    private BookingResponse mapToResponse(Booking booking, long days) {
        return BookingResponse.builder()
                .id(booking.getId())
                .userId(booking.getUser().getId())
                .userFullName(booking.getUser().getFullName())
                .userEmail(booking.getUser().getEmail())
                .userPhone(booking.getUser().getPhone())
                .carId(booking.getCar().getId())
                .carBrand(booking.getCar().getBrand())
                .carModel(booking.getCar().getModel())
                .carCategory(booking.getCar().getCategory())
                .carDailyRate(booking.getCar().getDailyRate())
                .carImageUrl(booking.getCar().getImageUrl())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .totalDays(days)
                .totalAmount(booking.getTotalAmount())
                .status(booking.getStatus())
                .createdAt(booking.getCreatedAt())
                .build();
    }
}
