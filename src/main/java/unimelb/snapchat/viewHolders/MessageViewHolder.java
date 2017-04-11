package unimelb.snapchat.viewHolders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import unimelb.snapchat.Model.MessageModel;
import unimelb.snapchat.Model.StoryModel;
import unimelb.snapchat.R;

/**
 * Created by Xiaoyu on 10/15/2016.
 */
public class MessageViewHolder extends RecyclerView.ViewHolder {

    View view;
    MessageModel message;
    Context context;
    public MessageViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        context = itemView.getContext();
    }

    public void bindMessage(MessageModel message)
    {
        this.message = message;
        TextView tv_me = (TextView)view.findViewById(R.id.tv_chat_me);
        TextView tv_friend = (TextView)view.findViewById(R.id.tv_chat_friend);
        ImageView iv_me = (ImageView) view.findViewById(R.id.iv_me);
        ImageView iv_friend = (ImageView)view.findViewById(R.id.iv_friend);

        if(message.getUrl() != null)
        {
            if(message.getIdentiy().equals("me"))
            {
                iv_me.setVisibility(View.VISIBLE);
                Picasso.with(context).load(message.getUrl())
                        .resize(200,200)
                        .centerCrop()
                        .into(iv_me);
            }else{
                iv_friend.setVisibility(View.VISIBLE);
                Picasso.with(context).load(message.getUrl())
                        .resize(200,200)
                        .centerCrop()
                        .into(iv_friend);
            }
        }
        if(message.getContent()!= null)
        {
            if(message.getIdentiy().equals("me"))
            {
                tv_me.setVisibility(View.VISIBLE);
                tv_me.setText(message.getContent());

            }else{
                tv_friend.setVisibility(View.VISIBLE);
                tv_friend.setText(message.getContent());
            }
        }

    }
}
