package unimelb.snapchat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import unimelb.snapchat.Model.MessageModel;
import unimelb.snapchat.Model.StoryModel;
import unimelb.snapchat.viewHolders.MessageViewHolder;
import unimelb.snapchat.viewHolders.StoryViewHolder;

/**
 * Created by Xiaoyu on 10/10/2016.
 */
public class ChatActivity extends Activity {

    private String email;
    private ImageButton senderButton;
    private EditText messageEdit;
    private RecyclerView chat_recycle;
    private ImageView send_photo;
    //private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseRecyclerAdapter<MessageModel, MessageViewHolder> firebaseAdapter;
    private LinearLayoutManager layoutManager;

    private static final int PHOTO_REQUEST_GALLERY = 2;
    private static final int PHOTO_REQUEST_CUT = 3;
    private Uri downloadUrl;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_avtivity_layout);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        senderButton = (ImageButton) findViewById(R.id.send_message_button);
        messageEdit = (EditText) findViewById(R.id.send_message_edit);
        send_photo = (ImageView) findViewById(R.id.imageView6);
        chat_recycle = (RecyclerView) findViewById(R.id.chat_recy);
        email = getIntent().getStringExtra("email");
        setUpinitialData();


        senderButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String message = messageEdit.getText().toString().trim();
                sendMsg(message, null);
                messageEdit.setText("");
            }
        });
        send_photo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                gallery();
            }
        });
    }

    public void gallery()
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }
    private void crop(Uri uri)
    {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PHOTO_REQUEST_GALLERY)
        {
            if(data != null)
            {
                Uri uri = data.getData();
                crop(uri);
            }

        }else if(requestCode == PHOTO_REQUEST_CUT)
        {
            if(data != null)
            {
                Bitmap bitmap = data.getParcelableExtra("data");
                uploadPicture(bitmap);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void sendMsg(String message, String url)
    {
        long l = System.currentTimeMillis();
        Date date = new Date(l);
        SimpleDateFormat formater = new SimpleDateFormat();

        MessageModel msg_to_me = new MessageModel();
        MessageModel msg_to_friend = new MessageModel();

        //msg_to_me.setContent(message);
        msg_to_me.setTime(formater.format(date));
        msg_to_me.setIdentiy("me");

        //msg_to_friend.setContent(message);
        msg_to_friend.setTime(formater.format(date));
        msg_to_friend.setIdentiy("friend");
        if(message != null)
        {
            msg_to_me.setContent(message);
            msg_to_friend.setContent(message);

        }
        if(url != null)
        {
            msg_to_me.setUrl(url);
            msg_to_friend.setUrl(url);
        }


        DatabaseReference df_me = FirebaseDatabase.getInstance().getReference();
        DatabaseReference df_friend = FirebaseDatabase.getInstance().getReference();

        df_me.child("chat").child(firebaseUser.getEmail().replace(".", ",")).child(email).push().setValue(msg_to_me);
        df_friend.child("chat").child(email).child(firebaseUser.getEmail().replace(".", ",")).push().setValue(msg_to_friend);
    }

    private void setUpFirebaseAdapter(Query query) {

        firebaseAdapter = new FirebaseRecyclerAdapter<MessageModel, MessageViewHolder>
                (MessageModel.class, R.layout.chat_line, MessageViewHolder.class, query) {
            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder, MessageModel model, int position) {
                //customeLoaderDialog.hide();
                viewHolder.bindMessage(model);
            }
        };

        chat_recycle.setHasFixedSize(true);
        chat_recycle.setLayoutManager(new LinearLayoutManager(this));
        chat_recycle.setAdapter(firebaseAdapter);
    }


    public void backto (View v){
        Intent intent = new Intent();
        intent.setClass(ChatActivity.this,MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void setUpinitialData() {
        DatabaseReference df = FirebaseDatabase.getInstance().getReference();
        df = df.child("chat").child(firebaseUser.getEmail().replace(".", ",")).child(email);
        Query query = df.limitToFirst(50); // for the first 50 user from users node
        setUpFirebaseAdapter(query);
    }

    public void uploadPicture(Bitmap bitmap){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://android-45ca8.appspot.com");

        long timestamp = System.currentTimeMillis();
        StorageReference imagesRef = storageRef.child("snapchat").child("chat_img").child(timestamp+".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        System.out.println();

        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(ChatActivity.this, "Image sending failure!",Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                downloadUrl = taskSnapshot.getDownloadUrl();
                sendMsg(null, downloadUrl.toString());
            }
        });
    }
}
