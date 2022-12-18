package in.softment.trueplayerdevelopment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import in.softment.trueplayerdevelopment.Fragment.CalendarFragment;
import in.softment.trueplayerdevelopment.Fragment.HomeFragment;
import in.softment.trueplayerdevelopment.Fragment.ProfileFragment;
import in.softment.trueplayerdevelopment.Fragment.TrainingPlanFragment;
import in.softment.trueplayerdevelopment.Fragment.TrainingRoomFragment;
import in.softment.trueplayerdevelopment.Util.Constants;
import in.softment.trueplayerdevelopment.Util.NonSwipeAbleViewPager;


public class MainActivity extends AppCompatActivity  {

    private TabLayout tabLayout;
    private NonSwipeAbleViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private HomeFragment homeFragment;
    private TrainingRoomFragment trainingRoomFragment;


    private final int[] tabIcons = {
            R.drawable.home,
            R.drawable.activity,
            R.drawable.calendar,
            R.drawable.graph,
            R.drawable.profile,
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //ViewPager
        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(5);


        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        viewPager.setCurrentItem(0);

        CollectionReference collection = FirebaseFirestore.getInstance().collection("TrainingRoomVideos");
        Query query = collection.whereEqualTo("categoryId", Constants.Category.basic_skills);
        AggregateQuery countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                AggregateQuerySnapshot snapshot = task.getResult();
                homeFragment.updateBasicSkillsCount((int)snapshot.getCount());
                trainingRoomFragment.updateBasicSkillsCount((int)snapshot.getCount());
            }
        });


        query = collection.whereEqualTo("categoryId", Constants.Category.ball_mastery);
        countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                AggregateQuerySnapshot snapshot = task.getResult();
                homeFragment.updateBallMasteryCount((int)snapshot.getCount());
                trainingRoomFragment.updateBallMasteryCount((int)snapshot.getCount());
            }
        });

        query = collection.whereEqualTo("categoryId", Constants.Category.elite_drill);
        countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                AggregateQuerySnapshot snapshot = task.getResult();
                homeFragment.updateEliteDrillCount((int)snapshot.getCount());
                trainingRoomFragment.updateEliteDrillCount((int)snapshot.getCount());

            }
        });

        query = collection.whereEqualTo("categoryId", Constants.Category.foot_speed);
        countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                AggregateQuerySnapshot snapshot = task.getResult();
                homeFragment.updateFootSpeedCount((int)snapshot.getCount());
                trainingRoomFragment.updateFootSpeedCount((int)snapshot.getCount());
            }
        });

        query = collection.whereEqualTo("categoryId", Constants.Category.attacking_moves);
        countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                AggregateQuerySnapshot snapshot = task.getResult();
                homeFragment.updateAttackingMoveCount((int)snapshot.getCount());
                trainingRoomFragment.updateAttackingMoveCount((int)snapshot.getCount());
            }
        });
    }


    public void setPagerItem(int item){
        viewPager.setCurrentItem(item);
    }

    private void setupTabIcons() {

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);


    }

    private void setupViewPager(ViewPager viewPager) {

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFrag(new HomeFragment());
        viewPagerAdapter.addFrag(new TrainingRoomFragment(this));
        viewPagerAdapter.addFrag(new CalendarFragment());
        viewPagerAdapter.addFrag(new TrainingPlanFragment());
        viewPagerAdapter.addFrag(new ProfileFragment(this));
        viewPager.setAdapter(viewPagerAdapter);

    }

    static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();


        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {


            return mFragmentList.get(position);
        }

        @Override
        public int getItemPosition(@NonNull @NotNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {

            return mFragmentList.size();
        }



        public void addFrag(Fragment fragment) {
            mFragmentList.add(fragment);

        }



    }

    public void initializeHomeFragment(HomeFragment homeFragment){
        this.homeFragment = homeFragment;
    }

    public void initializeTrainingRoomFragment(TrainingRoomFragment trainingRoomFragment){
        this.trainingRoomFragment = trainingRoomFragment;
    }


}


