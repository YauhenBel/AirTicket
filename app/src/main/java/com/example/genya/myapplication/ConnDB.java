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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * Created by YAUHENI on 18.02.17.
 * It class needed for connect with database and return of data
 */

class ConnDB {
    private String login, password, id_user, edName, edSurname,
            edCountry, edDate, edLogin, edPassw,
            id_flight, num_seat, name_class, query,
            s_depart, s_arr, edDate1;
    String server_name = "http://r2551241.beget.tech";
    private Integer x = 0;
    private HttpURLConnection conn;
    private ArrayList<prof_tick> prof_tabl = new ArrayList<prof_tick>();
    private ArrayList<String> p_of_dep = new ArrayList<String>();
    private ArrayList<String> destin = new ArrayList<String>();
    private ArrayList<prof_tick> obj = new ArrayList<prof_tick>();




    Integer id;

    JSONArray input(String _login, String _password, Integer _id)
    {
        login = _login;
        password = _password;
        id = _id;
        Input_profile dconn = new Input_profile();
        dconn.execute();
        try {
            return dconn.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
    Integer input(String _edName, String _edSurname, String _edCountry, String _edDate, String _edLogin, String _edPassw, Integer _id)
    {
        edName = _edName;
        edSurname = _edSurname;
        edCountry = _edCountry;
        edDate = _edDate;
        edLogin = _edLogin;
        edPassw = _edPassw;
        id = _id;
        Log.i("CinnDB", "+ register - дата: " + _edDate);
        Input_profile dconn = new Input_profile();
        dconn.execute();
        try {
            Log.i("Registers",
                    "+ ConnDB - try... catch... ");
            dconn.get();
            return 1;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
    Integer input(String _id_flight, String _num_seat, String _name_class, String _id_user, Integer _id)
    {
        id_flight = _id_flight;
        num_seat = _num_seat;
        name_class = _name_class;
        id_user = _id_user;
        id = _id;

        Input_profile dconn = new Input_profile();
        dconn.execute();
        try {
            dconn.get();
            return 1;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    JSONArray input(String _id_user, Integer _id)
    {
        id = _id;
        id_user = _id_user;
        Input_profile input_profile = new Input_profile();
        input_profile.execute();
        try {
            return input_profile.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    JSONArray input(Integer _id)
    {
        id = _id;
        Input_profile input_profile = new Input_profile();
        input_profile.execute();
        try {
            return input_profile.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    JSONArray input(String _s_depart, String _s_arr,  String _edDate1, Integer _id)
    {
        s_depart = _s_depart;
        s_arr = _s_arr;
        edDate1 = _edDate1;
        id = _id;
        try {
            query= server_name +"/text.php?action=n_t_table";
            //критерий 1
            if (!Objects.equals(s_depart, ""))
            {
                query = query +"&s_depar="
                        +URLEncoder.encode(s_depart, "UTF-8");
            }
            //критерий 2
            if (!Objects.equals(s_arr, ""))
            {
                query = query+ "&s_arr="
                        +URLEncoder.encode(s_arr, "UTF-8");
            }
            //критерий 3
            if (!Objects.equals(edDate1, ""))
            {
                query = query +"&eddate1="
                        + URLEncoder.encode(edDate1.trim(), "UTF-8");
            }

        } catch (Exception e) {
            Log.i("chat",
                    "+ MainActivity - ответ сервера ОШИБКА: "
                            + e.getMessage());
        }
        Log.i("ConnDB", "input_t_table - received a request: " + query);
        Input_profile input_profile = new Input_profile();
        input_profile.execute();
        try {
            return input_profile.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class Input_profile extends AsyncTask<Object, Object, JSONArray>
    {
        String ansver;
        JSONArray ja;

        @Override
        protected JSONArray doInBackground(Object... voids) {
            try {
                String input = "";
                if (id == 0)
                {
                    //for class MainActivity
                    input = server_name
                            + "/text.php?action=input&login="
                            + URLEncoder.encode(login, "UTF-8")
                            +"&password="
                            +URLEncoder.encode(password, "UTF-8");
                }
                if (id == 1)
                {
                    //for class register
                    input = server_name
                            + "/text.php?action=register&edlogin="
                            + URLEncoder.encode(edLogin, "UTF-8")
                            +"&edpassword="
                            +URLEncoder.encode(edPassw, "UTF-8")
                            +"&edname="
                            +URLEncoder.encode(edName, "UTF-8")
                            +"&edsurname="
                            +URLEncoder.encode(edSurname, "UTF-8")
                            +"&edcountry="
                            +URLEncoder.encode(edCountry, "UTF-8")
                            +"&eddate="
                            +URLEncoder.encode(edDate, "UTF-8");
                }
                if (id == 2)
                {
                    //for class profile, give a tickets info
                    input = server_name +"/text.php?action=output"
                            +"&id_user="
                            + URLEncoder.encode(id_user, "UTF-8");
                }
                if (id == 3)
                {
                    //for class profile, cancel booking a tickets
                    if (Objects.equals(name_class, "Эконом"))
                    {
                        input = server_name +"/text.php?action=cancel_booking"
                                +"&id_class=1"
                                +"&num_of_fly="
                                + URLEncoder.encode(id_flight, "UTF-8")
                                +"&num_of_seat="
                                +URLEncoder.encode(num_seat, "UTF-8")
                                +"&id_user="
                                + URLEncoder.encode(id_user, "UTF-8");;

                    }
                    if (Objects.equals(name_class, "Бизнес"))
                    {
                        input = server_name +"/text.php?action=cancel_booking"
                                +"&id_class=0"
                                +"&num_of_fly="
                                + URLEncoder.encode(id_flight, "UTF-8")
                                +"&num_of_seat="
                                +URLEncoder.encode(num_seat, "UTF-8")
                                +"&id_user="
                                + URLEncoder.encode(id_user, "UTF-8");;
                    }
                    if (Objects.equals(name_class, "Первый"))
                    {
                        input = server_name +"/text.php?action=cancel_booking"
                                +"&id_class=2"
                                +"&num_of_fly="
                                + URLEncoder.encode(id_flight, "UTF-8")
                                +"&num_of_seat="
                                +URLEncoder.encode(num_seat, "UTF-8")
                                +"&id_user="
                                + URLEncoder.encode(id_user, "UTF-8");;
                    }
                }
                if (id == 4)
                {
                    //for class t_table, give a schedule
                    input = server_name +"/text.php?action=t_table_rasp";
                }
                if (id == 5)
                {
                    //for class t_table, sorting flights
                    input = query;
                }
                if (id == 6)
                {
                    //for class t_table, give a current date for calendar
                        input = server_name +"/text.php?action=curr_date";
                }

                Log.i("chat",
                        "+ ChatActivity - send request on the server "
                                + input);
                URL url = new URL(input);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                conn.setDoInput(true);
                conn.connect();
                Integer res = conn.getResponseCode();
                Log.i("chat", "+ MainActivity - answer from server (200 = ОК): "
                        + res.toString());

            } catch (Exception e) {
                Log.i("chat",
                        "+ MainActivity - answer from server ERROR: "
                                + e.getMessage());
            }
            try {
                InputStream is = conn.getInputStream();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String bfr_st = null;
                while ((bfr_st = br.readLine()) != null) {
                    sb.append(bfr_st);
                }
                Log.i("chat", "+ FoneService - Full answer from server:\n"
                        + sb.toString());
                ansver = sb.toString();
                ansver = ansver.substring(ansver.indexOf("["), ansver.indexOf("]") + 1);

                Log.i("chat", "+ FoneService answer: " + ansver);

                is.close();
                br.close();
            }
            catch (Exception e) {
                Log.i("chat", "+ FoneService error: " + e.getMessage());
            }
            finally {
                conn.disconnect();
            }
            // запишем ответ в БД ---------------------------------->
            if (ansver != null && !ansver.trim().equals("")) {
                Log.i("ConnDB",
                        "+ Connect ---------- reply contains JSON:" + ansver);
                try {
                    // ответ превратим в JSON массив
                    if (id == 0 || id == 2 || id == 4 || id == 5 || id == 6)
                    {
                        ja = new JSONArray(ansver);
                    }
                    if (id == 1)
                    {
                        ansver = ansver.substring(ansver.indexOf("{"), ansver.indexOf("}") + 1);
                        Log.i("ConnDB",
                                "+ Connect ---------- оreply contains JSON:" + ansver);
                        JSONObject jo = new JSONObject(ansver);

                        Log.i("chat",
                                "=================>>> "
                                        + jo.getString("answer"));
                    }
                }
                catch (Exception e) {
                    Log.i("chat",
                            "+ ConnDB ---------- server response error:\n"
                                    + e.getMessage());
                }
            }

            if (id == 0 || id == 2 || id == 4 || id == 5 || id == 6)
            {
                return ja;
            }
            return null;

        }
    }
    }
