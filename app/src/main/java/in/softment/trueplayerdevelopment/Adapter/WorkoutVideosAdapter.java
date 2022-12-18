package in.softment.trueplayerdevelopment.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import in.softment.trueplayerdevelopment.Model.VideoModel;
import in.softment.trueplayerdevelopment.PlayVideoActivity;
import in.softment.trueplayerdevelopment.R;
import in.softment.trueplayerdevelopment.SignUpActivity;
import in.softment.trueplayerdevelopment.TrainingRoomVideosActivity;
import in.softment.trueplayerdevelopment.Util.Services;

public class WorkoutVideosAdapter extends RecyclerView.Adapter<WorkoutVideosAdapter.ViewHolder> {
    private Context context;
    private final ArrayList<VideoModel> videoModels;

    public WorkoutVideosAdapter(Context context, ArrayList<VideoModel> videoModels){
        this.context = context;
        this.videoModels = videoModels;
    }

    @NonNull
    @Override
    public WorkoutVideosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_video_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutVideosAdapter.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        VideoModel videoModel = videoModels.get(position);
        if (!videoModel.getThumbnail().isEmpty()) {
            Glide.with(context).load(videoModel.getThumbnail()).placeholder(R.drawable.placeholder1).into(holder.videoImage);
        }
        holder.title.setText(videoModel.getTitle());
        holder.duration.setText(Services.convertSecondsToMinutesAndSeconds(videoModel.getVideoLength())+" min");
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayVideoActivity.class);
                intent.putExtra("videoModels",videoModels);
                intent.putExtra("position",holder.getAbsoluteAdapterPosition());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (videoModels.size() > 0) {
            ((TrainingRoomVideosActivity)context).no_videos_available.setVisibility(View.GONE);
        }
        else {
            ((TrainingRoomVideosActivity)context).no_videos_available.setVisibility(View.VISIBLE);
        }

        return videoModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView videoImage;
        private TextView title, duration;
        private View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            this.duration = itemView.findViewById(R.id.duration);
            this.videoImage = itemView.findViewById(R.id.workoutImage);
            this.title = itemView.findViewById(R.id.title);
        }
    }
}
