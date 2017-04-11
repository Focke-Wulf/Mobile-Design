package unimelb.snapchat.tabs;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;


import unimelb.snapchat.Model.DiscoverModel;
import unimelb.snapchat.R;
import unimelb.snapchat.adapter.WaterFallAdapter;

/**
 * Created by Edwin on 15/02/2015.
 */
public class Discovery_Fragment extends Fragment{
    private RecyclerView recyclerView;
    private int mScreenWidth;
    private WaterFallAdapter adapter;
    private ArrayList<DiscoverModel> discoverList = new ArrayList<DiscoverModel>();
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.discovery_fragment,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_discover);
        recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        getPic();

        return view;
    }
    public void getPic()
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference().child("discover").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getValue().toString();
                int i = 0;
                for (DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    DiscoverModel post = snapshot.getValue(DiscoverModel.class);
                    discoverList.add(post);
                    //System.out.println("testestest: "+ post.getLabel());
                }
                //System.out.println("testestest: "+ discoverList.size());
                initData();

                adapter = new WaterFallAdapter(getActivity(),discoverList, mScreenWidth);
                adapter.setOnItemClickListener(new WaterFallAdapter.OnItemClickListener(){
                    @Override
                    public void onItemClick(View v, int position) {
                        Toast.makeText(getActivity(), position + "--click", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemLongClick(View v, int position) {
                        Toast.makeText(getActivity(), position + "--long click", Toast.LENGTH_SHORT).show();
                        discoverList.remove(position);
                        //urls.remove(position);
//                        ivs.remove(position);
                        adapter.notifyItemRemoved(position);
                    }
                });
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void initData() {
//        list = new ArrayList<String>();
//        //urls = new ArrayList<String>();
//        //ivs = new ArrayList<Bitmap>();
//        heights = new ArrayList<Integer>();
//        for (int i = 'A'; i <= 'Z'; i++) {
//            list.add("" + (char) i);
//            heights.add((int) (300 + Math.random() * 300));
//        }


//        for (int i = 0; i<discoverList.size(); i++)
//        {
//            String url = ((DiscoverModel)discoverList.get(i)).getUrl();
//            urls.add(url);
//
//        }

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
    }


}