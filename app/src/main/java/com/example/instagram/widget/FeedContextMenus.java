package com.example.instagram.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.instagram.R;
import com.example.instagram.Utils;

/**
 * Created by cyd on 2020/8/12.
 */
public class FeedContextMenus extends LinearLayout implements View.OnClickListener {
    public int mContextMenuWidth = 0;

    private TextView mReportBtn, mSharePhotoBtn, mCopyUrlBtn, mCancelBtn;

    private int mFeedItem = 0;

    private OnFeedContextMenuItemClickListener mMenuItemClickListener;

    public FeedContextMenus(Context context) {
        this(context, null);
        init(context);
    }

    private void init(Context context) {
        mContextMenuWidth = Utils.dp2px(context, 240);
        LayoutInflater.from(context).inflate(R.layout.view_context_menu, this, true);
        setBackgroundResource(R.drawable.bg_container_shadow);
        setOrientation(VERTICAL);
        setLayoutParams(new ViewGroup.LayoutParams(mContextMenuWidth, LayoutParams.WRAP_CONTENT));

        mReportBtn = findViewById(R.id.btn_report);
        mSharePhotoBtn = findViewById(R.id.btn_share_photo);
        mCopyUrlBtn = findViewById(R.id.btn_copy_url);
        mCancelBtn = findViewById(R.id.btn_cancel);

        mReportBtn.setOnClickListener(this);
        mSharePhotoBtn.setOnClickListener(this);
        mCopyUrlBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);
    }

    public FeedContextMenus(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FeedContextMenus(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public FeedContextMenus(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }




    public void dismiss() {
        ((ViewGroup) getParent()).removeView(FeedContextMenus.this);
    }

    @Override
    public void onClick(View v) {
        if (mMenuItemClickListener == null) return;
        switch (v.getId()) {
            case R.id.btn_report:
                mMenuItemClickListener.onReportClick(mFeedItem);
                break;
            case R.id.btn_share_photo:
                mMenuItemClickListener.onSharePhotoClick(mFeedItem);
                break;
            case R.id.btn_copy_url:
                mMenuItemClickListener.onCopyUrlClick(mFeedItem);
                break;
            case R.id.btn_cancel:
                mMenuItemClickListener.onCancelClick(mFeedItem);
                break;
        }
    }


    public interface OnFeedContextMenuItemClickListener {
        void onReportClick(int feedItem);

        void onSharePhotoClick(int feedItem);

        void onCopyUrlClick(int feedItem);

        void onCancelClick(int feedItem);
    }

    public void setFeedItem(int mFeedItem) {
        this.mFeedItem = mFeedItem;
    }

    public void setMenuItemClickListener(OnFeedContextMenuItemClickListener listener) {
        this.mMenuItemClickListener = listener;
    }
}
