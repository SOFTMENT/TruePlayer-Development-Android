package in.softment.trueplayerdevelopment;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import in.softment.trueplayerdevelopment.Adapter.WorkoutVideosAdapter;
import in.softment.trueplayerdevelopment.Interface.ImageDownloadLinkCallback;
import in.softment.trueplayerdevelopment.Interface.VideoArrayCallback;
import in.softment.trueplayerdevelopment.Model.UserModel;
import in.softment.trueplayerdevelopment.Model.VideoModel;
import in.softment.trueplayerdevelopment.Util.ProgressHud;
import in.softment.trueplayerdevelopment.Util.Services;

public class SignUpActivity extends AppCompatActivity {

    private final int PICK_IMAGE_REQUEST = 1;
    private ImageView profile_picture;
    private Uri resultUri = null;
    ActivityResultLauncher<CropImageContractOptions> cropImage = registerForActivityResult(new CropImageContract(), new ActivityResultCallback<CropImageView.CropResult>() {
        @Override
        public void onActivityResult(CropImageView.CropResult result) {
            if (result.isSuccessful()) {
                Uri uri = result.getUriContent();
                Bitmap bitmap = null;
                try {
                    resultUri = uri;
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    profile_picture.setImageBitmap(bitmap);


                } catch (IOException e) {

                }
            }
            else {
                Services.showDialog(SignUpActivity.this, "ERROR",result.getError().getLocalizedMessage());
            }
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        EditText name = findViewById(R.id.fullName);
        EditText emailAddress = findViewById(R.id.emailaddress);
        EditText password = findViewById(R.id.password);

        profile_picture = findViewById(R.id.user_profile);


        //BACK
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sName = name.getText().toString().trim();
                String sEmail = emailAddress.getText().toString().trim();
                String sPassword = password.getText().toString().trim();

                if (resultUri == null) {
                    Services.showCenterToast(SignUpActivity.this,"Upload Profile Picture");
                }
                else if (sName.isEmpty()) {
                    Services.showCenterToast(SignUpActivity.this,"Enter Full Name");
                }
                else if (sEmail.isEmpty()) {
                    Services.showCenterToast(SignUpActivity.this,"Enter Email Address");
                }
                else if (sPassword.isEmpty()) {
                    Services.showCenterToast(SignUpActivity.this,"Enter Password");
                }
                else {
                    ProgressHud.show(SignUpActivity.this,"Creating Account...");
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(sEmail, sPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                UserModel userModel = new UserModel();
                                userModel.email = sEmail;
                                userModel.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                userModel.fullName = sName;
                                userModel.registredAt = new Date();
                                userModel.regiType = "custom";
                                ProgressHud.show(SignUpActivity.this,"");
                                uploadImageOnCloudinary(new ImageDownloadLinkCallback() {
                                    @Override
                                    public void onCallBack(String downloadURL) {
                                        ProgressHud.dialog.dismiss();
                                        if (!downloadURL.isEmpty()) {
                                            userModel.profilePic = downloadURL;
                                            Services.addUserDataOnServer(SignUpActivity.this,userModel);
                                        }
                                        else {
                                            ProgressHud.dialog.dismiss();
                                        }
                                    }
                                });
                            }
                            else {
                                ProgressHud.dialog.dismiss();
                                Services.showDialog(SignUpActivity.this,"ERROR",task.getException().getLocalizedMessage());
                            }
                        }
                    });
                }
            }
        });

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //TapToChangeImage
        findViewById(R.id.taptochange).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkPermissionForReadExtertalStorage()) {
                    ShowFileChooser();
                }
                else {
                    requestStoragePermission();
                }

            }
        });

        if (!checkPermissionForReadExtertalStorage()) {
            requestStoragePermission();
        }
    }



    public void requestStoragePermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return;
        }


        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);//If the user has denied the permission previously your code will come to this block

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
    }

    public void ShowFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {

            Uri filepath = data.getData();

            CropImageContractOptions cropImageContractOptions = new CropImageContractOptions(filepath, new CropImageOptions());
            cropImageContractOptions.setAspectRatio(1,1);
            cropImageContractOptions.setFixAspectRatio(true);
            cropImageContractOptions.setOutputCompressQuality(60);
            cropImage.launch(cropImageContractOptions);


        }
    }

    public boolean checkPermissionForReadExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }


    private void uploadImageOnCloudinary(ImageDownloadLinkCallback downloadLinkCallback) {
        if (resultUri != null) {

            MediaManager.get().upload(resultUri)
                    .option("public_id",UserModel.data.getUid())
                    .option("folder","ProfilePicture")
                    .option("resource_type","image")
                    .callback(new UploadCallback() {
                        @Override
                        public void onStart(String requestId) {

                        }

                        @Override
                        public void onProgress(String requestId, long bytes, long totalBytes) {
                        }

                        @Override
                        public void onSuccess(String requestId, Map resultData) {

                            downloadLinkCallback.onCallBack(resultData.get("secure_url").toString());

                        }

                        @Override
                        public void onError(String requestId, ErrorInfo error) {
                            Services.showDialog(SignUpActivity.this,"ERROR",error.getDescription());
                            downloadLinkCallback.onCallBack("");
                        }

                        @Override
                        public void onReschedule(String requestId, ErrorInfo error) {
                            Services.showDialog(SignUpActivity.this,"ERROR",error.getDescription());
                            downloadLinkCallback.onCallBack("");
                        }
                    }).dispatch();


        }


    }


}
