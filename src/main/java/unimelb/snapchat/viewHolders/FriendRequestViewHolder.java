package unimelb.snapchat.viewHolders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import unimelb.snapchat.Model.UserModel;
import unimelb.snapchat.R;

/**
 * Created by Junwen on 10/11/2016.
 */
public class FriendRequestViewHolder extends RecyclerView.ViewHolder{

    View view;
    UserModel friends;
    Context context;

    public FriendRequestViewHolder (View itemView){
        super(itemView);
        view = itemView;
        context = itemView.getContext();

    }

    public void bindUser (UserModel friends){
        this.friends = friends;
        CircleImageView photo = (CircleImageView) view.findViewById(R.id.request_friend_pohoto);
        TextView friendname = (TextView)view.findViewById(R.id.reuqest_friend_name);

    }

    public void request_accept(View v){}

    public void request_de(View v){}

}
