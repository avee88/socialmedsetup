package com.example.abajpai.demo;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.abajpai.demo.fragment.FriendsListFragment;
import com.example.abajpai.demo.fragment.PhoneListFragment;
import com.example.abajpai.demo.model.FriendsModel;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abajpai on 7/1/2016.
 */
public class FriendsActivity extends Fragment {
    private View rootView;
    Context context;
    ArrayList<FriendsModel> friends = new ArrayList<FriendsModel>();
    JSONArray fList = new JSONArray();
    int length;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.friends_list_tab, container, false);
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        String jsondata = getArguments().getString("jsondata");
        JSONArray friendslist;


        try {
            friendslist = new JSONArray(jsondata);
            for (int l = 0; l < friendslist.length(); l++) {
                FriendsModel friendsModel = new FriendsModel();
                friendsModel.setName(friendslist.getJSONObject(l).getString("name"));
                friendsModel.setId((friendslist.getJSONObject(l).getString("id")));
                friends.add(friendsModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }
        tabLayout.setupWithViewPager(viewPager);

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new PhoneListFragment(), "Phone");
        adapter.addFragment(new FriendsListFragment(friends), "Facebook");
        // adapter.addFragment(new PhoneListFragment(), "Gmail");
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

    private ArrayList<FriendsModel> getFacebookFriendslist() {
        final ArrayList<FriendsModel> list = new ArrayList<>();
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        if (response != null) {
                            try {
                                fList = (JSONArray) response.getJSONObject().get("data");
                                length = fList.length();
                                FriendsModel friendsModel = new FriendsModel();
                                for (int i = 0; i < fList.length(); i++) {
                                    try {
                                        JSONObject objectInArray = fList.getJSONObject(i);
                                        friendsModel.setName(objectInArray.getString("name"));
                                        list.add(friendsModel);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        ).executeAsync();


        return list;
    }
}
