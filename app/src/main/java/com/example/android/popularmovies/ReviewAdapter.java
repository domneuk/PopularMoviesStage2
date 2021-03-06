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
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private static final String TAG = ReviewAdapter.class.getSimpleName();

    private final Context mContext;
    private List<Review> mReviewList;

    public ReviewAdapter(Activity context, List<Review> reviews) {
        this.mContext = context;
        this.mReviewList = reviews;
    }
    
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.movie_detail_review_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Review review = mReviewList.get(position);

        holder.reviewContent.setText(review.getContent());
        holder.reviewAuthor.setText(review.getAuthor());

        holder.review = review;
    }

    @Override
    public int getItemCount() {
        return mReviewList == null ? 0 : mReviewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView reviewContent;
        private final TextView reviewAuthor;
        private Review review;

        public ViewHolder(View itemView) {
            super(itemView);
            reviewContent = itemView.findViewById(R.id.tv_review_content);
            reviewAuthor = itemView.findViewById(R.id.tv_review_author);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent reviewIntent = new Intent(Intent.ACTION_VIEW);
                    reviewIntent.setData(Uri.parse(review.getUrl()));

                    // Verify it resolves
                    PackageManager packageManager = mContext.getPackageManager();
                    List<ResolveInfo> activities = packageManager.queryIntentActivities(reviewIntent, 0);
                    boolean isIntentSafe = activities.size() > 0;

                    // Start an activity if it's safe
                    if (isIntentSafe) {
                        mContext.startActivity(reviewIntent);
                    } else {
                        Toast.makeText(mContext, R.string.no_apps_review, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void setReviews(List<Review> data) {
        mReviewList = data;
        notifyDataSetChanged();
    }
}
