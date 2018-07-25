package com.example.genya.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

public class register extends AppCompatActivity {
    int DIALOG_DATE = 1;
    int myYear = 2017;
    int myMonth = 01;
    int myDay = 01;
    EditText edDate, edLogin, edPassw, edPassw1, edName, edSurname, edCountry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edDate = (EditText) findViewById(R.id.edDate);
        edName = (EditText) findViewById(R.id.ed_Name);
        edSurname = (EditText) findViewById(R.id.edSurname);
        edLogin = (EditText) findViewById(R.id.edLogin);
        edPassw = (EditText) findViewById(R.id.edPassw);
        edPassw1 = (EditText)  findViewById(R.id.ed_Passw1);
        edCountry = (EditText) findViewById(R.id.edCountry);
    }

    public void ed_Date(View view) {
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
            if (myMonth<10 && dayOfMonth < 10)
            {
                edDate.setText(myYear + "-" + 0 + myMonth + "-" + 0 + myDay);
            }
            if (myMonth>=10 && dayOfMonth < 10)
            {
                edDate.setText(myYear + "-" + myMonth + "-" + 0 + myDay);
            }
            if (myMonth<10 && dayOfMonth >= 10)
            {
                edDate.setText(myYear + "-" + 0 + myMonth + "-" + myDay);
            }
        }
    };

    public void btn_reg(View view) {
        String passw = edPassw.getText().toString();
        String passw1 = edPassw1.getText().toString();
        if (Objects.equals(passw, passw1))
        {
            String _edName = edName.getText().toString().trim();
            String _edSurname = edSurname.getText().toString().trim();
            String _edCountry = edCountry.getText().toString().trim();
            String _edDate = edDate.getText().toString().trim();
            String _edLogin = edLogin.getText().toString().trim();
            Integer x;
            ConnDB connDB = new ConnDB();
            x = connDB.input(_edName, _edSurname, _edCountry, _edDate, _edLogin, passw, 1);
            procc_answer(x);

        }
        else {
            Toast.makeText(getApplicationContext(),
                    "Пароли не совпадают", Toast.LENGTH_SHORT)
                    .show();
        }

    }

    private void procc_answer(Integer x) {
        if (x == 1)
        {
            intnts();
        }
        else
        {
            Toast.makeText(getApplicationContext(),
                    "Ошибка регистрации", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public void intnts()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
