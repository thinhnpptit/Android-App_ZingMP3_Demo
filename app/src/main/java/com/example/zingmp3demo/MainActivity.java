package com.example.zingmp3demo;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView title, totalTime, currentTime;
    ImageButton btnPlay, btnPrev, btnStop, btnPause, btnNext;
    SeekBar seekBar;
    ArrayList<Song> listSong;
    int posittion = 0;
    MediaPlayer mediaPlayer;
    ImageView ivDisc;
    Animation animation;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Mapping();
        InitSong();
        InitMediaPlayer();
        animation = AnimationUtils.loadAnimation(this,R.anim.disc_rotate);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    btnPlay.setImageResource(R.drawable.iconfinder_play_arrow_326577);
                    ivDisc.clearAnimation();
                } else {
                    mediaPlayer.start();
                    btnPlay.setImageResource(R.drawable.iconfinder_icon_pause_211871);
                    ivDisc.startAnimation(animation);
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                btnPlay.setImageResource(R.drawable.iconfinder_play_arrow_326577);
                InitMediaPlayer();
                ivDisc.clearAnimation();
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posittion--;
                if (posittion < 0) {
                    posittion = listSong.size() - 1;
                }
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                btnPlay.setImageResource(R.drawable.iconfinder_icon_pause_211871);
                InitMediaPlayer();
                mediaPlayer.start();
                ivDisc.startAnimation(animation);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posittion++;
                if (posittion > listSong.size() - 1) {
                    posittion = 0;
                }
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                btnPlay.setImageResource(R.drawable.iconfinder_icon_pause_211871);
                InitMediaPlayer();
                mediaPlayer.start();
                ivDisc.startAnimation(animation);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

    }

    private void UpdateCurrentTime() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                currentTime.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                // kiem tra neu ket thuc bai hat --> next
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        posittion++;
                        if (posittion > listSong.size() - 1) {
                            posittion = 0;
                        }
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                        }
                        btnPlay.setImageResource(R.drawable.iconfinder_icon_pause_211871);
                        InitMediaPlayer();
                        mediaPlayer.start();
                    }
                });
                handler.postDelayed(this, 500);
            }
        }, 100);
    }

    private void SetTotalTime() {
        totalTime.setText(simpleDateFormat.format(mediaPlayer.getDuration()) + "");
        seekBar.setMax(mediaPlayer.getDuration());
    }

    private void InitMediaPlayer() {
        mediaPlayer = MediaPlayer.create(MainActivity.this, listSong.get(posittion).getFile());
        title.setText(listSong.get(posittion).getTitle());
        SetTotalTime();
        currentTime.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()) + "");
        UpdateCurrentTime();
    }

    private void InitSong() {
        listSong = new ArrayList<>();
        listSong.add(new Song("Anh ơi ở lại", R.raw.anh_oi_o_lai));
        listSong.add(new Song("Gặp em đúng lúc", R.raw.gap_em_dung_luc));
        listSong.add(new Song("Lạ lùng", R.raw.la_lung));
        listSong.add(new Song("let me down slowly", R.raw.let_me_down_slowly));
        listSong.add(new Song("Sóng gió", R.raw.song_gio));
        listSong.add(new Song("Thanh xuân", R.raw.thanh_xuan));
        listSong.add(new Song("Yêu em dại khờ", R.raw.yeu_em_dai_kho));
    }

    private void Mapping() {
        title = findViewById(R.id.textviewTitle);
        totalTime = findViewById(R.id.txtTotalTime);
        currentTime = findViewById(R.id.txtCurrentTime);
        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);
        btnPlay = findViewById(R.id.btnPlay);
        btnStop = findViewById(R.id.btnStop);
        seekBar = findViewById(R.id.seekbar);
        ivDisc = findViewById(R.id.ivDisc);
    }
}
