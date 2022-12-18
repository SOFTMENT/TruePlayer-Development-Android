package in.softment.trueplayerdevelopment.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Date;

import in.softment.trueplayerdevelopment.Adapter.HighlightAdapter;
import in.softment.trueplayerdevelopment.Interface.HighlightsModelCallback;
import in.softment.trueplayerdevelopment.MainActivity;
import in.softment.trueplayerdevelopment.Model.HighlightModel;
import in.softment.trueplayerdevelopment.Model.StatusModel;
import in.softment.trueplayerdevelopment.Model.UpcomingVideoCountModel;
import in.softment.trueplayerdevelopment.Model.UserModel;
import in.softment.trueplayerdevelopment.NewsAndUpdateActivity;
import in.softment.trueplayerdevelopment.PlayLandscapeVideoActivity;
import in.softment.trueplayerdevelopment.R;
import in.softment.trueplayerdevelopment.SignUpActivity;
import in.softment.trueplayerdevelopment.TrainingRoomVideosActivity;
import in.softment.trueplayerdevelopment.Util.Constants;
import in.softment.trueplayerdevelopment.Util.ProgressHud;
import in.softment.trueplayerdevelopment.Util.Services;

public class HomeFragment extends Fragment {


    public LinearLayout no_highlights_available;
    private RecyclerView recyclerView;
    private HighlightAdapter highlightAdapter;
    private ArrayList<HighlightModel> highlightModels = new ArrayList<>();
    private TextView statusTV;
    TextView basicSkill;
    TextView ballMastery;
    TextView eliteDrill;
    TextView footSpeed;
    TextView attackingMoves;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        no_highlights_available = view.findViewById(R.id.no_highlights_available_ll);
        recyclerView = view.findViewById(R.id.recyclerview);

        highlightAdapter = new HighlightAdapter(getContext(),this,highlightModels);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(highlightAdapter);

        //AppPREVIEW
        view.findViewById(R.id.appPreview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),PlayLandscapeVideoActivity.class);
                intent.putExtra("link","https://firebasestorage.googleapis.com/v0/b/trueplayer-development.appspot.com/o/Preview%20(movie).mp4?alt=media&token=2f210a3c-ff70-43bd-bb75-a95d50133f68");
                startActivity(intent);
            }
        });

        UserModel userModel = UserModel.data;

        //STATUS
        statusTV = view.findViewById(R.id.status);
        getStatus();

        //UPCOMING_VIDEO_COUNT
        TextView dailyMotivationTV = view.findViewById(R.id.dailyMotivationTV);
        LinearLayout newVideoLL = view.findViewById(R.id.newvideoLL);
        TextView newVideoTV = view.findViewById(R.id.newVideoTV);


        FirebaseFirestore.getInstance().collection("UpcomingVideo")
                .document("eGm5CxGCNj6arfgE2laW").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                            UpcomingVideoCountModel upcomingVideoCountModel = task.getResult().toObject(UpcomingVideoCountModel.class);
                            if (upcomingVideoCountModel.isEnabled()) {
                                dailyMotivationTV.setVisibility(View.GONE);
                                newVideoLL.setVisibility(View.VISIBLE);
                                newVideoTV.setText(Services.getDateDifference(new Date(), upcomingVideoCountModel.getTime()));
                            }
                            else {
                                dailyMotivationTV.setVisibility(View.VISIBLE);
                                newVideoLL.setVisibility(View.GONE);
                            }
                        }
                        else {
                            dailyMotivationTV.setVisibility(View.VISIBLE);
                            newVideoLL.setVisibility(View.GONE);
                        }
                    }
                });

        //NOTIFICATION
        view.findViewById(R.id.newsandupdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), NewsAndUpdateActivity.class));
            }
        });

        //BASIC_SKILLS
        view.findViewById(R.id.basicSkills).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoTrainingRoomVideo("Basic Skills",Constants.Category.basic_skills);
            }
        });

        //BALL_MASTERY
        view.findViewById(R.id.ballMastery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoTrainingRoomVideo("Ball Mastery",Constants.Category.ball_mastery);
            }
        });

        //ELITE_DRILL
        view.findViewById(R.id.eliteDrill).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoTrainingRoomVideo("Elite Drill",Constants.Category.elite_drill);
            }
        });

        //FOOT_SPEED
        view.findViewById(R.id.footSpeed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoTrainingRoomVideo("Foot Speed",Constants.Category.foot_speed);
            }
        });

        //ATTACKING_MOVES
        view.findViewById(R.id.attackingMoves).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoTrainingRoomVideo("Attacking Moves",Constants.Category.attacking_moves);
            }
        });


        //NAME
        TextView fullName = view.findViewById(R.id.fullName);
        fullName.setText("Hi "+ userModel.fullName);

        //TODAY's DATE
        TextView todayDate = view.findViewById(R.id.todayDate);
        todayDate.setText(Services.convertDateToString(new Date()));

        //PROFILE IMAGE
        RoundedImageView profileImageView = view.findViewById(R.id.profileImageView);
        Glide.with(getContext()).load(userModel.profilePic).placeholder(R.drawable.profile_placeholder).into(profileImageView);

        TextView totalHightLightVideos = view.findViewById(R.id.totalHightlightVideos);
        Services.getHighLightsVideos(getContext(), new HighlightsModelCallback() {
            @Override
            public void onCallback(ArrayList<HighlightModel> highModels) {
                if (highModels != null) {
                    highlightModels.clear();
                    highlightModels.addAll(highModels);
                    totalHightLightVideos.setText(highModels.size()+" Videos");
                    highlightAdapter.notifyDataSetChanged();
                }
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

    private void getStatus(){
        FirebaseFirestore.getInstance().collection("Status").document("mystatus").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                 if (documentSnapshot.exists()) {
                     StatusModel statusModel = documentSnapshot.toObject(StatusModel.class);
                     statusTV.setText(statusModel.getStatus());
                 }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((MainActivity)context).initializeHomeFragment(this);
    }
}
