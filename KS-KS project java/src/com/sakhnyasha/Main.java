package com.sakhnyasha;
import javafx.application.Application;

import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Main {
    static AutoList autos;
    public static void main(String[] args) {
        try {
            //далі іде код
            out("Ласкаво просимо до KS-KS Project!");
            autos = new AutoList();
            autos.init();
            showMainMenu();
        }catch (Exception input){
            out(input.toString());
        }
    }

    public static void showMainMenu(){
        while(true) {
            out("------------------Головне меню----------------------");
            out("Оберіть дію: ");
            out("   1) Переглянути каталог");
            out("   2) Пошук по каталогу");
            out("   3) Додати авто");
            out("   4) Вихід");
            out(" Введіть:");
            int input = inNumber();
            switch (input) {
                case 1:
                    out("Перегляд каталогу:");
                    autos.show();
                    showCatalogMenu(autos);
                    break;
                case 2:
                    out("Пошук по каталогу.");
                    showSearchMenu();
                    break;
                case 3:
                    out("Додання авто:");
                    addAuto();
                    break;
                case 4:
                    out("Exit.");
                    return;
                default:
                    out("Невірне введення.");
                    break;
            }
        }
    }
    public static void showCatalogMenu(ArrayList<Auto> in_catalog){
        while(true) {
            out("-------------------Меню каталогу---------------------");
            out("Оберіть авто (-1 - до головного меню )");
            out(" Введіть:");
            int input = inNumber();
            if (input == -1)
                return;
            if (input >= 0 && input < in_catalog.size()) {
                showCarMenu(in_catalog.get(input));
                return;
            } else {
                out("Автомобіля з таким номером немає");
            }
        }
    }
    public static void showCarMenu(Auto in_auto) {
        while (true) {
            out("--------------------Меню автомобіля--------------------");
            out("Обраний автомобіль:");
            out(in_auto.toString(true));
            out("----------------------------------------");
            out("Оберіть дію: ");
            out("   1) Забронювати");
            out("   2) Видалити з бази");
            out("   3) В головне меню");
            out(" Введіть:");
            int input = inNumber();
            switch (input) {
                case 1:
                    addBookiang(in_auto);
                    return;
                case 2:
                    out(" Видалення з бази...");
                    autos.remove(in_auto);
                    out(" Видалено.");
                    return;
                case 3:
                    out(" В головне меню.");
                    return;
                default:
                    out("Невірне введення.");
                    break;
            }
        }
    }
    public static void showSearchMenu(){
        out("--------------------Меню пошуку--------------------");
        out("Введіть запит:");
        String string = inString();
        out("Пошук...");
        ArrayList<Auto> result = autos.search(string);
        if(result.isEmpty()){
            out("Нічого не знайдено.");
        }else {
            out("Результати пошуку:");
            for (int i = 0; i < result.size(); i++) {
                Main.out(i + ": " + result.get(i).toString(false));
            }
            showCatalogMenu(result);
        }
    }
    public static void addAuto(){
        out("--------------------Додання автомобіля--------------------");
        out("Введіть марку автомобіля:");
        String manufacturer=inString();
        out("Введіть модель автомобіля:");
        String model=inString();
        out("Введіть колір:");
        String color=inString();
        out("Введіть обєм двигуна:");
        double capacity=inDouble();
        out("Введіть швидкість:");
        int speed=inNumber();
        out("Введіть пробіг:");
        int mileage=inNumber();
        autos.add(new Auto(manufacturer, model, color,  capacity,  speed,  mileage));
        out("Внесено в базу.");
    }
    public static void addBookiang(Auto in_auto){
        out("--------------------Додання броні--------------------");
        out("Введіть дату початку у форматі hh:mm dd.mm.yyyy");
        Calendar begin = inDate();
        out("Введіть дату кінця у форматі hh:mm dd.mm.yyyy");
        Calendar end = inDate();
        out("Введіть Ваше імя:");
        String name = inString();
        out("Введіть Ваш телефон:");
        String phone = inString();
        out("Введіть Ваш E-mail:");
        String email = inString();
        try {
            in_auto.addBooking(new Booking(name, phone, email, begin, end));
            out("Бронювання збережено.");
        }catch (IncorrectBookingException e){
            out("Виявлено помилкові дані. Перевірте, чи не накладається Ваш проміжок часу на вже зареєстрований раніше.");
        }
    }

    public static void out(String text){
        System.out.println(text);
    }
    public static int inNumber(){
        try {
            int result = 0;
            boolean cont = true;
            while(cont) {
                String input = inString();
                try{
                    result = Integer.parseInt(input);
                    cont = false;
                }catch (NumberFormatException e){
                    cont = true;
                }
            }
            return result;
        }catch (Exception e){}
        return 0;
    }
    public static double inDouble(){
        try {
            double result = 0;
            boolean cont = true;
            while(cont) {
                String input = inString();
                try{
                    result = Double.parseDouble(input);
                    cont = false;
                }catch (NumberFormatException e){
                    cont = true;
                }
            }
            return result;
        }catch (Exception e){}
        return 0;
    }
    public static String inString(){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String result = "";
            while(result.equals("")){
                result = br.readLine();
            }
            return result;

        }catch (Exception e){}
        return "";
    }
    public static Calendar inDate(){
        GregorianCalendar result = new GregorianCalendar();
        boolean cont = true;
        while(cont){
            String date = inString();
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd.MM.yyyy");
                Date dateobject=sdf.parse(date);
                result.setTime(dateobject);
                cont = false;
            }catch (ParseException e){
                cont = true;
            }
        }
        return result;
    }
}
