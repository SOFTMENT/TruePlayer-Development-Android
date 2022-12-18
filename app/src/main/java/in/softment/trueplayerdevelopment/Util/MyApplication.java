package in.softment.trueplayerdevelopment.Util;

import android.app.Application;

import com.cloudinary.android.MediaManager;
import com.google.firebase.FirebaseApp;

import java.util.HashMap;
import java.util.Map;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        Map config = new HashMap();
        config.put("cloud_name", "dasjc77fn");
        config.put("api_key","278848993965218");
        config.put("api_secret","d-RAnPC9dG8E4Cm57iE4rhjCCE8");


        MediaManager.init(this, config);
    }
}
