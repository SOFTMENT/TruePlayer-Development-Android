package in.softment.trueplayerdevelopment.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.softment.trueplayerdevelopment.CreatePlanActivity;
import in.softment.trueplayerdevelopment.R;

public class TrainingPlanFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_training_plan, container, false);

        //CreatePlan
        view.findViewById(R.id.createPlan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CreatePlanActivity.class));
            }
        });

        return view;
    }
}
