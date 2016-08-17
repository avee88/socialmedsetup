package com.example.abajpai.demo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abajpai.demo.R;
import com.example.abajpai.demo.model.SocialModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ABajpai on 8/8/2016.
 */
public class SocialListAdapter extends BaseAdapter {
    Context context;
    List<SocialModel> socialModelsList;

    final ArrayList status = new ArrayList();

    public SocialListAdapter(Context _context, List<SocialModel> _social) {
        this.context = _context;
        this.socialModelsList = _social;
    }

    public SocialListAdapter() {

    }

    @Override
    public int getCount() {
        return socialModelsList.size();
    }

    @Override
    public Object getItem(int position) {
        return socialModelsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public boolean getStatus() {
        boolean statusFlag = false ;
        int length = socialModelsList.size();
        for(int i=0 ;i< length ;i++){
            if(getFromSP(socialModelsList.get(i).getSocialMediaType()) == true){
                statusFlag = true ;
                break;
            }
        }
        return statusFlag ;
    }

    private boolean getFromSP(String key) {
        SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(context);
        return mSharedPreference.getBoolean(key, false);
    }
    private void saveInSp(String key,boolean value){
        SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        final MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.social_list_item, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }
        mViewHolder.tvDesc.setText(socialModelsList.get(position).getUserName());
        String id = socialModelsList.get(position).getUserDp();
        final String socialMediaType = socialModelsList.get(position).getSocialMediaType().toString();
        mViewHolder.check.setChecked(getFromSP(socialMediaType));
        mViewHolder.check.setTag(socialMediaType);

        mViewHolder.check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                if (cb.isChecked()) {
                    saveInSp(socialMediaType,cb.isChecked());


                } else if (!cb.isChecked()) {
                    saveInSp(socialMediaType,cb.isChecked());

                }


            }
        });
        mViewHolder.ivIcon.setImageResource((Integer) socialModelsList.get(position).getSocialImage());
//        Picasso.with(context).load(id).into(mViewHolder.ivIcon, new Callback() {
//            @Override
//            public void onSuccess() {
//                Bitmap bitmap = ((BitmapDrawable) mViewHolder.ivIcon.getDrawable()).getBitmap();
//                mViewHolder.ivIcon.setImageBitmap(bitmap);
//
//            }
//
//            @Override
//            public void onError() {
//                Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.placeholder);
//                mViewHolder.ivIcon.setImageBitmap(bm);
//
//            }
//
//        });
        return convertView;
    }


    private class MyViewHolder {
        TextView tvDesc;
        ImageView ivIcon;
        CheckBox check;

        public MyViewHolder(View item) {
            ivIcon = (ImageView) item.findViewById(R.id.social_dp);
            tvDesc = (TextView) item.findViewById(R.id.social_user);
            check = (CheckBox) item.findViewById(R.id.checkBoxSocial);

        }
    }

}
