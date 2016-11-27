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
public class HotelsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.text_list, container, false);

        final ArrayList<Constructors> constructors = new ArrayList<>();
        constructors.add(new Constructors(R.string.hotel_intercontinental, R.string.hotel_intercontinental_descr, R.drawable.star_five));
        constructors.add(new Constructors(R.string.hotel_christina, R.string.hotel_christina_descr, R.drawable.star_four));
        constructors.add(new Constructors(R.string.hotel_epoque, R.string.hotel_epoque_descr, R.drawable.star_five));
        constructors.add(new Constructors(R.string.hotel_cismigiu, R.string.hotel_cismigiu_descr, R.drawable.star_four));
        constructors.add(new Constructors(R.string.hotel_rembrandt, R.string.hotel_rembrandt_descr, R.drawable.star_three));
        constructors.add(new Constructors(R.string.hotel_jw_marriott, R.string.hotel_jw_marriott_descr, R.drawable.star_five));
        constructors.add(new Constructors(R.string.hotel_mercure, R.string.hotel_mercure_descr, R.drawable.star_five));
        constructors.add(new Constructors(R.string.hotel_capitol, R.string.hotel_capitol_descr, R.drawable.star_five));
        constructors.add(new Constructors(R.string.hotel_grand_continental, R.string.hotel_grand_continental_decr, R.drawable.star_five));
        constructors.add(new Constructors(R.string.hotel_athenee_palace_hilton, R.string.hotel_athenee_palace_hilton_descr, R.drawable.star_five));

        ConstructorsAdapter adapter = new ConstructorsAdapter(getActivity(), constructors, R.color.category_hotels);
        ListView listView = (ListView) rootView.findViewById(R.id.text_list);
        listView.setAdapter(adapter);

        return rootView;
    }

}
