package unimelb.snapchat.LoginAdnRegi;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import unimelb.snapchat.R;

public class regi extends Activity {


    private TextView mUsernameByid;

    private EditText mEmail;
    private EditText mUsername;
    private EditText mPassword;
    private EditText mPassword2;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Uri photoID;
    private static final String TAG = "EmailPassword";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        mAuth = FirebaseAuth.getInstance();

        mEmail = (EditText)findViewById(R.id.email_edit);
        mPassword = (EditText)findViewById(R.id.password_edit);
        mPassword2 = (EditText)findViewById(R.id.password_edit_again);
        mUsername = (EditText)findViewById(R.id.username_edit);


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getDisplayName());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        // [END auth_state_listener]


    }


    public void regilogin (View v){
       attemptRegi();
    }

    public void backtoMain (View v) {
        Intent intent = new Intent();
        intent.setClass(regi.this,sign.class);
        startActivity(intent);


    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    private void attemptRegi(){
        mEmail.setError(null);
        mPassword.setError(null);
        mPassword2.setError(null);
        mUsername.setError(null);

        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        String password2 = mPassword2.getText().toString();
        final String userName = mUsername.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(email)){
            mEmail.setError(getString(R.string.error_field_required));
            focusView = mEmail;
            cancel = true;
        }
        else if (!isEmailValid(email)){
            mEmail.setError(getString(R.string.error_invalid_email));
            focusView = mEmail;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)|| !isPasswordValid(password)){
            mPassword.setError(getString(R.string.error_invalid_password));
            focusView = mPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(password2) || !isPassword2Valid(password, password2)){
            mPassword2.setError(getString(R.string.error_invalid_password2));
            focusView = mPassword2;
            cancel = true;
        }

        if (TextUtils.isEmpty(userName)){
            mUsername.setError(getString(R.string.error_field_required));
            focusView = mUsername;
            cancel = true;
        }

        if (cancel){
            focusView.requestFocus();
        }
        else {
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                    if (!task.isSuccessful()) {
                        Log.d(TAG, "onComplete: Failed=" + task.getException().getMessage());
                        Toast.makeText(regi.this, R.string.auth_failed,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(mUsername.getText().toString())
                                .setPhotoUri(Uri.parse("http://www.juitmun.in/wp-content/uploads/2015/08/profile-default-male.png"))
                                .build();
                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User profile updated.");
                                            Log.d(TAG, "createUserWithEmail:onComplete:" + user.getPhotoUrl());
                                        }
                                    }
                                });
                        Toast.makeText(regi.this, "Register Success!!!",
                                Toast.LENGTH_SHORT).show();
                        goMain();
                    }
                }
            });
        }
    }

    private void goMain() {
        Intent intent = new Intent();
        intent.setClass(regi.this,sign.class);
        startActivity(intent);
    }

    private boolean isPassword2Valid(String password, String password2) {
        return password.equals(password2);
    }

    private boolean isPasswordValid(String password) {
        return password.length()>7;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

}





