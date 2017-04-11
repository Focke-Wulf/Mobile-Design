package unimelb.snapchat.viewHolders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import unimelb.snapchat.Model.StoryModel;
import unimelb.snapchat.R;

/**
 * Created by Xiaoyu on 10/13/2016.
 */
public class StoryViewHolder extends RecyclerView.ViewHolder{

    View view;
    StoryModel story;
    Context context;
    public StoryViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        context = itemView.getContext();
    }

    public void bindStory(StoryModel story)
    {
        this.story = story;
        TextView tv_title = (TextView)view.findViewById(R.id.tv_title);
        TextView tv_content = (TextView)view.findViewById(R.id.tv_content);
        ImageView imageView = (ImageView)view.findViewById(R.id.img_in_story);
        TextView tv_author = (TextView)view.findViewById(R.id.tv_author);
        TextView tv_date = (TextView)view.findViewById(R.id.tv_date);


        tv_title.setText(story.getTitle());
        tv_content.setText(story.getContent());
        tv_author.setText(story.getUsername());
        tv_date.setText(story.getDate());

        Picasso.with(context).load(story.getUrl())
                .resize(200,200)
                .centerCrop()
                .into(imageView);

    }
}
