package com.nash.carrental.dto;

import java.math.BigDecimal;

public class AdminStatsResponse {

    private long totalCars;
    private long availableCars;
    private long rentedCars;
    private long maintenanceCars;
    private long totalBookings;
    private long pendingBookings;
    private long confirmedBookings;
    private BigDecimal totalRevenue;

    public AdminStatsResponse() {}

    public AdminStatsResponse(long totalCars, long availableCars, long rentedCars, long maintenanceCars,
                              long totalBookings, long pendingBookings, long confirmedBookings, BigDecimal totalRevenue) {
        this.totalCars = totalCars;
        this.availableCars = availableCars;
        this.rentedCars = rentedCars;
        this.maintenanceCars = maintenanceCars;
        this.totalBookings = totalBookings;
        this.pendingBookings = pendingBookings;
        this.confirmedBookings = confirmedBookings;
        this.totalRevenue = totalRevenue;
    }

    public long getTotalCars() { return totalCars; }
    public void setTotalCars(long totalCars) { this.totalCars = totalCars; }

    public long getAvailableCars() { return availableCars; }
    public void setAvailableCars(long availableCars) { this.availableCars = availableCars; }

    public long getRentedCars() { return rentedCars; }
    public void setRentedCars(long rentedCars) { this.rentedCars = rentedCars; }

    public long getMaintenanceCars() { return maintenanceCars; }
    public void setMaintenanceCars(long maintenanceCars) { this.maintenanceCars = maintenanceCars; }

    public long getTotalBookings() { return totalBookings; }
    public void setTotalBookings(long totalBookings) { this.totalBookings = totalBookings; }

    public long getPendingBookings() { return pendingBookings; }
    public void setPendingBookings(long pendingBookings) { this.pendingBookings = pendingBookings; }

    public long getConfirmedBookings() { return confirmedBookings; }
    public void setConfirmedBookings(long confirmedBookings) { this.confirmedBookings = confirmedBookings; }

    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private long totalCars;
        private long availableCars;
        private long rentedCars;
        private long maintenanceCars;
        private long totalBookings;
        private long pendingBookings;
        private long confirmedBookings;
        private BigDecimal totalRevenue;

        public Builder totalCars(long totalCars) { this.totalCars = totalCars; return this; }
        public Builder availableCars(long availableCars) { this.availableCars = availableCars; return this; }
        public Builder rentedCars(long rentedCars) { this.rentedCars = rentedCars; return this; }
        public Builder maintenanceCars(long maintenanceCars) { this.maintenanceCars = maintenanceCars; return this; }
        public Builder totalBookings(long totalBookings) { this.totalBookings = totalBookings; return this; }
        public Builder pendingBookings(long pendingBookings) { this.pendingBookings = pendingBookings; return this; }
        public Builder confirmedBookings(long confirmedBookings) { this.confirmedBookings = confirmedBookings; return this; }
        public Builder totalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; return this; }

        public AdminStatsResponse build() {
            return new AdminStatsResponse(totalCars, availableCars, rentedCars, maintenanceCars,
                    totalBookings, pendingBookings, confirmedBookings, totalRevenue);
        }
    }
}
