package com.sakhnyasha;

import java.util.ArrayList;



/**
 * Created by Dr. Failov on 01.05.2014.
 */
public class Auto extends Object implements BookingInterface {
    String manufacturer = "";
    String model = "";
    double engineCapacity = 0;
    int maxSpeed = 0;
    String color = "";
    int mileage = 0;
    ArrayList<Booking> bookings;

    Auto(String _manuf, String _model, String _color, double _capacity, int _maxSpeed, int _mileage){
        manufacturer = _manuf;
        model = _model;
        color = _color;
        mileage = _mileage;
        maxSpeed = _maxSpeed;
        engineCapacity = _capacity;
        bookings = new ArrayList<Booking>();
    }
    public String toString(boolean showBooking) {
        String result = manufacturer + " " + model + " " + color + " (" + String.format("%.1f", engineCapacity) + "л.) " + maxSpeed + "км/час,  пробег: " + mileage + " км.";
        if(!bookings.isEmpty() && showBooking){
            result += "\n   Заброньовано: \n";
            result += printBookings();
        }
        return result;
    }
    @Override public void addBooking(Booking booking) throws IncorrectBookingException{
        if(checkBooking(booking))
            bookings.add(booking);
        else
        {
            throw new IncorrectBookingException("NakladkaDat");
        }
    }
    @Override public String printBookings() {
        String result="";
        for (int i=0; i<bookings.size(); i++)
            result += "       "+bookings.get(i).toString() + "\n";
        return result;
    }

    @Override public boolean checkBooking(Booking booking) {  //переопределение
        boolean result = true;
        for (int i=0; i<bookings.size(); i++)
            result &= bookings.get(i).isOK(booking);
        return result;
    }
}
