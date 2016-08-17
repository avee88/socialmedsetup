package com.example.abajpai.demo;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.abajpai.demo.dummy.DummyContent;
import com.example.abajpai.demo.fragment.EventFragment;
import com.example.abajpai.demo.model.EventModel;
import com.example.abajpai.demo.util.BitmapImage;
import com.example.abajpai.demo.util.MySingleton;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

import static com.twitter.sdk.android.Twitter.getSessionManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<EventModel> adDataList = new ArrayList<EventModel>();
    JSONObject fbObj = null;
    ImageView profilePictureView;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        context = this;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View hView = navigationView.getHeaderView(0);

        profilePictureView = (ImageView) hView.findViewById(R.id.fbimg);
        TextView user = (TextView) hView.findViewById(R.id.user);
        final BitmapImage bitmapImage = new BitmapImage();
        Intent intent = getIntent();
        FragmentManager fragmentManager = getSupportFragmentManager();
        EventModel eventModel1 = new EventModel();
        eventModel1.setEventName("Clean Ganga");
        eventModel1.setEventImg("http://economictimes.indiatimes.com/photo/37924918.cms");
        adDataList.add(eventModel1);

        EventModel eventModel2 = new EventModel();
        eventModel2.setEventName("Teach One");
        eventModel2.setEventImg("http://www.dbatg.com/img/event_images/each-one-teach-one.jpg");
        adDataList.add(eventModel2);


        EventFragment eveFragment = new EventFragment(adDataList);
        fragmentManager.beginTransaction().add(R.id.event_frame, eveFragment).commit();
        try {
            if (intent.getStringExtra("fbObj") != null) {
                fbObj = new JSONObject(intent.getStringExtra("fbObj"));
                editor.putString("fbUserId",fbObj.getString("name"));
                editor.commit();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if (fbObj != null) {
                user.setText(fbObj.getString("name"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if (fbObj != null) {
                Picasso.with(getApplicationContext()).load("https://graph.facebook.com/" + fbObj.getString("id") + "/picture?type=normal").into(profilePictureView, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap bitmap = ((BitmapDrawable) profilePictureView.getDrawable()).getBitmap();
                        profilePictureView.setImageBitmap(bitmapImage.getCircleBitmap(bitmap));

                    }

                    @Override
                    public void onError() {
                        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.placeholder);
                        profilePictureView.setImageBitmap(bitmapImage.getCircleBitmap(bm));

                    }

                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_settings:

                return true;

            case R.id.action_search:

                // Not implemented here
                return false;
            default:
                break;
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        final FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_camera) {



        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_home) {

            fragmentManager.beginTransaction().replace(R.id.event_frame, new EventFragment(adDataList)).commit();
        } else if (id == R.id.nav_manage) {

        } else {
            if (id == R.id.nav_share) {
                String token = AccessToken.getCurrentAccessToken().getToken();
                String Usrid = null;
                try {
                    Usrid = fbObj.getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String url = "https://graph.facebook.com/v2.7/"+Usrid+"/feed?fields=from,picture,message,type,created_time,link,comments.limit(0).summary(true),reactions.limit(0).summary(true)&limit=10&access_token="+token+"";

                JsonObjectRequest jsObjRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    Object rawName = response.get("data");
                                    Bundle bundle = new Bundle();
                                    bundle.putString("feed", rawName.toString());
                                    FeedActivity feedActivity = new FeedActivity();
                                    feedActivity.setArguments(bundle);
                                    fragmentManager.beginTransaction().replace(R.id.event_frame, feedActivity).commit();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO Auto-generated method stub

                            }
                        });

                MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);

            } else if (id == R.id.nav_send) {
                FriendsActivity friendsActivity = new FriendsActivity();
                Bundle bundle = new Bundle();
                bundle.putString("jsondata", "");
                friendsActivity.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.event_frame, friendsActivity).commit();

//            GraphRequestAsyncTask graphRequestAsyncTask = new GraphRequest(
//
//                    AccessToken.getCurrentAccessToken(),
//                    "/me/friends",
//                    null,
//                    HttpMethod.GET,
//                    new GraphRequest.Callback() {
//                        public void onCompleted(GraphResponse response) {
//                            try {
//                                JSONArray rawName = response.getJSONObject().getJSONArray("data");
//                                Bundle bundle = new Bundle();
//                                bundle.putString("jsondata", rawName.toString());
//                                FriendsActivity friendsActivity = new FriendsActivity();
//                                friendsActivity.setArguments(bundle);
//                                fragmentManager.beginTransaction().replace(R.id.event_frame, friendsActivity).commit();
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//            ).executeAsync();
                //

            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
