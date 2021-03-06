package com.example.instagram.homePage;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextSwitcher;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.R;
import com.example.instagram.Utils;
import com.example.instagram.widget.SquareImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> implements View.OnClickListener {

    private static final int ANIMATED_ITEM_COUNT = 2;

    private int mItemCount = 0;

    private int lastAnimatedPos = -1;

    private Context mContext;

    private OnFeedItemClickListener onFeedItemClickListener;

    private Map<Integer, Integer> mLikeCountSet = new HashMap<>();


    private List<Integer> mLikePosition = new ArrayList<>();

    private Random random;

    private Map<Integer, AnimatorSet> mLikeAnimationMap = new HashMap<>();

    private AccelerateInterpolator accelerateInterpolator = new AccelerateInterpolator();

    private OvershootInterpolator overshootInterpolator = new OvershootInterpolator();

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

        holder.ibLike.setOnClickListener(this);
        holder.ibLike.setTag(holder);

        if (position % 2 == 0) {
            hd.ivFeedCenter.setImageResource(R.drawable.img_feed_center_1);
            hd.ivFeedBottom.setImageResource(R.drawable.img_feed_bottom_1);
        } else {
            hd.ivFeedCenter.setImageResource(R.drawable.img_feed_center_2);
            hd.ivFeedBottom.setImageResource(R.drawable.img_feed_bottom_2);
        }

        updateLikeBtnState(holder, false, position);
        updateLikeCount(holder, position, false);


        if (mLikeAnimationMap.containsKey(position)) {
            mLikeAnimationMap.get(position).cancel();
        }
        resetLikeBtnAnimated(position);
    }

    private void updateLikeBtnState(FeedViewHolder holder, boolean animated, int position) {
        if (animated) {
            if (!mLikeAnimationMap.containsKey(position)) {

                AnimatorSet animationSet = new AnimatorSet();
                mLikeAnimationMap.put(position, animationSet);

                ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(holder.ibLike, "rotation", 0f, 360f);
                rotationAnim.setDuration(600);
                rotationAnim.setInterpolator(accelerateInterpolator);

                ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(holder.ibLike, "scaleX", 0.2f, 1f);
                scaleXAnim.setDuration(300);
                scaleXAnim.setInterpolator(overshootInterpolator);

                ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(holder.ibLike, "scaleY", 0.2f, 1f);
                scaleYAnim.setDuration(300);
                scaleYAnim.setInterpolator(overshootInterpolator);

                scaleYAnim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        holder.ibLike.setImageResource(R.drawable.ic_heart_red);
                    }
                });

                animationSet.play(rotationAnim);
                animationSet.play(scaleXAnim).with(scaleYAnim).after(rotationAnim);

                animationSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        resetLikeBtnAnimated(position);
                    }
                });
                animationSet.start();


            }
        } else {
            if (mLikePosition.contains(position)) {
                holder.ibLike.setImageResource(R.drawable.ic_heart_red);
            } else {
                holder.ibLike.setImageResource(R.drawable.ic_heart_outline_grey);
            }
        }
    }

    private void resetLikeBtnAnimated(int position) {
        mLikeAnimationMap.remove(position);
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


    private void updateLikeCount(FeedAdapter.FeedViewHolder holder, int position, boolean animated) {
        int curLikeCount = 0;
        if (animated) {
            curLikeCount = mLikeCountSet.get(position) + 1;
            String likeCountStr = mContext.getResources().getQuantityString(R.plurals.likes_count, curLikeCount, curLikeCount);
            holder.tsLikeCount.setText(likeCountStr);
        } else {
            curLikeCount = mLikeCountSet.get(position);
            String likeCountStr = mContext.getResources().getQuantityString(R.plurals.likes_count, curLikeCount, curLikeCount);
            holder.tsLikeCount.setCurrentText(likeCountStr);
        }

        mLikeCountSet.put(position, curLikeCount);
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
                    onFeedItemClickListener.onFeedCommentClick(v, (Integer) (v.getTag()));
                }
                break;
            case R.id.btnMore:
                if (onFeedItemClickListener != null) {
                    onFeedItemClickListener.onFeedMoreClick(v, (Integer) (v.getTag()));
                }
                break;
            case R.id.btnLike:
                FeedViewHolder holder = (FeedViewHolder) (v.getTag());
                if (!mLikePosition.contains(holder.getPosition())) {
                    mLikePosition.add(holder.getPosition());
                    updateLikeBtnState(holder, true, holder.getPosition());
                    updateLikeCount(holder, holder.getPosition(), true);
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
