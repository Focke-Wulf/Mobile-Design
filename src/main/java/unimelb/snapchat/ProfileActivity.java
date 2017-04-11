package unimelb.snapchat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.auth.api.model.StringList;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;
import unimelb.snapchat.Model.UserModel;

/**
 * Created by Xiaoyu on 10/10/2016.
 */
public class ProfileActivity extends Activity {


    private EditText mRealName;
    private EditText mAddress;
    private EditText mPhone;
    private EditText mEmail;
    private CircleImageView mPhoto;
    private String mPhotoUrl;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private static String TAG = "Profile";

    private  String userID;
    private String uniname;
    private String uniemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);

        mRealName = (EditText) findViewById(R.id.et_realname);
        mAddress = (EditText) findViewById(R.id.et_address);
        mPhone = (EditText) findViewById(R.id.et_phone);
        mEmail = (EditText) findViewById(R.id.et_email);
        mPhoto = (CircleImageView) findViewById(R.id.friend_pohoto);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mPhotoUrl = firebaseUser.getPhotoUrl().toString();
        userID = firebaseUser.getUid().toString();
        uniname = firebaseUser.getDisplayName().toString();
        uniemail = firebaseUser.getEmail().toString();

    }

    public void updateProfile(View v) {
        updataInfo();
    }

    public void return_profile(View v) {
        Intent intent = new Intent();
        intent.setClass(ProfileActivity.this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void updataInfo() {
        String uusername = uniname;
        String uniqueemail = uniemail;
        String userid = userID;
        String username = mRealName.getText().toString();
        String address = mAddress.getText().toString();
        String phone = mPhone.getText().toString();
        String pohoto = mPhotoUrl.toString();
        // 很重要！

        String newtext = uniqueemail.replace(".",",");


        //比较烦人，还要往 Auth 里的用户数据库同步更新一下
        UserProfileChangeRequest profileUpdatas = new UserProfileChangeRequest.Builder()
                .setDisplayName(username).build();
        firebaseUser.updateProfile(profileUpdatas);
        UserModel user = new UserModel(userid,username, address, phone, pohoto, "Online",firebaseUser.getEmail().toString());
        databaseReference.child("userInformation").child(newtext).setValue(user);
        Toast.makeText(this, "Profile Update Success", Toast.LENGTH_LONG).show();

    }


}