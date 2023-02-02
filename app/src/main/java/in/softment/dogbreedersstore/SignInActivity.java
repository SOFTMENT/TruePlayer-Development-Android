package in.softment.dogbreedersstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import in.softment.dogbreedersstore.Util.ProgressHud;
import in.softment.dogbreedersstore.Util.Services;

public class SignInActivity extends AppCompatActivity {

    EditText emailAddress, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_acitivity);
        emailAddress = findViewById(R.id.emailaddress);
        password = findViewById(R.id.password);


        //Reset Btn Clicked
        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sEmail = emailAddress.getText().toString().trim();

                if (sEmail.isEmpty()) {
                    Services.showCenterToast(SignInActivity.this,"Enter Email Address");
                }
                else {
                    ProgressHud.show(SignInActivity.this,"Processing...");
                    FirebaseAuth.getInstance().sendPasswordResetEmail(sEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            ProgressHud.dialog.dismiss();
                            if (task.isSuccessful()) {
                                Services.showDialog(SignInActivity.this,"RESET PASSWORD","We have sent reset password link on your mail address.");
                            }
                            else {
                                Services.showDialog(SignInActivity.this,"ERROR",task.getException().getLocalizedMessage());
                            }
                        }
                    });
                }
            }
        });

        //RegisterBtnClicked
        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
            }
        });

        //LoginBtnClicked
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sEmail = emailAddress.getText().toString().trim();
                String sPassword = password.getText().toString().trim();

                if (sEmail.isEmpty()) {
                    Services.showCenterToast(SignInActivity.this,"Enter Email Address");
                }
                else if (sPassword.isEmpty()) {
                    Services.showCenterToast(SignInActivity.this,"Enter Password");
                }
                else {
                    ProgressHud.show(SignInActivity.this,"Sign In...");
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(sEmail, sPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            ProgressHud.dialog.dismiss();
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user != null) {

                                        Services.getCurrentUserData(SignInActivity.this,user.getUid(),true);

                                }
                            }
                            else {
                                Services.showDialog(SignInActivity.this,"ERROR",task.getException().getLocalizedMessage());
                            }
                        }
                    });
                }
            }
        });


    }

}
