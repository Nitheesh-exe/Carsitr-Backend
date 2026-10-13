package com.nash.carrental.dto;

import com.nash.carrental.entity.BookingStatus;
import jakarta.validation.constraints.NotNull;

public class BookingStatusRequest {

    @NotNull(message = "Status is required")
    private BookingStatus status;

    public BookingStatusRequest() {}

    public BookingStatusRequest(BookingStatus status) {
        this.status = status;
    }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private BookingStatus status;

        public Builder status(BookingStatus status) { this.status = status; return this; }

        public BookingStatusRequest build() { return new BookingStatusRequest(status); }
    }
}
