package unimelb.snapchat;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Junwen on 10/11/2016.
 */
public class FriendRequest extends Activity{

    private CircleImageView mPhoto;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private TextView friendName;

    private String userId;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_request);


        recyclerView = (RecyclerView) findViewById(R.id.rv_request);


    }


    public void initObj(){

        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseUser.getUid();




    }

}
