package com.example.instagram.comment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.R;
import com.example.instagram.transformation.RoundedTransformation;
import com.squareup.picasso.Picasso;

/**
 * Created by cyd on 2020/7/27.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context mContext;

    public CommentAdapter(Context mContext) {
        this.mContext = mContext;
        mAvatarSize = mContext.getResources().getDimensionPixelSize(R.dimen.btn_fab_size);
    }

    private int mItemCount = 0;
    private int mLastAnimatedPosition = -1;

    private int mAvatarSize;
    private boolean mAnimationsLocked = false;

    private boolean mDelayEnterAnimation = true;

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        startEnterAnimation(holder.itemView, position);
        switch (position % 3) {
            case 0:
                holder.tvComment.setText("Lorem ipsum dolor sit amet, consectetur adipisicing elit.");
                break;
            case 1:
                holder.tvComment.setText("Cupcake ipsum dolor sit amet bear claw.");
                break;
            case 2:
                holder.tvComment.setText("Cupcake ipsum dolor sit. Amet gingerbread cupcake. Gummies ice cream dessert icing marzipan apple pie dessert sugar plum.");
                break;
        }
        Picasso.get()
                .load(R.drawable.ic_launcher)
                .centerCrop()
                .resize(mAvatarSize, mAvatarSize)
                .transform(new RoundedTransformation())
                .into(holder.ivUserAvatar);
    }

    private void startEnterAnimation(View view, int position) {
        if (mAnimationsLocked) return;

        if (position > mLastAnimatedPosition) {
            mLastAnimatedPosition = position;
            view.setTranslationY(100);
            view.setAlpha(0);
            view.animate()
                    .translationY(0)
                    .alpha(1)
                    .setStartDelay(mDelayEnterAnimation ? 20 * position : 0)
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mAnimationsLocked = true;
                        }
                    })
                    .start();
        }


    }

    @Override
    public int getItemCount() {
        return mItemCount;
    }


    public void addItem() {
        mItemCount++;
        notifyItemInserted(mItemCount - 1);
    }

    public void updateData() {
        mItemCount = 10;
        notifyDataSetChanged();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView ivUserAvatar;
        TextView tvComment;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUserAvatar = itemView.findViewById(R.id.ivUserAvatar);
            tvComment = itemView.findViewById(R.id.tvComment);
        }
    }

    public boolean isAnimationsLocked() {
        return mAnimationsLocked;
    }

    public void setAnimationsLocked(boolean mAnimationsLocked) {
        this.mAnimationsLocked = mAnimationsLocked;
    }

    public boolean isDelayEnterAnimation() {
        return mDelayEnterAnimation;
    }

    public void setDelayEnterAnimation(boolean mDelayEnterAnimation) {
        this.mDelayEnterAnimation = mDelayEnterAnimation;
    }
}
