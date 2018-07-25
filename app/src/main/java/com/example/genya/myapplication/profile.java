package com.example.genya.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Objects;

public class profile extends AppCompatActivity {

    TextView name, surname, d_of_birth, country, gender, currency;
    ArrayList<prof_tick> prof_tabl = new ArrayList<prof_tick>();
    SharedPreferences sPref;
    String id_user, id_flight, num_seat, name_class;
    prof_adapter adapter;
    private static final int CM_DELETE_ID = 1;
    ListView lvMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = (TextView)  findViewById(R.id.name);
        surname = (TextView) findViewById(R.id.surname);
        d_of_birth = (TextView) findViewById(R.id.d_of_birth);
        country = (TextView) findViewById(R.id.country);

        info();
        ConnDB connDB = new ConnDB();
        procc_answer(connDB.input(id_user, 2));

        adapter = new prof_adapter(this, prof_tabl);
        Log.i("t_table", "создаем адаптер");
        // настраиваем список
        lvMain = (ListView) findViewById(R.id.prof_list);
        lvMain.setAdapter(adapter);
        Log.i("t_table", "настроили список");
        //список городов
        registerForContextMenu(lvMain);

    }



    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_DELETE_ID, 0, "Отменит бронь");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == CM_DELETE_ID) {
            // получаем инфу о пункте списка
            AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();
            Log.i("t_table", "Позиция - " + acmi.position);

            Object obj = adapter.getItem(acmi.position);

            Log.i("t_table", "getItemId - " + obj);

            String[] s = obj.toString().split(" ");
            id_flight = s[0];
            num_seat = s[1];
            name_class =s[2];
            Log.i("profile", "id_flight - " + id_flight);
            Log.i("profile", "num_seat - " + num_seat);
            Log.i("profile", "name_class - " + name_class);

            Integer x;
            ConnDB connDB = new ConnDB();
            x = connDB.input(id_flight, num_seat, name_class, id_user, 3);
            if (x==1)
            {
                Log.i("profile", "Отмена бронирования прошла успешно");
            }
            else
            {
                Log.i("profile", "Ошибка отмены бронирования");
            }
            prof_tabl.clear();
            procc_answer(connDB.input(id_user, 2));

            adapter = new prof_adapter(this, prof_tabl);
            Log.i("t_table", "создаем адаптер");
            // настраиваем список
            lvMain = (ListView) findViewById(R.id.prof_list);
            lvMain.setAdapter(adapter);
            Log.i("t_table", "настроили список");
            //список городов
            registerForContextMenu(lvMain);

            Toast.makeText(getApplicationContext(),
                    "Бронирование успешно отменено", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onContextItemSelected(item);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       int id = item.getItemId();
        switch (id)
        {
            case R.id.t_table:
                Intent intent = new Intent(this, t_table.class);
                startActivity(intent);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return  true;
    }

    private  void info() {
        sPref = getSharedPreferences("info", MODE_PRIVATE);
        name.setText(sPref.getString("name", ""));
        surname.setText(sPref.getString("surname", ""));
        d_of_birth.setText(sPref.getString("d_of_birth", ""));
        country.setText(sPref.getString("country", ""));
        id_user =  sPref.getString("id_user", "");
           }

    private void procc_answer(JSONArray ja)
    {
        try {
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
                                + jo.getString("class")
                                + jo.getString("id_flight"));

                String id_flight = jo.getString("id_flight");
                String order_num_seat = jo.getString("order_num_seat");
                String name_class = "";
                if (Objects.equals(jo.getString("class"), "E")){
                    name_class= "Эконом";
                }
                if (Objects.equals(jo.getString("class"), "B")){
                    name_class= "Бизнес";
                }
                if (Objects.equals(jo.getString("class"), "F")){
                    name_class= "Первый";
                }
                Log.i("chat", "id_flight= "+id_flight);
                Log.i("chat", "id_flight= "+order_num_seat);
                Log.i("chat", "id_flight= "+name_class);
                prof_tabl.add(new prof_tick(id_flight, order_num_seat, name_class));
                i++;
            }
        }
        catch (Exception e)
        {
            Log.i("Profile",
                    "ошибка ответа сервера:\n"
                            + e.getMessage());
        }


    }
}

