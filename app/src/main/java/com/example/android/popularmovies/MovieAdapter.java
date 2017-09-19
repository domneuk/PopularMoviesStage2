package com.example.android.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();

    private final Context mContext;
    private final List<Movie> mMovieList;

    public MovieAdapter(Activity context, List<Movie> movies) {
        this.mContext = context;
        this.mMovieList = movies;
    }

    @Override
    public int getItemCount() {
        return mMovieList == null ? 0 : mMovieList.size();
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.movie_overview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.ViewHolder holder, int position) {
        Movie movie = mMovieList.get(position);

        Picasso.with(mContext)
                .load(NetworkUtils.buildPosterUri(movie.getPosterUri()))
                .into(holder.posterView);

        holder.movie = movie;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView posterView;
        private Movie movie;

        public ViewHolder(View itemView) {
            super(itemView);
            posterView = itemView.findViewById(R.id.iv_movie_poster);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MovieDetailActivity.class);
                    intent.putExtra(MainActivity.INTENT_MOVIE, movie);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
