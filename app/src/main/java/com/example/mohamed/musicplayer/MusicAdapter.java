package com.example.mohamed.musicplayer;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by CompuCity on 2/6/2018.
 */

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.musicviewholder> {
    private final String TAG = MusicAdapter.class.getSimpleName();
    Context mcontext;
    Cursor mcursor;
    MediaPlayer mp;

    MusicAdapter(Context context, Cursor cursor) {
        mcontext = context;
        mcursor = cursor;
        mp = new MediaPlayer();
    }

    @Override
    public MusicAdapter.musicviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_view, parent, false);
        musicviewholder mviewholder = new musicviewholder(view);
        return mviewholder;
    }

    @Override
    public void onBindViewHolder(MusicAdapter.musicviewholder holder, final int position) {
        // Log.e(TAG,""+position);
        TextView tv_track_name = holder.tv_track_name;
        if (mcursor.moveToPosition(position)) {
            String name = mcursor.getString(mcursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
            tv_track_name.setText(name);
            holder.button_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mcursor.moveToPosition(position);
                    String path = mcursor.getString(mcursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    Log.e(TAG, "playing  id : " + position + " name:" + path);
                    try {
                        mp.reset();
                        mp.setDataSource(path);
                        mp.prepare();
                        mp.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (mcursor != null) {
            return mcursor.getCount();
        }
        return 0;
    }

    class musicviewholder extends RecyclerView.ViewHolder {
        TextView tv_track_name;
        Button button_play;

        public musicviewholder(View itemView) {
            super(itemView);
            tv_track_name = (TextView) itemView.findViewById(R.id.track_name);
            button_play = (Button) itemView.findViewById(R.id.button_play);
        }
    }
}
