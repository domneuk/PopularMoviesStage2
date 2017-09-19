package com.example.android.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private static final String TAG = TrailerAdapter.class.getSimpleName();

    private final Context mContext;
    private List<Trailer> mTrailerList;

    public TrailerAdapter(Activity context, List<Trailer> trailers) {
        this.mContext = context;
        this.mTrailerList = trailers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.movie_detail_trailer_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Trailer trailer = mTrailerList.get(position);

        holder.trailerImage.setContentDescription(mContext.getString(R.string.trailer_contentDescription, trailer.getName()));
        Picasso.with(mContext)
                .load(trailer.getImageUrl())
                .into(holder.trailerImage);

        holder.trailer = trailer;
    }

    @Override
    public int getItemCount() {
        return mTrailerList == null ? 0 : mTrailerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView trailerImage;
        private Trailer trailer;

        public ViewHolder(View itemView) {
            super(itemView);
            trailerImage = itemView.findViewById(R.id.iv_trailer);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                Intent trailerIntent = new Intent(Intent.ACTION_VIEW);
                    trailerIntent.setData(Uri.parse(trailer.getVideoUrl()));

                // Verify it resolves
                PackageManager packageManager = mContext.getPackageManager();
                List<ResolveInfo> activities = packageManager.queryIntentActivities(trailerIntent, 0);
                boolean isIntentSafe = activities.size() > 0;

                // Start an activity if it's safe
                if (isIntentSafe) {
                    mContext.startActivity(trailerIntent);
                } else {
                    Toast.makeText(mContext, R.string.no_apps_trailer, Toast.LENGTH_SHORT).show();
                }
                }
            });
        }
    }

    public void setTrailers(List<Trailer> data) {
        mTrailerList = data;
        notifyDataSetChanged();
    }
}
