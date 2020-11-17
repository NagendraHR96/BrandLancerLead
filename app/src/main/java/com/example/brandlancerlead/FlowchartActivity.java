package com.example.brandlancerlead;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;

public class FlowchartActivity extends AppCompatActivity { ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flowchart);


        final ZoomableImageView flowChartImag = findViewById(R.id.workflowchart);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        Intent chartData = getIntent();
        if (chartData != null) {
            Bundle chartBundle = chartData.getExtras();
            if (chartBundle != null) {
                int chart = chartBundle.getInt("chartImg");
                flowChartImag.setImageResource(chart);

            }else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                Glide.with(this)
                        .asBitmap()
                        .load("http://49.206.229.93:8089/Layouts/gardenia.png")
                        .apply(new RequestOptions().override(2400, 2400)) //This is important
                        .into(new BitmapImageViewTarget(flowChartImag) {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                super.onResourceReady(resource, transition);
                                flowChartImag.setImageBitmap(resource);
                                flowChartImag.setZoom(1); //This is important
                            }
                        });
            }

        }
    }


}
