package com.example.abajpai.demo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.abajpai.demo.EventDetailActivity;
import com.example.abajpai.demo.R;
import com.example.abajpai.demo.model.EventModel;
import com.example.abajpai.demo.util.LoadImage;

import java.util.ArrayList;
import java.util.List;


public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {
    private Context context;
    private List<EventModel> adData;
    private LayoutInflater layoutInflater;
    public ImageView imageView;
    public TextView eventName;
    public CardView cardView;
    private List<EventModel> adsSearchCopy;

    public EventListAdapter(Context context, List<EventModel> adData) {
        this.context = context;
        this.adData = adData;
        adsSearchCopy = new ArrayList<EventModel>(adData);
        this.layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.event_item, null);
        LinearLayout f = (LinearLayout) view.findViewById(R.id.sample);
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EventDetailActivity.class);
                intent.putExtra("event_name", "Clean Ganga");
            }
        });
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (adData.get(position).getEventImg() != null) {
            new LoadImage(imageView, context, adData.get(position).getEventImg()).execute();
            //LoadImage loadImage = new LoadImage(imageView,context);

        }
        if (adData.get(position).getEventName() != null) {
            eventName.setText(adData.get(position).getEventName());
        }

    }

    @Override
    public int getItemCount() {
        return adData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.event_image_view);
            eventName = (TextView) view.findViewById(R.id.event_name);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EventDetailActivity.class);
                    intent.putExtra("event_name", "Clean Ganga");
                    context.startActivity(intent);
                }
            });

        }

    }

    public void animateTo(List<EventModel> adDataList) {
        applyAndAnimateRemovals(adDataList);
        applyAndAnimateAdditions(adDataList);
        applyAndAnimateMovedItems(adDataList);
    }

    private void applyAndAnimateRemovals(List<EventModel> newModels) {
        for (int i = adsSearchCopy.size() - 1; i >= 0; i--) {
            final EventModel ad = adsSearchCopy.get(i);
            if (!newModels.contains(ad)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<EventModel> newAds) {
        for (int i = 0, count = newAds.size(); i < count; i++) {
            final EventModel ad = newAds.get(i);
            if (!adsSearchCopy.contains(ad)) {
                addItem(i, ad);
            }
        }
    }

    public EventModel removeItem(int position) {
        final EventModel adData = adsSearchCopy.remove(position);
        notifyItemRemoved(position);
        return adData;
    }

    public void addItem(int position, EventModel model) {
        adsSearchCopy.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final EventModel model = adsSearchCopy.remove(fromPosition);
        adsSearchCopy.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    private void applyAndAnimateMovedItems(List<EventModel> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final EventModel ad = newModels.get(toPosition);
            final int fromPosition = adsSearchCopy.indexOf(ad);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }
}


