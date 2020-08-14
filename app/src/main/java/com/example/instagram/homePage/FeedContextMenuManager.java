package com.example.instagram.homePage;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.Utils;
import com.example.instagram.widget.FeedContextMenus;

/**
 * Created by cyd on 2020/8/12.
 */
public class FeedContextMenuManager extends RecyclerView.OnScrollListener implements View.OnAttachStateChangeListener {

    private static FeedContextMenuManager instance = new FeedContextMenuManager();

    private FeedContextMenus mContextMenus;

    private boolean mIsMenuShowing = false;
    private boolean mIsMenuDismissing = false;
    private OvershootInterpolator overshootInterpolator;
    private AccelerateInterpolator accelerateInterpolator;


    public static FeedContextMenuManager getInstance() {
        return instance;
    }

    @Override
    public void onViewAttachedToWindow(View v) {
        overshootInterpolator = new OvershootInterpolator();
        accelerateInterpolator = new AccelerateInterpolator();

    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        mContextMenus = null;
        overshootInterpolator = null;
        accelerateInterpolator = null;
    }

    //监听recyclerView的滚动
    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        if (mContextMenus != null) {
            hideContextMenu();
            mContextMenus.setTranslationY(mContextMenus.getTranslationY() - dy);
        }
    }

    public void toggleMenuFromView(final View openingView, int feedItem, FeedContextMenus.OnFeedContextMenuItemClickListener listener) {
        if (mContextMenus == null) {
            showMenuFromView(openingView, feedItem, listener);
        } else {
            hideContextMenu();
        }
    }

    public void hideContextMenu() {
        if (!mIsMenuDismissing) {
            mIsMenuDismissing = true;
            performHidingAnimation();
        }
    }

    private void showMenuFromView(final View openingView, int feedItem, FeedContextMenus.OnFeedContextMenuItemClickListener listener) {
        if (!mIsMenuShowing) {
            //初始化contextMenu
            mIsMenuShowing = true;
            mContextMenus = new FeedContextMenus(openingView.getContext());
            mContextMenus.setFeedItem(feedItem);
            mContextMenus.setMenuItemClickListener(listener);
            mContextMenus.addOnAttachStateChangeListener(this);

            ((ViewGroup) openingView.getRootView().findViewById(android.R.id.content)).addView(mContextMenus);

            mContextMenus.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mContextMenus.getViewTreeObserver().removeOnPreDrawListener(this);
                    setUpContextMenuInitialPosition(openingView);
                    performShowAnimation();
                    return false;
                }
            });
        }
    }

    private void setUpContextMenuInitialPosition(View openingView) {
        final int[] openingViewLocation = new int[2];
        int additionalBottomMargin = Utils.dp2px(openingView.getContext(), 16);
        openingView.getLocationOnScreen(openingViewLocation);
        mContextMenus.setTranslationX(openingViewLocation[0] - mContextMenus.getWidth() / 3);
        mContextMenus.setTranslationY(openingViewLocation[1] - mContextMenus.getHeight() - additionalBottomMargin);
    }

    private void performShowAnimation() {
        performAnimation(
                mContextMenus.getWidth() / 2,
                mContextMenus.getHeight(),
                150, 0.1f, 0.1f, 1.0f, 1.0f,
                overshootInterpolator,
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mIsMenuShowing = false;
                    }
                }, 0);
    }

    private void performHidingAnimation() {
        performAnimation(
                mContextMenus.getWidth() / 2,
                mContextMenus.getHeight(),
                150, 1.0f, 1.0f, 0.1f, 0.1f,
                accelerateInterpolator,
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (mContextMenus != null) {
                            mIsMenuDismissing = false;
                            mContextMenus.dismiss();
                        }
                        mIsMenuDismissing = false;
                    }
                }, 100);
    }

    private void performAnimation(int pivotX, int pivotY, int duration, float scaleStartX, float scaleStartY, float scaleEndX, float scaleEndY, Interpolator interpolator, AnimatorListenerAdapter listener, int startDelay) {
        //设置缩放中心点
        mContextMenus.setPivotX(pivotX);
        mContextMenus.setPivotY(pivotY);
        //设置缩放倍数
        mContextMenus.setScaleX(scaleStartX);
        mContextMenus.setScaleY(scaleStartY);
        //启动动画
        mContextMenus.animate()
                .setDuration(duration)
                .setStartDelay(startDelay)
                .scaleX(scaleEndX)
                .scaleY(scaleEndY)
                .setInterpolator(interpolator)
                .setListener(listener);
    }
}
