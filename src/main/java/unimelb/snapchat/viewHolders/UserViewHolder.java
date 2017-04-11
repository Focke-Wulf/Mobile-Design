package unimelb.snapchat.viewHolders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import unimelb.snapchat.ChatActivity;
import unimelb.snapchat.Model.UserModel;
import unimelb.snapchat.R;

/**
 * Created by Xiaoyu on 10/10/2016.
 */
public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    View view;
    UserModel user;
    Context context;
    private static final int MAX_WIDTH = 200;
    private static final int MAX_HEIGHT = 200;

    public UserViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        context = itemView.getContext();
        itemView.setOnClickListener(this);
    }
    public void bindUser(UserModel user)
    {
        this.user = user;
        CircleImageView photo = (CircleImageView) view.findViewById(R.id.friend_pohoto);
        TextView tv_name = (TextView)view.findViewById(R.id.tvName);
        TextView tv_state = (TextView)view.findViewById(R.id.tvStatus);

        if(user.state.equals("Online")){
            tv_state.setTextColor(Color.parseColor("#2F8638"));
            tv_state.setText(user.state);
        }else if (user.state.equals("Offline")){
            tv_state.setTextColor(Color.parseColor("#d1395c"));
            tv_state.setText(user.state);
        }
        tv_name.setText(user.username);


        if(user.profileImageUri.equals(""))
        {
            photo.setImageResource(R.drawable.touxiang);
        }else{
            Picasso.with(context).load(user.profileImageUri)
                    .resize(MAX_WIDTH,MAX_HEIGHT)
                    .centerCrop()
                    .into(photo);
        }

    }

    @Override
    public void onClick(View view) {
//        Log.d("UserHolder","This is userEmail" + user.email.toString());


        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("email", user.email);

//        intent.putExtra("reciverUid", userModel.getUserId());
//        intent.putExtra("reciverProfilePic", userModel.getProfileImageUri());
        context.startActivity(intent);
        ((Activity) context).finish();
    }
}
