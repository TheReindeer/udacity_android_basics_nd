package com.example.android.tourguideapp;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodFragment extends Fragment {

    private MediaPlayer mMediaPlayer;

    private AudioManager mAudioManager;

    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };

    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                            focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        mMediaPlayer.pause();
                        mMediaPlayer.seekTo(0);
                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        mMediaPlayer.start();
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        releaseMediaPlayer();
                    }
                }
            };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.text_list, container, false);

        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Constructors> constructors = new ArrayList<>();
        constructors.add(new Constructors(R.string.food_ciorba_burta, R.string.food_ciorba_burta_descr,
                R.drawable.food_ciorba_de_burta, R.drawable.ic_play_arrow_white_24dp, R.raw.raw_ciorba_burta));
        constructors.add(new Constructors(R.string.food_ciorba_fasole, R.string.food_ciorba_fasole_descr,
                R.drawable.food_ciorba_de_fasole, R.drawable.ic_play_arrow_white_24dp, R.raw.raw_ciorba_fasole));
        constructors.add(new Constructors(R.string.food_sarmale, R.string.food_sarmale_descr,
                R.drawable.food_sarmale, R.drawable.ic_play_arrow_white_24dp, R.raw.raw_sarmale));
        constructors.add(new Constructors(R.string.food_bulz, R.string.food_bulz_descr,
                R.drawable.food_bulz, R.drawable.ic_play_arrow_white_24dp, R.raw.raw_bulz));
        constructors.add(new Constructors(R.string.food_carnati_afumati, R.string.food_carnati_afumati_descr,
                R.drawable.food_carnati_afumati, R.drawable.ic_play_arrow_white_24dp, R.raw.raw_carnati_afumati));
        constructors.add(new Constructors(R.string.food_jumari, R.string.food_jumari_descr,
                R.drawable.food_jumari, R.drawable.ic_play_arrow_white_24dp, R.raw.raw_jumari));
        constructors.add(new Constructors(R.string.food_mici, R.string.food_mici_descr,
                R.drawable.food_mititei, R.drawable.ic_play_arrow_white_24dp, R.raw.raw_mici));
        constructors.add(new Constructors(R.string.food_sunca_afumata, R.string.food_sunca_afumata_descr,
                R.drawable.food_sunca_afumata, R.drawable.ic_play_arrow_white_24dp, R.raw.raw_sunca_afumata));
        constructors.add(new Constructors(R.string.food_porumb_copt, R.string.food_porumb_copt_descr,
                R.drawable.food_porumb_copt, R.drawable.ic_play_arrow_white_24dp, R.raw.raw_porumb_copt));
        constructors.add(new Constructors(R.string.food_cozonac, R.string.food_cozonac_descr,
                R.drawable.food_cozonac, R.drawable.ic_play_arrow_white_24dp, R.raw.raw_cozonac));

        ConstructorsAdapter adapter = new ConstructorsAdapter(getActivity(), constructors, R.color.category_food);
        ListView listView = (ListView) rootView.findViewById(R.id.text_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Constructors constr = constructors.get(position);
                releaseMediaPlayer();

                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener, AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                    mMediaPlayer = MediaPlayer.create(getActivity(), constr.getmAudioResourceId());
                    mMediaPlayer.start();
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer(){
        if (mMediaPlayer != null){
            mMediaPlayer.release();
            mMediaPlayer = null;
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }
}
