package com.example.genya.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Genya on 05.12.2016.
 */

public class TimeAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<prof_tick> objects;

    TimeAdapter(Context context, ArrayList<prof_tick> time_tabl) {
        Log.i("TimeAdapter", "Конструктор");
        ctx = context;
        objects = time_tabl;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    // кол-во элементов
    @Override
    public int getCount() {
        Log.i("TimeAdapter", "Количество элементов");
        return objects.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        Log.i("TimeAdapter", "Элементо по позиции");
        return objects.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        Log.i("TimeAdapter", "id по позиции");
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        Log.i("TimeAdapter", "public View getView");
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.list, parent, false);
        }
        Log.i("TimeAdapter", "Time_Table p = getProduct");
        prof_tick p = getProduct(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
        ((TextView) view.findViewById(R.id.dest)).setText(p.depart);
        ((TextView) view.findViewById(R.id.point_of_dest)).setText(p.arrived);
        ((TextView) view.findViewById(R.id.date)).setText(p.date);
        ((TextView) view.findViewById(R.id.cost_table)).setText(p.cost+"");
        ((TextView) view.findViewById(R.id.id_fly)).setText(p.id_flight);
        Log.i("TimeAdapter", "заполняем View");
        return view;
    }

    // товар по позиции
    prof_tick getProduct(int position) {
        Log.i("TimeAdapter", "товар по позиции");
        return ((prof_tick) getItem(position));
    }
}
