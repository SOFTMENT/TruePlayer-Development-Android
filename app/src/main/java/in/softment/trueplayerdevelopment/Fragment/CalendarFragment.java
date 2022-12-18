package in.softment.trueplayerdevelopment.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.LinearLayout;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import in.softment.trueplayerdevelopment.Adapter.CalendarWorkoutAdapter;
import in.softment.trueplayerdevelopment.AddReminderActivity;
import in.softment.trueplayerdevelopment.Model.CalendarWorkoutModel;
import in.softment.trueplayerdevelopment.Model.UserModel;
import in.softment.trueplayerdevelopment.R;
import in.softment.trueplayerdevelopment.Util.ProgressHud;


public class CalendarFragment extends Fragment {

    private ArrayList<CalendarWorkoutModel> calendarWorkoutModels = new ArrayList<>();
    public LinearLayout no_reminders_available;
    private CalendarWorkoutAdapter calendarWorkoutAdapter;
    private CalendarView calendarView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        calendarWorkoutAdapter = new CalendarWorkoutAdapter(getContext(),this,calendarWorkoutModels);
        recyclerView.setAdapter(calendarWorkoutAdapter);

        //No_REMINDERS_AVAILABLE
        no_reminders_available = view.findViewById(R.id.no_reminder_available_ll);

        view.findViewById(R.id.reminder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddReminderActivity.class));
            }
        });

        //CalendarView
        calendarView = view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                getAllReminders(calendar.getTime());
            }
        });

        //GetAllReminders
        getAllReminders(new Date());
        return view;
    }

    public void getAllReminders(Date date){
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.HOUR, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.HOUR_OF_DAY, 0);
        Date startTime = now.getTime();
        now.set(Calendar.HOUR, 23);
        now.set(Calendar.MINUTE,59);
        now.set(Calendar.SECOND,59);
        Date endTime = now.getTime();

        FirebaseFirestore.getInstance().collection("Users").document(UserModel.data.getUid()).collection("Reminders").orderBy("reminderDate").whereGreaterThan("reminderDate",startTime).whereLessThanOrEqualTo("reminderDate",endTime).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error == null) {
                        calendarWorkoutModels.clear();
                        if (value != null && !value.isEmpty()) {
                            for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                                CalendarWorkoutModel calendarWorkoutModel = documentSnapshot.toObject(CalendarWorkoutModel.class);
                                calendarWorkoutModels.add(calendarWorkoutModel);
                            }
                        }

                        calendarWorkoutAdapter.notifyDataSetChanged();
                    }
            }
        });
    }
}
