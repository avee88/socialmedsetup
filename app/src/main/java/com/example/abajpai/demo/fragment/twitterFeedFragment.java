package com.example.abajpai.demo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.abajpai.demo.R;
import com.example.abajpai.demo.model.FacebookFeedModel;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import java.util.ArrayList;


public class twitterFeedFragment extends Fragment {
    static Context mcontext;
    private ArrayList<FacebookFeedModel> twitterFeedslist;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ListView rv = (ListView) inflater.inflate(R.layout.feed_list, container, false);
        mcontext = getActivity();
        setupRecyclerView(rv);
        return rv;
    }

    private void setupRecyclerView(ListView recyclerView) {
        // recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        final UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName("AvanishBahpai")
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(getActivity())
                .setTimeline(userTimeline)
                .build();

        recyclerView.setAdapter(adapter);
    }


}
