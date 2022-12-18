package in.softment.trueplayerdevelopment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;

public class PlayLandscapeVideoActivity extends AppCompatActivity {

    private ExoPlayer exoPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
// Hide status bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_play_landscape_video);

        exoPlayer = new ExoPlayer.Builder(this).build();
        StyledPlayerView styledPlayerView = findViewById(R.id.exoplayer);
        styledPlayerView.setPlayer(exoPlayer);

        String link = getIntent().getStringExtra("link");
        long position = getIntent().getLongExtra("position",0);
        MediaItem mediaItem = MediaItem.fromUri(link);
        exoPlayer.setMediaItem(mediaItem);
        if (position > 0) {
            exoPlayer.seekTo(position);
        }

        exoPlayer.prepare();
        exoPlayer.play();

    }

    @Override
    protected void onPause() {
        super.onPause();

            exoPlayer.stop();


    }
}
