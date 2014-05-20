package com.sahno.ksksproject;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * контейнер для сохранения всей "даты"
 * Created by Dr. Failov on 01.05.2014.
 */
interface BookingInterface{
    void addBooking(Booking booking) throws IncorrectBookingException;
    String printBookings();
    boolean checkBooking(Booking booking);
}

public class Booking {
    final int COMPARE_BIGGER = 1;
    final int COMPARE_SMALLEST = 2;
    final int COMPARE_EQUALS = 3;

    Calendar begin = new GregorianCalendar();
    Calendar end = new GregorianCalendar();
    String userEMail = "";
    String userName = "";
    String userPhone = "";

    public Booking(String _name, String _phone, String _email, Calendar _begin, Calendar _end) throws IllegalArgumentException {
        if(_begin.after(_end))
            throw  new IllegalArgumentException("Дата початку не може бути більшою за дату закінчення");
        begin = _begin;
        end = _end;
        userName = _name;
        userEMail = _email;
        userPhone = _phone;
        //проверка на то чтобы дата начала была мееньше даты конца
    }
    boolean isOK(Booking _booking){
        int begin1begin2 = compareDates(begin, _booking.begin);
        int begin1end2 = compareDates(begin, _booking.end);
        int end1begin2 = compareDates(end, _booking.begin);
        int end1end2 = compareDates(end, _booking.end);

        return (begin1begin2 == begin1end2) && (begin1begin2 == end1begin2) && (begin1begin2 == end1end2);
    }
    int compareDates(Calendar main, Calendar relatively){
        if(main.before(relatively))
            return COMPARE_SMALLEST;
        else if(main.after(relatively))
            return COMPARE_BIGGER;
        else
            return COMPARE_EQUALS;
    }
    @Override
    public String toString() {
        return "Дата початку: " + begin.getTime().toLocaleString() + "; Дата кінця: " + end.getTime().toLocaleString() + "; Імя користувача: " + userName + "; Телефон користувача: " + userPhone + "; E-mail користувача: " + userEMail + ";";
    }

}
