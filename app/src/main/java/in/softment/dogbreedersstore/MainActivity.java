package in.softment.dogbreedersstore;

import static com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.softment.dogbreedersstore.Fragment.ChatFragment;
import in.softment.dogbreedersstore.Fragment.HomeFragment;
import in.softment.dogbreedersstore.Fragment.ProfileFragment;
import in.softment.dogbreedersstore.Fragment.StoreFragment;
import in.softment.dogbreedersstore.Util.Constants;
import in.softment.dogbreedersstore.Util.NonSwipeAbleViewPager;
import in.softment.dogbreedersstore.Util.ProgressHud;


public class MainActivity extends AppCompatActivity  {

    private TabLayout tabLayout;
    private NonSwipeAbleViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private HomeFragment homeFragment;


    private final int[] tabIcons = {
            R.drawable.home,
            R.drawable.shoppingbag,
            R.drawable.chat,
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


        ProgressHud.show(this,"");


        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (!enabled) {
            allowGPS();
        } else {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            fusedLocationClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, new CancellationToken() {
                @NonNull
                @Override
                public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                    return null;
                }

                @Override
                public boolean isCancellationRequested() {
                    return false;
                }
            }).addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful() && task.getResult() != null) {

                        Constants.latitude = task.getResult().getLatitude();
                        Constants.longitude = task.getResult().getLongitude();


                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

                        try {
                            addresses = geocoder.getFromLocation(Constants.latitude, Constants.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                            String city = addresses.get(0).getLocality();
                            String country = addresses.get(0).getCountryName();
                            homeFragment.load(city,country);

                        } catch (IOException e) {

                            e.printStackTrace();
                        }

                    }





                }
            });
        }

    }

    public void setPagerItem(int item){
        viewPager.setCurrentItem(item);
    }

    private void setupTabIcons() {

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);


    }
    public void allowGPS(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
        builder.setTitle("GPS");
        builder.setMessage("Please enable gps service.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        builder.setCancelable(false);
        builder.show();

    }
    private void setupViewPager(ViewPager viewPager) {

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFrag(new HomeFragment());
        viewPagerAdapter.addFrag(new StoreFragment());
        viewPagerAdapter.addFrag(new ChatFragment());
        viewPagerAdapter.addFrag(new ProfileFragment());
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

}


