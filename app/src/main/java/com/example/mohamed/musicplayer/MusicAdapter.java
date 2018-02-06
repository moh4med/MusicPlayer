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
    private int currentplayingid;
    private int currenttracktime;

    MusicAdapter(Context context, Cursor cursor) {
        mcontext = context;
        mcursor = cursor;
        mp = new MediaPlayer();
        currentplayingid = -1;
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
                    if (v.getTag().equals("play")) {
                        mcursor.moveToPosition(position);
                        String path = mcursor.getString(mcursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                        if (currentplayingid == position) {
                            mp.seekTo(currenttracktime);
                            mp.start();
                            Log.e(TAG, "resume  id : " + position + " name:" + path);
                        } else {
                            if (currentplayingid != -1) {           //return the playing music text view to its initial value
                                musicviewholder currentplayidvh = (musicviewholder) ((MainActivity) mcontext).rv_music_tracks.findViewHolderForAdapterPosition(currentplayingid);
                                if (currentplayidvh != null) {
                                    Log.e(TAG, "stop  id : " + currentplayingid + " name:" + currentplayidvh.tv_track_name.getText().toString());
                                    currentplayidvh.button_play.setTag("play");
                                    currentplayidvh.button_play.setText("play");
                                }
                            }
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
                        currentplayingid = position;
                        v.setTag("pause");
                        ((Button) v).setText("pause");
                    } else {
                        currenttracktime = mp.getCurrentPosition();
                        mp.pause();
                        v.setTag("play");
                        ((Button) v).setText("play");
                    }
                }
            });
            if (position == currentplayingid) {
                holder.button_play.setTag("pause");
                holder.button_play.setText("pause");
            }else if (holder.button_play.getTag().equals("pause")) {
                holder.button_play.setTag("play");
                holder.button_play.setText("play");
            }
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
