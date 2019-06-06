package com.summer.helper.recycle;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorUpdateListener;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.malata.summer.helper.R;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;

/**
 * @author https://github.com/android-cjj/Android-MaterialRefreshLayout
 */
public class MaterialRefreshLayout extends FrameLayout {

	public static final String Tag = MaterialRefreshLayout.class.getSimpleName();
	private final static int DEFAULT_WAVE_HEIGHT = 140;
	private final static int HIGHER_WAVE_HEIGHT = 180;
	private final static int DEFAULT_HEAD_HEIGHT = 70;
	private final static int hIGHER_HEAD_HEIGHT = 100;
	/* 默认圈圈大小 */
	private final static int DEFAULT_PROGRESS_SIZE = 60;
	private final static int BIG_PROGRESS_SIZE = 60;
	private final static int PROGRESS_STOKE_WIDTH = 3;

	private MaterialHeaderView mMaterialHeaderView;
	private MaterialFooterView mMaterialFooterView;
	private SunLayout mSunLayout;
	private boolean isOverlay;
	private int waveType;
	private int waveColor;
	protected float mWaveHeight;
	protected float mHeadHeight;
	private View mChildView;
	protected boolean isRefreshing;
	private float mTouchY;
	private float mCurrentY;
	private float mTouchX;
	private float mCurrentX;
	private DecelerateInterpolator decelerateInterpolator;
	private float headHeight;
	private float waveHeight;
	private int[] colorSchemeColors;
	private int colorsId;
	private int progressTextColor;
	private int progressValue, progressMax;
	private boolean showArrow = true;
	private int textType;
	private MaterialRefreshListener refreshListener;
	private boolean showProgressBg;
	private int progressBg;
	private boolean isShowWave;
	private int progressSizeType;
	private int progressSize = 0;
	private boolean isLoadMoreing;
	private boolean isLoadMore;
	private boolean isSunStyle = false;
	/* 当前是否为横向 */
	private boolean isHor = false;

	private RecyclerView refreshView;

	ImageView emptyView;
	protected boolean refreshable = true;

	public MaterialRefreshLayout(Context context) {
		this(context, null, 0);
	}

	public RecyclerView getRefreshView() {
		return refreshView;
	}

	public MaterialRefreshLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MaterialRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		refreshView = new RecyclerView(context);
		this.addView(refreshView);
		init(context, attrs, defStyleAttr);
	}

	private void init(Context context, AttributeSet attrs, int defstyleAttr) {
		if (isInEditMode()) {
			return;
		}

		if (getChildCount() > 1) {
			throw new RuntimeException("can only have one child widget");
		}

		decelerateInterpolator = new DecelerateInterpolator(10);

		final float density = getContext().getResources().getDisplayMetrics().density;
		TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.MaterialRefreshLayout, defstyleAttr, 0);
		isOverlay = t.getBoolean(R.styleable.MaterialRefreshLayout_overlay, false);
		/** attrs for materialWaveView */
		waveType = t.getInt(R.styleable.MaterialRefreshLayout_wave_height_type, 0);
		if (waveType == 0) {
			headHeight = DEFAULT_HEAD_HEIGHT;
			waveHeight = DEFAULT_WAVE_HEIGHT;
			MaterialWaveView.DefaulHeadHeight = DEFAULT_HEAD_HEIGHT;
			MaterialWaveView.DefaulWaveHeight = DEFAULT_WAVE_HEIGHT;
		} else {
			headHeight = hIGHER_HEAD_HEIGHT;
			waveHeight = HIGHER_WAVE_HEIGHT;
			MaterialWaveView.DefaulHeadHeight = hIGHER_HEAD_HEIGHT;
			MaterialWaveView.DefaulWaveHeight = HIGHER_WAVE_HEIGHT;
		}
		waveColor = t.getColor(R.styleable.MaterialRefreshLayout_wave_color, Color.WHITE);
		isShowWave = t.getBoolean(R.styleable.MaterialRefreshLayout_wave_show, true);

		/** attrs for circleprogressbar */
		colorsId = t.getResourceId(R.styleable.MaterialRefreshLayout_progress_colors, R.array.material_colors);
		colorSchemeColors = context.getResources().getIntArray(colorsId);
		showArrow = t.getBoolean(R.styleable.MaterialRefreshLayout_progress_show_arrow, true);
		textType = t.getInt(R.styleable.MaterialRefreshLayout_progress_text_visibility, 1);
		progressTextColor = t.getColor(R.styleable.MaterialRefreshLayout_progress_text_color, Color.BLACK);
		progressValue = t.getInteger(R.styleable.MaterialRefreshLayout_progress_value, 0);
		progressMax = t.getInteger(R.styleable.MaterialRefreshLayout_progress_max_value, 100);
		showProgressBg = t.getBoolean(R.styleable.MaterialRefreshLayout_progress_show_circle_backgroud, true);
		progressBg = t.getColor(R.styleable.MaterialRefreshLayout_progress_backgroud_color,
				CircleProgressBar.DEFAULT_CIRCLE_BG_LIGHT);
		progressSizeType = t.getInt(R.styleable.MaterialRefreshLayout_progress_size_type, 0);
		if (progressSizeType == 0) {
			progressSize = (int) (DEFAULT_PROGRESS_SIZE / density);
		} else {
			progressSize = BIG_PROGRESS_SIZE;
		}
		isLoadMore = t.getBoolean(R.styleable.MaterialRefreshLayout_isLoadMore, false);
		t.recycle();
	}

	public void setIsHor() {
		isHor = true;
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		Log.i(Tag, "onAttachedToWindow");

		Context context = getContext();

		mChildView = getChildAt(0);

		if (mChildView == null) {
			return;
		}

		setWaveHeight(Util.dip2px(context, waveHeight));
		setHeaderHeight(Util.dip2px(context, headHeight));

		if (isSunStyle) {
			mSunLayout = new SunLayout(context);
			LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					Util.dip2px(context, hIGHER_HEAD_HEIGHT));
			layoutParams.gravity = Gravity.TOP;
			mSunLayout.setVisibility(View.GONE);
			setHeaderView(mSunLayout);
		} else {
			mMaterialHeaderView = new MaterialHeaderView(context);
			LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					Util.dip2px(context, hIGHER_HEAD_HEIGHT));
			if (!isHor) {
				layoutParams.gravity = Gravity.TOP;
			} else {
				layoutParams.width = Util.dip2px(context, hIGHER_HEAD_HEIGHT);
				layoutParams.gravity = Gravity.CENTER_VERTICAL;
			}
			mMaterialHeaderView.setLayoutParams(layoutParams);
			mMaterialHeaderView.showProgressArrow(showArrow);
			mMaterialHeaderView.setProgressSize(progressSize);
			mMaterialHeaderView.setProgressColors(colorSchemeColors);
			mMaterialHeaderView.setProgressStokeWidth(PROGRESS_STOKE_WIDTH);
			mMaterialHeaderView.setTextType(textType);
			mMaterialHeaderView.setProgressValue(progressValue);
			mMaterialHeaderView.setProgressValueMax(progressMax);
			mMaterialHeaderView.setIsProgressBg(showProgressBg);
			mMaterialHeaderView.setProgressBg(progressBg);
			mMaterialHeaderView.setVisibility(View.GONE);
			setHeaderView(mMaterialHeaderView);
		}

		mMaterialFooterView = new MaterialFooterView(context);
		LayoutParams layoutParams2 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				Util.dip2px(context, hIGHER_HEAD_HEIGHT));
		if (!isHor) {
			layoutParams2.gravity = Gravity.BOTTOM;
		} else {
			layoutParams2.width = Util.dip2px(context, hIGHER_HEAD_HEIGHT);
			layoutParams2.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
		}
		mMaterialFooterView.setLayoutParams(layoutParams2);
		mMaterialFooterView.showProgressArrow(showArrow);
		mMaterialFooterView.setProgressSize(progressSize);
		mMaterialFooterView.setProgressColors(colorSchemeColors);
		mMaterialFooterView.setProgressStokeWidth(PROGRESS_STOKE_WIDTH);
		mMaterialFooterView.setTextType(textType);
		mMaterialFooterView.setProgressValue(progressValue);
		mMaterialFooterView.setProgressValueMax(progressMax);
		mMaterialFooterView.setIsProgressBg(showProgressBg);
		mMaterialFooterView.setProgressBg(progressBg);
		mMaterialFooterView.setVisibility(View.GONE);
		setFooderView(mMaterialFooterView);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (isRefreshing)
			return false;
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mTouchY = ev.getY();
			mCurrentY = mTouchY;
			mTouchX = ev.getX();
			mCurrentX = mTouchX;
			break;
		case MotionEvent.ACTION_MOVE:
			if (!refreshable) {
				return false;
			}
			float currentY = ev.getY();
			float dy = currentY - mTouchY;
			float currentX = ev.getX();
			float dx = currentX - mTouchX;
			// 如果为竖向
			if (!isHor) {
				if (dy > 0 && !canChildScrollUp()) {
					if (mMaterialHeaderView != null) {
						mMaterialHeaderView.setVisibility(View.VISIBLE);
						mMaterialHeaderView.onBegin(this);
					} else if (mSunLayout != null) {
						mSunLayout.setVisibility(View.VISIBLE);
						mSunLayout.onBegin(this);
					}
					return true;
				} else if (dy < 0 && !canChildScrollDown() && isLoadMore) {
					if (mMaterialFooterView != null && !isLoadMoreing) {
						soveLoadMoreLogic();
					}
					return super.onInterceptTouchEvent(ev);
				}
			} else {
				if (dx > 0 && !canChildScrollUp()) {
					if (mMaterialHeaderView != null) {
						mMaterialHeaderView.setVisibility(View.VISIBLE);
						mMaterialHeaderView.onBegin(this);
					} else if (mSunLayout != null) {
						mSunLayout.setVisibility(View.VISIBLE);
						mSunLayout.onBegin(this);
					}
					return true;
				} else if (dx < 10 && !canChildScrollDown() && isLoadMore) {
					if (mMaterialFooterView != null && !isLoadMoreing) {
						soveLoadMoreLogic();
					}
					return super.onInterceptTouchEvent(ev);
				}
			}

			break;
		}
		return super.onInterceptTouchEvent(ev);
	}

	private void soveLoadMoreLogic() {
		isLoadMoreing = true;
		mMaterialFooterView.setVisibility(View.VISIBLE);
		mMaterialFooterView.onBegin(this);
		mMaterialFooterView.onRefreshing(this);
		if (refreshListener != null) {
			refreshListener.onRefreshLoadMore(MaterialRefreshLayout.this);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		if (isRefreshing) {
			return super.onTouchEvent(e);
		}

		switch (e.getAction()) {
		case MotionEvent.ACTION_MOVE:
			if (!refreshable) {
				return true;
			}
			mCurrentY = e.getY();
			mCurrentX = e.getX();
			float axis;
			float dy = mCurrentY - mTouchY;
			float dx = mCurrentX - mTouchX;
			if (isHor) {
				axis = dx;
			} else {
				axis = dy;
			}
			axis = Math.min(mWaveHeight * 2, axis);
			axis = Math.max(0, axis);
			if (mChildView != null) {
				float offsetY = decelerateInterpolator.getInterpolation(axis / mWaveHeight / 2) * axis / 2;
				float fraction = offsetY / mHeadHeight;
				if (mMaterialHeaderView != null) {
					if (!isHor) {
						mMaterialHeaderView.getLayoutParams().height = (int) offsetY;
					} else {
						mMaterialHeaderView.getLayoutParams().width = (int) offsetY;
					}

					mMaterialHeaderView.requestLayout();
					mMaterialHeaderView.onPull(this, fraction);
				} else if (mSunLayout != null) {
					mSunLayout.getLayoutParams().height = (int) offsetY;
					mSunLayout.requestLayout();
					mSunLayout.onPull(this, fraction);
				}
				if (!isOverlay) {
					if (isHor) {
						ViewCompat.setTranslationX(mChildView, offsetY);
					} else {
						ViewCompat.setTranslationY(mChildView, offsetY);
					}

				}

			}
			return true;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			if (!refreshable) {
				return true;
			}
			if (mChildView != null) {
				if (mMaterialHeaderView != null) {
					if (isHor) {
						if (isOverlay) {
							if (mMaterialHeaderView.getLayoutParams().width > mHeadHeight) {
								updateListener();
								mMaterialHeaderView.getLayoutParams().width = (int) mHeadHeight;
								mMaterialHeaderView.requestLayout();

							} else {
								mMaterialHeaderView.getLayoutParams().width = 0;
								mMaterialHeaderView.requestLayout();
							}

						} else {
							/*
							 * if (ViewCompat.getTranslationY(mChildView) >=
							 * mHeadHeight) {
							 * createAnimatorTranslationY(mChildView,
							 * mHeadHeight, mMaterialHeaderView);
							 * updateListener(); } else {
							 * createAnimatorTranslationY(mChildView, 0,
							 * mMaterialHeaderView); }
							 */
						}
					} else {
						if (isOverlay) {
							if (mMaterialHeaderView.getLayoutParams().height > mHeadHeight) {

								updateListener();

								mMaterialHeaderView.getLayoutParams().height = (int) mHeadHeight;
								mMaterialHeaderView.requestLayout();

							} else {
								mMaterialHeaderView.getLayoutParams().height = 0;
								mMaterialHeaderView.requestLayout();
							}

						} else {
							if (ViewCompat.getTranslationY(mChildView) >= mHeadHeight) {
								createAnimatorTranslationY(mChildView, mHeadHeight, mMaterialHeaderView);
								updateListener();
							} else {
								createAnimatorTranslationY(mChildView, 0, mMaterialHeaderView);
							}
						}
					}

				} else if (mSunLayout != null) {
					if (isOverlay) {
						if (mSunLayout.getLayoutParams().height > mHeadHeight) {

							updateListener();

							mSunLayout.getLayoutParams().height = (int) mHeadHeight;
							mSunLayout.requestLayout();

						} else {
							mSunLayout.getLayoutParams().height = 0;
							mSunLayout.requestLayout();
						}

					} else {
						if (ViewCompat.getTranslationY(mChildView) >= mHeadHeight) {
							createAnimatorTranslationY(mChildView, mHeadHeight, mSunLayout);
							updateListener();
						} else {
							createAnimatorTranslationY(mChildView, 0, mSunLayout);
						}
					}
				}

			}
			return true;
		}

		return super.

		onTouchEvent(e);

	}

	public void resetLocation() {
		if (mChildView != null) {
			mChildView.clearAnimation();
			mChildView.setTranslationX(0);
			mChildView.setTranslationY(0);
		}
	}

	public void setSunStyle(boolean isSunStyle) {
		this.isSunStyle = isSunStyle;
	}

	public void autoRefresh() {
		this.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (!isRefreshing) {
					if (mMaterialHeaderView != null) {
						mMaterialHeaderView.setVisibility(View.VISIBLE);

						if (isOverlay) {
							mMaterialHeaderView.getLayoutParams().height = (int) mHeadHeight;
							mMaterialHeaderView.requestLayout();
						} else {
							createAnimatorTranslationY(mChildView, mHeadHeight, mMaterialHeaderView);
						}
					} else if (mSunLayout != null) {
						mSunLayout.setVisibility(View.VISIBLE);
						if (isOverlay) {
							mSunLayout.getLayoutParams().height = (int) mHeadHeight;
							mSunLayout.requestLayout();
						} else {
							createAnimatorTranslationY(mChildView, mHeadHeight, mSunLayout);
						}
					}

					updateListener();

				}
			}
		}, 50);

	}

	public void autoRefreshLoadMore() {
		this.post(new Runnable() {
			@Override
			public void run() {
				if (isLoadMore) {
					soveLoadMoreLogic();
				} else {
					// throw new RuntimeException("you must setLoadMore ture");
				}
			}
		});
	}

	public void updateListener() {
		isRefreshing = true;

		if (mMaterialHeaderView != null) {
			mMaterialHeaderView.onRefreshing(MaterialRefreshLayout.this);
		} else if (mSunLayout != null) {
			mSunLayout.onRefreshing(MaterialRefreshLayout.this);
		}

		if (refreshListener != null) {
			refreshListener.onRefresh(MaterialRefreshLayout.this);
		}

	}

	protected void setLoadMore(boolean isLoadMore) {
		this.isLoadMore = isLoadMore;
	}

	public void setProgressColors(int[] colors) {
		this.colorSchemeColors = colors;
	}

	public void setShowArrow(boolean showArrow) {
		this.showArrow = showArrow;
	}

	public void setShowProgressBg(boolean showProgressBg) {
		this.showProgressBg = showProgressBg;
	}

	protected void setWaveColor(int waveColor) {
		this.waveColor = waveColor;
	}

    protected void setWaveShow(boolean isShowWave) {
        this.isShowWave = isShowWave;
    }

    protected void setIsOverLay(boolean isOverLay) {
        this.isOverlay = isOverLay;
    }

//    public void setProgressValue(int progressValue) {
//        this.progressValue = progressValue;
//        mMaterialHeaderView.setProgressValue(progressValue);
//    }

    public void createAnimatorTranslationY(final View v, final float h, final FrameLayout fl) {
        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = ViewCompat.animate(v);
        viewPropertyAnimatorCompat.setDuration(250);
        viewPropertyAnimatorCompat.setInterpolator(new DecelerateInterpolator());
        viewPropertyAnimatorCompat.translationY(h);
        viewPropertyAnimatorCompat.start();
        viewPropertyAnimatorCompat.setUpdateListener(new ViewPropertyAnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(View view) {
                float height = ViewCompat.getTranslationY(v);
                fl.getLayoutParams().height = (int) height;
                fl.requestLayout();
            }
        });
    }

	/**
	 * @return Whether it is possible for the child view of this layout to
	 *         scroll up. Override this if the child view is a custom view.
	 */
	public boolean canChildScrollUp() {
		if (mChildView == null) {
			return false;
		}
		if (Build.VERSION.SDK_INT < 14) {
			if (mChildView instanceof AbsListView) {
				final AbsListView absListView = (AbsListView) mChildView;
				return absListView.getChildCount() > 0 && (absListView.getFirstVisiblePosition() > 0
						|| absListView.getChildAt(0).getTop() < absListView.getPaddingTop());
			} else {
				if (isHor) {
					return ViewCompat.canScrollHorizontally(mChildView, -1) || mChildView.getScrollX() > 0;
				}
				return ViewCompat.canScrollVertically(mChildView, -1) || mChildView.getScrollY() > 0;
			}
		} else {
			if (isHor) {
				return ViewCompat.canScrollHorizontally(mChildView, -1);
			}
			return ViewCompat.canScrollVertically(mChildView, -1);
		}
	}

	public boolean canChildScrollDown() {
		if (mChildView == null) {
			return false;
		}
		if (Build.VERSION.SDK_INT < 14) {
			if (mChildView instanceof AbsListView) {
				final AbsListView absListView = (AbsListView) mChildView;
				if (absListView.getChildCount() > 0) {
					int lastChildBottom = absListView.getChildAt(absListView.getChildCount() - 1).getBottom();
					return absListView.getLastVisiblePosition() == absListView.getAdapter().getCount() - 1
							&& lastChildBottom <= absListView.getMeasuredHeight();
				} else {
					return false;
				}

            } else {
                if(isHor){
                    return ViewCompat.canScrollHorizontally(mChildView,1)|| mChildView.getScrollX() > 0;
                }
                return ViewCompat.canScrollVertically(mChildView, 1) || mChildView.getScrollY() > 0;
            }
        } else {
            if(isHor){
                return ViewCompat.canScrollHorizontally(mChildView,1);
            }
            return ViewCompat.canScrollVertically(mChildView, 1);
        }
    }

	public void setWaveHigher() {
		headHeight = hIGHER_HEAD_HEIGHT;
		waveHeight = HIGHER_WAVE_HEIGHT;
		MaterialWaveView.DefaulHeadHeight = hIGHER_HEAD_HEIGHT;
		MaterialWaveView.DefaulWaveHeight = HIGHER_WAVE_HEIGHT;
	}

    public void finishRefreshing() {
        if (mChildView != null) {
            ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = ViewCompat.animate(mChildView);
            viewPropertyAnimatorCompat.setDuration(200);
            viewPropertyAnimatorCompat.y(ViewCompat.getTranslationY(mChildView));
            viewPropertyAnimatorCompat.translationY(0);
            viewPropertyAnimatorCompat.setInterpolator(new DecelerateInterpolator());
            viewPropertyAnimatorCompat.start();

            if (mMaterialHeaderView != null) {
                mMaterialHeaderView.onComlete(MaterialRefreshLayout.this);
            } else if (mSunLayout != null) {
                mSunLayout.onComlete(MaterialRefreshLayout.this);
            }

			if (refreshListener != null) {
				refreshListener.onfinish();
			}
		}
		showEmptyView();
		isRefreshing = false;
		progressValue = 0;
	}

    public void finishRefresh() {
        this.post(new Runnable() {
            @Override
            public void run() {
                finishRefreshing();
            }
        });
    }

    public void finishRefreshLoadMore() {
        this.post(new Runnable() {
            @Override
            public void run() {
                if (mMaterialFooterView != null && isLoadMoreing) {
                    isLoadMoreing = false;
                    mMaterialFooterView.onComlete(MaterialRefreshLayout.this);
                }
            }
        });

		showEmptyView();
	}
	
	private void showEmptyView(){
		if (refreshView != null) {
			int count = refreshView.getAdapter().getItemCount();
			if (count == 0) {
				if (emptyView == null) {
					int left = 0;
					int top = 0;
					if (this.getParent() != null) {
						left = ((View) this.getParent()).getLeft();
						top = ((View) this.getParent()).getTop();
					}
					emptyView = new ImageView(getContext());
					FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(201, 268);
					SUtils.initScreenDisplayMetrics((Activity) getContext());
					Logs.i("xia", refreshView.getMeasuredWidth() + ",," + refreshView.getWidth() + ",,"
							+ refreshView.getLayoutParams().width);
					params.leftMargin = (SUtils.screenWidth - 201) / 2 - left;
					params.topMargin = (SUtils.screenHeight - 268) / 2 - top;
					emptyView.setLayoutParams(params);
					emptyView.setBackgroundResource(R.drawable.emptyview);
					this.addView(emptyView);
				}
			} else {
				if(emptyView != null){
					this.removeView(emptyView);
					emptyView = null;
				}
			}
		}
	}

    private void setHeaderView(final View headerView) {
        addView(headerView);
    }

    public void setHeader(final View headerView) {
        setHeaderView(headerView);
    }

    public void setFooderView(final View fooderView) {
        this.addView(fooderView);
    }


    public void setWaveHeight(float waveHeight) {
        this.mWaveHeight = waveHeight;
    }

    public void setHeaderHeight(float headHeight) {
        this.mHeadHeight = headHeight;
    }

    public void setMaterialRefreshListener(MaterialRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

}
