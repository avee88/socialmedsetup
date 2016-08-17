package com.example.abajpai.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.abajpai.demo.fragment.FacebookFeedFragment;
import com.example.abajpai.demo.fragment.twitterFeedFragment;
import com.example.abajpai.demo.model.FacebookFeedModel;
import com.example.abajpai.demo.util.MySingleton;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FeedActivity extends Fragment {
    JSONArray feedlist = null;
    private View rootView;
    ArrayList<FacebookFeedModel> feeds = new ArrayList<FacebookFeedModel>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.feeds_list_tab, null);
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PostActivity.class);
                startActivityForResult (intent, 5);
            }
        });
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.feed_viewpager);
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.feed_tabs);
        String feed = getArguments().getString("feed");
        try {
            if (feed != null && feed != "") {
                feedlist = new JSONArray(feed);
            }

            for (int l = 0; l < feedlist.length(); l++) {
                final FacebookFeedModel feedModel = new FacebookFeedModel();

                feedModel.setStatus(feedlist.getJSONObject(l).get("message").toString());
                feedModel.setTimeStamp(feedlist.getJSONObject(l).get("created_time").toString());
                Object from = feedlist.getJSONObject(l).get("from");
                Object comments =feedlist.getJSONObject(l).get("comments");
                Object likes =   feedlist.getJSONObject(l).get("reactions");
                Object totalLikes = ((JSONObject)((JSONObject) likes).get("summary")).get("total_count");
                Object totaComments = ((JSONObject)((JSONObject) comments).get("summary")).get("total_count");
                feedModel.setLikes(totalLikes.toString());
                feedModel.setComments(totaComments.toString());
                // Object shares = feedlist.getJSONObject(l).get("shares");
               // Object totalShares = ((JSONObject) shares).get("total_count");
                // feedModel.setShare(totalShares.toString());
                feedModel.setUrl(feedlist.getJSONObject(l).get("id").toString());
                feedModel.setName(String.valueOf(((JSONObject) from).get("name")));
                String id = String.valueOf(((JSONObject) from).get("id"));
                String feedType = String.valueOf(feedlist.getJSONObject(l).get("type"));
                feedModel.setId(id);
                if (!feedType.equalsIgnoreCase("status")) {
                    feedModel.setImage(String.valueOf(feedlist.getJSONObject(l).get("picture")));
                }

                feeds.add(feedModel);
            }

            if (viewPager != null) {
                setupViewPager(viewPager);
            }
            tabLayout.setupWithViewPager(viewPager);
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        Button show = (Button) getActivity().findViewById(R.id.feed);
//        show.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        return rootView;
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new twitterFeedFragment(), "Twitter");
        adapter.addFragment(new FacebookFeedFragment(feeds), "Facebook");

        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
