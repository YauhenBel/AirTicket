package com.example.genya.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    Button click_btn;
    EditText login, password;
    SharedPreferences sPref;
    String name="", surname="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        click_btn = (Button) findViewById(R.id.button);
        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);
    }

    public void click_btn(View view) {
        Log.i("MainActivity", "Вход в личный кабинет");
        //делаем кнопку неактивной
        click_btn.setEnabled(false);
        //Создаем переменную, которая подскажет можно ли переходить в кабинет пользователя
        //Тоесть она указывает на успешность авторизации
        Integer x;

        //Создаем объект класса ConnDB
        ConnDB connDB = new ConnDB();
        //Выполняем метод класса ConnDB, а именно метод input
        procc_answer(connDB.input(login.getText().toString().trim(), password.getText().toString().trim(), 0));
        //Результат выполнения метода запишем впеременную х
        click_btn.setEnabled(true);
       }

    private void procc_answer(JSONArray ja) {
        try {
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
                ed.putString("id_user", jo.getString("id_user"));
                ed.putString("time_last_change", jo.getString("time_last_change"));
                ed.commit();
                name = jo.getString("name");
                surname = jo.getString("surname");
                i++;
            }
        }
        catch (Exception e) {
            // если ответ сервера не содержит валидный JSON
            Log.i("chat",
                    "+ ConnDB ---------- ошибка ответа сервера:\n"
                            + e.getMessage());
        }
        if ( !name.equals("") && !surname.equals("")) {
            intnts();
        }
        else
        {
            Toast.makeText(getApplicationContext(),
                    "Неправильный логин или пароль", Toast.LENGTH_SHORT)
                    .show();
        }

    }

    //Метод перехода в кабинет пользователя
    public void intnts()
    {
        Intent intent = new Intent(this, profile.class);
        startActivity(intent);
    }
    //Метод перехода на актвити регистрации
    public void register(View view) {
        Intent intent = new Intent(this, register.class);
        startActivity(intent);
    }
}
