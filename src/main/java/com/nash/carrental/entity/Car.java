package com.nash.carrental.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String brand;

    @Column(nullable = false, length = 100)
    private String model;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false, length = 50)
    private String category; // Sedan, SUV, Luxury, Sports

    @Column(name = "daily_rate", nullable = false, precision = 10, scale = 2)
    private BigDecimal dailyRate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CarStatus status;

    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

    @Column(length = 30)
    private String transmission;

    @Column(name = "fuel_type", length = 30)
    private String fuelType;

    @Column(name = "seating_capacity")
    private Integer seatingCapacity;

    public Car() {}

    public Car(Long id, String brand, String model, Integer year, String category, BigDecimal dailyRate,
               CarStatus status, String imageUrl, String transmission, String fuelType, Integer seatingCapacity) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.category = category;
        this.dailyRate = dailyRate;
        this.status = status;
        this.imageUrl = imageUrl;
        this.transmission = transmission;
        this.fuelType = fuelType;
        this.seatingCapacity = seatingCapacity;
    }

    @PrePersist
    public void prePersist() {
        if (this.status == null) {
            this.status = CarStatus.AVAILABLE;
        }
        if (this.transmission == null) {
            this.transmission = "Automatic";
        }
        if (this.fuelType == null) {
            this.fuelType = "Petrol";
        }
        if (this.seatingCapacity == null) {
            this.seatingCapacity = 5;
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public BigDecimal getDailyRate() { return dailyRate; }
    public void setDailyRate(BigDecimal dailyRate) { this.dailyRate = dailyRate; }

    public CarStatus getStatus() { return status; }
    public void setStatus(CarStatus status) { this.status = status; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getTransmission() { return transmission; }
    public void setTransmission(String transmission) { this.transmission = transmission; }

    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }

    public Integer getSeatingCapacity() { return seatingCapacity; }
    public void setSeatingCapacity(Integer seatingCapacity) { this.seatingCapacity = seatingCapacity; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String brand;
        private String model;
        private Integer year;
        private String category;
        private BigDecimal dailyRate;
        private CarStatus status;
        private String imageUrl;
        private String transmission;
        private String fuelType;
        private Integer seatingCapacity;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder brand(String brand) { this.brand = brand; return this; }
        public Builder model(String model) { this.model = model; return this; }
        public Builder year(Integer year) { this.year = year; return this; }
        public Builder category(String category) { this.category = category; return this; }
        public Builder dailyRate(BigDecimal dailyRate) { this.dailyRate = dailyRate; return this; }
        public Builder status(CarStatus status) { this.status = status; return this; }
        public Builder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
        public Builder transmission(String transmission) { this.transmission = transmission; return this; }
        public Builder fuelType(String fuelType) { this.fuelType = fuelType; return this; }
        public Builder seatingCapacity(Integer seatingCapacity) { this.seatingCapacity = seatingCapacity; return this; }

        public Car build() {
            return new Car(id, brand, model, year, category, dailyRate, status, imageUrl, transmission, fuelType, seatingCapacity);
        }
    }
}
