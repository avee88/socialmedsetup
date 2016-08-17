package com.example.abajpai.demo.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.example.abajpai.demo.R;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by abajpai on 6/28/2016.
 */
public class LoadImage extends AsyncTask<String, String, Bitmap> {
    ProgressDialog pDialog;
    Bitmap bitmap;
    ImageView imageView;
    Context context;
    String url;

    public LoadImage(ImageView imageView, Context context, String url) {
        this.url = url;
        this.imageView = imageView;
        this.context = context;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading Image ....");
        pDialog.show();

    }

    protected Bitmap doInBackground(String... args) {
        try {
            bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap image) {

        if (image != null) {
            imageView.setImageBitmap(image);
            pDialog.dismiss();

        } else {
            imageView.setImageResource(R.drawable.icon_person);
            //  pDialog.dismiss();
            //  Toast.makeText(context, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();

        }
    }
}
