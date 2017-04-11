package unimelb.snapchat.tabs;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import unimelb.snapchat.MainActivity;
import unimelb.snapchat.Model.UserModel;
import unimelb.snapchat.R;
import unimelb.snapchat.SearchNewFriends;
import unimelb.snapchat.viewHolders.UserViewHolder;

/**
 * Created by Edwin on 15/02/2015.
 */
public class Contact_Fragment extends Fragment {
    private static final int REQUEST_INVITE = 1;



    private ImageButton imageButton;
    private RecyclerView recyclerView;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private LinearLayoutManager layoutManager;
    private FirebaseRecyclerAdapter <UserModel, UserViewHolder> mFirebaseAdapter;
    private String userId;
    private Uri mPhotoUrl;


    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_layout_fragment,container,false);
        recyclerView = (RecyclerView)view.findViewById(R.id.rv_friendlist);

        initObj();
        setUpinitialData();
        return view;
    }
    public void initObj()
    {
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        firebaseDatabase = FirebaseDatabase.getInstance();
    }
    private void setUpFirebaseAdapter(Query query) {

        mFirebaseAdapter = new FirebaseRecyclerAdapter<UserModel, UserViewHolder>
                (UserModel.class, R.layout.roll_user_list, UserViewHolder.class, query) {
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, UserModel model,
                                              int position) {
                viewHolder.bindUser(model);
            }
        };
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mFirebaseAdapter);
    }

    private void setUpinitialData() {

        DatabaseReference databaseReference = firebaseDatabase.getReference().child("FriendLists").child(firebaseAuth.getCurrentUser().getEmail().replace(".",","));
        Query query = databaseReference.limitToFirst(50); // for the first 50 user from users node
        setUpFirebaseAdapter(query);
       // System.out.println("hahahha:"+query.toString());
    }

    //FireBase Invite New Friends
//    private void sendInvitation() {
//        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
//                .setMessage(getString(R.string.invitation_message))
//                .setCallToActionText(getString(R.string.invitation_cta))
//                .build();
//        startActivityForResult(intent, REQUEST_INVITE);
//    }

}