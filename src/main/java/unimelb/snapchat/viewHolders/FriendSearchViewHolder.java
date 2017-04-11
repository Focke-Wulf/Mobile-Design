package unimelb.snapchat.viewHolders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import unimelb.snapchat.MainActivity;
import unimelb.snapchat.Model.UserModel;
import unimelb.snapchat.R;

/**
 * Created by Junwen on 10/11/2016.
 */
public class FriendSearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    View view;
    UserModel friends;
    Context context;
    private static final int MAX_WIDTH = 200;
    private static final int MAX_HEIGHT = 200;

    public FriendSearchViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        itemView.setOnClickListener(this);
        context=itemView.getContext();}


    public void bindUser (UserModel friends){
        this.friends = friends;
        CircleImageView photo = (CircleImageView) view.findViewById(R.id.search_pohoto);
        TextView friendname = (TextView)view.findViewById(R.id.tvName_search);
        friendname.setText(friends.username);
        Picasso.with(context).load(friends.profileImageUri)
                .resize(MAX_WIDTH,MAX_HEIGHT)
                .centerCrop()
                .into(photo);
    }
    @Override
    public void onClick(View view) {
        new AlertDialog.Builder(context).setTitle("确认").setMessage("确定吗？")
                .setPositiveButton("是",
                        null)
                .setNegativeButton("否",null).show();


    }
}
