package com.example.genya.myapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Genya on 12.01.2017.
 */

public class prof_adapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<prof_tick> objects;

    prof_adapter(Context context, ArrayList<prof_tick> prof_tick) {
        Log.i("prof_adapter", "Конструктор");
        ctx = context;
        objects = prof_tick;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    // кол-во элементов
    @Override
    public int getCount() {
        Log.i("prof_adapter", "Количество элементов");
        return objects.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        Log.i("prof_adapter", "Элементо по позиции");
        return objects.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        Log.i("prof_adapter", "id по позиции");
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        Log.i("prof_adapter", "public View getView");
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.list_profile, parent, false);
        }
        Log.i("prof_adapter", "Time_Table p = getProduct");
        prof_tick p = getProduct(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
        ((TextView) view.findViewById(R.id.id_flight)).setText(p.id_flight);
        ((TextView) view.findViewById(R.id.num_seat)).setText(p.num_seat);
        ((TextView) view.findViewById(R.id.name_class)).setText(p.name_class);

        Log.i("TimeAdapter", "заполняем View");
        return view;
    }

    // товар по позиции
    private prof_tick getProduct(int position) {
        Log.i("prof_adapter", "товар по позиции");
        return ((prof_tick) getItem(position));
    }
}
