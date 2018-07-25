package com.example.genya.myapplication;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;


public class t_table extends AppCompatActivity {
    String server_name = "http://r2551241.beget.tech";

    public ArrayList<String> p_of_dep = new ArrayList<String>();
    public ArrayList<String> destin = new ArrayList<String>();
    ArrayList<prof_tick> time_tabl = new ArrayList<prof_tick>();
    TimeAdapter adapter;
    int DIALOG_DATE = 1;
    int myYear = 0;
    int myMonth = 0;
    int myDay = 0;
    EditText edDate1;
    String s_depart="", s_arr="";
    Spinner depart, destine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("t_table", "OPEN CLASS T_TABLE");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_table);
        edDate1 = (EditText) findViewById(R.id.edDate1);
        depart = (Spinner) findViewById(R.id.s_depart);
        destine = (Spinner) findViewById(R.id.s_ariv);

        ConnDB connDB = new ConnDB();
        procc_answer(connDB.input(4));

        current_date(connDB.input(6));

        adapter = new TimeAdapter(this, time_tabl);
        Log.i("t_table", "создаем адаптер");
        // настраиваем список
        ListView lvMain = (ListView) findViewById(R.id.list_view_tables);
        lvMain.setAdapter(adapter);
        Log.i("t_table", "настроили список");
        //список городов


        //Пункты отбытия
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, p_of_dep);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Spinner depart = (Spinner) findViewById(R.id.s_depart);
        depart.setAdapter(adapter);

        //Пункты прибытия

        ArrayAdapter<String> adapter_dest = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, destin);
        adapter_dest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //final Spinner
        destine.setAdapter(adapter_dest);

        // устанавливаем обработчик нажатия для пунктов отбытия
        depart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
                Log.i("chat", "spinner1");
                //Toast.makeText(getBaseContext(), "Город = " + depart.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(getBaseContext(), "Position = " + depart.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                s_depart = depart.getSelectedItem().toString();
            }
        });

        // устанавливаем обработчик нажатия для пунктов прибытия
        destine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
                Log.i("chat", "spinner2");
                //Toast.makeText(getBaseContext(), "Город = " + destine.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                s_arr = destine.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
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
                break;
            case R.id.info_air:
                Intent inten = new Intent(this, info_air.class);
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

    public void ed_Date1(View view) {
        showDialog(DIALOG_DATE);
    }
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_DATE) {
            DatePickerDialog tpd = new DatePickerDialog(this, myCallBack, myYear, myMonth, myDay);
            return tpd;
        }
        return super.onCreateDialog(id);
    }
    DatePickerDialog.OnDateSetListener myCallBack = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myYear = year;
            myMonth = monthOfYear+1;
            myDay = dayOfMonth;
            edDate1.setText(myYear + "-" + myMonth + "-" + myDay);
            if (myMonth<10 && dayOfMonth < 10)
            {
                edDate1.setText(myYear + "-" + 0 + myMonth + "-" + 0 + myDay);
            }
            if (myMonth>=10 && dayOfMonth < 10)
            {
                edDate1.setText(myYear + "-" + myMonth + "-" + 0 + myDay);
            }
            if (myMonth<10 && dayOfMonth >= 10)
            {
                edDate1.setText(myYear + "-" + 0 + myMonth + "-" + myDay);
            }
        }
    };

    public void OK(View view) {
        if (!Objects.equals(s_depart, "") || !Objects.equals(s_arr, "") || !Objects.equals(edDate1.getText().toString(), ""))
        {
            time_tabl.clear();
            ConnDB connDB = new ConnDB();
            procc_answer_1(connDB.input(s_depart, s_arr, edDate1.getText().toString(), 5));
            adapter = new TimeAdapter(this, time_tabl);
            Log.i("t_table", "создаем адаптер");
            // настраиваем список
            ListView lvMain = (ListView) findViewById(R.id.list_view_tables);
            lvMain.setAdapter(adapter);
            Log.i("t_table", "настроили список");
        }
        else
        {
            Toast.makeText(getApplicationContext(),
                    "Укажите хотябы один критерий для поиска", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public void clear_info(View view) {
        edDate1.setText("");
        destine.setSelection(0);
        depart.setSelection(0);
        time_tabl.clear();

        ConnDB connDB = new ConnDB();
        procc_answer(connDB.input(4));

        adapter = new TimeAdapter(this, time_tabl);
        Log.i("t_table", "создаем адаптер");
        // настраиваем список
        ListView lvMain = (ListView) findViewById(R.id.list_view_tables);
        lvMain.setAdapter(adapter);
        Log.i("t_table", "настроили список");
        //список городов
    }

    private void procc_answer(JSONArray ja)
    {
        try {
            // ответ превратим в JSON массив
            JSONObject jo;

            Integer i = 0;
            Log.i("procc_answer: ", String.valueOf(ja.length()));
            destin.add("");
            p_of_dep.add("");
            while (i < ja.length()) {
                Log.i("procc_answer: ", String.valueOf(ja.getJSONObject(i)));
                // разберем JSON массив построчно
                jo = ja.getJSONObject(i);
                //Log.i("chat", jo.getString("POINT_OF_DEPARTURE") + " " + jo.getString("DESTINATION"));

                Log.i("procc_answer: ",
                        "=================>>> "
                                + jo.getString("point_of_dep")
                                + jo.getString("dest"));
                Integer y = 1, z=0, z1=0;


                while (y < p_of_dep.size())
                {
                    if (Objects.equals(p_of_dep.get(y), jo.getString("point_of_dep")))
                    {
                        z++;
                        break;
                    }
                    y++;
                }
                y=1;
                while (y < destin.size())
                {
                    if (Objects.equals(destin.get(y), jo.getString("dest")))
                    {
                        z1++;
                        break;
                    }
                    y++;
                }
                if (z==0)
                {
                    p_of_dep.add(jo.getString("point_of_dep"));
                }

                if (z1==0)
                {
                    destin.add(jo.getString("dest"));
                }

                String p_o_d = jo.getString("point_of_dep");
                String destin = jo.getString("dest");
                String date_ru = jo.getString("depart_date");
                Integer cost =  jo.getInt("cost");
                String id_flight =  jo.getString("id_flight");

                time_tabl.add(new prof_tick(p_o_d, destin, date_ru, cost, id_flight));
                i++;
            }
        } catch (Exception e) {
            // если ответ сервера не содержит валидный JSON
            Log.i("procc_answer: ",
                    "+ ошибка ответа сервера через класс ConnDB:\n"
                            + e.getMessage());
        }
    }
    private void procc_answer_1(JSONArray ja)
    {
        try {
            // ответ превратим в JSON массив
            JSONObject jo;

            Integer i = 0;
            Log.i("procc_answer_1: ", String.valueOf(ja.length()));
            while (i < ja.length()) {
                Log.i("procc_answer_1: ", String.valueOf(ja.getJSONObject(i)));
                // разберем JSON массив построчно
                jo = ja.getJSONObject(i);

                Log.i("procc_answer_1: ",
                        "=================>>> "
                                + jo.getString("point_of_dep")
                                + jo.getString("dest"));

                String p_o_d = jo.getString("point_of_dep");
                String destin = jo.getString("dest");
                String date_ru = jo.getString("depart_date");
                Integer cost =  jo.getInt("cost");
                String id_f =  jo.getString("id_flight");

                time_tabl.add(new prof_tick(p_o_d, destin, date_ru, cost, id_f));
                i++;
            }
        } catch (Exception e) {
            // если ответ сервера не содержит валидный JSON
            Log.i("procc_answer_1: ",
                    "+ ошибка ответа сервера через класс ConnDB:\n"
                            + e.getMessage());
        }
    }
    private void current_date(JSONArray ja)
    {
        JSONObject jo = null;
        try {
            jo = ja.getJSONObject(0);
            Log.i("t_table: ", "current_date + получение объекта JSON");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("t_table: ", "current_date + ошибка получения объекта JSON: " + e.getMessage());
        }
        String date = null;
        try {
            date = jo.getString("CURRENT_DATE()");
            Log.i("t_table: ", "current_date + получение текста JSON: " + date);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("t_table: ", "current_date + ошибка получения текста JSON: " + e.getMessage());
        }
        String[] s = date.split("-");
        myYear = Integer.parseInt(s[0]);
        myMonth = Integer.parseInt(s[1])-1;
        myDay = Integer.parseInt(s[2]);
    }
}
