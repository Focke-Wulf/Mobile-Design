package unimelb.snapchat;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import unimelb.snapchat.Model.UserModel;
import unimelb.snapchat.viewHolders.FriendSearchViewHolder;
import unimelb.snapchat.viewHolders.UserViewHolder;

/**
 * Created by Junwen on 10/11/2016.
 */
public class SearchNewFriends extends Activity {


    private LinearLayout linear_result;
    private ImageView img_profile;
    private TextView tv_name;
    private TextView tv_status;

    private RecyclerView recyclerView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private LinearLayoutManager layoutManager;
    private Context context = this;

    private static final int MAX_WIDTH = 200;
    private static final int MAX_HEIGHT = 200;

    private FirebaseRecyclerAdapter<UserModel, FriendSearchViewHolder> mFirebaseAdapter;

    private String friendEmail;
    private Uri mPhotoUrl;
    private String friendName;
    private UserModel searchUser;

    String TAG = "SearchActivity";

    private ImageButton ButtonSearch;
    private EditText text_search;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_search);
        recyclerView = (RecyclerView) findViewById(R.id.rv_Search);
        linear_result = (LinearLayout)findViewById(R.id.linear_result);
        img_profile = (ImageView)findViewById(R.id.img_search);
        tv_name = (TextView)findViewById(R.id.tv_search_name);
        tv_status = (TextView)findViewById(R.id.tv_search_state);

        ButtonSearch = (ImageButton) findViewById(R.id.start_search_text);
        text_search = (EditText) findViewById(R.id.Search_text);
        //firebaseDatabase=FirebaseDatabase.getInstance();
        initObj();

    }
    public void friend_search (View v){
        getSearch();
    }

    public void search_back (View v){
        Intent intent = new Intent();
        intent.setClass(SearchNewFriends.this,MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void initObj () {
        firebaseAuth = FirebaseAuth.getInstance();
       // userId = firebaseAuth.getCurrentUser().getUid();
        firebaseDatabase=FirebaseDatabase.getInstance();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void getSearch() {

        String textS = text_search.getText().toString();
        String newtext = textS.replace(".",",");
        Log.d(TAG, "This is Searching Text Value in Search Activity=== " + textS);
        Toast.makeText(SearchNewFriends.this, "Searching....",
                Toast.LENGTH_SHORT).show();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.child("userInformation").child(newtext).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //System.out.println("hahahahahha:" + dataSnapshot.getValue().toString());
                if(dataSnapshot != null)
                {
                    HashMap searchUser = (HashMap) dataSnapshot.getValue();
                    linear_result.setVisibility(View.VISIBLE);
                    if(searchUser.get("profileImageUri").equals(""))
                    {
                        img_profile.setImageResource(R.drawable.touxiang);
                    }else{
                        Picasso.with(context).load((String)searchUser.get("profileImageUri"))
                                .resize(MAX_WIDTH,MAX_HEIGHT)
                                .centerCrop()
                                .into(img_profile);
                    }

                    tv_name.setText((String)searchUser.get("username"));
                    String state = (String)searchUser.get("state");
                    if(state.equals("Online"))
                    {
                        tv_status.setTextColor(Color.parseColor("#42cc0c"));
                    }

                    tv_status.setText((String)searchUser.get("state"));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }



        });

    }

    public void startaddfriend(View v) {

        new AlertDialog.Builder(context).setTitle("Confirm").setMessage("确定?")
                .setPositiveButton("是",
                        new android.content.DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String textS = text_search.getText().toString();


                                String newtext = textS.replace(".",",");
                                getUser(newtext);

                            }
                        }
                        )
                .setNegativeButton("否",

                        new android.content.DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialogInterface, int i) {

                                 dialogInterface.dismiss();
                            }
                        }

                        ).show();


    }
    public void getUser(final String text){
        final UserModel user = new UserModel();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        String textS = text_search.getText().toString();
        final String newtext = textS.replace(".",",");

        databaseReference.child("userInformation").child(text).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap searchUser = (HashMap) dataSnapshot.getValue();
                user.username =(String) searchUser.get("username");
                user.profileImageUri = (String) searchUser.get("profileImageUri");
                user.state = (String)searchUser.get("state");
                user.email = (String)searchUser.get("email");

                databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("FriendLists").child(firebaseUser.getEmail().replace(".",",")).child(text).setValue(user);
                Log.d("Get USer", "This is Get user : " + user.username.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }




}
