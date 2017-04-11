package unimelb.snapchat.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import unimelb.snapchat.Model.DiscoverModel;
import unimelb.snapchat.R;

/**
 * Created by Xiaoyu on 10/17/2016.
 */
public class WaterFallAdapter extends RecyclerView.Adapter{
    private Context context;
    private LayoutInflater layoutInflater;
    private List<Integer> heights;
    private List<DiscoverModel> discoverList;
    //private List<Bitmap> ivs;
    //private List<String> urls;
    private int mScreenWidth;
    public OnItemClickListener listener;

    public WaterFallAdapter(Context context, List<DiscoverModel> discoverList, int mScreenWidth) {
        super();
        this.context = context;
        this.discoverList = discoverList;
        this.mScreenWidth = mScreenWidth;
        layoutInflater = LayoutInflater.from(context);

        heights = new ArrayList<Integer>();
        for(int i=0; i< discoverList.size(); i++){
            heights.add((int) (500 + Math.random() * 300));
        }

    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
        void onItemLongClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.roll_discover, null);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MyViewHolder vHolder = (MyViewHolder) holder;
        ViewGroup.LayoutParams tvLp = vHolder.iv.getLayoutParams();
        tvLp.height = heights.get(position);
        tvLp.width = mScreenWidth / 2;

        vHolder.iv.setLayoutParams(tvLp);
        //vHolder.iv.setImageBitmap(ivs.get(position));
        Picasso.with(context).load(discoverList.get(position).getUrl())
                .resize(200,200)
                .centerCrop()
                .into(vHolder.iv);

        vHolder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {;
                int position2 = vHolder.getPosition();
                listener.onItemClick(v, position2);
            }
        });

        vHolder.iv.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                int position2 = vHolder.getPosition();
                listener.onItemLongClick(v, position2);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return discoverList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv_item_discover);
        }
    }
}
