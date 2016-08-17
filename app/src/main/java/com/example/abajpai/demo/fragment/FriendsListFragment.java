package com.example.abajpai.demo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abajpai.demo.R;
import com.example.abajpai.demo.model.FriendsModel;
import com.example.abajpai.demo.util.LoadImage;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class FriendsListFragment extends Fragment {
    static Context mcontext;
    private ArrayList<FriendsModel> facebookFriendslist;

    public FriendsListFragment(ArrayList<FriendsModel> facebookFriendslist) {
        this.facebookFriendslist = facebookFriendslist;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.friends_list, container, false);
        mcontext = getActivity();
        setupRecyclerView(rv);
        return rv;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(), facebookFriendslist));
    }


    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private ArrayList<FriendsModel> mValues;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public FriendsModel mBoundString;

            public View mView;
            public ImageView mImageView;
            public TextView mTextView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                this.mImageView = (ImageView) view.findViewById(R.id.avatar);
                this.mTextView = (TextView) view.findViewById(R.id.user_name);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTextView.getText();
            }
        }

        public FriendsModel getValueAt(int position) {
            return mValues.get(position);
        }

        public SimpleStringRecyclerViewAdapter(Context context, ArrayList<FriendsModel> items) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_list_item, parent, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            URL img_value = null;
            holder.mBoundString = mValues.get(position);
            holder.mTextView.setText((CharSequence) mValues.get(position).getName());
            try {
                img_value = new URL("https://graph.facebook.com/" + mValues.get(position).getId() + "/picture");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            new LoadImage(holder.mImageView, mcontext, img_value.toString()).execute();


        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }


    }
}
