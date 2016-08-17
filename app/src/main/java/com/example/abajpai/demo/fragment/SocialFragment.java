package com.example.abajpai.demo.fragment;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DialerFilter;
import android.widget.ListView;

import com.example.abajpai.demo.R;
import com.example.abajpai.demo.adapter.SocialListAdapter;
import com.example.abajpai.demo.model.SocialModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ABajpai on 8/8/2016.
 */
public class SocialFragment extends DialogFragment {
     SocialListAdapter baseAdapter ;
     boolean feedButtonFlag ;
     ArrayList<SocialModel> socialAcList ;
    public SocialFragment(ArrayList<SocialModel> socialAcList){
        this.socialAcList = socialAcList ;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.social_list, container, false);
        Button done = (Button)rootView.findViewById(R.id.ok);
        done.setOnClickListener(new View.OnClickListener() {
        @Override
            public void onClick(View v) {
                feedButtonFlag =  baseAdapter.getStatus();
                getActivity().invalidateOptionsMenu();
                getDialog().dismiss();
            }
        });
        ListView socialList = (ListView) rootView.findViewById(R.id.social_list);

        getDialog().setTitle("Select Social Media ");
        getDialog().setCanceledOnTouchOutside(false);
        baseAdapter = new SocialListAdapter(getActivity(),socialAcList);
        socialList.setAdapter(baseAdapter);
        return rootView;
    }
    public boolean getPostFlag(){
        return feedButtonFlag ;
    }



}
