package com.example.genya.myapplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Genya on 12.01.2017.
 */

public class prof_tick {

    String id_flight, num_seat, name_class, depart, arrived, date;
    Integer cost;
    ArrayList<prof_tick> prof_tabl;
    ArrayList<String> p_of_dep, destin;




    prof_tick(String _id_flight, String _num_seat, String _name_class)
    {
        id_flight = _id_flight;
        num_seat = _num_seat;
        name_class = _name_class;
    }

    prof_tick(String _depart, String _arrived, String _date, Integer _cost, String _id_flight)
    {
        depart = _depart;
        arrived = _arrived;
        date = _date;
        cost = _cost;
        id_flight = _id_flight;
    }
    prof_tick(ArrayList<prof_tick> _prof_tabl, ArrayList<String> _p_of_dep, ArrayList<String> _destin)
    {
        prof_tabl = _prof_tabl;
        p_of_dep = _p_of_dep;
        destin = _destin;
    }




    @Override
    public String toString()
    {
        return id_flight+" "+num_seat+" "+name_class;
    }

    /*public Map<String, String> hashMap()
    {
        Map<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("id_flight", id_flight);
        hashMap.put("num_seat", num_seat);
        hashMap.put("name_class", name_class);
        return hashMap;
    }*/
}
