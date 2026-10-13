package com.nash.carrental.dto;

import com.nash.carrental.entity.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public class BookingResponse {

    private Long id;
    private Long userId;
    private String userFullName;
    private String userEmail;
    private String userPhone;

    private Long carId;
    private String carBrand;
    private String carModel;
    private String carCategory;
    private BigDecimal carDailyRate;
    private String carImageUrl;

    private LocalDate startDate;
    private LocalDate endDate;
    private long totalDays;
    private BigDecimal totalAmount;
    private BookingStatus status;
    private OffsetDateTime createdAt;

    public BookingResponse() {}

    public BookingResponse(Long id, Long userId, String userFullName, String userEmail, String userPhone,
                           Long carId, String carBrand, String carModel, String carCategory, BigDecimal carDailyRate, String carImageUrl,
                           LocalDate startDate, LocalDate endDate, long totalDays, BigDecimal totalAmount,
                           BookingStatus status, OffsetDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.userFullName = userFullName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.carId = carId;
        this.carBrand = carBrand;
        this.carModel = carModel;
        this.carCategory = carCategory;
        this.carDailyRate = carDailyRate;
        this.carImageUrl = carImageUrl;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalDays = totalDays;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserFullName() { return userFullName; }
    public void setUserFullName(String userFullName) { this.userFullName = userFullName; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getUserPhone() { return userPhone; }
    public void setUserPhone(String userPhone) { this.userPhone = userPhone; }

    public Long getCarId() { return carId; }
    public void setCarId(Long carId) { this.carId = carId; }

    public String getCarBrand() { return carBrand; }
    public void setCarBrand(String carBrand) { this.carBrand = carBrand; }

    public String getCarModel() { return carModel; }
    public void setCarModel(String carModel) { this.carModel = carModel; }

    public String getCarCategory() { return carCategory; }
    public void setCarCategory(String carCategory) { this.carCategory = carCategory; }

    public BigDecimal getCarDailyRate() { return carDailyRate; }
    public void setCarDailyRate(BigDecimal carDailyRate) { this.carDailyRate = carDailyRate; }

    public String getCarImageUrl() { return carImageUrl; }
    public void setCarImageUrl(String carImageUrl) { this.carImageUrl = carImageUrl; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public long getTotalDays() { return totalDays; }
    public void setTotalDays(long totalDays) { this.totalDays = totalDays; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Long userId;
        private String userFullName;
        private String userEmail;
        private String userPhone;
        private Long carId;
        private String carBrand;
        private String carModel;
        private String carCategory;
        private BigDecimal carDailyRate;
        private String carImageUrl;
        private LocalDate startDate;
        private LocalDate endDate;
        private long totalDays;
        private BigDecimal totalAmount;
        private BookingStatus status;
        private OffsetDateTime createdAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder userId(Long userId) { this.userId = userId; return this; }
        public Builder userFullName(String userFullName) { this.userFullName = userFullName; return this; }
        public Builder userEmail(String userEmail) { this.userEmail = userEmail; return this; }
        public Builder userPhone(String userPhone) { this.userPhone = userPhone; return this; }
        public Builder carId(Long carId) { this.carId = carId; return this; }
        public Builder carBrand(String carBrand) { this.carBrand = carBrand; return this; }
        public Builder carModel(String carModel) { this.carModel = carModel; return this; }
        public Builder carCategory(String carCategory) { this.carCategory = carCategory; return this; }
        public Builder carDailyRate(BigDecimal carDailyRate) { this.carDailyRate = carDailyRate; return this; }
        public Builder carImageUrl(String carImageUrl) { this.carImageUrl = carImageUrl; return this; }
        public Builder startDate(LocalDate startDate) { this.startDate = startDate; return this; }
        public Builder endDate(LocalDate endDate) { this.endDate = endDate; return this; }
        public Builder totalDays(long totalDays) { this.totalDays = totalDays; return this; }
        public Builder totalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; return this; }
        public Builder status(BookingStatus status) { this.status = status; return this; }
        public Builder createdAt(OffsetDateTime createdAt) { this.createdAt = createdAt; return this; }

        public BookingResponse build() {
            return new BookingResponse(id, userId, userFullName, userEmail, userPhone, carId, carBrand, carModel, carCategory,
                    carDailyRate, carImageUrl, startDate, endDate, totalDays, totalAmount, status, createdAt);
        }
    }
}
