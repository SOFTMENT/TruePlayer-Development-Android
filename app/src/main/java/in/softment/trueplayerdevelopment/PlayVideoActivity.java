package in.softment.trueplayerdevelopment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.StyledPlayerView;

import java.util.ArrayList;

import in.softment.trueplayerdevelopment.Model.VideoModel;
import in.softment.trueplayerdevelopment.Util.Services;

public class PlayVideoActivity extends AppCompatActivity {
    private ExoPlayer exoPlayer;
    private Boolean hasFullScreen = false;
    private CardView fullScreen;
    private ArrayList<VideoModel> videoModels;
    private int position = 0;
    private TextView videoTitle, duration, level, previous, next;
    private LinearLayout playPauseBtn;
    private ImageView playPauseImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        videoModels = (ArrayList<VideoModel>) getIntent().getSerializableExtra("videoModels");
        position = getIntent().getIntExtra("position",0);

        fullScreen = findViewById(R.id.fullScreen);

        exoPlayer = new ExoPlayer.Builder(this).build();
        StyledPlayerView styledPlayerView = findViewById(R.id.exoplayer);
        styledPlayerView.setPlayer(exoPlayer);

        hasFullScreen = false;
        //BACK
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (exoPlayer.isPlaying()) {
                        exoPlayer.stop();
                    }
                    finish();


            }
        });


        fullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoModel videoModel = videoModels.get(position);
                Intent intent = new Intent(PlayVideoActivity.this,PlayLandscapeVideoActivity.class);
                intent.putExtra("link",videoModel.getVideoUrl());
                intent.putExtra("position",exoPlayer.getCurrentPosition());
                startActivity(intent);
            }
        });

        videoTitle = findViewById(R.id.videotitle);
        level = findViewById(R.id.level);
        duration = findViewById(R.id.duration);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        playPauseBtn = findViewById(R.id.playPauseBtn);
        playPauseImage = findViewById(R.id.playPauseImage);

        playPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exoPlayer != null) {
                    if (exoPlayer.isPlaying()) {
                        exoPlayer.pause();

                    }
                    else {
                        exoPlayer.play();

                    }
                }

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position < videoModels.size() - 1) {
                    previous.setVisibility(View.VISIBLE);
                    position = position + 1;

                }

            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position > 0) {
                    position = position - 1;

                }

            }
        });

        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                Player.Listener.super.onIsPlayingChanged(isPlaying);
                if (isPlaying) {
                    playPauseImage.setImageResource(R.drawable.pause_white);
                }
                else {
                    playPauseImage.setImageResource(R.drawable.play_white);
                }
            }
        });

        updateUI();

    }





    public void updateUI(){

        VideoModel videoModel = videoModels.get(position);
        MediaItem mediaItem = MediaItem.fromUri(videoModel.getVideoUrl());
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.play();

        videoTitle.setText(videoModel.getTitle());
        level.setText(String.format("%02d", videoModel.getLevel()));
        int min = videoModel.getVideoLength() / 60;
        if (min == 0) {
            min = 1;
        }
        duration.setText(min+" Min");

    }
    @Override
    protected void onPause() {
        super.onPause();
        exoPlayer.stop();
    }
}




