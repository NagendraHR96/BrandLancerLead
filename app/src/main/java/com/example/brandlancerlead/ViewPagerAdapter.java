package com.example.brandlancerlead;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Uri> imageUriLIst ;
    private HashMap<String,ArrayList<Uri>>listHashMap;

    public ViewPagerAdapter(FragmentManager fm) {

        super ( fm );
        imageUriLIst = new ArrayList<Uri>();
        listHashMap=new HashMap<>();
    }

    @Override
    public Fragment getItem(int i) {

        Bundle b = new Bundle();
        b.putString("url", imageUriLIst.get(i).toString());
        b.putInt("pos",i);

        BlankFragment fragment = new BlankFragment();

        fragment.setArguments(b);

      return  fragment;

    }
    @Override
    public int getItemPosition(@NonNull Object object) {

        return POSITION_NONE;
    }

    @Override
    public int getCount() {

        return imageUriLIst.size();
    }

    public void updateImageuri(ArrayList<Uri> image){
        imageUriLIst.clear();
        imageUriLIst.addAll(image);
        notifyDataSetChanged();
    }

    public void reImageuri(Uri image){
        imageUriLIst.remove(image) ;
        notifyDataSetChanged();
    }

}
