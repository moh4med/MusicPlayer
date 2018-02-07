package com.example.mohamed.musicplayer;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mohamed.musicplayer.MusicAdapter;
import com.example.mohamed.musicplayer.R;

import java.util.ArrayList;

/**
 * Created by coderzpassion on 05/04/16.
 */
public class viewPagerAdapter extends Fragment {
    Cursor cursor;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView recyclerView=(RecyclerView) inflater.inflate(R.layout.content_home,container,false);
        setUpRecyclerView(recyclerView);
        return  recyclerView;
    }

    private void setUpRecyclerView(RecyclerView rv)
    {
        Cursor c = rv.getContext().getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                null, null, null);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rv.getContext(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        MusicAdapter musicAdapter = new MusicAdapter(rv.getContext(), c);
        rv.setAdapter(musicAdapter);
    }

}