package unimelb.snapchat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import unimelb.snapchat.Model.StoryModel;

/**
 * Created by Xiaoyu on 10/11/2016.
 */
public class TellStoryActivity extends Activity {
    private EditText et_title;
    private EditText et_content;
    private ImageView img_photo;
    private Button btn_submit;
    private Button btn_camera;
    private Button btn_gallery;
    private Bitmap bitmap;
    private Uri downloadUrl;
    private String username;

    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;

    private static final int PHOTO_REQUEST_GALLERY = 2;
    private static final int PHOTO_REQUEST_CUT = 3;
    private static final int PHOTO_REQUEST_CAREMA = 1;

    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    private File tempFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tell_story_activity);

        et_title = (EditText)findViewById(R.id.et_story_title);
        et_content = (EditText)findViewById(R.id.et_story_content);
        img_photo = (ImageView) findViewById(R.id.img_photo);
        btn_submit = (Button) findViewById(R.id.btn_tell_story);
        btn_camera = (Button) findViewById(R.id.btn_cam);
        btn_gallery = (Button)findViewById(R.id.btn_gallery);

        btn_gallery.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                gallery();
            }
        });

        btn_camera.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                camera();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                uploadPicture();
                //submit();
            }
        });
    }

    public void submit()
    {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("userInformation").child(firebaseUser.getEmail().replace(".",",")).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                username = (String)dataSnapshot.getValue();
                long l = System.currentTimeMillis();
                Date date = new Date(l);
                SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");

                StoryModel story = new StoryModel(username, et_title.getText().toString(), et_content.getText().toString(), downloadUrl.toString(),formater.format(date));
                databaseReference.child("story").push().setValue(story);
                Toast.makeText(TellStoryActivity.this, "Uploading story...", Toast.LENGTH_LONG).show();

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Toast.makeText(TellStoryActivity.this, "Upload success", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(TellStoryActivity.this, "Upload fail", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void uploadPicture()
    {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://android-45ca8.appspot.com");

        long timestamp = System.currentTimeMillis();
        StorageReference imagesRef = storageRef.child("snapchat").child("story_img").child(timestamp+".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(TellStoryActivity.this, "Image upload failure!",Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                downloadUrl = taskSnapshot.getDownloadUrl();
                System.out.println("Picture URL:"+downloadUrl.toString());
                submit();
            }
        });
    }

    public void gallery()
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    public void camera()
    {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        if (hasSdcard())
        {
            tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME);
            Uri uri = Uri.fromFile(tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
    }

    public boolean hasSdcard()
    {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            return true;
        }else{
            return false;
        }

    }

    private void crop(Uri uri)
    {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 400);
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
                bitmap = data.getParcelableExtra("data");
                img_photo.setImageBitmap(bitmap);
            }
            //delete temp file
            if(tempFile != null)
            {
                tempFile.delete();
            }
        }else if(requestCode == PHOTO_REQUEST_CAREMA)
        {
            if (hasSdcard())
            {
                crop(Uri.fromFile(tempFile));
            }else{
                Toast.makeText(TellStoryActivity.this, "No sd card！",Toast.LENGTH_SHORT).show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
