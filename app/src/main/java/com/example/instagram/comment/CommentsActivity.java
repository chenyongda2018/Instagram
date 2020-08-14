package com.example.instagram.comment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.instagram.R;
import com.example.instagram.Utils;

public class CommentsActivity extends AppCompatActivity implements SendCommentButton.OnSendClickListener {

    public static final String ARG_DRAWING_START_LOCATION = "arg_drawing_start_location";
    RecyclerView mCommentsRv;
    LinearLayout mCommentEditLayout;
    LinearLayout mContentRootLayout;
    Toolbar mToolbar;
    private CommentAdapter mCommentAdapter;
    private SendCommentButton mSendCommentBtn;
    private EditText mEdComment;

    private int mStartDrawingHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        initViews();
        mStartDrawingHeight = getIntent().getIntExtra(ARG_DRAWING_START_LOCATION, 0);
        mContentRootLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mContentRootLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                startLayoutAnimation();
                return false;
            }
        });

        setUpComment();
    }

    private void initViews() {
        mCommentsRv = findViewById(R.id.rvComments);
        mCommentEditLayout = findViewById(R.id.llAddComment);
        mSendCommentBtn = findViewById(R.id.btnSendComment);
        mSendCommentBtn.setOnSendClickListener(this);
        mContentRootLayout = findViewById(R.id.contentRoot);
        mToolbar = findViewById(R.id.toolbar);
        mEdComment = findViewById(R.id.ed_comment);
    }

    private void setUpComment() {
        mCommentAdapter = new CommentAdapter(this);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        mCommentsRv.setLayoutManager(linearLayout);
        mCommentsRv.setAdapter(mCommentAdapter);
        mCommentsRv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    mCommentAdapter.setAnimationsLocked(true);
                }
            }
        });
    }

    private void startLayoutAnimation() {
        mContentRootLayout.setScaleY(0.1f);
        mContentRootLayout.setPivotY(mStartDrawingHeight);

        mCommentEditLayout.setTranslationY(100);

        mContentRootLayout.animate()
                .scaleY(1)
                .setDuration(200)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        startContentAnimation();
                    }
                })
                .start();
    }

    @Override
    public void onBackPressed() {
        mContentRootLayout.animate()
                .translationY(Utils.getScreenHeight(this))
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        CommentsActivity.super.onBackPressed();
                        overridePendingTransition(0, 0);
                    }
                })
                .start();
    }

    private void startContentAnimation() {
        mCommentAdapter.updateData();
        mCommentEditLayout.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(200)
                .start();
    }


    @Override
    public void onSendClickListener(View v) {
        if (validateComment()) {
            mCommentAdapter.addItem();
            mCommentAdapter.setAnimationsLocked(false);
            mCommentAdapter.setDelayEnterAnimation(false);
            mCommentsRv.smoothScrollBy(0, mCommentsRv.getChildAt(0).getHeight() * mCommentAdapter.getItemCount());
            mEdComment.setText(null);

            mSendCommentBtn.setCurrentState(SendCommentButton.STATE_DONE);
        }
    }

    private boolean validateComment() {
        if (TextUtils.isEmpty(mEdComment.getText().toString())) {
            mSendCommentBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake_error));
            return false;
        }
        return true;
    }
}