package in.softment.trueplayerdevelopment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Calendar;

import in.softment.trueplayerdevelopment.Adapter.AddReminderAdapter;
import in.softment.trueplayerdevelopment.Interface.VideoArrayCallback;
import in.softment.trueplayerdevelopment.Model.CalendarWorkoutModel;
import in.softment.trueplayerdevelopment.Model.UserModel;
import in.softment.trueplayerdevelopment.Model.VideoModel;
import in.softment.trueplayerdevelopment.Util.Constants;
import in.softment.trueplayerdevelopment.Util.ProgressHud;
import in.softment.trueplayerdevelopment.Util.Services;

public class AddReminderActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private ArrayList<VideoModel> videoModels = new ArrayList<>();
    private AddReminderAdapter addReminderAdapter;
    private String[] categories = { "Choose Category", "Basic Skills", "Ball Mastery","Elite Drill","FootSpeed","Attacking Moves"};
    private EditText chooseDateTimeET;
    int day, month, year, hour, minute;
    int myday, myMonth, myYear, myHour, myMinute;
    public CalendarWorkoutModel calendarWorkoutModel = null;
    public LinearLayout no_videos_available;
    private Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        //NO_VIDEOS_AVAILABLE
        no_videos_available = findViewById(R.id.no_videos_available_ll);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        addReminderAdapter = new AddReminderAdapter(this, videoModels);
        recyclerView.setAdapter(addReminderAdapter);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        AppCompatSpinner spinner = findViewById(R.id.categorySpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position > 0) {
                        ProgressHud.show(AddReminderActivity.this,"Loading...");
                        String cat_id = "";
                        if (position == 1){
                            cat_id = Constants.Category.basic_skills;
                        }
                        else if (position == 2){
                            cat_id = Constants.Category.ball_mastery;
                        }
                        else if (position == 3){
                            cat_id = Constants.Category.elite_drill;
                        }
                        else if (position == 4){
                            cat_id = Constants.Category.foot_speed;
                        }
                        else if (position == 5){
                            cat_id = Constants.Category.attacking_moves;
                        }
                        Services.getVideosForCategory(AddReminderActivity.this, cat_id, new VideoArrayCallback() {
                            @Override
                            public void oncompletioncallback(ArrayList<VideoModel> vModels) {
                                ProgressHud.dialog.dismiss();
                                if (videoModels != null) {
                                    videoModels.clear();
                                    videoModels.addAll(vModels);

                                    addReminderAdapter.notifyDataSetChanged();
                                }

                            }
                        });
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> ad = new ArrayAdapter<String>(this,
                R.layout.myfont, categories) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                Typeface externalFont= ResourcesCompat.getFont(AddReminderActivity.this, R.font.montregular);
                ((TextView) v).setTypeface(externalFont);

                return v;
            }


            public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);

                Typeface externalFont = ResourcesCompat.getFont(AddReminderActivity.this, R.font.montregular);
                ((TextView) v).setTypeface(externalFont);
                return v;
            }
        };


        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        spinner.setAdapter(ad);


        //ChooseDateAndTime
        chooseDateTimeET = findViewById(R.id.chooseDateandTime);
        chooseDateTimeET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddReminderActivity.this, AddReminderActivity.this,year, month,day);
                datePickerDialog.show();
            }
        });

        //ADDREMINDER
        findViewById(R.id.addReminder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sDateAndTime = chooseDateTimeET.getText().toString();
                if (calendarWorkoutModel == null) {
                    Services.showCenterToast(AddReminderActivity.this,"Select Video");
                }
                else if (sDateAndTime.isEmpty()) {
                    Services.showCenterToast(AddReminderActivity.this,"Choose Date and Time");
                }
                else {
                    ProgressHud.show(AddReminderActivity.this,"Loading...");
                    FirebaseFirestore.getInstance().collection("Users").document(UserModel.data.getUid()).collection("Reminders").document(calendarWorkoutModel.videoId).
                            set(calendarWorkoutModel, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            ProgressHud.dialog.dismiss();
                            if (task.isSuccessful()){
                                Services.showCenterToast(AddReminderActivity.this,"Added");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                },2500);
                            }
                            else {
                                Services.showDialog(AddReminderActivity.this,"ERROR",task.getException().getLocalizedMessage());
                            }

                        }
                    });
                }
            }
        });



    }

    public void addCalendarModel(String videoId){
        calendarWorkoutModel = new CalendarWorkoutModel();
        calendarWorkoutModel.completed = false;
        calendarWorkoutModel.videoId = videoId;
    }

    public void removeCalendarModel(){
        calendarWorkoutModel = null;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myYear = year;
        myday = dayOfMonth;
        myMonth = month + 1;
        myCalendar = Calendar.getInstance();

        myCalendar.set(Calendar.DAY_OF_MONTH, myday);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.YEAR,year);

        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(AddReminderActivity.this, AddReminderActivity.this, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        myHour = hourOfDay;
        myMinute = minute;

        myCalendar.set(Calendar.HOUR,myHour);
        myCalendar.set(Calendar.MINUTE, myMinute);
        calendarWorkoutModel.reminderDate = myCalendar.getTime();
        chooseDateTimeET.setText(String.format("%02d",myday)+"-"+Services.getMonthName(myMonth)+"-"+String.format("%02d",myYear)+", "+String.format("%02d",myHour)+" : "+String.format("%02d",myMinute));
    }

}
