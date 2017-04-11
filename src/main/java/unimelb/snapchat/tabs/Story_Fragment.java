package unimelb.snapchat.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import unimelb.snapchat.Model.StoryModel;
import unimelb.snapchat.R;
import unimelb.snapchat.TellStoryActivity;
import unimelb.snapchat.viewHolders.StoryViewHolder;

public class Story_Fragment extends Fragment {
    private TextView tell_story;
    private RecyclerView rv_story;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private LinearLayoutManager layoutManager;
    private FirebaseRecyclerAdapter<StoryModel, StoryViewHolder> firebaseAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.story_fragment,container,false);

        rv_story = (RecyclerView) view.findViewById(R.id.rv_story_list);
                           rv_story.setHasFixedSize(true);
                           StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3 , StaggeredGridLayoutManager.VERTICAL);
                            rv_story.setLayoutManager(layoutManager);

        tell_story = (TextView)view.findViewById(R.id.tv_tell_story);
        tell_story.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TellStoryActivity.class);
                startActivity(intent);
            }
        });

        initObj();
        setUpinitialData();
        return view;
    }

    public void initObj()
    {
        firebaseAuth = FirebaseAuth.getInstance();
        layoutManager = new LinearLayoutManager(getActivity());
        rv_story.setLayoutManager(layoutManager);
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    private void setUpFirebaseAdapter(Query query) {

        firebaseAdapter = new FirebaseRecyclerAdapter<StoryModel, StoryViewHolder>
                (StoryModel.class, R.layout.roll_story_list, StoryViewHolder.class, query) {
            @Override
            protected void populateViewHolder(StoryViewHolder viewHolder, StoryModel model, int position) {
                //customeLoaderDialog.hide();
                viewHolder.bindStory(model);
            }
        };
        rv_story.setHasFixedSize(true);
        rv_story.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_story.setAdapter(firebaseAdapter);
    }
    private void setUpinitialData() {
        //Query query = firebaseDatabase.getReference().orderByChild("story").equalTo("KTtI_wGCLva5wH50n6y");
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("story");
        Query query = databaseReference.limitToFirst(50); // for the first 50 user from users node
        setUpFirebaseAdapter(query);
        //System.out.println("hahahha:"+query.toString());
    }
}