package com.example.genya.myapplication;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class info_air extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    TextView p_of_depart, date_dep, time_dep, destine, arr_date, arr_time, cost, col_bui, col_econ, col_firs;
    String id_user, p_of_depart1, date_dep1, time_dep1, destine1, arr_date1, arr_time1, cost1, col_b, col_eco, col_f, id_type="";
    public ArrayList<String> s_class = new ArrayList<String>();
    EditText et, edit_col;
    RadioGroup rg;
    Integer x= 0;
    Spinner sp_class;
    SharedPreferences sPref;
    Dialog dialog, dialog_error;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_air);

        p_of_depart = (TextView) findViewById(R.id.p_of_dep);
        date_dep = (TextView) findViewById(R.id.date);
        time_dep = (TextView) findViewById(R.id.p_time);
        destine = (TextView) findViewById(R.id.dest);
        arr_date = (TextView) findViewById(R.id.dest_date);
        arr_time = (TextView) findViewById(R.id.dest_time);
        cost = (TextView) findViewById(R.id.cost_tick);
        et = (EditText) findViewById(R.id.editText);
        edit_col = (EditText) findViewById(R.id.edit_col);
        col_bui=(TextView) findViewById(R.id.col_bui);
        col_econ = (TextView) findViewById(R.id.col_eco);
        col_firs = (TextView) findViewById(R.id.col_firs);
        rg = (RadioGroup) findViewById(R.id.RG);
        rg.setOnCheckedChangeListener(this);
        sp_class = (Spinner) findViewById(R.id.s_class);
        dialog = new Dialog(info_air.this);
        dialog_error = new Dialog(info_air.this);

        // Установите заголовок
        dialog.setTitle("Успешное бронирование");
        // Передайте ссылку на разметку
        dialog.setContentView(R.layout.dialog_view);
        // Найдите элемент TextView внутри вашей разметки
        // и установите ему соответствующий текст
        TextView text = (TextView) dialog.findViewById(R.id.textView34);
        text.setText("Билеты успешно забронированы. Отмена бронирования возможна в вашем " +
                "профиле либо по телефону хх ххх хх хх");



        sPref = getSharedPreferences("info", MODE_PRIVATE);
        id_user =  sPref.getString("id_user", "");

        sp_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
                //Toast.makeText(getBaseContext(), "Город = " + depart.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                Log.i("info_air", "Обработчик Spinner");
                id_type = String.valueOf(sp_class.getSelectedItemId());
                Log.i("info_air", "id_type  - " + id_type);
                //Toast.makeText(getBaseContext(), "Position = " + sp_class.getSelectedItemId(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_info_air, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.prifile:
                Intent intent = new Intent(this, profile.class);
                startActivity(intent);
                break;
            case R.id.t_table:
                Intent inten = new Intent(this, t_table.class);
                startActivity(inten);
                break;
            case R.id.information:
                Intent info = new Intent(this, spravka.class);
                startActivity(info);
                break;
            case R.id.info_prog:
                Intent info_p = new Intent(this, ab_the_prog.class);
                startActivity(info_p);
                break;
            case R.id.out:
                this.finishAffinity();
            default:
                return super.onOptionsItemSelected(item);

        }
        return false;
    }

    public void click_btn(View view) {
        s_class.clear();
        List_Time list_time = new List_Time();
        list_time.execute();
        try {
            Log.i("t_table", "работает метод get");
            int x = list_time.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.i("t_table", "get not work. Error: "+ e);
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, s_class);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_class.setAdapter(adapter);

    }
    // устанавливаем обработчик нажатия для пунктов отбытия


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

        switch (checkedId){
            case R.id.radio_byn:
                Log.i("info_air", "byn");
                cost.setText("$"+Math.round(x*1.98));
                break;
            case R.id.radio_usd:
                Log.i("info_air", "usd");
                cost.setText("$"+x*1);
                break;
            case R.id.radio_eur:
                Log.i("info_air", "eur");
                cost.setText("$"+Math.round(x*1.2));
                break;
            }
    }

    public void buy(View view) {
        s_class.clear();
        List_Time list_time = new List_Time();
        list_time.execute();
        try {
            Log.i("t_table", "работает метод get");
            list_time.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.i("t_table", "get not work. Error: "+ e);
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, s_class);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_class.setAdapter(adapter);
        Integer x = Integer.parseInt(String.valueOf(edit_col.getText()));
        Integer y;
        if (Objects.equals(id_type, "0"))
        {
            y = Integer.parseInt(col_b);
            if (x < y)
            {
                Log.i("info_air", "бизнес-класс");
                Bron_ticket bron = new Bron_ticket();
                bron.execute();
                try {
                    Log.i("t_table", "работает метод get");
                    bron.get();
                } catch (InterruptedException | ExecutionException e) {
                    Log.i("t_table", "get not work. Error: "+ e);
                    e.printStackTrace();
                }
                good_bron();
                s_class.clear();
                List_Time list = new List_Time();
                list.execute();
                try {
                    Log.i("t_table", "работает метод get");
                    list.get();
                } catch (InterruptedException | ExecutionException e) {
                    Log.i("t_table", "get not work. Error: "+ e);
                    e.printStackTrace();
                }
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_class.setAdapter(adapter);


            }
            else
            {
                Log.i("info_air", "бизнес-класс - ошибка");
                error_bron();
            }


        }
        if (Objects.equals(id_type, "1"))
        {
            Log.i("info_air", "эконом-класс");
            y = Integer.parseInt(col_eco);
            if (x < y)
            {
                Bron_ticket bron = new Bron_ticket();
                bron.execute();
                try {
                    Log.i("t_table", "работает метод get");
                    bron.get();
                } catch (InterruptedException | ExecutionException e) {
                    Log.i("t_table", "get not work. Error: "+ e);
                    e.printStackTrace();
                }
                good_bron();
                s_class.clear();
                List_Time list = new List_Time();
                list.execute();
                try {
                    Log.i("t_table", "работает метод get");
                    list.get();
                } catch (InterruptedException | ExecutionException e) {
                    Log.i("t_table", "get not work. Error: "+ e);
                    e.printStackTrace();
                }
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_class.setAdapter(adapter);

            }
            else
            {
                Log.i("info_air", "эконом-класс - ошибка");
                error_bron();
            }


        }
        if (Objects.equals(id_type, "2"))
        {
            Log.i("info_air", "первый-класс");
            y = Integer.parseInt(col_f);
            if (x < y)
            {
                Bron_ticket bron = new Bron_ticket();
                bron.execute();
                try {
                    Log.i("t_table", "работает метод get");
                    bron.get();
                } catch (InterruptedException | ExecutionException e) {
                    Log.i("t_table", "get not work. Error: "+ e);
                    e.printStackTrace();
                }
                good_bron();
                s_class.clear();
                List_Time list = new List_Time();
                list.execute();
                try {
                    Log.i("t_table", "работает метод get");
                    list.get();
                } catch (InterruptedException | ExecutionException e) {
                    Log.i("t_table", "get not work. Error: "+ e);
                    e.printStackTrace();
                }
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_class.setAdapter(adapter);
            }
            else
            {
                Log.i("info_air", "первый-класс - ошибка");
                error_bron();
            }
        }
    }

    private void error_bron() {
        dialog.setTitle("Ошибка бронирования");
        // Передайте ссылку на разметку
        dialog.setContentView(R.layout.dialog_view);
        // Найдите элемент TextView внутри вашей разметки
        // и установите ему соответствующий текст
        TextView text = (TextView) dialog.findViewById(R.id.textView34);
        text.setText("Недостаточно свободных мест.");
        dialog.show();
    }

    private void good_bron() {
        dialog.setTitle("Бронирование билетов");
        // Передайте ссылку на разметку
        dialog.setContentView(R.layout.dialog_view);
        // Найдите элемент TextView внутри вашей разметки
        // и установите ему соответствующий текст
        TextView text = (TextView) dialog.findViewById(R.id.textView34);
        text.setText("Билеты успешно забронированы. Отмена бронирования возможна в вашем " +
                "профиле либо по телефону хх-ххх-ххх");
        dialog.show();
    }


    public void close_dialog(View view) {
        dialog.dismiss();
    }

    class List_Time extends AsyncTask<Void, Void, Integer> {

        String server_name = "http://r2551241.beget.tech";
        Integer res;
        HttpURLConnection conn;
        String ansver;


        @SuppressWarnings({"WrongThread", "deprecation"})
        protected Integer doInBackground(Void... params) {

            try {
                String t_table = server_name +"/text.php?action=t_table&num_of_fly="
                        + URLEncoder.encode(et.getText().toString().trim(), "UTF-8");
                Log.i("chat",
                        "+ ChatActivity - отправляем на сервер запрос "
                                + t_table);
                URL url = new URL(t_table);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                conn.setDoInput(true);
                conn.connect();
                res = conn.getResponseCode();
                Log.i("chat", "+ MainActivity - ответ сервера (200 = ОК): "
                        + res.toString());

            } catch (Exception e) {
                Log.i("chat",
                        "+ MainActivity - ответ сервера ОШИБКА: "
                                + e.getMessage());
            }
            // получаем ответ ---------------------------------->
            try {
                InputStream is = conn.getInputStream();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String bfr_st = null;
                while ((bfr_st = br.readLine()) != null) {
                    sb.append(bfr_st);
                }
                Log.i("chat", "+ FoneService - полный ответ сервера:\n"
                        + sb.toString());
                // сформируем ответ сервера в string
                // обрежем в полученном ответе все, что находится за "]"
                // это необходимо, т.к. json ответ приходит с мусором
                // и если этот мусор не убрать - будет невалидным
                ansver = sb.toString();
                ansver = ansver.substring(ansver.indexOf("["), ansver.indexOf("]") + 1);

                Log.i("chat", "+ FoneService ansver: " + ansver);

                is.close(); // закроем поток
                br.close(); // закроем буфер
            }
            catch (Exception e) {
                Log.i("chat", "+ FoneService ошибка: " + e.getMessage());
            }
            finally {
                conn.disconnect();
            }


            // запишем ответ в БД ---------------------------------->
            if (ansver != null && !ansver.trim().equals("")) {

                Log.i("chat",
                        "+ FoneService ---------- ответ содержит JSON:" + ansver);

                try {
                    // ответ превратим в JSON массив
                    JSONArray ja = new JSONArray(ansver);
                    JSONObject jo;

                    Integer i = 0;
                    //Log.i("chat", String.valueOf(ja.length()));
                    while (i < ja.length()) {
                        Log.i("info_air", String.valueOf(ja.getJSONObject(i)));
                        // разберем JSON массив построчно
                        jo = ja.getJSONObject(i);

                        //Log.i("chat", jo.getString("POINT_OF_DEPARTURE") + " " + jo.getString("DESTINATION"));

                    /*Log.i("chat",
                            "=================>>> "
                                    + jo.getString("POINT_OF_DEPARTURE")+" - "
                                    + jo.getString("DESTINATION"));*/


                        p_of_depart1 = jo.getString("point_of_dep");
                        destine1 = jo.getString("dest");
                        date_dep1 = jo.getString("depart_date");
                        time_dep1 = jo.getString("depart_time");
                        arr_date1 = jo.getString("arr_date");
                        arr_time1 = jo.getString("arr_time");
                        cost1 = jo.getString("cost");
                        x=Integer.parseInt(jo.getString("cost"));
                        col_b = jo.getString("b_class");
                        col_eco = jo.getString("eco_class");
                        col_f=jo.getString("first_class");
                        if (Integer.parseInt(jo.getString("b_class"))!=0)
                        {
                            s_class.add("Бизнес-класс");
                        }
                        if (Integer.parseInt(jo.getString("eco_class"))!=0)
                        {
                            s_class.add("Эконом-класс");
                        }
                        if (Integer.parseInt(jo.getString("first_class"))!=0)
                        {
                            s_class.add("Первый-класс");
                        }
                        i++;
                    }



                } catch (Exception e) {
                    // если ответ сервера не содержит валидный JSON
                    Log.i("chat",
                            "+ List_Time_Table ---------- ошибка ответа сервера:\n"
                                    + e.getMessage());
                }


            }

            return res;
        }
        protected void onPostExecute(Integer result) {


            try {
                if (result == 200 && !p_of_depart.equals("") && !destine.equals("")) {
                    p_of_depart.setText(p_of_depart1);
                    destine.setText(destine1);
                    date_dep.setText(date_dep1);
                    time_dep.setText(time_dep1);
                    arr_date.setText(arr_date1);
                    arr_time.setText(arr_time1);
                    cost.setText("$"+cost1);
                    col_bui.setText(col_b);
                    col_econ.setText(col_eco);
                    col_firs.setText(col_f);

                    //sname.setText(surname);

                    //Toast.makeText(getApplicationContext(), "Значение выведено на экран!!", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(getApplicationContext(),
                        "нет данных", Toast.LENGTH_SHORT)
                        .show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "Что-то пошло не так...", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }


    class Bron_ticket extends AsyncTask<Void, Void, Integer> {

        String server_name = "http://r2551241.beget.tech";
        Integer res;
        HttpURLConnection conn;
        String ansver;


        @SuppressWarnings({"WrongThread", "deprecation"})
        protected Integer doInBackground(Void... params) {

            try {
                String t_table = server_name +"/text.php?action=bron&col_tick="
                        + URLEncoder.encode(edit_col.getText().toString().trim(), "UTF-8")
                        +"&num_of_fly="
                        +URLEncoder.encode(et.getText().toString().trim(), "UTF-8")
                        +"&id_class="
                        +URLEncoder.encode(id_type, "UTF-8")
                        +"&id_user="
                        +URLEncoder.encode(id_user, "UTF-8");
                Log.i("chat",
                        "+ ChatActivity - отправляем на сервер запрос "
                                + t_table);
                URL url = new URL(t_table);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                conn.setDoInput(true);
                conn.connect();
                res = conn.getResponseCode();
                Log.i("chat", "+ MainActivity - ответ сервера (200 = ОК): "
                        + res.toString());

            } catch (Exception e) {
                Log.i("chat",
                        "+ MainActivity - ответ сервера ОШИБКА: "
                                + e.getMessage());
            }
            // получаем ответ ---------------------------------->
            try {
                InputStream is = conn.getInputStream();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String bfr_st = null;
                while ((bfr_st = br.readLine()) != null) {
                    sb.append(bfr_st);
                }
                Log.i("chat", "+ FoneService - полный ответ сервера:\n"
                        + sb.toString());
                // сформируем ответ сервера в string
                // обрежем в полученном ответе все, что находится за "]"
                // это необходимо, т.к. json ответ приходит с мусором
                // и если этот мусор не убрать - будет невалидным
                ansver = sb.toString();
                ansver = ansver.substring(ansver.indexOf("{"), ansver.indexOf("}") + 1);

                Log.i("chat", "+ FoneService ansver: " + ansver);

                is.close(); // закроем поток
                br.close(); // закроем буфер
            }
            catch (Exception e) {
                Log.i("chat", "+ FoneService ошибка: " + e.getMessage());
            }
            finally {
                conn.disconnect();
            }


            // запишем ответ в БД ---------------------------------->
            if (!Objects.equals(ansver, "")) {

                Log.i("chat",
                        "+ FoneService ---------- ответ содержит JSON:" + ansver);

                try {
                    // ответ превратим в JSON массив
                    JSONObject ja = new JSONObject(ansver);
                    Log.i("info_air answer",
                            "=================>>> "
                                    + ja.getString("answer"));


                } catch (Exception e) {
                    // если ответ сервера не содержит валидный JSON
                    Log.i("chat",
                            "+ List_Time_Table ---------- ошибка ответа сервера:\n"
                                    + e.getMessage());
                }


            }

            return res;
        }

    }
}
