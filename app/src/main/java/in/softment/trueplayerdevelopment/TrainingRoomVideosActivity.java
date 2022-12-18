package in.softment.trueplayerdevelopment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.softment.trueplayerdevelopment.Adapter.WorkoutVideosAdapter;
import in.softment.trueplayerdevelopment.Interface.VideoArrayCallback;
import in.softment.trueplayerdevelopment.Model.VideoModel;
import in.softment.trueplayerdevelopment.Util.ProgressHud;
import in.softment.trueplayerdevelopment.Util.Services;

public  class TrainingRoomVideosActivity extends AppCompatActivity {

    private TextView categoryName;
    private WorkoutVideosAdapter workoutVideosAdapter;
    public LinearLayout no_videos_available;
    private ArrayList<VideoModel> videoModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeColor();
        setContentView(R.layout.activity_training_room_videos);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //CATEGORY_NAME
        categoryName = findViewById(R.id.categoryName);
        categoryName.setText(getIntent().getStringExtra("categoryTitle"));

        //NO_VIDEOS_AVAILABLE
        no_videos_available = findViewById(R.id.no_videos_available_ll);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        workoutVideosAdapter = new WorkoutVideosAdapter(this, videoModels);
        recyclerView.setAdapter(workoutVideosAdapter);

        String categoryId = getIntent().getStringExtra("categoryId");

        ProgressHud.show(this,"Loading...");
        Services.getVideosForCategory(this, categoryId, new VideoArrayCallback() {
            @Override
            public void oncompletioncallback(ArrayList<VideoModel> videoModels) {
                ProgressHud.dialog.dismiss();
                if (videoModels != null) {
                    TrainingRoomVideosActivity.this.videoModels.addAll(videoModels);
                    workoutVideosAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void changeColor() {
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.light_neon_green));

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.light_neon_green)));
        }

    }
}
