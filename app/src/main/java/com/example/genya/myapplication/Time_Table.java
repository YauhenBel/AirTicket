package com.example.genya.myapplication;

import android.app.Activity;

/**
 * Created by Genya on 05.12.2016.
 */

public class Time_Table {

String depart, arrived, date;
    Integer cost, id;

Time_Table(String _depart, String _arrived, String _date, Integer _cost, Integer _id)
{
    depart = _depart;
    arrived = _arrived;
    date = _date;
    cost = _cost;
    id = _id;
}

    @Override
    public String toString() {
        return "Пункт вылета: "+depart+" Пункт прибытия: "+arrived+" Дата вылета: "+date+" Стоимость: "+cost+
                " Номер рейса: "+id;
    }
}
