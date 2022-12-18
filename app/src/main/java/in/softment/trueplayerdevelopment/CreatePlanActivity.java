package in.softment.trueplayerdevelopment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Date;

import in.softment.trueplayerdevelopment.Model.UserModel;
import in.softment.trueplayerdevelopment.Model.WeeklyPlanModel;
import in.softment.trueplayerdevelopment.Util.Constants;
import in.softment.trueplayerdevelopment.Util.ProgressHud;
import in.softment.trueplayerdevelopment.Util.Services;

public class CreatePlanActivity extends AppCompatActivity implements  TextWatcher{

    private EditText weeklyHoursET;
    private TextView availableHoursTV;
    private EditText basicSkillsHours,ballMasteryHours, eliteDrillHours, footSpeedHours, attackingMovesHours;
    private TextView basicSkillTV, ballMasteryTV, eliteDrillTV, footSpeedTV, attackingMovesTV;
    private int availableHours;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);

        weeklyHoursET = findViewById(R.id.weeklyHoursET);
        availableHoursTV = findViewById(R.id.availableHoursTV);

        basicSkillTV = findViewById(R.id.basicSkillsCount);
        ballMasteryTV = findViewById(R.id.ballMasteryCount);
        eliteDrillTV = findViewById(R.id.eliteDrillCount);
        footSpeedTV = findViewById(R.id.footSpeedCount);
        attackingMovesTV = findViewById(R.id.attackingMovesCount);



        CollectionReference collection = FirebaseFirestore.getInstance().collection("TrainingRoomVideos");
        Query query = collection.whereEqualTo("categoryId", Constants.Category.basic_skills);
        AggregateQuery countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                AggregateQuerySnapshot snapshot = task.getResult();
                basicSkillTV.setText(snapshot.getCount()+" Videos");
            }
        });


        query = collection.whereEqualTo("categoryId", Constants.Category.ball_mastery);
        countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                AggregateQuerySnapshot snapshot = task.getResult();
                ballMasteryTV.setText(snapshot.getCount()+" Videos");
            }
        });

        query = collection.whereEqualTo("categoryId", Constants.Category.elite_drill);
        countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                AggregateQuerySnapshot snapshot = task.getResult();
                eliteDrillTV.setText(snapshot.getCount()+" Videos");

            }
        });

        query = collection.whereEqualTo("categoryId", Constants.Category.foot_speed);
        countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                AggregateQuerySnapshot snapshot = task.getResult();
                footSpeedTV.setText(snapshot.getCount()+" Videos");
            }
        });

        query = collection.whereEqualTo("categoryId", Constants.Category.attacking_moves);
        countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                AggregateQuerySnapshot snapshot = task.getResult();
                attackingMovesTV.setText(snapshot.getCount()+" Videos");
            }
        });


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        weeklyHoursET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() > 0) {
                        availableHoursTV.setText(s.toString()+ " Hours");
                        availableHours = Integer.parseInt(s.toString());
                    }
                    else {
                        availableHoursTV.setText("0 Hour");
                    }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        basicSkillsHours = findViewById(R.id.basicSkillsHours);
        ballMasteryHours = findViewById(R.id.ballMasteryHours);
        eliteDrillHours = findViewById(R.id.eliteDrillHours);
        footSpeedHours = findViewById(R.id.footSpeedHours);
        attackingMovesHours = findViewById(R.id.attackingMovesHours);

        basicSkillsHours.addTextChangedListener(this);

        ballMasteryHours.addTextChangedListener(this);

        eliteDrillHours.addTextChangedListener(this);

        footSpeedHours.addTextChangedListener(this);

        attackingMovesHours.addTextChangedListener(this);


        findViewById(R.id.createPlan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sWeeklyHours = weeklyHoursET.getText().toString();
                String sBasicSkills = basicSkillsHours.getText().toString();
                String sBallMastery = ballMasteryHours.getText().toString();
                String sEliteDrill =  eliteDrillHours.getText().toString();
                String sFootSpeed = footSpeedHours.getText().toString();
                String sAttackingMoves = attackingMovesHours.getText().toString();

                int iBasicSkills = 0;
                int iBallMastery = 0;
                int iEliteDrill = 0;
                int iFootSpeed = 0;
                int iAttackingMoves = 0;
                int iWeeklyHours = 0;


                if (!sBasicSkills.isEmpty()) {
                    iBasicSkills = Integer.parseInt(sBasicSkills);
                }
                if (!sBallMastery.isEmpty()) {
                    iBallMastery = Integer.parseInt(sBallMastery);
                }
                if (!sEliteDrill.isEmpty()) {
                    iEliteDrill = Integer.parseInt(sEliteDrill);
                }
                if (!sFootSpeed.isEmpty()) {
                    iFootSpeed = Integer.parseInt(sFootSpeed);
                }
                if (!sAttackingMoves.isEmpty()) {
                    iAttackingMoves = Integer.parseInt(sAttackingMoves);
                }
                if (!sWeeklyHours.isEmpty()) {
                    iWeeklyHours = Integer.parseInt(sWeeklyHours);
                }


                ProgressHud.show(CreatePlanActivity.this,"Updated");
                WeeklyPlanModel weeklyPlanModel = new WeeklyPlanModel();
                weeklyPlanModel.addedDate = new Date();
                weeklyPlanModel.ballMasteryHours = iBallMastery;
                weeklyPlanModel.basicHours = iBasicSkills;
                weeklyPlanModel.footSpeed = iFootSpeed;
                weeklyPlanModel.attackingHours = iAttackingMoves;
                weeklyPlanModel.eliteDrillHours = iEliteDrill;
                weeklyPlanModel.weeklyHours = iWeeklyHours;


                FirebaseFirestore.getInstance().collection("Users").document(UserModel.data.getUid())
                        .collection("WeeklyPlan").document(UserModel.data.getUid()).set(weeklyPlanModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                ProgressHud.dialog.dismiss();
                                if (task.isSuccessful()) {
                                    Services.showCenterToast(CreatePlanActivity.this,"Updated ");

                                }
                                else {
                                    Services.showDialog(CreatePlanActivity.this,"ERROR",task.getException().getLocalizedMessage());
                                }
                            }
                        });

            }
        });

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {



            String sWeeklyHours = weeklyHoursET.getText().toString();
            String sBasicSkills = basicSkillsHours.getText().toString();
            String sBallMastery = ballMasteryHours.getText().toString();
            String sEliteDrill =  eliteDrillHours.getText().toString();
            String sFootSpeed = footSpeedHours.getText().toString();
            String sAttackingMoves = attackingMovesHours.getText().toString();

            int iBasicSkills = 0;
            int iBallMastery = 0;
            int iEliteDrill = 0;
            int iFootSpeed = 0;
            int iAttackingMoves = 0;
            int iWeeklyHours = 0;


            if (!sBasicSkills.isEmpty()) {
                iBasicSkills = Integer.parseInt(sBasicSkills);
            }
            if (!sBallMastery.isEmpty()) {
                iBallMastery = Integer.parseInt(sBallMastery);
            }
            if (!sEliteDrill.isEmpty()) {
                iEliteDrill = Integer.parseInt(sEliteDrill);
            }
            if (!sFootSpeed.isEmpty()) {
                iFootSpeed = Integer.parseInt(sFootSpeed);
            }
            if (!sAttackingMoves.isEmpty()) {
                iAttackingMoves = Integer.parseInt(sAttackingMoves);
            }
            if (!sWeeklyHours.isEmpty()) {
                iWeeklyHours = Integer.parseInt(sWeeklyHours);
            }

            if ( iWeeklyHours >= iBasicSkills + iBallMastery + iEliteDrill + iAttackingMoves + iFootSpeed) {

                availableHours = iWeeklyHours - (iBasicSkills + iBallMastery + iEliteDrill + iAttackingMoves + iFootSpeed);
                availableHoursTV.setText(availableHours + " Hours");
            }
            else {
                if (ballMasteryHours.getText().hashCode() == s.hashCode()) {
                    ballMasteryHours.setText("0");
                }
                else if (basicSkillsHours.getText().hashCode() == s.hashCode()) {
                    basicSkillsHours.setText("0");
                }
                else if (eliteDrillHours.getText().hashCode() == s.hashCode()) {
                    eliteDrillHours.setText("0");
                }
                else if (footSpeedHours.getText().hashCode() == s.hashCode()) {
                    footSpeedHours.setText("0");
                }
                else if (attackingMovesHours.getText().hashCode() == s.hashCode()) {
                    attackingMovesHours.setText("0");
                }
                Services.showCenterToast(CreatePlanActivity.this,"Please increase weekly hours");
            }

    }

    @Override
    public void afterTextChanged(Editable s) {
    }


}
