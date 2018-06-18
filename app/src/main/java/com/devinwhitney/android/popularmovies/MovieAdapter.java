package com.devinwhitney.android.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.devinwhitney.android.popularmovies.model.MovieInformation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by devin on 5/8/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private ArrayList<MovieInformation> mMovieInformation = new ArrayList<>();
    private final MovieAdapterOnClickHandler mClickHandler;
    private MovieAdapterScrollListener mScrollListener;


    public MovieAdapter(MovieAdapterOnClickHandler clickHandler, MovieAdapterScrollListener scrollListener) {
        mClickHandler = clickHandler;
        mScrollListener = scrollListener;
    }
    public interface MovieAdapterOnClickHandler {
        void onClick(MovieInformation movieInformation);
    }

    public interface MovieAdapterScrollListener {
        void loadMore(int scrollItem, int arraySize);
    }


    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layout = R.layout.movie_poster_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layout, parent, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        //System.out.println(String.valueOf(position));
        Picasso.get().load("http://image.tmdb.org/t/p/w185" +mMovieInformation.get(position).getImage()).into(holder.mMoviePoster);
        holder.loadMore(position, mMovieInformation.size());
    }


    @Override
    public int getItemCount() {
        if (null == mMovieInformation) return 0;
        return mMovieInformation.size();
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, MovieAdapterScrollListener {
        public ImageView mMoviePoster;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mMoviePoster = itemView.findViewById(R.id.movie_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int x = getAdapterPosition();
            mClickHandler.onClick(mMovieInformation.get(x));

        }

        @Override
        public void loadMore(int scrollItem, int arraySize) {
            mScrollListener.loadMore(scrollItem, arraySize);
        }
    }

    public void setMovieInformation(ArrayList<MovieInformation> movieInformation) {
        if (movieInformation != null) {
            for (MovieInformation movie : movieInformation){
                mMovieInformation.add(movie);
            }
            notifyDataSetChanged();
        }
        else{
            mMovieInformation.clear();
        }
    }


}
