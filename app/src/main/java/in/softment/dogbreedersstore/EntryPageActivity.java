package in.softment.dogbreedersstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class EntryPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_page);

        SharedPreferences sharedPreferences = getSharedPreferences("MBS_DB",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        findViewById(R.id.user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("role","user").apply();
                startActivity(new Intent(EntryPageActivity.this,SignInActivity.class));
            }
        });

        findViewById(R.id.breeder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("role","breeder").apply();
            }
        });
    }
}
