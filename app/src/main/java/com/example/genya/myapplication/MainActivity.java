package com.example.genya.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import java.util.jar.Attributes;

public class MainActivity extends AppCompatActivity {

    String server_name = "http://10.0.2.2/scripts";
    Boolean x = false;

    Button click_btn;
    //TextView fname, sname;
    EditText login, password;

    SharedPreferences sPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        click_btn = (Button) findViewById(R.id.button);
        //fname = (TextView) findViewById(R.id.Name);
        //sname = (TextView) findViewById(R.id.Surname);
        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);
    }

    public void click_btn(View view) {
        Log.i("chat", "+ MainActivity - первый запрос на проверку");
        click_btn.setEnabled(false);
        SELECTfromChat select_from_chat = new SELECTfromChat();
        select_from_chat.execute();

    }

    public void intnts()
    {
        Intent intent = new Intent(this, profile.class);
        startActivity(intent);
    }

    private class SELECTfromChat extends AsyncTask<Void, Void, Integer> {

        Integer res;
        HttpURLConnection conn;
        String ansver, name="", surname="";

        @SuppressWarnings({"WrongThread", "deprecation"})
        protected Integer doInBackground(Void... params) {

            try {
                String input = server_name
                        + "/text.php?action=input&login="
                        + URLEncoder.encode(login.getText().toString().trim(), "UTF-8")
                        +"&password="
                        +URLEncoder.encode(password.getText().toString().trim(), "UTF-8");
                //URL url = new URL(server_name + "/text.php?action=select");
                Log.i("chat",
                        "+ ChatActivity - отправляем на сервер запрос "
                                + input);
                URL url = new URL(input);
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
                        Log.i("chat", jo.getString("name") + " " + jo.getString("surname"));

                        Log.i("chat",
                                "=================>>> "
                                        + jo.getString("name")
                        + jo.getString("surname"));
                        sPref = getSharedPreferences("info", MODE_PRIVATE);
                        SharedPreferences.Editor ed = sPref.edit();
                        ed.putString("login", jo.getString("login"));
                        ed.putString("password", jo.getString("password"));
                        ed.putString("name", jo.getString("name"));
                        ed.putString("surname", jo.getString("surname"));
                        ed.putString("d_of_birth", jo.getString("d_of_birth"));
                        ed.putString("country", jo.getString("country"));
                        ed.putString("gender", jo.getString("gender"));
                        ed.putString("currency", jo.getString("currency"));
                        ed.putString("time_last_change", jo.getString("time_last_change"));
                        ed.commit();
                        name = jo.getString("name");
                        surname = jo.getString("surname");

                        i++;
                    }
                } catch (Exception e) {
                    // если ответ сервера не содержит валидный JSON
                    Log.i("chat",
                            "+ FoneService ---------- ошибка ответа сервера:\n"
                                    + e.getMessage());
                }


            }

            return res;
        }

        protected void onPostExecute(Integer result) {


            try {
                if (result == 200 && !name.equals("") && !surname.equals("")) {
                    //fname.setText(name);
                    //sname.setText(surname);
                    intnts();
                    Toast.makeText(getApplicationContext(),
                            "Значение выведено на экран!!", Toast.LENGTH_SHORT)
                            .show();
                }
                else Toast.makeText(getApplicationContext(),
                        "Неправильный логин или пароль", Toast.LENGTH_SHORT)
                        .show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "Ошибка выполнения запроса.", Toast.LENGTH_SHORT)
                        .show();
            } finally {
                // сделаем кнопку активной
                click_btn.setEnabled(true);
            }
        }
    }

}
