package com.devinwhitney.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import java.util.ArrayList;

/**
 * Created by devin on 6/15/2018.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {
    private ArrayList<String> mTrailer = new ArrayList<>();
    private final TrailerAdapterOnClickHandler mClickHandler;


    public TrailerAdapter(TrailerAdapterOnClickHandler clickHandler, ArrayList<String> trailers) {
        mTrailer = trailers;
        mClickHandler = clickHandler;
    }
    public interface TrailerAdapterOnClickHandler {
        void onClick(String trailer);
    }


    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layout = R.layout.trailer_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layout, parent, false);
        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder holder, final int position) {
        System.out.println(String.valueOf(position));
        holder.mButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mClickHandler.onClick(mTrailer.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mTrailer == null) return 0;
        return mTrailer.size();
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder  {
        private Button mButton;

        public TrailerAdapterViewHolder(View itemView) {
            super(itemView);
            mButton = itemView.findViewById(R.id.button);
            mButton.setText("Play");
        }

    }

}
