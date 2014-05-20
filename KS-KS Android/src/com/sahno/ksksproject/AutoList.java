package com.sahno.ksksproject;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Random;

/**
 *
 * Created by Dr. Failov on 01.05.2014.
 */
public class AutoList extends ArrayList<Auto> {
    Random random;


    ArrayList<Auto> search(String pattern) {
        ArrayList<Auto> result = new ArrayList<Auto>();
        for (int i = 0; i < size(); i++) {
            Auto auto = get(i);
            if (auto.manufacturer.contains(pattern)
                    || auto.model.contains(pattern)
                    || auto.color.contains(pattern))
                result.add(auto);
        }
        return result;
    }
    String show(boolean showBookings){
        String result = "";
        for(int i=0; i<size(); i++) {
            result += (i + ": " + get(i).toString(showBookings)) + "\n";
        }
        return result;
    }
    void init(){
        random = new Random();
        for(int i=0; i<20; i++) {
            Auto auto  = new Auto(getRandomManufacturer(),
                    getRandomModel(),
                    getRandomColor(),
                    getRandomCapacity(),
                    getRandomSpeed(),
                    getRandomMileage());
            try {
                if(random.nextBoolean()) {
                    int c =  random.nextInt(20);
                    for(int j=0; j<c; j++) {
                        try {
                            auto.addBooking(new Booking(
                                    "user" + String.valueOf(Math.abs(random.nextInt(15) + 2000)),
                                    String.valueOf(Math.abs(random.nextInt())),
                                    getSalt(random.nextInt(20) + 2) + "@mail.ru",
                                    new GregorianCalendar(random.nextInt(15) + 2000, random.nextInt(12), random.nextInt(30), random.nextInt(24), random.nextInt(60)),
                                    new GregorianCalendar(random.nextInt(15) + 2000, random.nextInt(12), random.nextInt(30), random.nextInt(24), random.nextInt(60))));
                        }catch (IllegalArgumentException e){
                            //continue;
                        }
                    }
                }
            }catch (Exception e){
                //System.out.println("Помилка при ініціалізації: дати накладаюсться одна на одну");
            }
            add(auto);
        }
    }
    String getSalt(int length){
        String source = "1234567890qwertyuiopasfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
        Random random = new Random();
        StringBuffer result = new StringBuffer();
        for(int i=0; i<length; i++){
            int r = random.nextInt(source.length() - 2);
            result.append(source.substring(r, r+1));
        }
        return result.toString();
    }
    String getRandomManufacturer(){
        int r = random.nextInt(10);
        switch(r){
            case 0:
                return "Audi";
            case 1:
                return "Mitsubishi";
            case 2:
                return "Ford";
            case 3:
                return "Kia";
            case 4:
                return "Lada";
            case 5:
                return "Ferrari";
            case 6:
                return "BMW";
            case 7:
                return "Suzuki";
            case 8:
                return "Daewoo";
            case 9:
                return "Volkswagen";
            default:
                return "Tesla";
        }
    }
    String getRandomModel(){
        int r = random.nextInt(6);
        int q = random.nextInt(10);
        switch(r){
            case 0:
                return "A"+q;
            case 1:
                return "X"+q;
            case 2:
                return "Model"+q*16;
            case 3:
                switch (q){
                    case 0:
                        return "Lanos";
                    case 1:
                        return "Gentra";
                    case 2:
                        return "Matiz";
                    case 3:
                        return "Sens";
                    case 4:
                        return "Nexia";
                    case 5:
                        return "Trancelute";
                    case 6:
                        return "Scace";
                    case 7:
                        return "Brick";
                    case 8:
                        return "Kalina";
                    case 9:
                        return "Matreshka";
                }
            case 4:
                switch (q) {
                    case 0:
                        return "ВАЗ";
                    case 1:
                        return "Самара хэтчбэк";
                    case 2:
                        return "Калина универсал";
                    case 3:
                        return "Калина хэтчбеэк";
                    case 4:
                        return "Калина спорт";
                    case 5:
                        return "Матрешка";
                    case 6:
                        return "Приора";
                    case 7:
                        return "Ларгус";
                    case 8:
                        return "Приора седан";
                    case 9:
                        return "Ларгус фургон";
                }
            case 5:
                switch (q) {
                    case 0:
                        return "А";
                    case 1:
                        return "G";
                    case 2:
                        return "W";
                    case 3:
                        return "J";
                    case 4:
                        return "S";
                    case 5:
                        return "U";
                    case 6:
                        return "T";
                    case 7:
                        return "L";
                    case 8:
                        return "V";
                    case 9:
                        return "N";
                }
            default:
                return "lol";
        }
    }
    String getRandomColor(){
        int r = random.nextInt(9);
        switch(r){
            case 0:
                return "Красный";
            case 1:
                return "Зеленый";
            case 2:
                return "Черный";
            case 3:
                return "Белый";
            case 4:
                return "Синий";
            case 5:
                return "Серый";
            case 6:
                return "Фиолетовый";
            case 7:
                return "Желтый";
            case 8:
                return "Розовый";
            default:
                return "Фуксийный";
        }
    }
    double getRandomCapacity(){
        return random.nextDouble()%3 + 3;
    }
    int getRandomSpeed(){
        return random.nextInt()%100 + 250;
    }
    int getRandomMileage(){
        return Math.abs(random.nextInt() % 10000);
    }
}
