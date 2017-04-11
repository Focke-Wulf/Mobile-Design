package unimelb.snapchat.tabs;

import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.File;
import java.io.IOException;

import unimelb.snapchat.R;
import unimelb.snapchat.TimerView;


/**
 * Created by Xiaoyu on 9/15/2016.
 */
public class Camera_Fragment extends Fragment implements SurfaceHolder.Callback{
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    //private Button btn_start;
    private ImageButton btn_switch;
    private String fileName;
    private MediaRecorder mediaRecorder;
    private Camera camera;
    private Camera.Parameters parameters;
    private boolean isLongClick = false;
    private boolean isFrontFacing = true;
    private TimerView timer;
    private int totalProgress = 30;
    private int currentProgress = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.camera_fragment, container, false);
        surfaceView = (SurfaceView) view.findViewById(R.id.sv_cam);
        //btn_start = (Button) view.findViewById(R.id.btn_cam);
        btn_switch = (ImageButton) view.findViewById(R.id.btn_switch_cam);
        timer = (TimerView) view.findViewById(R.id.timer_view);
        initSurfaceView();

        btn_switch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                switchCam();
            }
        });
        timer.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view) {
                //btn_start.setText("starting");
                startCamera();
                ProgressRunnable progress = new ProgressRunnable();
                Thread thread = new Thread(progress);
                thread.start();
                isLongClick = true;
                return true;
            }
        });

        timer.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(isLongClick == true)
                {
                    switch (motionEvent.getActionMasked()){
                        case MotionEvent.ACTION_UP:
                        {
                            isLongClick = false;
                            //btn_start.setText("record");
                            mediaRecorder.stop();
                            mediaRecorder.release();
                        }
                    }
                }
                return false;
            }
        });
        return view;
    }
    public void initSurfaceView()
    {
        //Set full Screen
        //getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //set the surface view transparent
        getActivity().getWindow().setFormat(PixelFormat.TRANSLUCENT);
        // automatic buffer management
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.setFixedSize(1280, 720);
        surfaceHolder.setKeepScreenOn(true);
    }
    public void switchCam()
    {
        camera.stopPreview();
        if(camera != null)
        {
            camera.release();
            camera = null;
        }
        if(isFrontFacing == true)
        {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            isFrontFacing = false;
        }else{
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
            isFrontFacing = true;
        }
        camera.setDisplayOrientation(90);
        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
    }
    public void startCamera()
    {
        //mkdir for SnapChat
        String snapFolder = Environment.getExternalStorageDirectory().getAbsolutePath()+"/SnapChat";
        File dir = new File(snapFolder);
        if(!dir.exists())
        {
            dir.mkdir();
        }
        // init video file
        fileName = snapFolder+"/abc.mp4";
        dir = new File(fileName);

        mediaRecorder = new MediaRecorder();
        mediaRecorder.reset();
        camera.unlock();
        mediaRecorder.setCamera(camera);
        // Audio source from MIC
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // Video source from Camera
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        // Output file format
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        // Audio encoder
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        // Video encoder
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);

        mediaRecorder.setVideoSize(1280, 720);
        //mediaRecorder.setVideoFrameRate(20);
        mediaRecorder.setVideoEncodingBitRate(8*1024*1024);
        mediaRecorder.setOutputFile(fileName);

        mediaRecorder.setPreviewDisplay(surfaceView.getHolder().getSurface());
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
        parameters = camera.getParameters();
        camera.setParameters(parameters);
        camera.setDisplayOrientation(90);
        try {
            camera.setPreviewDisplay(surfaceHolder);

        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
    }

    @Override
    public void onPause() {
        super.onPause();
        camera.release();
    }
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    class ProgressRunnable implements Runnable
    {
        @Override
        public void run() {
            while(totalProgress > currentProgress && isLongClick==true)
            {
                currentProgress ++;
                timer.setProgress(currentProgress);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
