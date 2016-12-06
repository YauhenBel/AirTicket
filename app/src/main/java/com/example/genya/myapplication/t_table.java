package com.example.genya.myapplication;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class t_table extends AppCompatActivity {
    String server_name = "http://10.0.2.2/scripts";

    public ArrayList<String> p_of_dep = new ArrayList<String>();
    public ArrayList<String> destin = new ArrayList<String>();
    String[] times = {"00:00", "01:00", "02:00", "03:00", "04:00", "00:00", "00:00", "00:00", "00:00"};
    ArrayList<Time_Table> time_tabl = new ArrayList<Time_Table>();
    TimeAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_table);




        //список рейсов

        List_Time_Table list_time_table = new List_Time_Table();
        list_time_table.execute();
        time_tabl = list_time_table.getList();
        Log.i("t_table", "сотавили список рейсов");
        adapter = new TimeAdapter(this, time_tabl);
        Log.i("t_table", "создаем адаптер");
        // настраиваем список
        ListView lvMain = (ListView) findViewById(R.id.list_view_tables);
        lvMain.setAdapter(adapter);
        Log.i("t_table", "настроили список");
        //список городов
        List_City_Table list_city_table = new List_City_Table();
        list_city_table.execute();

        //Пункты отбытия
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, p_of_dep);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner depart = (Spinner) findViewById(R.id.s_depart);
        depart.setAdapter(adapter);

        //Пункты прибытия

        ArrayAdapter<String> adapter_dest = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, destin);
        adapter_dest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final Spinner destine = (Spinner) findViewById(R.id.s_ariv);
        destine.setAdapter(adapter_dest);

        // устанавливаем обработчик нажатия для пунктов отбытия
        depart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
                Log.i("chat", "spinner1");
                //Toast.makeText(getBaseContext(), "Город = " + depart.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getBaseContext(), "Position = " + depart.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        // устанавливаем обработчик нажатия для пунктов прибытия
        destine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
                Log.i("chat", "spinner2");
                Toast.makeText(getBaseContext(), "Город = " + destine.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    void adapter(ArrayList<Time_Table> time_tabls) {


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_t_table, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.prifile:
                Intent intent = new Intent(this, profile.class);
                startActivity(intent);
            case R.id.out:
                this.finishAffinity();
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private class List_City_Table extends AsyncTask<Void, Void, Integer> {

        Integer res;
        HttpURLConnection conn;
        String ansver;

        @SuppressWarnings({"WrongThread", "deprecation"})
        protected Integer doInBackground(Void... params) {

            try {
                String t_table = server_name +"/text.php?action=t_table";
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
                        Log.i("chat", jo.getString("POINT_OF_DEPARTURE") + " " + jo.getString("DESTINATION"));

                        Log.i("chat",
                                "=================>>> "
                                        + jo.getString("POINT_OF_DEPARTURE")
                                        + jo.getString("DESTINATION"));
                        Integer y = 0, z=0, z1=0;


                        while (y < p_of_dep.size())
                        {
                            if (Objects.equals(p_of_dep.get(y), jo.getString("POINT_OF_DEPARTURE")))
                            {
                             z++;
                                break;
                            }
                            y++;
                        }


                        y=0;
                        while (y < destin.size())
                        {
                            if (Objects.equals(destin.get(y), jo.getString("DESTINATION")))
                            {
                                z1++;
                                break;
                            }
                            y++;
                        }


                        if (z==0)
                        {
                            p_of_dep.add(jo.getString("POINT_OF_DEPARTURE"));
                        }


                        if (z1==0)
                        {
                            destin.add(jo.getString("DESTINATION"));
                        }
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

    }


}
