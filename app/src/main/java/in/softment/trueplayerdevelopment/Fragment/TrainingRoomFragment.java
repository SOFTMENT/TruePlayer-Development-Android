package in.softment.trueplayerdevelopment.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.softment.trueplayerdevelopment.MainActivity;
import in.softment.trueplayerdevelopment.R;
import in.softment.trueplayerdevelopment.SignUpActivity;
import in.softment.trueplayerdevelopment.TrainingRoomVideosActivity;
import in.softment.trueplayerdevelopment.Util.Constants;

public class TrainingRoomFragment extends Fragment {

    private MainActivity mainActivity;
    TextView basicSkill;
    TextView ballMastery;
    TextView eliteDrill;
    TextView footSpeed;
    TextView attackingMoves;
    public TrainingRoomFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_training_room, container, false);

        view.findViewById(R.id.homePage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.setPagerItem(0);
            }
        });

        //BASIC_SKILLS
        view.findViewById(R.id.basicSkillsRR).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoTrainingRoomVideo("Basic Skills", Constants.Category.basic_skills);
            }
        });

        //BALL_MASTERY
        view.findViewById(R.id.ballMasteryRR).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoTrainingRoomVideo("Ball Mastery",Constants.Category.ball_mastery);
            }
        });

        //ELITE_DRILL
        view.findViewById(R.id.eliteDrillRR).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoTrainingRoomVideo("Elite Drill",Constants.Category.elite_drill);
            }
        });

        //FOOT_SPEED
        view.findViewById(R.id.footSpeedRR).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoTrainingRoomVideo("Foot Speed",Constants.Category.foot_speed);
            }
        });

        //ATTACKING_MOVES
        view.findViewById(R.id.attackingMovesRR).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoTrainingRoomVideo("Attacking Moves",Constants.Category.attacking_moves);
            }
        });

        basicSkill = view.findViewById(R.id.basicSkillsCount);
        ballMastery = view.findViewById(R.id.ballMasteryCount);
        eliteDrill = view.findViewById(R.id.eliteDrillCount);
        footSpeed = view.findViewById(R.id.footSpeedCount);
        attackingMoves = view.findViewById(R.id.attackingMovesCount);


        return view;
    }

    public void updateBasicSkillsCount(int count) {
        basicSkill.setText(count+" Videos");
    }
    public void updateBallMasteryCount(int count) {
        ballMastery.setText(count+" Videos");
    }

    public void updateEliteDrillCount(int count) {
        eliteDrill.setText(count+" Videos");
    }
    public void updateFootSpeedCount(int count) {
        footSpeed.setText(count+" Videos");
    }

    public void updateAttackingMoveCount(int count) {
        attackingMoves.setText(count+" Videos");
    }


    private void gotoTrainingRoomVideo(String categoryName,String categoryId){
        Intent intent = new Intent(getContext(), TrainingRoomVideosActivity.class);
        intent.putExtra("categoryId",categoryId);
        intent.putExtra("categoryTitle",categoryName);
        startActivity(intent);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((MainActivity)context).initializeTrainingRoomFragment(this);
    }
}
