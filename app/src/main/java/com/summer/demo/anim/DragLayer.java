package com.summer.demo.anim;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.summer.demo.AcMain;
import com.summer.demo.R;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;

/**
 * 画布，所有的人物都在这个画布中
 *
 * @author xiaqiliang
 */
public class DragLayer extends FrameLayout implements ViewGroup.OnHierarchyChangeListener {
    AcMain mCreator;
    /* 当前处理的拖动项 */
    BaseDragView mDragItem = null;
    /**
     * 当前DragItem的LayoutParams
     */
    LayoutParams mDragItemParams;
    /**
     * DragLayer的LayoutParams
     */
    RelativeLayout.LayoutParams mDragLayerParams;
    /**
     * 缩放视图，暂时去除
     */
    // ScaleGestureDetector mDetector;
    Point downPoint = null;
    /**
     * 走动前的位置
     */
    Point mMovePre = new Point();
    /**
     * 走动后的位置
     */
    Point mMoveAfter = new Point();
    /**
     * 背景图片
     */
    ImageView mBackgroundView;
    /**
     * 装饰框
     */
    ImageView mFrameView;

    /**
     * 是否在于宠物编辑动作模式
     */
    boolean mViewOnEditMode = false;
    /**
     * 是否正按在缩放键上,0为左，1为上，2为右，3为下
     */
    int mViewOnScaleMode = -1;
    /**
     * 是否处在走动模式
     */
    boolean onMoveMode = false;
    /* 按下时间，判断长按 */
    long mDownTime = 0;
    /**
     * 播放或预览模式
     */
    boolean mPreviewMode = false;
    /**
     * 有人物正在走动中
     */
    boolean mMoving;
    /* 判断双击 */
    int mClickIndex = 0;
    /* 全屏模式 */
    boolean mFullScreenMode = false;
    /* 左边还是右边 */
    int leftOrRight = 0;
    /* 正在拖动中 */
    boolean mOnDrag = false;
    /* 背景图片 */
    String mBackgroundImg = null;
    /* 当前触摸按下位置X */
    int mMotionDownX = 0;
    /* 当前触摸按下位置Y */
    int mMotionDownY = 0;
    boolean frameNotifyed = false;
    /**
     * 编辑状态下与播放状态下的比例
     */
    float mPlayScale = 0;
    float mPlayScaleY = 0;

    float mShrinkX = 0;
    float mShrinkY = 0;

    float mDragItemOffsetX;
    float mDragItemOffsetY;

    int left40;

    public DragLayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        setChildrenDrawingOrderEnabled(true);
        setOnHierarchyChangeListener(this);
        addBackgroundView();
        mCreator = (AcMain) context;
        mPlayScale = 1240 / 1031f;
        mPlayScaleY = 697.5f / 580f;
        mShrinkX = 1031f / 1240;
        mShrinkY = 580f / 697.5f;
    }

    /**
     * 添加背景
     */
    public void addBackgroundView() {
        mBackgroundView = new ImageView(getContext());
        addView(mBackgroundView);
        mBackgroundView.setClickable(false);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mBackgroundView.setLayoutParams(params);
        mBackgroundView.setScaleType(ScaleType.FIT_XY);
    }

    /**
     * 编辑模式缩小
     *
     * @param fullscreen
     */
    public void setScaleMode(boolean anim, boolean fullscreen, int leftOrRight) {
        this.leftOrRight = leftOrRight;
        this.mFullScreenMode = fullscreen;
        if (mDragLayerParams == null) {
            mDragLayerParams = (RelativeLayout.LayoutParams) this.getLayoutParams();
        }
        Logs.i("xia", "fullscreen:" + fullscreen);
        if (fullscreen) {
            mDragLayerParams.width = SUtils.getSWidth(mCreator, 1840);
            mDragLayerParams.height = SUtils.getSHeight(mCreator, 979);
            mDragLayerParams.leftMargin = left40;
            mDragLayerParams.topMargin = (SUtils.screenHeight - mDragLayerParams.height) / 2;
        }
        invalidate();
        requestLayout();
    }

    public void initFirst() {
        this.removeAllViews();
        mDragLayerParams = (RelativeLayout.LayoutParams) this.getLayoutParams();
        addBackgroundView();
        /* 必须设置 */
        setmDragItem(null);
        SUtils.setPic(mBackgroundView, null, mDragLayerParams.width, mDragLayerParams.height, R.color.white, true);
    }

//	/**
//	 * 还原场景
//	 */
//	public void addViewDefaultPosition(SceneMovement item) {
//		DragRelativeLayout imageView = new DragRelativeLayout(getContext());
//		imageView.setLayoutPosition(item.getStartX(), item.getStartY());
//		ItemInfo info = new ItemInfo();
//		info.setLocal_path(SFileUtils.getDirectorySafe(SFileUtils.SOURCE_PATH + "/" + info.getRes_name()));
//		imageView.setItemInfo(info);
//		addView(imageView);
//		imageView.setSceneItem(item);
//		imageView.setDefalultIcon();
//	}

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mOnDrag) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @SuppressLint( "NewApi" )
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();
        Logs.i("xia", "onTouch:" + ismMoving());
		/* 移动时不让点击 */
        if (ismMoving()) {
            return true;
        }
        int x = (int) event.getX();
        int y = (int) event.getY();

        // if(null != mDetector){
        // mDetector.onTouchEvent(event);
        // }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downPoint = new Point(x, y);
                mOnDrag = false;
                mDragItem = checkOnChild(x, y);
                if (mDragItem instanceof DragRelativeLayout) {
                    SUtils.hideSoftInpuFromWindow(this);
                    DragRelativeLayout layout = (DragRelativeLayout) mDragItem;
                    if (layout.fortbitMoveBeforeAnim()) {
                        return false;
                    }
                }
			/* 检查是否按在物品四个角落点 */
                // if(checkOnScaleButton(x,y)){
                // return true;
                // }
                mDownTime = System.currentTimeMillis();

                mClickIndex++;
                if (null != mDragItem) {
                    mDragItemOffsetY = y - mDragItem.getViewTop();
                    mDragItemOffsetX = x - mDragItem.getViewLeft();
                    Logs.i("xia", "左偏移:" + y + ",," + mDragLayerParams.topMargin + ",,," + mDragItem.getViewTop());
                    Logs.i("xia", "左偏移:" + mDragItemOffsetX + ",,,上偏移:" + mDragItemOffsetY);
                    mMotionDownX = x;
                    mMotionDownY = y;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mDragItem != null) {
                    if (mDragItem instanceof DragRelativeLayout) {
					/* 当选择走动而选择其他人物时，取消走动 */
                        DragRelativeLayout layout = (DragRelativeLayout) mDragItem;
                        if (layout.fortbitMoveBeforeAnim()) {
                            return false;
                        }
                        if (!layout.isPrepareMoving()) {
                            onMoveMode = false;
                            clearAllPrepareMoving();
                        }
                    }
                }
                if (0 == mMotionDownX && 0 == mMotionDownY) {
                    mMotionDownX = x;
                    mMotionDownY = y;
                }

                handleMove(x, y);
                break;
            case MotionEvent.ACTION_UP:
                if (mDragItem == null) {
                    setmDragItem(null);
                }
                mOnDrag = true;
			/* 走动模式 */
                if (onMoveMode) {
                    if (mDragItem != null) {
                        mDragItem.setTranslationX(0);
                        mDragItem.setTranslationY(0);
                        Point point = restrainPosition((int) (x - mDragItemOffsetX), (int) (y - mDragItemOffsetY));
                        mMoveAfter.x = point.x;
                        mMoveAfter.y = point.y;
                    }
                    Logs.i("xia", "处理后的Y:" + mMoveAfter.y);
                    handleMove(false);
                    return true;
                }
                long curTime = System.currentTimeMillis();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        mClickIndex = 0;
                    }
                }, 200);
                if (curTime - mDownTime > 500) {
                    hanleActionUP(x, y);
                } else {
                    if (Math.abs(x - mMotionDownX) < 5 && Math.abs(y - mMotionDownY) < 5) {
                        setOnEditMode(true);
                        if (mDragItem != null) {
                        }
                    } else {
                        hanleActionUP(x, y);
                    }
                }

                mDownTime = 0;
            case MotionEvent.ACTION_CANCEL:
                Logs.i("DragLayer -- > ACTION_CANCEL");
                mOnDrag = true;
                if (onMoveMode) {
                    return true;
                }
                hanleActionUP(x, y);
                mDownTime = 0;
                break;
        }
        return true;
    }


    private Point restrainPosition(int x, int y) {
        Point point = new Point();
        point.x = x;
        point.y = y;
        if (x < 0) {
            point.x = 0;
        } else if (x + mDragItem.getmWidth() > getViewWidth()) {
            point.x = (int) (getViewWidth() - mDragItem.getmWidth());
        }
        if (y < mDragLayerParams.topMargin) {
            point.y = 10;
        }
        int height = mDragItem.getmHeight() + y;
        if (height > getViewHeight()) {
            point.y = (int) (getViewHeight() - mDragItem.getmHeight());
        }
        return point;
    }

    /**
     * 检查触摸点是否在物品对象上
     *
     * @param x
     * @param y
     * @return
     */
    public BaseDragView checkOnChild(int x, int y) {
        int count = this.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = this.getChildAt(count - i - 1);
            if (null != child) {
                Rect outRect = new Rect();
                if (child instanceof BaseDragView) {
                    BaseDragView view = (BaseDragView) child;
                    outRect = view.getCoor();
                    DragRelativeLayout layout = null;
                    if (view instanceof DragRelativeLayout) {
                        layout = (DragRelativeLayout) view;
                    }
                    if (outRect != null && outRect.contains(x, y)) {
                        if (layout != null) {
                            mPreviewMode = false;
                            setmDragItem(layout);
                            requestLayout();
                            invalidate();
                            return layout;
                        }
                        return null;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 移动放手时，去除临时的视图
     */
    public void handleMove(boolean playMode) {
        if (mDragItem == null) {
            return;
        }
        mPreviewMode = playMode;
        onMoveMode = false;
        DragRelativeLayout layout = isRelativeLayout();
        if (null == layout)
            return;
//		/* 添加一个新的行为 */
//		layout.updateSceneItem();
//		SceneMovement item = layout.getSceneItem();
//		if (item == null)
//			return;
//
//		Logs.i("移动后更新位置:x" + mMoveAfter.x + ",,,,y:" + mMoveAfter.y);
//		if (!mPreviewMode) {
//			item.setEndX(mMoveAfter.x);
//			item.setEndY(mMoveAfter.y);
//			SceneUser user = layout.getSceneUser();
//			user.setMoveable(true);
//			user.setEndX(mMoveAfter.x);
//			user.setEndY(mMoveAfter.y);
//		} else {
//			mMovePre = new Point(item.getStartX(), item.getStartY());
//			mMoveAfter = new Point(item.getEndX(), item.getEndY());
//
//			if (!mCreator.isSingleOrEditMode()) {
//				// if (mMoveAfter.x > mMovePre.x) {
//				// mMovePre.x = (int) ((item.getStartX() * (mPlayScale +
//				// (mPlayScale -1)/3)));
//				// mMovePre.y = (int) (item.getStartY()* (mPlayScaleY +
//				// (mPlayScaleY -1)/3));
//				// }
//				// int tempx = item.getEndX();
//				// if (item.getEndX() > item.getStartX()) {
//				//
//				// }
//				mMoveAfter.x = (int) (item.getEndX() * (mPlayScale));
//				mMoveAfter.y = (int) (item.getEndY() * (mPlayScaleY));
//				// mMoveAfter.x = tempx;
//			}
//		}
//		// Logs.i("xia", mMoveAfter.x + ",,,," + mMoveAfter.y + ",,," +
//		// mMovePre.x + ",,," + mMovePre.y);
//		layout.startMove();
//		layout.moveToDestination(mMovePre, mMoveAfter);
    }

    /**
     * 抬手后设置宠物所在位置，并设置疆界束缚
     *
     * @param x
     * @param y
     */
    @SuppressLint( "NewApi" )
    private void hanleActionUP(int x, int y) {
        if (null == mDragItem)
            return;
        mDragItem.setTranslationX(0);
        mDragItem.setTranslationY(0);
        int left = x - mMotionDownX + mDragItem.getViewLeft();
        if (left < 0) {
            left = 0;

        } else if (left + mDragItem.getWidth() > SUtils.screenWidth - SUtils.getSWidth(mCreator, 240)) {
            left = SUtils.screenWidth - mDragItem.getWidth() - SUtils.getSWidth(mCreator, 336);
        }
        int top = y - mMotionDownY + mDragItem.getViewTop();

        if (top < 0) {
            top = 0;
        } else if (top + mDragItem.getmHeight() > SUtils.screenHeight - SUtils.getSWidth(mCreator, 180)) {
            top = SUtils.screenHeight - SUtils.getSWidth(mCreator, 180) - mDragItem.getmHeight();
        }
        if (mDragItem instanceof DragRelativeLayout) {
            DragRelativeLayout layout = (DragRelativeLayout) mDragItem;

            layout.setLayoutPosition(left, top);
            layout.cancelLongPress();
        } else {
            mDragItem.setTranslationX(0);
            mDragItem.setTranslationY(0);
            mDragItem.setLayoutPosition(left, top);
        }
        mDragItem.requestLayout();
        mDragItem.invalidate();
        deleteDragItem();
    }

    public void deleteDragItem() {
        mDragItem = null;
        mMotionDownX = 0;
        mMotionDownY = 0;
    }

    /**
     * 移动时改变拖动项的位置
     *
     * @param x
     * @param y
     */
    @SuppressLint( "NewApi" )
    public void handleMove(int x, int y) {
        if (null == mDragItem) {
            mMotionDownX = 0;
            mMotionDownY = 0;
            return;
        }
        if (mMotionDownX == 0 && mMotionDownY == 0) {
            return;
        }
        int tx = x - mMotionDownX;
        int ty = y - mMotionDownY;
        mDragItem.setTranslationX(tx);
        mDragItem.setTranslationY(ty);
    }

    @Override
    public void onChildViewAdded(View parent, View child) {
//		if (child instanceof VoiceoverTextView == false && mFrameView != null && !frameNotifyed
//				&& mCreator.getPageIndex() == PageIndex.PAGE_CREATOR) {
//			frameNotifyed = true;
//			removeView(mFrameView);
//			mFrameView = new ImageView(getContext());
//			SUtils.setPic(mFrameView, mCreator.getCurScene().getFrameIcon());
//			addView(mFrameView);
//		}
//		if (mCreator != null && mCreator.getmPlayMode() == PlayMode.EDIT_MODE) {
//			if (child instanceof BaseDragView) {
//				if (child instanceof DragVideoView == false) {
//					scaleChild(child);
//				}
//			}
//		}
//		frameNotifyed = false;
    }

    /**
     * 设置装饰框背景
     *
     * @param path
     */
    public void setFrameBackground(String path) {
        if (mFrameView == null) {
            mFrameView = new ImageView(getContext());
            addView(mFrameView);
        }
        try {
            SUtils.setPic(mFrameView, path, true, new SimpleTarget() {

                @Override
                public void onResourceReady(Object arg0, GlideAnimation arg1) {
                    if (arg0 != null && arg0 instanceof GlideBitmapDrawable) {
                        Bitmap bitmap = ((GlideBitmapDrawable) arg0).getBitmap();
                        if (!bitmap.isRecycled()) {
                            mFrameView.setImageBitmap(bitmap);
                            invalidate();
                            requestLayout();
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onChildViewRemoved(View parent, View child) {
    }

    public View getmDragItem() {
        return mDragItem;
    }

    public void setmDragItem(BaseDragView item) {
        mDragItem = null;
        if (null == item) {
            mDragItemParams = null;
            return;
        }
        this.mDragItem = item;
        // mDetector = new ScaleGestureDetector(getContext(), listener);
        mDragItemParams = (LayoutParams) mDragItem.getLayoutParams();
    }

    private DragRelativeLayout isRelativeLayout() {
        if (null != mDragItem && mDragItem instanceof DragRelativeLayout) {
            DragRelativeLayout layout = (DragRelativeLayout) mDragItem;
            return layout;
        }
        return null;
    }

    /**
     * 设置为编辑模式
     *
     * @param b
     */
    public void setOnEditMode(boolean b) {
        this.mViewOnEditMode = b;
    }

    /**
     * 设置背景图
     *
     * @param img
     */
    public void setBackground(final String img) {
        SUtils.setPic(mBackgroundView, img, 1280, 720, R.color.white, true);
    }

    @Override
    public void setBackgroundResource(int resid) {
        if (mBackgroundView != null) {
            mBackgroundView.setBackgroundResource(resid);
        }
    }

    public boolean ismPreviewMode() {
        return mPreviewMode;
    }

    public void setmPreviewMode(boolean mPreviewMode) {
        this.mPreviewMode = mPreviewMode;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
    //
    // private OnScaleGestureListener listener = new OnScaleGestureListener() {
    //
    // @Override
    // public void onScaleEnd(ScaleGestureDetector detector) {
    // }
    //
    // @Override
    // public boolean onScaleBegin(ScaleGestureDetector detector) {
    // return true;
    // }
    //
    // @Override
    // public boolean onScale(ScaleGestureDetector detector) {
    // float factor = detector.getScaleFactor();
    // mMatrix.setScale(factor, factor);
    // if(null != mDragItem && mDragItem instanceof DragRelativeLayout){
    // DragRelativeLayout layout = (DragRelativeLayout) mDragItem;
    // layout.setScaleX(factor);
    // layout.setScaleY(factor);
    // }
    // return false;
    // }
    // };

    public void clearAllChildMove() {
        clearAll();
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof DragRelativeLayout) {
                DragRelativeLayout draglayout = (DragRelativeLayout) view;
            }
        }
    }

//	/**
//	 * 根据userID获取Layout
//	 * 
//	 * @param mActionScene
//	 * @return
//	 */
//	public DragRelativeLayout getSceneLayoutWithUserId(String userID) {
//		int count = getChildCount();
//		for (int i = 0; i < count; i++) {
//			View view = getChildAt(i);
//			if (view instanceof DragRelativeLayout) {
//				DragRelativeLayout draglayout = (DragRelativeLayout) view;
//				String id = draglayout.getSceneUserID();
//				if (id != null && id.equals(userID)) {
//					return draglayout;
//				}
//			}
//		}
//		return null;
//	}

    /**
     * 清除所有的准备走动状态
     */
    private void clearAllPrepareMoving() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof DragRelativeLayout) {
                DragRelativeLayout draglayout = (DragRelativeLayout) view;
                draglayout.setPrepareMoving(false);
            }
        }
    }

    public int getLeftMargin() {
        if (mDragLayerParams == null) {
            return 0;
        }
        return mDragLayerParams.leftMargin;
    }

    public int getLeftOrRight() {
        return leftOrRight;
    }

    public int getViewHeight() {
        if (mDragLayerParams == null) {
            return 0;
        }
        return mDragLayerParams.height;
    }

    public int getViewWidth() {
        if (mDragLayerParams == null) {
            return 0;
        }
        return mDragLayerParams.width;
    }

    public void clearAll() {
        mMoving = false;
        onMoveMode = false;
        mMotionDownX = 0;
        mMotionDownY = 0;
        mPreviewMode = false;
        mDragItem = null;
    }

    public boolean ismMoving() {
        return mMoving;
    }

    public void setmMoving(boolean mMoving) {
        this.mMoving = mMoving;
    }

}
