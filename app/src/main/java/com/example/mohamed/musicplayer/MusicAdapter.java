package com.example.mohamed.musicplayer;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by CompuCity on 2/6/2018.
 */

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.musicviewholder> {
    private final String TAG = MusicAdapter.class.getSimpleName();
    private final Handler seekbarprogresshandler;
    Context mcontext;
    Cursor mcursor;
    MediaPlayer mp;
    MediaPlayer dummymp;
    private int currentplayingid;
    private int currenttracktime;
    int cnt = 0;

    MusicAdapter(Context context, Cursor cursor) {
        mcontext = context;
        mcursor = cursor;
        mp = new MediaPlayer();
        mp.setLooping(false);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                musicviewholder currentplayidvh = (musicviewholder) ((MainActivity) mcontext).rv_music_tracks.findViewHolderForAdapterPosition(currentplayingid);
                if (currentplayidvh != null) {
                    Log.e(TAG, "stop  id : " + currentplayingid + " name:" + currentplayidvh.tv_track_name.getText().toString());
                    currentplayidvh.button_play.setTag("play");
                    currentplayidvh.button_play.setText("play");
                    currentplayidvh.tv_track_name.setSelected(false);
                }

            }
        });
        seekbarprogresshandler=new Handler();
        seekbarprogresshandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                musicviewholder currentplayidvh = (musicviewholder) ((MainActivity) mcontext).rv_music_tracks.findViewHolderForAdapterPosition(currentplayingid);
                int deltime=300;
                if(mp.isPlaying()&&currentplayidvh!=null){
                    Log.e(TAG, "updating seek bar " + mp.getCurrentPosition());
                    currentplayidvh.seek_bar_time.setProgress(100*mp.getCurrentPosition()/mp.getDuration());
                    deltime=mp.getDuration()/100;
                    long millis=mp.getCurrentPosition();
                    String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                            TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                    currentplayidvh.tv_current_time.setText(hms);
                }
                seekbarprogresshandler.postDelayed(this, deltime);
            }
        }, 300);
        currentplayingid = -1;
    }

    @Override
    public MusicAdapter.musicviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_view, parent, false);
        musicviewholder mviewholder = new musicviewholder(view);
        return mviewholder;
    }

    @Override
    public void onBindViewHolder(final MusicAdapter.musicviewholder holder, final int position) {
        // Log.e(TAG,""+position);
        final TextView tv_track_name = holder.tv_track_name;
        if (mcursor.moveToPosition(position)) {
            String name = mcursor.getString(mcursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
            final long millis = mcursor.getInt(mcursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            tv_track_name.setText(name);
            holder.button_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ButtonPlayClicked(holder, v, position);
                }
            });
            holder.seek_bar_time.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    int t=seekBar.getProgress()*(int)millis/100;
                    mp.seekTo(t);
                    mp.start();
                }
            });
            if (position == currentplayingid) {
                holder.button_play.setTag("pause");
                holder.button_play.setText("pause");
                holder.tv_track_name.setSelected(true);
                holder.seek_bar_time.setEnabled(true);
            } else if (holder.button_play.getTag().equals("pause")) {
                holder.button_play.setTag("play");
                holder.button_play.setText("play");
                holder.tv_track_name.setSelected(false);
                holder.tv_current_time.setText("00:00:00");
                holder.seek_bar_time.setProgress(0);
                holder.seek_bar_time.setEnabled(false);
            }else{
                holder.seek_bar_time.setEnabled(false);
            }
            String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            holder.tv_full_time.setText(hms);
        }
    }

    private void ButtonPlayClicked(musicviewholder holder, View v, int position) {
        if (v.getTag().equals("play")) {
            holder.tv_track_name.setSelected(true);
            mcursor.moveToPosition(position);
            String path = mcursor.getString(mcursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            if (currentplayingid == position) {
                mp.seekTo(currenttracktime);
                holder.seek_bar_time.setEnabled(true);
                mp.start();
                Log.e(TAG, "resume  id : " + position + " name:" + path);
            } else {
                if (currentplayingid != -1) {           //return the playing music text view to its initial value
                    musicviewholder currentplayidvh = (musicviewholder) ((MainActivity) mcontext).rv_music_tracks.findViewHolderForAdapterPosition(currentplayingid);
                    if (currentplayidvh != null) {
                        Log.e(TAG, "stop  id : " + currentplayingid + " name:" + currentplayidvh.tv_track_name.getText().toString());
                        currentplayidvh.button_play.setTag("play");
                        currentplayidvh.button_play.setText("play");
                        currentplayidvh.tv_track_name.setSelected(false);
                        currentplayidvh.seek_bar_time.setProgress(0);
                        currentplayidvh.tv_current_time.setText("00:00:00");
                        currentplayidvh.seek_bar_time.setEnabled(false);
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
            holder.seek_bar_time.setEnabled(true);
            ((Button) v).setText("pause");
        } else {
            currenttracktime = mp.getCurrentPosition();
            mp.pause();
            v.setTag("play");
            ((Button) v).setText("play");
            holder.tv_track_name.setSelected(false);
            holder.seek_bar_time.setEnabled(false);
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
        TextView tv_current_time;
        TextView tv_full_time;
        TextView tv_track_name;
        Button button_play;
        SeekBar seek_bar_time;

        public musicviewholder(View itemView) {
            super(itemView);
            tv_track_name = (TextView) itemView.findViewById(R.id.track_name);
            tv_current_time = (TextView) itemView.findViewById(R.id.tv_current_time);
            tv_full_time = (TextView) itemView.findViewById(R.id.tv_full_time);
            button_play = (Button) itemView.findViewById(R.id.button_play);
            seek_bar_time = (SeekBar) itemView.findViewById(R.id.seek_bar_time);
        }
    }
}
