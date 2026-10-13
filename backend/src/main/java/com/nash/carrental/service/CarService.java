package com.nash.carrental.service;

import com.nash.carrental.dto.CarRequest;
import com.nash.carrental.entity.Car;
import com.nash.carrental.entity.CarStatus;
import com.nash.carrental.repository.CarRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarService {

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Transactional(readOnly = true)
    public List<Car> getCars(String category, String brand, CarStatus status) {
        String cleanCategory = (category != null && !category.trim().equalsIgnoreCase("ALL") && !category.trim().isEmpty())
                ? category.trim()
                : null;
        String cleanBrand = (brand != null && !brand.trim().isEmpty())
                ? brand.trim().toLowerCase()
                : null;

        if (cleanCategory == null && cleanBrand == null && status == null) {
            return carRepository.findAll();
        }

        Specification<Car> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (cleanCategory != null) {
                predicates.add(cb.equal(cb.lower(root.get("category")), cleanCategory.toLowerCase()));
            }

            if (cleanBrand != null) {
                String pattern = "%" + cleanBrand + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("brand")), pattern),
                        cb.like(cb.lower(root.get("model")), pattern)
                ));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return carRepository.findAll(spec);
    }

    @Transactional(readOnly = true)
    public Car getCarById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Car not found with id: " + id));
    }

    @Transactional
    public Car createCar(CarRequest request) {
        Car car = Car.builder()
                .brand(request.getBrand().trim())
                .model(request.getModel().trim())
                .year(request.getYear())
                .category(request.getCategory().trim())
                .dailyRate(request.getDailyRate())
                .status(request.getStatus() != null ? request.getStatus() : CarStatus.AVAILABLE)
                .imageUrl(request.getImageUrl().trim())
                .transmission(request.getTransmission() != null ? request.getTransmission() : "Automatic")
                .fuelType(request.getFuelType() != null ? request.getFuelType() : "Petrol")
                .seatingCapacity(request.getSeatingCapacity() != null ? request.getSeatingCapacity() : 5)
                .build();

        return carRepository.save(car);
    }

    @Transactional
    public Car updateCar(Long id, CarRequest request) {
        Car car = getCarById(id);

        car.setBrand(request.getBrand().trim());
        car.setModel(request.getModel().trim());
        car.setYear(request.getYear());
        car.setCategory(request.getCategory().trim());
        car.setDailyRate(request.getDailyRate());
        if (request.getStatus() != null) {
            car.setStatus(request.getStatus());
        }
        car.setImageUrl(request.getImageUrl().trim());
        if (request.getTransmission() != null) {
            car.setTransmission(request.getTransmission());
        }
        if (request.getFuelType() != null) {
            car.setFuelType(request.getFuelType());
        }
        if (request.getSeatingCapacity() != null) {
            car.setSeatingCapacity(request.getSeatingCapacity());
        }

        return carRepository.save(car);
    }

    @Transactional
    public void deleteCar(Long id) {
        Car car = getCarById(id);
        carRepository.delete(car);
    }
}
