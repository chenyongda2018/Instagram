package com.example.instagram.comment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ViewAnimator;

import com.example.instagram.R;

/**
 * 发送评论按钮动画
 * Created by cyd on 2020/8/6.
 */
public class SendCommentButton extends ViewAnimator implements View.OnClickListener {
    public static final int STATE_SEND = 0;
    public static final int STATE_DONE = 1;

    public static final long RESET_SEND_STATE = 2000;

    private int mCurrentState = 0;

    private OnSendClickListener onSendClickListener;

    private Runnable revertStateRunnable = new Runnable() {
        @Override
        public void run() {
            setCurrentState(STATE_SEND);
        }
    };

    public SendCommentButton(Context context) {
        super(context);
        init(context);
    }

    public SendCommentButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_send_comment_button, this, true);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setCurrentState(STATE_SEND);
        super.setOnClickListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        removeCallbacks(revertStateRunnable);
        super.onDetachedFromWindow();
    }

    @Override
    public void onClick(View v) {
        if(onSendClickListener != null) {
            onSendClickListener.onSendClickListener(v);
        }
    }

    public void setCurrentState(int state) {
        if(mCurrentState == state) return;
        mCurrentState = state;

        if(state == STATE_DONE) {
            setEnabled(false);
            postDelayed(revertStateRunnable,RESET_SEND_STATE);
            setInAnimation(getContext(),R.anim.slide_in_done);
            setOutAnimation(getContext(),R.anim.slide_out_send);
        } else if(state == STATE_SEND) {
            setEnabled(true);
            setInAnimation(getContext(),R.anim.slide_in_send);
            setOutAnimation(getContext(),R.anim.slide_out_done);
        }
        showNext();
    }

    public void setOnSendClickListener(OnSendClickListener onSendClickListener) {
        this.onSendClickListener = onSendClickListener;
    }

    interface OnSendClickListener{
        void onSendClickListener(View v);
    }




}
