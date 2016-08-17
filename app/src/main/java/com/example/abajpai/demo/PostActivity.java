package com.example.abajpai.demo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.abajpai.demo.fragment.SocialFragment;
import com.example.abajpai.demo.model.SocialModel;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.TwitterSession;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import twitter4j.StatusUpdate;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterResponse;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by ABajpai on 8/5/2016.
 */
public class PostActivity extends AppCompatActivity {
    private MenuItem mRefreshMenuItem;
    ArrayList<SocialModel> socialAcList = new ArrayList<>();
    ImageView camera;
    ImageView time;
    ImageView location;
    ImageView imageView1;
    ImageView dp ;
    boolean Flag = false ;
    SocialFragment socialFragment ;
    EditText status ;



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);
        status = (EditText)findViewById(R.id.post_text);
        SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(this);
        Button select = (Button) findViewById(R.id.social_apps);
         SocialModel socialModel = new SocialModel();
        socialModel.setUserDp("https://graph.facebook.com/" +"100000936943308"+ "/picture?type=small");
        socialModel.setSocialMediaType("1");
        socialModel.setSocialImage(R.drawable.com_facebook_button_icon);
        socialModel.setUserName(mSharedPreference.getString("fbUserId", "Default_Value"));

        SocialModel socialModel2 = new SocialModel();
        socialModel2.setUserDp("https://graph.facebook.com/" +"100000936943308"+ "/picture?type=small");
        socialModel2.setUserName(mSharedPreference.getString("twUserId", "Default_Value"));
        socialModel2.setSocialMediaType("2");
        socialModel2.setSocialImage(R.drawable.tw__ic_logo_default);

        socialAcList.add(socialModel);
        socialAcList.add(socialModel2);

        socialFragment  = new SocialFragment(socialAcList);;
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.FragmentManager fm = getFragmentManager();
                if(socialFragment.isAdded())
                {
                    return; //or return false/true, based on where you are calling from
                }else{
                    socialFragment.show(fm, "sample");
                }

            }
        });

        String value=(mSharedPreference.getString("fbUserId", "Default_Value"));
        //value ="100000936943308" ;
        camera = (ImageView) findViewById(R.id.camera);
        dp = (ImageView)findViewById(R.id.dp);
        Picasso.with(getApplicationContext()).load("https://graph.facebook.com/" + value + "/picture?type=normal").into(dp, new Callback() {
            @Override
            public void onSuccess() {
                Bitmap bitmap = ((BitmapDrawable) dp.getDrawable()).getBitmap();
                dp.setImageBitmap(bitmap);

            }

            @Override
            public void onError() {
                Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.placeholder);
                dp.setImageBitmap(bm);

            }

        });
        time = (ImageView) findViewById(R.id.feed_time);
        location = (ImageView) findViewById(R.id.location);
        imageView1 = (ImageView) findViewById(R.id.pic1);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageActionPopup();
            }
        });


    }

    public void showImageActionPopup() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo !");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
//                        intent.setAction(Intent.ACTION_GET_CONTENT);//
                    //Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }


            }
        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         Flag = socialFragment.getPostFlag();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.post_menu, menu);
        mRefreshMenuItem =  menu.findItem(R.id.feed_post);
        if(Flag == false){
            mRefreshMenuItem.setEnabled(false);
        }else{
            mRefreshMenuItem.setEnabled(true);
        }


        return true;
    }
    public void  postFeed() throws Exception {
     SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(this);
     for(int i=0;i<socialAcList.size();i++){
         String type = socialAcList.get(i).getSocialMediaType() ;
         if(type.equalsIgnoreCase("1")){
           boolean fbCheck = mSharedPreference.getBoolean(type,false);
             if(fbCheck == true){
                 postToFacebook( (byte[]) imageView1.getTag(),status.getText().toString());
             }
         }else if(type.equalsIgnoreCase("2")){
             boolean twCheck = mSharedPreference.getBoolean(type,false);
             if(twCheck == true){
                 postToTwitter((byte[]) imageView1.getTag(),status.getText().toString());

             }
         }

     }
    }
    public void postToTwitter(byte[] img, String text){
        new RetrieveFeedTask(img,text).execute();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.feed_post:
                try {
                    postFeed();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            default:
                break;
        }
        return false ;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            Bitmap bitmap = null;
            byte[] bFile = new byte[0];

            if (requestCode == 1) {
                bitmap = (Bitmap) data.getExtras().get("data");
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, bytes);
            } else if (requestCode == 2) {

                Uri selectedImage = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);


            }
            byte[] byteArray = bytes.toByteArray();
            imageView1.setImageBitmap(bitmap);
            imageView1.setTag(byteArray);
            // postToFacebook(byteArray);
//            int newHeight = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
//            Bitmap scaledBitmapImage = Bitmap.createScaledBitmap(bitmap, 512, newHeight, true);
//            Bitmap img = bitmap;
//            if (img != null) {
//
//
//            }


        }
    }

    public void postToFacebook(byte[] img, String text) {
        String path ;
        if (AccessToken.getCurrentAccessToken() != null) {

            AccessToken at = AccessToken.getCurrentAccessToken();
            Bundle params = new Bundle();
            if(img != null){
                 path = "me/photos";
                params.putByteArray("photo", img);
                params.putString("caption", text);
            }
            else {
                 path = "me/feed";
                params.putString("message", text);
            }

            // params.putString("url", "http://2.bp.blogspot.com/-R-l1kw9_U4s/VcwXp6g1ITI/AAAAAAAAE_A/Wcvlx8pszFM/s640/INDIAN%2BHERITAGE%2BAND%2BCULTURE.jpg");
/* make the API call */
            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    path,
                    params,
                    HttpMethod.POST,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                           Intent intent = new Intent();
                            PostActivity.this.setResult(5, intent);
                            PostActivity.this.finish();
                            Toast.makeText(PostActivity.this, "Successfully image uploaded to Facebook", Toast.LENGTH_SHORT).show();
                        }
                    }
            ).executeAsync();


        } else {

            Toast.makeText(PostActivity.this, "You are not logged into Facebook", Toast.LENGTH_SHORT).show();
        }

    }
    class RetrieveFeedTask extends AsyncTask<String, Void, TwitterResponse> {

       String feed ;
        byte[]byteArray ;
       RetrieveFeedTask(byte[]byteArray ,String feed){
           this.feed = feed ;
           this.byteArray = byteArray ;
       }

        @Override
        protected TwitterResponse doInBackground(String... params) {
            try {
                ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
                configurationBuilder.setOAuthConsumerKey("y87OGLpZk0IlgVnAIlNRUAaNK");
                configurationBuilder.setOAuthConsumerSecret("5EFNRV0SgUKNlGDARkrebfybk3LdAVOV83OWL21dt48l4ChdjM");
                configurationBuilder.setOAuthAccessToken("90324216-v0Nsoib9jezan3DN33MZuBRcQfBZKtOaAgmHbkRFL");
                configurationBuilder.setOAuthAccessTokenSecret("7kZtiYsagEYjMMbXhP0vEotbCGDKS4gWHmlY0X7Ny0Bi2");

                Configuration configuration = configurationBuilder.build();
                twitter4j.Twitter twitter = new TwitterFactory(configuration).getInstance();
                StatusUpdate status = new StatusUpdate(feed);
                if(byteArray != null){
                    ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
                    status.setMedia("newyear", bis);
                }

                twitter.updateStatus(status);

            } catch (TwitterException e) {
                Log.d("TAG", "Pic Upload error" + e.getErrorMessage());


            }
            return null ;
        }

        @Override
        protected void onPostExecute(TwitterResponse result) {
          Toast.makeText(PostActivity.this, "Successfully image uploaded to Twitter", Toast.LENGTH_SHORT).show();
          System.out.print("post succesful");
        }
    }}
