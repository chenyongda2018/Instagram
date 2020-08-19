package com.example.instagram.homePage;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.R;
import com.example.instagram.Utils;
import com.example.instagram.comment.CommentsActivity;
import com.example.instagram.widget.FeedContextMenus;

public class MainActivity extends AppCompatActivity implements OnFeedItemClickListener,
        FeedContextMenus.OnFeedContextMenuItemClickListener {


    MenuItem mInboxMenuItem;
    Toolbar mToolbar;
    RecyclerView mRvFeed;
    ImageButton mCreateBtn;
    ImageView mLogoIv;
    private FeedAdapter mFeedAdapter;
    private boolean pendingIntroAnimation;
    private static final int ANIM_DURATION_TOOLBAR = 300;
    private static final int ANIM_DURATION_FAB = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        if (savedInstanceState == null) {
            pendingIntroAnimation = true;
        }

        setUpToolBar();
        setUpFeed();
    }

    private void initView() {
        mToolbar = findViewById(R.id.toolbar);
        mRvFeed = findViewById(R.id.rvFeed);
        mCreateBtn = findViewById(R.id.btnCreate);
        mLogoIv = findViewById(R.id.ivLogo);
    }

    private void setUpToolBar() {
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_menu_white);

    }

    private void setUpFeed() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };
        mRvFeed.setLayoutManager(layoutManager);
        mFeedAdapter = new FeedAdapter(this);
        mRvFeed.setAdapter(mFeedAdapter);
        mRvFeed.addOnScrollListener(FeedContextMenuManager.getInstance());
        mFeedAdapter.setOnFeedItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        mInboxMenuItem = menu.findItem(R.id.action_inbox);
        mInboxMenuItem.setActionView(R.layout.menu_item_view);
        if (pendingIntroAnimation) {
            pendingIntroAnimation = false;
            startIntroAnimation();
        }
        return true;
    }

    private void startIntroAnimation() {
        //hide view when open activity
        mCreateBtn.setTranslationY(2 * getResources().getDimensionPixelOffset(R.dimen.btn_fab_size));

        int actionBarSize = Utils.dp2px(this, 56);
        mToolbar.setTranslationY(-actionBarSize);
        mLogoIv.setTranslationY(-actionBarSize);
        mInboxMenuItem.getActionView().setTranslationY(-actionBarSize);

        mToolbar.animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(300);
        mLogoIv.animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(400);
        mInboxMenuItem.getActionView().animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        startContentAnimation();
                    }
                })
                .start();


    }

    private void startContentAnimation() {
        mCreateBtn.animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_FAB)
                .setStartDelay(300)
                .setInterpolator(new OvershootInterpolator(0.1f))
                .start();
        mFeedAdapter.updateData();
    }


    @Override
    public void onFeedCommentClick(View v, int position) {
        final Intent intent = new Intent(this, CommentsActivity.class);
        int[] startAnimationLocation = new int[2];
        v.getLocationOnScreen(startAnimationLocation);
        intent.putExtra(CommentsActivity.ARG_DRAWING_START_LOCATION, startAnimationLocation[1]);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    public void onFeedMoreClick(View v, int position) {
        FeedContextMenuManager.getInstance().toggleMenuFromView(v, position, this);
    }

    @Override
    public void onReportClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();
    }

    @Override
    public void onSharePhotoClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();

    }

    @Override
    public void onCopyUrlClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();

    }

    @Override
    public void onCancelClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();

    }
}
