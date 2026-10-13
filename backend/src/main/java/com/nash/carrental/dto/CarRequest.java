package com.nash.carrental.dto;

import com.nash.carrental.entity.CarStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CarRequest {

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotBlank(message = "Model is required")
    private String model;

    @NotNull(message = "Year is required")
    private Integer year;

    @NotBlank(message = "Category is required")
    private String category; // Sedan, SUV, Luxury, Sports

    @NotNull(message = "Daily rate is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Daily rate must be greater than zero")
    private BigDecimal dailyRate;

    private CarStatus status; // Defaults to AVAILABLE if null

    @NotBlank(message = "Image URL is required")
    private String imageUrl;

    private String transmission;
    private String fuelType;
    private Integer seatingCapacity;

    public CarRequest() {}

    public CarRequest(String brand, String model, Integer year, String category, BigDecimal dailyRate,
                      CarStatus status, String imageUrl, String transmission, String fuelType, Integer seatingCapacity) {
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

    public static Builder builder() { return new Builder(); }

    public static class Builder {
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

        public CarRequest build() {
            return new CarRequest(brand, model, year, category, dailyRate, status, imageUrl, transmission, fuelType, seatingCapacity);
        }
    }
}
