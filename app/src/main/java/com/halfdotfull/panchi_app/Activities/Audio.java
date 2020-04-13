package com.halfdotfull.panchi_app.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.halfdotfull.panchi_app.R;

import java.io.IOException;
import java.util.UUID;

public class Audio extends AppCompatActivity {
    Button btnStartRecord,btnStopRecord,btnPlayRecord,btnStop;
    String pathsave="";
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;

    final int REQUEST_PERMISSION_CODE=1000;
    @RequiresApi(api = Build.VERSION_CODES.M)



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_POWER) {
            // Do something here...
            event.startTracking(); // Needed to track long presses
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_POWER) {
            // Do something here...
            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        if(!checkPermissionFromDevice())
            requestPermissions();

            btnStartRecord=(Button)findViewById(R.id.btnStartRecord);
            btnStopRecord=(Button)findViewById(R.id.btnStopRecord);
            btnPlayRecord=(Button)findViewById(R.id.btnPlayRecord);
            btnStop=(Button)findViewById(R.id.btnStop);


            btnStartRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(checkPermissionFromDevice())
                    {

                    pathsave= Environment.getExternalStorageDirectory().getAbsolutePath()+"/"
                            + UUID.randomUUID().toString()+"_audio_record.3gp";
                    setupMediaRecorder();
                    try{
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    btnPlayRecord.setEnabled(false);
                    btnStop.setEnabled(false);
                    Toast.makeText(Audio.this,"Recording........",Toast.LENGTH_SHORT).show();
                    }

                    else{
                        requestPermissions();
                    }
                }
            });

            btnStopRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaRecorder.stop();
                    btnStopRecord.setEnabled(false);
                    btnPlayRecord.setEnabled(true);
                    btnStartRecord.setEnabled(true);
                    btnStop.setEnabled(false);
                    Log.d("Path",pathsave);


//                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
//                    sendIntent.setClassName("com.android.mms", "com.android.mms.ui.ComposeMessageActivity");
//                    sendIntent.putExtra("address", "9920696047");
//                    sendIntent.putExtra("sms_body", "if you are sending text");
//                    final File file1 = new File(pathsave);
//                    Uri uri = Uri.fromFile(file1);
//
//                    sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
//                    sendIntent.setType("audio/3gp");
//                    startActivity(Intent.createChooser(sendIntent, "Send file"));
                }
            });
            btnPlayRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnStop.setEnabled(true);
                    btnStopRecord.setEnabled(false);
                    btnStartRecord.setEnabled(false);
                        mediaPlayer=new MediaPlayer();
                        try{
                            mediaPlayer.setDataSource(pathsave);
                            mediaPlayer.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mediaPlayer.start();
                    Toast.makeText(Audio.this, "Playing....", Toast.LENGTH_SHORT).show();
                }
            });

            btnStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnStopRecord.setEnabled(false);
                    btnStartRecord.setEnabled(true);
                    btnStop.setEnabled(false);
                    btnPlayRecord.setEnabled(true);

                    if(mediaPlayer!=null){
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        setupMediaRecorder();
                    }
                }


            });




    }
    private void setupMediaRecorder() {
        mediaRecorder= new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(pathsave);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_PERMISSION_CODE:
            {
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"Permission",Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(this,"Permission denied",Toast.LENGTH_SHORT).show();

                }
            }
            break;
        }
    }

    private void requestPermissions() {

        ActivityCompat.requestPermissions(this,new String[]{

                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        },REQUEST_PERMISSION_CODE);



    }

    private boolean checkPermissionFromDevice(){
        int write_external_storage_result= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result= ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result== PackageManager.PERMISSION_GRANTED &&
                record_audio_result == PackageManager.PERMISSION_GRANTED;


    }
}
