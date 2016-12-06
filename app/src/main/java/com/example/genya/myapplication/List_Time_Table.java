package com.example.genya.myapplication;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Genya on 05.12.2016.
 */

public class List_Time_Table extends AsyncTask<Void, Void, Integer> {
    public ArrayList<Time_Table> time_tables = new ArrayList<Time_Table>();
    String server_name = "http://10.0.2.2/scripts";
    Integer res;
    HttpURLConnection conn;
    String ansver;


    @SuppressWarnings({"WrongThread", "deprecation"})
    protected Integer doInBackground(Void... params) {

        try {
            String t_table = server_name +"/text.php?action=t_table_rasp";
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
                Log.i("chat", String.valueOf(ja.length()));
                while (i < ja.length()) {
                    Log.i("chat", String.valueOf(ja.getJSONObject(i)));
                    // разберем JSON массив построчно
                    jo = ja.getJSONObject(i);

                    //Log.i("chat", jo.getString("POINT_OF_DEPARTURE") + " " + jo.getString("DESTINATION"));

                    Log.i("chat",
                            "=================>>> "
                                    + jo.getString("POINT_OF_DEPARTURE")+" - "
                                    + jo.getString("DESTINATION"));

                    String p_o_d = jo.getString("POINT_OF_DEPARTURE");
                    String destin = jo.getString("DESTINATION");
                    String date_ru = jo.getString("date_ru");
                    Integer cost =  jo.getInt("COST");
                    Integer id_f =  jo.getInt("ID_FLIGHT");

                    time_tables.add(new Time_Table(p_o_d, destin, date_ru, cost, id_f));
                    i++;
                }
                //t_table t = new t_table();
                //t.adapter(time_tables);
                /*for (Time_Table name : time_tables) {
                    System.out.println(name);
                }*/

                //adapter = new TimeAdapter(this, time_tables);
            } catch (Exception e) {
                // если ответ сервера не содержит валидный JSON
                Log.i("chat",
                        "+ List_Time_Table ---------- ошибка ответа сервера:\n"
                                + e.getMessage());
            }


        }

        return res;
    }
    public ArrayList<Time_Table> getList(){
        return time_tables;
    }
}
