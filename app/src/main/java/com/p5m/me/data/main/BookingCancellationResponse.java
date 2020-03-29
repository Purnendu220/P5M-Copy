package com.p5m.me.data.main;

public class BookingCancellationResponse {

    /**
     * id : 1
     * cancellationReason : Parking Problem
     * status : true
     */

    private int id;
    private String cancellationReason;
    private boolean status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
