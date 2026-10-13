package com.nash.carrental.controller;

import com.nash.carrental.dto.AdminStatsResponse;
import com.nash.carrental.dto.BookingResponse;
import com.nash.carrental.dto.BookingStatusRequest;
import com.nash.carrental.dto.CarRequest;
import com.nash.carrental.entity.BookingStatus;
import com.nash.carrental.entity.Car;
import com.nash.carrental.service.BookingService;
import com.nash.carrental.service.CarService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    private final CarService carService;
    private final BookingService bookingService;

    public AdminController(CarService carService, BookingService bookingService) {
        this.carService = carService;
        this.bookingService = bookingService;
    }

    @PostMapping("/cars")
    public ResponseEntity<?> addCar(@Valid @RequestBody CarRequest request) {
        try {
            Car createdCar = carService.createCar(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCar);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", ex.getMessage()));
        }
    }

    @PutMapping("/cars/{id}")
    public ResponseEntity<?> updateCar(@PathVariable Long id, @Valid @RequestBody CarRequest request) {
        try {
            Car updatedCar = carService.updateCar(id, request);
            return ResponseEntity.ok(updatedCar);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", ex.getMessage()));
        }
    }

    @DeleteMapping("/cars/{id}")
    public ResponseEntity<?> deleteCar(@PathVariable Long id) {
        try {
            carService.deleteCar(id);
            return ResponseEntity.ok(Map.of("message", "Car successfully deleted", "id", id));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", ex.getMessage()));
        }
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<BookingResponse>> getAllBookings(
            @RequestParam(required = false) BookingStatus status
    ) {
        List<BookingResponse> bookings = bookingService.getAllBookings(status);
        return ResponseEntity.ok(bookings);
    }

    @PutMapping("/bookings/{id}/status")
    public ResponseEntity<?> updateBookingStatus(
            @PathVariable Long id,
            @Valid @RequestBody BookingStatusRequest request
    ) {
        try {
            BookingResponse response = bookingService.updateBookingStatus(id, request.getStatus());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", ex.getMessage()));
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<AdminStatsResponse> getStats() {
        AdminStatsResponse stats = bookingService.getAdminStats();
        return ResponseEntity.ok(stats);
    }
}
