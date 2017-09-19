package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private final Context mContext;
    private Cursor mCursor;

    public FavoriteAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.movie_overview_item, parent, false);
        return new FavoriteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteAdapter.ViewHolder holder, int position) {
        holder.movie = new Movie(mCursor, position);

        Picasso.with(mContext)
                .load(NetworkUtils.buildPosterUri(holder.movie.getPosterUri()))
                .into(holder.posterView);
    }

    @Override
    public int getItemCount() {
        return mCursor == null ? 0 : mCursor.getCount();
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

    public void setCursor(Cursor cursor) {
        this.mCursor = cursor;
        if (cursor != null) {
            notifyDataSetChanged();
        }
    }
}
