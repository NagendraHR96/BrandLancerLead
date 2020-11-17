package com.example.brandlancerlead;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


public class BlankFragment extends Fragment {

    Uri listOfImages ;
    int pos;

    public BlankFragment() {

    }
    @SuppressLint("ValidFragment")
    public BlankFragment(Uri listOfImages, int pos) {
        this.listOfImages = listOfImages;
        this.pos= pos;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate ( R.layout.fragment_blank, container, false );
        ImageView  imageLoad=view.findViewById(R.id.loadImages);

        Bundle eb = getArguments();
        String path =  eb.getString("url");
        listOfImages = Uri.parse(path);
        pos = eb.getInt("pos");

        Glide.with(getActivity()).load(listOfImages).into(imageLoad);
        Button remove = view.findViewById(R.id.removeImageList);
        remove.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if(getActivity() instanceof SiteProgressActivity)
                    ((SiteProgressActivity) getActivity()).removeList(pos);
             }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}
