package com.sahno.ksksproject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MyActivity extends Activity {
    //---------console service
    TextView tw;//текстовое поле с консолькой(мимимимим)
    EditText et;//+поле для ввода текста
    ScrollView scrollView;//контейнер для прокрутки текста
    Handler handler;//"портал между потоками"-магия(из 2 потока нельзя изменить интерфейс)
    boolean inputReady = false;//флаг готовности ввода текста
    String toInput = "", consoleText = "";//буфер текста консоли для ввода и вывода
    @Override public boolean dispatchKeyEvent(KeyEvent event) { //оброботка действий от пользователя
        if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){ //якщо "ентер"
            String text = et.getText().toString(); //получить данные с текстового поля(клава андроид)
            et.setText(""); //сброс текстового поля
            out(text);//вывод введенного текства на екран
            toInput = text;//буфер обмена между потоками (в д.с.)
            inputReady = true;//флаг-текст введен
            return true;//предотвратить оброботку сигнала системой(шоб лишний ентерЬ не вилез)
        }
        return super.dispatchKeyEvent(event);// если не ентерЬ-отправлять в систему
    }
    @Override public void onCreate(Bundle savedInstanceState) {//запускается при старте проги!!!
        super.onCreate(savedInstanceState);//обработать запуск системы
        setTitle("FP Console Emulator (KS-KS Project)");//Задать заголовок
        handler = new Handler();//инициализация
        et = new EditText(this);
        tw = new TextView(this);
        tw.setTextSize(10);//размер текста
        scrollView = new ScrollView(this);//новый контейнер для прокрутки
        scrollView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));//размер контейнера на весь, НА ВЕСЬ! екран
        scrollView.addView(tw);//вставить в контейнер текст
        LinearLayout ll;//контейнер для размещения один-под-одним
        ll = new LinearLayout(this);//инициализация контейнера
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(scrollView);
        ll.addView(et);//едит текст
        setContentView(ll);//отобразить контейнер LinearLayou
        doInThread(new Runnable() {//выполнить во втором потоке
            @Override //прога стартует во втором потоке, где выполняется функция мейн
            public void run() {
                main();
            }
        }, "main");
    }
    public void doInThread(Runnable r, String name){
        //запускает выполнение Runnable во втором потоке
        //MAKE TASK
        //И тут началась магия и теория высших небесных потоков... смертным недоступна.Аве
        AsyncTask<Object, String, Void> asyncTask=new AsyncTask<Object, String, Void>() {
            @Override protected Void doInBackground(Object[] objects) {
                String name = "empty";
                try{
                    name = (String)objects[1];
                }catch (Exception e){
                    publishProgress("ошибка при получении имени", "0");
                }

                try{
                    Runnable r = (Runnable)objects[0];
                    r.run();
                }catch (Exception e){
                    publishProgress("Exception: " + e.toString());
                }catch (OutOfMemoryError e){
                    publishProgress("OutOfMemoryError: " + e.toString());
                }
                publishProgress("Выполнено: " + name, "0");
                return null;
            }
            @Override protected void onProgressUpdate(String... message) {
                Log.d("debug", message[0]);
            }
        };
        asyncTask.execute(r, name);
    }
    //---------end console service

    //---------console I/O
    public int inNumber(){
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
    public double inDouble(){
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
    public Calendar inDate(){
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
    public String inString(){//принять от пользователя текстовые данные(эта функция выполняется во ВТОРОМ потоке)
        try {                       //ожидание ввода в основном потоке
            while(!inputReady) {
                Thread.sleep(100);
            }
            inputReady = false;//флажок-фолс, для того чтобы функция ждала следуйщий ввод
            return new String(toInput);//отправляем результат который берем из буфера(куда положили при вводе)

        }catch (Exception e){}
        return "";//если в процессе ввода возникла ошибка возвращаем пустую строку
    }
    void out(String text){//ЗЛО(вывод даных в консоль(во как!))
        consoleText = consoleText + text + "\n";//добавить текст к содержимому буфера консоли
        handler.post(new Runnable() {//обратиться к порталу и отправитть в него задание на выполнение в первом потоке
            @Override public void run() {
                tw.setText(consoleText);//обновить содержимое текстового поля
                scrollView.post(new Runnable() {//отправить в текстовое поле асинхронное задание на выполнение прокрутки в конец поля
                    @Override public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);//прокрутка в самый низ списка
                    }
                });
            }
        });
    }
    //---------end console I/O

    //---------user code
    static AutoList autos;
    public void main(){
        out("Ласкаво просимо до KS-KS Project!");
        autos = new AutoList();
        autos.init();
        showMainMenu();
    }
    public void showMainMenu(){
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
                    out(autos.show(false));
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
    public void showCatalogMenu(ArrayList<Auto> in_catalog){
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
    public void showCarMenu(Auto in_auto) {
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
    public void showSearchMenu(){
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
                out(i + ": " + result.get(i).toString(false));
            }
            showCatalogMenu(result);
        }
    }
    public void addAuto(){
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
    public void addBookiang(Auto in_auto){
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
    //---------end user code
}
