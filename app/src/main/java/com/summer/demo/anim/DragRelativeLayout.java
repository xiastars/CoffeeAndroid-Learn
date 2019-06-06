package com.summer.demo.anim;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.summer.demo.R;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;

import java.lang.ref.WeakReference;
import java.util.Arrays;

public class DragRelativeLayout extends BaseDragView implements OnClickListener {

	/** 文字类型 */
	public static final String ACTION_NAME_TEXT = "文字类型__";
	/* 气泡文字 */
	public String dialogtext;
	Context mCreator = null;
	Dialog mMoveTipDialog;
	/* 当前角色所属类型 */
	int mUserType = 1;
	/* 是否可以切换数据 */
	boolean mChameleon = false;
	/* 绑定的头部录音 */
	boolean mAnimTriggered = false;
	/* 是否在触摸状态 */
	boolean mIsPressed = false;
	/* 执行动作类型 */
	int mActionMode = -1;
	/* 动画是不是循环类型 */
	boolean mCircleMode = false;
	/* 走动X轴每次距离 */
	float mPerPaceX;
	/* 走动Y轴每次距离 */
	float mPerPaceY;
	int mPerTime;
	int mMoveIndex = 0;
	/* 预览模式 */
	boolean mPreviewMode = false;
	/* 播放动画的当前帧index */
	int playIndex = 0;
	/* 音频路径 */
	String mAudioPath;
	/* 默认背景 */
	String mDefaultIcon;
	/* 音频正在播放中 */
	boolean mAudioPlaying;
	/* 要进行的动作类型与URL */
	String mActionUrl;
	/* 结束时X轴 */
	int endX;
	/* 默认情况下，不能拖动，只有选中了才能拖动 */
	boolean dragAble;
	/* 结束时Y轴 */
	int endY;
	/* 音频正在录制中 */
	boolean mAudioRecording;
	/* 准备走动阶段 */
	boolean prepareMoving;
	/* 第一次走动提醒 */
	String FRIST_MOVE_TIPS = "FRIST_MOVE_TIPS";
	/* 行动名称 */
	String mActionName;
	/* 监听动作播放 */
	OnActionFinishListener onActionFinishListener;
	/* 第一个非透明的点 */
	Point firstCP = new Point();
	boolean mMeasured = false;
	/* 带有图片可以缩放的 */
	private RelativeLayout rlPet;
	private ImageView ivImage;
	private View view1, view2, view3, view4;
	private CheckLongPressHelper mLongPressHelper;

	private MyHandler mHandler = null;
	private Bitmap[] mBitmaps = null;

	public DragRelativeLayout(Context context) {
		super(context);
		mCreator = context;
		this.setOnClickListener(this);
		mHandler = new MyHandler(this);
		mLongPressHelper = new CheckLongPressHelper(this);
		mParams = (FrameLayout.LayoutParams) this.getLayoutParams();
		if (null == mParams) {
			mParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			setLayoutParams(mParams);
		}
		addChildView();
	}

	private void addChildView() {
		View view = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.view_creator, null);
		rlPet = (RelativeLayout) view.findViewById(R.id.rl_pet);
		ivImage = (ImageView) view.findViewById(R.id.iv_pet);
		ivImage.setScaleType(ScaleType.FIT_XY);
		this.addView(view);
//		disableClip(ivImage);
	}
	
/*	private static void disableClip(View view) {
		if (view.getParent() instanceof View) {
			View g = (View) view.getParent();
			if (g != null && g instanceof ViewGroup) {
				ViewGroup v = (ViewGroup) g;
				v.setClipChildren(false);
				v.setClipToPadding(false);
				disableClip(v);
			}
		}
	}*/

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * 当View处于选中状态时，设置四个点显示
	 * 
	 * @param visible
	 */
	public void showOrHideFocusedView(int visible) {
		view1.setVisibility(visible);
		view2.setVisibility(visible);
		view3.setVisibility(visible);
		view4.setVisibility(visible);
		if (visible == View.VISIBLE) {
			dragAble = true;
		} else {
			dragAble = false;
		}
		this.requestLayout();
		this.invalidate();
	}

	@Override
	public boolean performClick() {
		return super.performClick();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// setScaleX(1);
			// setScaleY(1);
			Logs.i("DragReletiveLayout -- > ACTION_CANCEL");
			// mDragLayer.checkOnScaleButton((int)event.getX(),
			// (int)event.getY());
			performClick();
			if (!mIsPressed) {
				mIsPressed = true;
				// ValueAnimator mScaleAnimation = ValueAnimator.ofFloat(0.85f,
				// getScaleX());
				// mScaleAnimation.addUpdateListener(new
				// AnimatorUpdateListener() {
				// public void onAnimationUpdate(ValueAnimator animation) {
				// float value = ((Float)
				// animation.getAnimatedValue()).floatValue();
				// setScaleX(value);
				// setScaleY(value);
				// }
				// });
				// mScaleAnimation.setInterpolator(new
				// OvershootInterpolator(1.2f));
				// mScaleAnimation.setDuration(100);
				// mScaleAnimation.start();
			}

			mLongPressHelper.postCheckForLongPress();
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			mIsPressed = false;
			mLongPressHelper.cancelLongPress();
			break;
		}
		return false;
	}

	public void cancelLongPress() {
		mIsPressed = false;
		mLongPressHelper.cancelLongPress();
	}

	@Override
	public void setLayoutPosition(int x, int y) {
		// Logs.i("xia","重新X:"+x+",,,最后Y:"+y+",,播放模式:"+mCreator.getmPlayMode());
//		if (!mCreator.isSingleOrEditMode()) {
//			float scale = mCreator.getDragLayer().mPlayScale;
//			x = (int) ((scale * (float) x));
//			y = (int) (mCreator.getDragLayer().mPlayScaleY * (float) y);
//		}
		if (null != mParams) {
			mParams.leftMargin = x;
			mParams.topMargin = y;
			requestLayout();
			invalidate();
		}
	}

	/**
	 * 设置默认资源图片
	 *
	 */
	@SuppressWarnings("rawtypes")
	public void setDefalultIcon() {
		Logs.i("xia", mDefaultIcon);
		if (!TextUtils.isEmpty(mDefaultIcon)) {
			try {
				SUtils.setPic(ivImage, mDefaultIcon, true, new SimpleTarget() {

					@Override
					public void onResourceReady(Object arg0, GlideAnimation arg1) {
						if (arg0 != null && arg0 instanceof GlideBitmapDrawable) {
							Bitmap bitmap = ((GlideBitmapDrawable) arg0).getBitmap();
							if (!bitmap.isRecycled()) {
								ivImage.setImageBitmap(bitmap);
								invalidate();
								requestLayout();
								long time = System.currentTimeMillis();
								if (firstCP.x == 0 && firstCP.y == 0) {
									for (int i = 0; i < bitmap.getWidth(); i++) {
										if(firstCP.x  != 0){
											break;
										}
										for (int j = 0; j < bitmap.getHeight(); j++) {
											int color = bitmap.getPixel(i, j);
											if (color != 0) {
												firstCP.x = i;
												break;
											}
										}
									}
									for (int i = 0; i < bitmap.getHeight(); i++) {
										for (int j = 0; j < bitmap.getWidth(); j++) {
											int color = bitmap.getPixel(j, i);
											if (color != 0) {
												firstCP.y = i;
												return;
											}
										}
									}
								}
								Logs.i("xia","TIME:"+(System.currentTimeMillis() - time));
							}

						}
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressLint("NewApi")
	private void setPhotoIndex() {
		if (null == mBitmaps || mBitmaps.length < playIndex) {
			return;
		}
		Bitmap bitmap = mBitmaps[playIndex];
		if (bitmap != null && !bitmap.isRecycled()) {
			try {
				ivImage.setImageBitmap(bitmap);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 进行动画时不允许移动
	 */
	public boolean fortbitMoveBeforeAnim() {
		if (mBitmaps == null || mBitmaps.length == 0)
			return false;
		if (mBitmaps.length > playIndex && playIndex > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 循环播放动画
	 */
	public void circlePlay(Bitmap[] mBitmaps) {
		this.mBitmaps = mBitmaps;
		/* 如果退出本类，则停止 */
		if (((Activity)mCreator).isFinishing()) {
			return;
		}
		mCircleMode = true;
		/* 保险情况再获取一次 */
		if (null == mBitmaps) {
		}
		if (null == mBitmaps) {
			if (null != onActionFinishListener) {
				onActionFinishListener.finish();
			}
			return;
		}
		playIndex++;
		if (playIndex > mBitmaps.length) {
//			stopCicelMode();
			return;
		}
		/* 判断结束动画 */
		if (mBitmaps.length == playIndex) {
			mAnimTriggered = false;
			playIndex = 0;
			if (mCircleMode) {
				mHandler.sendEmptyMessageDelayed(0, CommomData.PLAY_TIME);
			} else {
				// setDefalultIcon();

				setDefalultIcon();
				afterPlayEnd();
			}
			return;
		}

		mHandler.sendEmptyMessageDelayed(0, CommomData.PLAY_TIME);
	}

	private void startCirclePlay() {
		circlePlay(mBitmaps);
	}

	private void setCancelDialog() {
		if (null != mMoveTipDialog) {
			mMoveTipDialog.cancel();
			mMoveTipDialog = null;
		}
	}



	/**
	 * 结束动画，走动和音乐播放后
	 */
	private void afterPlayEnd() {
		// 关闭循环模式
		mCircleMode = false;
		// 关闭Draglayer里的动画播放限制人物操作
		Logs.i("xia", "结束动作");
		// 动画指针移动首位
		playIndex = 0;
		// 音乐播放状态还原
		mAudioPlaying = false;
		mPreviewMode = false;
		mActionMode = -1;
		mAnimTriggered = false;
		dialogtext = null;
		mAudioPath = null;
		mActionUrl = null;
		/* 切换时，必须等到当前帧播完再回调 */
		new Handler().postDelayed(new Runnable() {
			public void run() {
				removeAllBitmap();
				Logs.i("xia", onActionFinishListener + ",,,");
				if (null != onActionFinishListener) {
					onActionFinishListener.finish();
				}
			}
		}, CommomData.PLAY_TIME);
	}

	@SuppressLint("NewApi")
	private void removeTransaction() {
		int left = (int) (getViewLeft() + getTranslationX());
		int top = (int) (getViewTop() + getTranslationY());
		setTranslationX(0);
		setTranslationY(0);
		if (null != mParams) {
			mParams.leftMargin = left;
			mParams.topMargin = top;
			requestLayout();
			invalidate();
		}
	}

	private String[] retriveActionBitmap(String actionname) {
		if (null == actionname)
			return null;
		String actioninfo = "";
		String[] picinfo = null;
		if (actionname.contains("/")) {
			actioninfo = (String) actionname.subSequence(actionname.lastIndexOf("/") + 1, actionname.lastIndexOf("."));
			Logs.i("actionInfo:" + actioninfo);
			picinfo = actioninfo.split("_");
			Logs.i(Arrays.toString(picinfo));
			mAudioPlaying = false;
			return picinfo;
		}
		return null;
	}

	@Override
	public void onClick(View v) {

	}

	private void removeAllBitmap() {
		if (null != mBitmaps) {
			for (Bitmap bitmap : mBitmaps) {
				if (null != bitmap) {
					bitmap.recycle();
					bitmap = null;
				}
			}
		}
		mBitmaps = null;
	}

	@Override
	public Rect getCoor() {
		mCoor = new Rect();
		mCoor.left = mParams.leftMargin;
		mCoor.right = mParams.leftMargin + mWidth;
		mCoor.top = mParams.topMargin;
		mCoor.bottom = mParams.topMargin + mHeight;
		return mCoor;
	}

	@Override
	public android.view.ViewGroup.LayoutParams getLayoutParams() {
		return mParams;
	}

	public boolean isPrepareMoving() {
		return prepareMoving;
	}

	public void setPrepareMoving(boolean prepareMoving) {
		this.prepareMoving = prepareMoving;
	}

	public interface OnActionFinishListener {
		void finish();
	}

	private interface OnGetBitmapListener {
		void onSucceed();

		void onFailure();
	}

	private static class MyHandler extends Handler {
		private final WeakReference<DragRelativeLayout> mCreator;

		public MyHandler(DragRelativeLayout activity) {
			mCreator = new WeakReference<DragRelativeLayout>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			DragRelativeLayout view = mCreator.get();
			if (null != view) {
				switch (msg.what) {
				case 0:
					view.setPhotoIndex();
					view.circlePlay(view.mBitmaps);
					break;
				case 1:

					break;
				case 2:
					view.startCirclePlay();
					break;
				case 3:
					break;
				case 4:
					break;
				case 5:
					break;
				case 6:
					view.circlePlay(view.mBitmaps);
					break;
				case 7:
					break;
				}
			}
		}
	}

}
