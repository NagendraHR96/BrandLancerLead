package com.example.brandlancerlead;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brandlancerlead.brandUtility.ServerConnection;
import com.example.brandlancerlead.brandUtility.WebContentsInterface;
import com.example.brandlancerlead.model.GetLeadCallFeedbackJsonObjects;
import com.example.brandlancerlead.model.GetLeadCallFeedbackJsonResult;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallDetailsActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl,MediaPlayer.OnSeekCompleteListener {
    RecyclerView telecaller_Reycler,executive_Reycler;

    String type,Id,BookingID;
    private Dialog progressOnLoad;
    LinearLayout executivr_Layout,telecaller_Layout;

    private MediaPlayer mediaPlayer;
    private MediaController mediaController;
    private Handler handler = new Handler();
    TextView telecallerTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_details);
        mediaController=findViewById(R.id.mediaCon);
        telecallerTxt=findViewById(R.id.telecallerTxt);


        telecaller_Reycler=findViewById(R.id.telecaller_Reycler);
        executive_Reycler=findViewById(R.id.executive_Reycler);

        telecaller_Layout=findViewById(R.id.leadLayout);
        executivr_Layout=findViewById(R.id.bookingLayout);

        LinearLayoutManager manager=new LinearLayoutManager(this);
        LinearLayoutManager manager1=new LinearLayoutManager(this);

        telecaller_Reycler.setLayoutManager(manager);
        executive_Reycler.setLayoutManager(manager1);

        executive_Reycler.setNestedScrollingEnabled(false);
        telecaller_Reycler.setNestedScrollingEnabled(false);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);

       // mediaController = new MediaController(this);
        progressOnLoad = new Dialog(this);
        mediaController=new MediaController(CallDetailsActivity.this){
            @Override
            public void hide() {
                //super.hide();
            }
            @Override
            public boolean dispatchKeyEvent(KeyEvent event) {

                if(event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

                    if (mediaPlayer != null) {
                        mediaPlayer.reset();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                    super.hide();
                    Activity a = (Activity)getContext();
                    a.finish();

                }
                return true;
            }
        };

        progressOnLoad.setContentView(new ProgressBar(this));

        if( progressOnLoad.getWindow() != null)
            progressOnLoad.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        progressOnLoad.show();

        Intent intent=getIntent();
        if (intent!=null){
            Bundle filterBundle=intent.getExtras();
            if (filterBundle!=null){
                type=filterBundle.getString("Type");
                if (type.equals("Booking")){
                    BookingID=filterBundle.getString("ID");
                    bindBookingCallDetails();
                    telecallerTxt.setVisibility(View.GONE);
                }else if (type.equals("Lead")){
                    Id=filterBundle.getString("ID");
                    bindCallDetails(Id);

                }
//                else {
//                    progressOnLoad.dismiss();
//                    finish();
//                }

            }
        }
       // bindCallDetails();
    }

    public  void  playAudio(String audioFile){

        if (audioFile!=null && !TextUtils.isEmpty(audioFile)) {

            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.reset();
            }
            try {
                if (mediaPlayer != null) {
                    mediaPlayer.reset();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setDataSource(audioFile);

                    mediaPlayer.prepare();
                    mediaPlayer.start();
                }
            } catch (IOException e) {
                Log.e("Play", "Could not open file " + audioFile + " for playback.", e);
            }
        }else {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.reset();
            }
            Toast.makeText(CallDetailsActivity.this,"Empty Data Source",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer!=null) {
            mediaController.hide();
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    public void onPrepared(final MediaPlayer mediaPlayer) {
        Log.d("", "onPrepared");

        mediaController.setMediaPlayer(this);
        mediaController.setAnchorView(findViewById(R.id.mediaCon));

        mediaController.setEnabled(true);
        mediaController.show();

        handler.post(new Runnable() {
            public void run() {

//                if(mediaPlayer != null){
//                    int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
//                   // mSeekBar.setProgress(mCurrentPosition);
//                }
//                handler.postDelayed(this, 1000);

            }
        });
    }

    private void bindBookingCallDetails() {
        //executivr_Layout.setVisibility(View.GONE);
        WebContentsInterface contentsInterface= ServerConnection.createRetrofitConnection(WebContentsInterface.class);
        Call<GetLeadCallFeedbackJsonResult>jsonResultCall=contentsInterface.bindBookingCallDetails(BookingID);
        jsonResultCall.enqueue(new Callback<GetLeadCallFeedbackJsonResult>() {
            @Override
            public void onResponse(Call<GetLeadCallFeedbackJsonResult> call, Response<GetLeadCallFeedbackJsonResult> response) {
                if (response.code()==200){

                    GetLeadCallFeedbackJsonResult jsonResult=response.body();
                    if (jsonResult!=null && jsonResult.isResult() && jsonResult.getResultset()!=null){
                        displayDetails(jsonResult.getResultset(),true);
                    }else {
                        Toast.makeText(CallDetailsActivity.this,jsonResult.getResultmessage(),Toast.LENGTH_SHORT).show();
                        progressOnLoad.dismiss();
                    }
                }else {
                    Toast.makeText(CallDetailsActivity.this,response.message(),Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<GetLeadCallFeedbackJsonResult> call, Throwable t) {
                Toast.makeText(CallDetailsActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();

                progressOnLoad.dismiss();

            }
        });

    }


    private void bindCallDetails(String id) {
        executivr_Layout.setVisibility(View.VISIBLE);
        telecaller_Layout.setVisibility(View.VISIBLE);
        WebContentsInterface contentsInterface= ServerConnection.createRetrofitConnection(WebContentsInterface.class);
        Call<GetLeadCallFeedbackJsonResult>jsonResultCall=contentsInterface.bindCallDetails(id);
        jsonResultCall.enqueue(new Callback<GetLeadCallFeedbackJsonResult>() {
            @Override
            public void onResponse(Call<GetLeadCallFeedbackJsonResult> call, Response<GetLeadCallFeedbackJsonResult> response) {
                if (response.code()==200){

                    GetLeadCallFeedbackJsonResult jsonResult=response.body();
                    if (jsonResult!=null && jsonResult.isResult() && jsonResult.getExecutiveCallFeedback()!=null){
                        displayDetails(jsonResult.getExecutiveCallFeedback(),true);
                    }else {
                        progressOnLoad.dismiss();
                        executivr_Layout.setVisibility(View.GONE);
                    }
                    if (jsonResult.getTelecallerCallFeedback()!=null){
                        displayDetails(jsonResult.getTelecallerCallFeedback(),false);

                    }else {
                        telecaller_Layout.setVisibility(View.GONE);
                        progressOnLoad.dismiss();
                    }
                }else {
                    progressOnLoad.dismiss();
                    Toast.makeText(CallDetailsActivity.this,response.message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetLeadCallFeedbackJsonResult> call, Throwable t) {
                Toast.makeText(CallDetailsActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();

                progressOnLoad.dismiss();

            }
        });


    }

    private void displayDetails(ArrayList<GetLeadCallFeedbackJsonObjects> resultset,boolean isExecutive) {
        if (isExecutive){
            progressOnLoad.dismiss();
            CallDisplayAdapter callDisplayAdapter=new CallDisplayAdapter(CallDetailsActivity.this,resultset);
            executive_Reycler.setAdapter(callDisplayAdapter);
        }else {
            progressOnLoad.dismiss();
            CallDisplayAdapter callDisplayAdapter=new CallDisplayAdapter(CallDetailsActivity.this,resultset);
            telecaller_Reycler.setAdapter(callDisplayAdapter);
        }

    }


    @Override
    public void start() {
        mediaPlayer.start();
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        mediaPlayer.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }


    @Override
    protected void onDestroy() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer=null;
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer=null;
        }else {
            mediaPlayer=null;
        }
        super.onBackPressed();
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        SystemClock.sleep(100);

    }
}
