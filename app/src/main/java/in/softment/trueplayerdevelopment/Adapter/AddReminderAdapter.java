package in.softment.trueplayerdevelopment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Calendar;

import in.softment.trueplayerdevelopment.AddReminderActivity;
import in.softment.trueplayerdevelopment.Model.CalendarWorkoutModel;
import in.softment.trueplayerdevelopment.Model.VideoModel;
import in.softment.trueplayerdevelopment.R;
import in.softment.trueplayerdevelopment.Util.Constants;
import in.softment.trueplayerdevelopment.Util.Services;

public class AddReminderAdapter extends RecyclerView.Adapter<AddReminderAdapter.ViewHolder> {
    private Context context;
    private ArrayList<VideoModel> videoModels;
    private int checkedPosition = -1;
    public AddReminderAdapter(Context context, ArrayList<VideoModel> videoModels){
        this.videoModels = videoModels;
        this.context = context;
    }

    @NonNull
    @Override
    public AddReminderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_workout_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddReminderAdapter.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        VideoModel videoModel = videoModels.get(position);
        Glide.with(context).load(videoModel.thumbnail).placeholder(R.drawable.placeholder).into(holder.roundedImageView);
        holder.title.setText(videoModel.title);
        holder.category.setText(Constants.Category.getCategoryName(videoModel.getCategoryId()));
        holder.duration.setText(Services.convertSecondsToMinutesAndSeconds(videoModel.getVideoLength()));

        holder.checkBox.setChecked(holder.getBindingAdapterPosition() == checkedPosition);

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    checkedPosition = holder.getBindingAdapterPosition();
                    ((AddReminderActivity)context).addCalendarModel(videoModel.id);
                    notifyDataSetChanged();
                }
                else {
                    checkedPosition = -1;
                    ((AddReminderActivity)context).removeCalendarModel();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (videoModels.size() > 0) {
            ((AddReminderActivity)context).no_videos_available.setVisibility(View.GONE);
        }
        else {
            ((AddReminderActivity)context).no_videos_available.setVisibility(View.VISIBLE);
        }

        return videoModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView roundedImageView;
        TextView title, category, duration;
        CheckBox checkBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            roundedImageView = itemView.findViewById(R.id.workoutImage);
            title = itemView.findViewById(R.id.title);
            category = itemView.findViewById(R.id.category);
            duration = itemView.findViewById(R.id.duration);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
