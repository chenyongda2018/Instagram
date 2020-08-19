package com.example.instagram.homePage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextSwitcher;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.R;
import com.example.instagram.Utils;
import com.example.instagram.widget.SquareImageView;

import java.util.Map;
import java.util.Random;


public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> implements View.OnClickListener {

    private static final int ANIMATED_ITEM_COUNT = 2;

    private int mItemCount = 0;

    private int lastAnimatedPos = -1;

    private Context mContext;

    private OnFeedItemClickListener onFeedItemClickListener;

    private Map<Integer,Integer> mLikeCountSet;
    private Random random;

    FeedAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public FeedAdapter.FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_feed, parent, false);
        return new FeedViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull FeedAdapter.FeedViewHolder holder, int position) {
        runEnterAnimation(holder.itemView, position);
        FeedViewHolder hd = (FeedViewHolder) holder;
        holder.ivFeedBottom.setOnClickListener(this);
        holder.ibMore.setOnClickListener(this);
        holder.ibMore.setTag(position);
        holder.ivFeedBottom.setTag(position);
        if (position % 2 == 0) {
            hd.ivFeedCenter.setImageResource(R.drawable.img_feed_center_1);
            hd.ivFeedBottom.setImageResource(R.drawable.img_feed_bottom_1);
        } else {
            hd.ivFeedCenter.setImageResource(R.drawable.img_feed_center_2);
            hd.ivFeedBottom.setImageResource(R.drawable.img_feed_bottom_2);
        }
    }

    private void runEnterAnimation(View view, int position) {
        if (position >= ANIMATED_ITEM_COUNT - 1) {
            return;
        }

        if (position > lastAnimatedPos) {
            lastAnimatedPos = position;
            view.setTranslationY(Utils.getScreenHeight(mContext));
            view.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(700)
                    .start();
        }
    }


    private void updateLikeCount (FeedAdapter.FeedViewHolder holder,int position,boolean animated) {
        int curLikeCount = mLikeCountSet.get(position) + 1;
        String likeCountStr = mContext.getResources().getQuantityString(R.plurals.likes_count, curLikeCount,curLikeCount);

        if(animated) {
            holder.tsLikeCount.setText(likeCountStr);
        } else {
            holder.tsLikeCount.setCurrentText(likeCountStr);
        }

        mLikeCountSet.put(position,curLikeCount);
    }

    public void updateData() {
        mItemCount = 20;
        random = new Random();
        for (int i = 0; i < mItemCount; i++) {
            mLikeCountSet.put(i, random.nextInt(1000));
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mItemCount;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivFeedBottom:
                if (onFeedItemClickListener != null) {
                    onFeedItemClickListener.onFeedCommentClick(v, (Integer)(v.getTag()));
                }
                break;
            case R.id.btnMore:
                if (onFeedItemClickListener != null) {
                    onFeedItemClickListener.onFeedMoreClick(v, (Integer)(v.getTag()));
                }
                break;
        }
    }

    static class FeedViewHolder extends RecyclerView.ViewHolder {
        SquareImageView ivFeedCenter;
        ImageView ivFeedBottom;
        ImageButton ibLike;
        ImageButton ibComments;
        ImageButton ibMore;
        TextSwitcher tsLikeCount;

        private FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFeedCenter = itemView.findViewById(R.id.ivFeedCenter);
            ivFeedBottom = itemView.findViewById(R.id.ivFeedBottom);
            ibLike = itemView.findViewById(R.id.btnLike);
            ibComments = itemView.findViewById(R.id.btnComments);
            ibMore = itemView.findViewById(R.id.btnMore);
            tsLikeCount = itemView.findViewById(R.id.ts_like_count);
        }
    }

    public void setOnFeedItemClickListener(OnFeedItemClickListener onFeedItemClickListener) {
        this.onFeedItemClickListener = onFeedItemClickListener;
    }
}
