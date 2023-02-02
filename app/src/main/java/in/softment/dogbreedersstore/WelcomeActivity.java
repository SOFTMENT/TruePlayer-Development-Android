package in.softment.dogbreedersstore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import in.softment.dogbreedersstore.Util.Services;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        FirebaseMessaging.getInstance().subscribeToTopic("mbs");

        SharedPreferences sharedPreferences = getSharedPreferences("MBS_DB", MODE_PRIVATE);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            String role = sharedPreferences.getString("role","");
            if (role.equals("user")) {
                Services.getCurrentUserData(this,FirebaseAuth.getInstance().getCurrentUser().getUid(),false);
            }
            else {
                //FOR BREEDER
            }


        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(WelcomeActivity.this, EntryPageActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            },1500);

        }


    }
}

