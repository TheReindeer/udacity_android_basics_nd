package com.example.android.tourguideapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttractionsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.text_list, container, false);

        final ArrayList<Constructors> constructors = new ArrayList<>();
        constructors.add(new Constructors(R.string.attr_people_house, R.string.attr_people_descr, R.drawable.attr_peoples_house));
        constructors.add(new Constructors(R.string.attr_antipa, R.string.attr_antipa_descr, R.drawable.attr_antipa_museum));
        constructors.add(new Constructors(R.string.attr_center, R.string.attr_center_descr, R.drawable.attr_old_city_center));
        constructors.add(new Constructors(R.string.attr_herastrau, R.string.attr_herastrau_descr, R.drawable.attr_herastrau_park));
        constructors.add(new Constructors(R.string.attr_ior, R.string.attr_ior_descr, R.drawable.attr_ior_park));
        constructors.add(new Constructors(R.string.attr_revolution_square, R.string.attr_revolution_descr, R.drawable.attr_revolution_square));
        constructors.add(new Constructors(R.string.attr_victory_square, R.string.attr_victory_descr, R.drawable.attr_victory_square));
        constructors.add(new Constructors(R.string.attr_university_square, R.string.attr_university_descr, R.drawable.attr_university_square));
        constructors.add(new Constructors(R.string.attr_village_museum, R.string.attr_village_descr, R.drawable.attr_village_museum));
        constructors.add(new Constructors(R.string.attr_triumph_arc, R.string.attr_triumph_descr, R.drawable.attr_triumph_arc));

        ConstructorsAdapter adapter = new ConstructorsAdapter(getActivity(), constructors, R.color.category_attractions);
        ListView listView = (ListView) rootView.findViewById(R.id.text_list);
        listView.setAdapter(adapter);

        return rootView;
    }
}