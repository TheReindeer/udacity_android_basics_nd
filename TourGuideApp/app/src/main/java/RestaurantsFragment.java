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
public class RestaurantsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.text_list, container, false);

        final ArrayList<Constructors> constructors = new ArrayList<>();
        constructors.add(new Constructors(R.string.rest_zahana, R.string.rest_zahana_descr, R.drawable.star_five));
        constructors.add(new Constructors(R.string.rest_zexe, R.string.rest_zexe_descr, R.drawable.star_five));
        constructors.add(new Constructors(R.string.rest_angus, R.string.rest_angus_descr, R.drawable.star_five));
        constructors.add(new Constructors(R.string.rest_beef, R.string.rest_beef_descr, R.drawable.star_five));
        constructors.add(new Constructors(R.string.rest_amvrosia, R.string.rest_amvrosia_descr, R.drawable.star_five));
        constructors.add(new Constructors(R.string.rest_argentine, R.string.rest_argentine_descr, R.drawable.star_five));
        constructors.add(new Constructors(R.string.rest_veranda, R.string.rest_veranda_descr, R.drawable.star_five));
        constructors.add(new Constructors(R.string.rest_stejarii, R.string.rest_stejarii_descr, R.drawable.star_five));
        constructors.add(new Constructors(R.string.rest_mandarin, R.string.rest_mandarin_descr, R.drawable.star_four));
        constructors.add(new Constructors(R.string.rest_doina, R.string.rest_doina_descr, R.drawable.star_four));

        ConstructorsAdapter adapter = new ConstructorsAdapter(getActivity(), constructors, R.color.category_restaurants);
        ListView listView = (ListView) rootView.findViewById(R.id.text_list);
        listView.setAdapter(adapter);

        return rootView;
    }
}
