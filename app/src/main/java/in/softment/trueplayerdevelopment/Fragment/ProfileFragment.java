package in.softment.trueplayerdevelopment.Fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;


import org.checkerframework.checker.units.qual.A;
import org.w3c.dom.Text;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import in.softment.trueplayerdevelopment.BuildConfig;
import in.softment.trueplayerdevelopment.Interface.VideoDownloadLinkCallback;
import in.softment.trueplayerdevelopment.MainActivity;
import in.softment.trueplayerdevelopment.Model.HighlightModel;
import in.softment.trueplayerdevelopment.Model.UserModel;
import in.softment.trueplayerdevelopment.NewsAndUpdateActivity;
import in.softment.trueplayerdevelopment.R;
import in.softment.trueplayerdevelopment.Util.ProgressHud;
import in.softment.trueplayerdevelopment.Util.Services;


public class ProfileFragment extends Fragment {


    private MainActivity mainActivity;
    private ProgressDialog progressBar;
    public ProfileFragment(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        progressBar = new ProgressDialog(getContext());
        progressBar.setCancelable(true);

        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);
        progressBar.setMax(100);

        //USERNAME
        TextView userName = view.findViewById(R.id.username);
        TextView email = view.findViewById(R.id.email);
        ImageView profileImage = view.findViewById(R.id.user_profile);

        UserModel userModel = UserModel.data;
        userName.setText(userModel.getFullName());
        email.setText(userModel.getEmail());
        if (!userModel.profilePic.isEmpty()) {
            Glide.with(getContext()).load(userModel.profilePic).placeholder(R.drawable.profile_placeholder).into(profileImage);
        }
        //NewsAndUpdate
        view.findViewById(R.id.notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), NewsAndUpdateActivity.class));
            }
        });

        //MyWorkout
        view.findViewById(R.id.myworkout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.setPagerItem(3);
            }
        });

        //WorkoutReminders
        view.findViewById(R.id.workoutReminders).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.setPagerItem(2);
            }
        });


        //HelpCenter
        view.findViewById(R.id.help_cener).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "trueplayerdev@gmail.com" });
                startActivity(Intent.createChooser(intent, ""));
            }
        });

        //RateApp
        view.findViewById(R.id.rateApp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID);
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(myAppLinkToMarket);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getContext(), " unable to find market app", Toast.LENGTH_LONG).show();
                }
            }
        });

        //ShareApp
        view.findViewById(R.id.shareApp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "TruePlayer Development");
                    String shareMessage= "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
            }
        });

        //LOGOUT
        view.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
                builder.setTitle("Logout");
                builder.setMessage("Are you sure you want to logout?");
                builder.setNegativeButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Services.logout(getContext());
                    }
                });
                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                    }
                });

                builder.show();
            }
        });

        //MEMBERSHIP
        view.findViewById(R.id.membershipBanner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Services.showCenterToast(getContext(),"App must be live.");
            }
        });

        //SUBMITVIDEO
        view.findViewById(R.id.submitvideo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
                builder.setTitle("Upload Video");
                builder.setMessage("Upload your video and get a chance to select for weekly highlight.");
                builder.setPositiveButton("Choose Video", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (checkPermissionForReadExtertalStorage()) {
                            choosevideo();
                        }
                        else {
                            requestStoragePermission();
                        }
                    }
                });
                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

                builder.show();
            }
        });

        return view;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101 && grantResults[0] == RESULT_OK) {
            choosevideo();
        }
    }
    public void requestStoragePermission() {

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);//If the user has denied the permission previously your code will come to this block

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
    }

    public boolean checkPermissionForReadExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }


    private void choosevideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            String id = FirebaseFirestore.getInstance().collection("WeeklyVideos").document().getId();
            uploadVideOnCloudinary(data.getData(), id, new VideoDownloadLinkCallback() {
                @Override
                public void onCallBack(String downloadURL, double duration) {
                    if (downloadURL.isEmpty()) {
                        Services.showCenterToast(getContext(),"Upload Failed");
                    }
                    else {
                        ProgressHud.show(getContext(),"");
                        HighlightModel highlightModel = new HighlightModel();
                        highlightModel.id = id;
                        highlightModel.videoUrl = downloadURL;
                        highlightModel.userName = UserModel.data.getFullName();
                        highlightModel.videoLength = (int)duration;
                        highlightModel.addedDate = new Date();
                        highlightModel.thumbnail = downloadURL.replace(".mp4",".jpg");

                        FirebaseFirestore.getInstance().collection("WeeklyVideos").document(id).set(highlightModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                ProgressHud.dialog.dismiss();
                                if (task.isSuccessful()) {
                                    Services.showDialog(getContext(),"Uploaded","Thank You! We have received your video and we will review this for weekly highlight.");
                                }
                                else {
                                    Services.showDialog(getContext(),"ERROR",task.getException().getLocalizedMessage());
                                }
                            }
                        });
                    }
                }
            });
        }
    }




    private void uploadVideOnCloudinary(Uri videouri,String id, VideoDownloadLinkCallback downloadLinkCallback) {


        if (videouri != null) {

            MediaManager.get().upload(videouri)
                    .option("public_id",id)
                    .option("folder","WeeklyVideos")
                    .option("resource_type","video")
                    .callback(new UploadCallback() {
                        @Override
                        public void onStart(String requestId) {
                            progressBar.setMessage("Uploading Video...");
                            progressBar.show();
                        }

                        @Override
                        public void onProgress(String requestId, long bytes, long totalBytes) {
                            int percentage = (int) ((bytes*1.0 / totalBytes)*100);
                            progressBar.setProgress(percentage);

                        }

                        @Override
                        public void onSuccess(String requestId, Map resultData) {
                            progressBar.dismiss();
                            downloadLinkCallback.onCallBack(resultData.get("secure_url").toString(),(double)resultData.get("duration"));

                        }

                        @Override
                        public void onError(String requestId, ErrorInfo error) {
                            Services.showDialog(getContext(),"ERROR",error.getDescription());


                            progressBar.dismiss();

                            downloadLinkCallback.onCallBack("",-1);
                        }

                        @Override
                        public void onReschedule(String requestId, ErrorInfo error) {
                            Services.showDialog(getContext(),"ERROR",error.getDescription());
                            progressBar.dismiss();
                            downloadLinkCallback.onCallBack("",-1);
                        }
                    }).dispatch();




        }
    }
}
