package com.example.genya.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class profile extends AppCompatActivity {

    TextView name, surname, d_of_birth, country, gender, currency;

    SharedPreferences sPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = (TextView)  findViewById(R.id.name);
        surname = (TextView) findViewById(R.id.surname);
        d_of_birth = (TextView) findViewById(R.id.d_of_birth);
        country = (TextView) findViewById(R.id.country);
        gender = (TextView) findViewById(R.id.gender);
        currency = (TextView) findViewById(R.id.currency);

        info();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       int id = item.getItemId();
        switch (id)
        {
            case R.id.t_table:
                Intent intent = new Intent(this, t_table.class);
                startActivity(intent);
            case R.id.out:
                this.finishAffinity();
                default:
                    return super.onOptionsItemSelected(item);
        }
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
        currency.setText(sPref.getString("currency", ""));
        gender.setText(sPref.getString("gender", ""));
           }

}
