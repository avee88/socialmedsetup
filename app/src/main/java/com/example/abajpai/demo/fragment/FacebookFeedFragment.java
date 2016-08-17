package com.example.abajpai.demo.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.abajpai.demo.R;
import com.example.abajpai.demo.model.FacebookFeedModel;
import com.example.abajpai.demo.util.BitmapImage;
import com.facebook.share.widget.LikeView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


public class FacebookFeedFragment extends Fragment {
    static Context mcontext;
    private ArrayList<FacebookFeedModel> facebookFeedslist;

    public FacebookFeedFragment(ArrayList<FacebookFeedModel> facebookFeedslist) {
        this.facebookFeedslist = facebookFeedslist;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.fb_feed_list, container, false);
        mcontext = getActivity();
        setupRecyclerView(rv);
        return rv;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(), facebookFeedslist));
    }


    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private ArrayList<FacebookFeedModel> mValues;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public View mView;
            public TextView status ;
            public  ImageView feedImage ;
            public TextView name ;
            public TextView time ;
            public TextView url ;
            public ImageView dp ;
            public LinearLayout fbLayout ;
            public TextView likes ;
            public TextView comments ;
            public TextView shares ;


            public ViewHolder(View view) {
                super(view);
                mView = view;
                this.fbLayout=(LinearLayout)view.findViewById(R.id.fb_layout);
                this.status = (TextView) view.findViewById(R.id.txtStatusMsg);
                this.feedImage = (ImageView) view.findViewById(R.id.feedImage1);
                this.name = (TextView) view.findViewById(R.id.name);
                this.time = (TextView) view.findViewById(R.id.timestamp);
                this.dp = (ImageView)view.findViewById(R.id.profilePic);
                this.likes = (TextView)view.findViewById(R.id.like);
                this.comments= (TextView)view.findViewById(R.id.comment);
                this.shares= (TextView)view.findViewById(R.id.share);

            }

            @Override
            public String toString() {
                return super.toString() + " '" + status.getText();
            }
        }

        public FacebookFeedModel getValueAt(int position) {
            return mValues.get(position);
        }

        public SimpleStringRecyclerViewAdapter(Context context, ArrayList<FacebookFeedModel> items) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item, parent, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final BitmapImage bitmapImage = new BitmapImage();
            holder.status.setText((CharSequence) mValues.get(position).getStatus());
            holder.name.setText((CharSequence) mValues.get(position).getName());
            holder.likes.setText((CharSequence)mValues.get(position).getLikes());
            holder.comments.setText((CharSequence)mValues.get(position).getComments());
            String feedDate = mValues.get(position).getTimeStamp();
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");

            String finalString = null;
            try {
                Date date = (Date) formatter.parse(feedDate);
                SimpleDateFormat newFormat = new SimpleDateFormat("MM-dd-yyyy");
                finalString = newFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            holder.time.setText(finalString);
            String img = mValues.get(position).getImage();
            if (img == "" || img == null) {
                holder.feedImage.setVisibility(View.GONE);
            } else {
                Picasso.with(mcontext).load(img).into(holder.feedImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap bitmap = ((BitmapDrawable) holder.feedImage.getDrawable()).getBitmap();
                        holder.feedImage.setImageBitmap(bitmap);

                    }

                    @Override
                    public void onError() {
                        Bitmap bm = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.placeholder);
                        holder.feedImage.setImageBitmap(bm);

                    }

                });
            }
            String id = mValues.get(position).getId();
            Picasso.with(mcontext).load("https://graph.facebook.com/" + id + "/picture?type=small").into(holder.dp, new Callback() {
                @Override
                public void onSuccess() {
                    Bitmap bitmap = ((BitmapDrawable) holder.dp.getDrawable()).getBitmap();
                    holder.dp.setImageBitmap(bitmap);

                }

                @Override
                public void onError() {
                    Bitmap bm = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.placeholder);
                    holder.dp.setImageBitmap(bm);

                }

            });

        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }


    }
}
