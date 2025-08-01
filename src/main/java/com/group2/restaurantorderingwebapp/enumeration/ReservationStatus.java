package com.group2.restaurantorderingwebapp.enumeration;

public enum ReservationStatus {
    PENDING_RESERVATION(1,"Pending Reservation"),
    RESERVATION_CONFIRMED(2,"Reservation Confirmed"),
    PAID(3,"Paid Reservation");
    private int value;
    private String text;
    ReservationStatus(int value, String text) {
        this.value = value;
        this.text = text;
    }
     public static String getText (int value){
        if(value == ReservationStatus.PENDING_RESERVATION.value){
            return ReservationStatus.PENDING_RESERVATION.text;
        }
        else if(value == ReservationStatus.RESERVATION_CONFIRMED.value){
            return ReservationStatus.RESERVATION_CONFIRMED.text;
        }
        else return ReservationStatus.PAID.text;
     }
}
