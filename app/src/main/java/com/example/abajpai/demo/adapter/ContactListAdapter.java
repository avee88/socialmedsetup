package com.example.abajpai.demo.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abajpai.demo.R;
import com.example.abajpai.demo.model.FriendsModel;

import java.util.ArrayList;

/**
 * Created by ABajpai on 7/4/2016.
 */

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.PersonViewHolder> {
    ArrayList<FriendsModel> contacts;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    public ContactListAdapter(ArrayList<FriendsModel> contacts) {
        this.contacts = contacts;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_list_item, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        holder.personName.setText(contacts.get(position).getName());
        String thumbnailUri = contacts.get(position).getImage();
        if (thumbnailUri != null) {
            holder.personPhoto.setImageURI(Uri.parse(thumbnailUri));
        } else {
            holder.personPhoto.setImageResource(R.drawable.placeholder);
        }

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        TextView personName;
        ImageView personPhoto;


        PersonViewHolder(View itemView) {
            super(itemView);
            personName = (TextView) itemView.findViewById(R.id.user_name);
            // personAge = (TextView)itemView.findViewById(R.id.person_age);
            personPhoto = (ImageView) itemView.findViewById(R.id.avatar);
        }
    }

}