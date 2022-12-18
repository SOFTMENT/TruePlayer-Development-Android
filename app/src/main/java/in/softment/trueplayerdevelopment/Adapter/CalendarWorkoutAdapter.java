package in.softment.trueplayerdevelopment.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.makeramen.roundedimageview.RoundedImageView;

import org.checkerframework.checker.units.qual.A;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.softment.trueplayerdevelopment.Fragment.CalendarFragment;
import in.softment.trueplayerdevelopment.Interface.VideoModelCallback;
import in.softment.trueplayerdevelopment.Model.CalendarWorkoutModel;
import in.softment.trueplayerdevelopment.Model.VideoModel;
import in.softment.trueplayerdevelopment.R;
import in.softment.trueplayerdevelopment.Util.Constants;
import in.softment.trueplayerdevelopment.Util.ProgressHud;
import in.softment.trueplayerdevelopment.Util.Services;

public class CalendarWorkoutAdapter extends RecyclerView.Adapter<CalendarWorkoutAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CalendarWorkoutModel> calendarWorkoutModels;
    private CalendarFragment calendarFragment;

    public CalendarWorkoutAdapter(Context context, CalendarFragment calendarFragment, ArrayList<CalendarWorkoutModel> calendarWorkoutModels ) {
        this.context = context;
        this.calendarWorkoutModels = calendarWorkoutModels;
        this.calendarFragment = calendarFragment;
    }

    @NonNull
    @Override
    public CalendarWorkoutAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_workout_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarWorkoutAdapter.ViewHolder holder, int position) {
            holder.setIsRecyclable(false);
            CalendarWorkoutModel calendarWorkoutModel  = calendarWorkoutModels.get(position);
            if (calendarWorkoutModel.completed) {
                holder.checkBox.setChecked(true);
            }
            else {
                holder.checkBox.setChecked(false);
            }
            holder.reminderAt.setText("Reminder at "+ Services.convertDateToHourMin(calendarWorkoutModel.reminderDate));

            Services.getWorkoutVideoById(calendarWorkoutModel.videoId, new VideoModelCallback() {
                @Override
                public void oncompletioncallback(VideoModel videoModel) {
                    if (videoModel != null) {
                        Glide.with(context).load(videoModel.thumbnail).placeholder(R.drawable.placeholder).into(holder.roundedImageView);
                        holder.title.setText(videoModel.title);
                        holder.category.setText(Constants.Category.getCategoryName(videoModel.categoryId));
                    }
                    else{
                        FirebaseFirestore.getInstance().collection("Reminders").document(calendarWorkoutModel.videoId).delete();
                    }
                }
            });

            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Map<String, Boolean> map = new HashMap();
                    if (isChecked) {

                        map.put("completed",false);
                    }
                    else {
                        map.put("completed",true);
                    }
                    FirebaseFirestore.getInstance().collection("Reminders").document(calendarWorkoutModel.videoId).set(map, SetOptions.merge());
                }
            });

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder  = new AlertDialog.Builder(context,R.style.AlertDialogTheme);
                    builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            ProgressHud.show(context,"Deleting...");
                            FirebaseFirestore.getInstance().collection("Reminders").document(calendarWorkoutModel.videoId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                        ProgressHud.dialog.dismiss();
                                        Services.showCenterToast(context,"Deleted");
                                 }
                            });
                        }
                    });
                    builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builder.setTitle("DELETE");
                    builder.setMessage("Are you sure you want to delete this reminder?");
                    builder.show();
                }
            });
    }

    @Override
    public int getItemCount() {
        if (calendarWorkoutModels.size() > 0) {
            calendarFragment.no_reminders_available.setVisibility(View.GONE);
        }
        else {
            calendarFragment.no_reminders_available.setVisibility(View.VISIBLE);
        }
        return calendarWorkoutModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView roundedImageView;
        TextView title, category, reminderAt;
        CheckBox checkBox;
        ImageView delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            roundedImageView = itemView.findViewById(R.id.workoutImage);
            title = itemView.findViewById(R.id.title);
            category = itemView.findViewById(R.id.category);
            reminderAt = itemView.findViewById(R.id.reminderat);
            checkBox = itemView.findViewById(R.id.checkBox);
            delete = itemView.findViewById(R.id.trash);
        }
    }
}
