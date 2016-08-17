package com.example.abajpai.demo.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.abajpai.demo.R;
import com.example.abajpai.demo.adapter.EventListAdapter;
import com.example.abajpai.demo.model.EventModel;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ABajpai on 6/26/2016.
 */
public class EventFragment extends Fragment implements SearchView.OnQueryTextListener {
    private Context context;
    private View view;
    CoordinatorLayout rootLayout;
    public List<EventModel> adDataList;
    EventListAdapter adapter;
    final TwitterAuthClient mTwitterAuthClient= new TwitterAuthClient();
    public EventFragment(ArrayList<EventModel> adDataList) {
        this.adDataList = adDataList;
    }

    public EventFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_eventlist, null);
        context = getActivity();
        rootLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
        RecyclerView listView = (RecyclerView) view.findViewById(R.id.adlist_list_view);
        listView.setLayoutManager(new LinearLayoutManager(context));

        if (adDataList != null) {
            adapter = new EventListAdapter(context, adDataList);
            listView.setAdapter(adapter);

        }
        return view;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        final List<EventModel> adsFiltered = filter(adDataList, query);
        adapter.animateTo(adsFiltered);
        RecyclerView adsRecView = (RecyclerView) view.findViewById(R.id.adlist_list_view);
        adsRecView.scrollToPosition(0);
        return true;
    }

    private List<EventModel> filter(List<EventModel> adDataList, String query) {
        query = query.toLowerCase();

        final List<EventModel> filteredModelList = new ArrayList<>();
        for (EventModel adData : adDataList) {
            final String text = adData.getEventName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(adData);
            }
        }
        return filteredModelList;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) context.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.menu_item_share) {
            return false;
        }

        return true;
    }

}
