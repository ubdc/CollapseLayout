package com.example.collapselayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.example.expendlayout.R;

public class CollapseLayout extends FrameLayout {
	private static final Interpolator DEFAULT_INTERPOLATOR = new AccelerateDecelerateInterpolator();
	private static final int DEFAULT_ANIM_TIME = 600;
	private State state;
	private Scroller mScroller;
	private int mMeasuredWidth;
	private int mMeasuredHeight;
	private Interpolator mInterpolator;
	private int mAnimTime = DEFAULT_ANIM_TIME;
	private CollapseMode mMode;
	private Orientation mCollapseOrientation;
	private CollapseListener mCollapseListener;
	private boolean mIsFirstLayout = true;
	
	private Runnable mVerticalAnim = new Runnable() {

		@Override
		public void run() {
			if (mScroller.computeScrollOffset()) {
				android.view.ViewGroup.LayoutParams lp = getLayoutParams();
				lp.width = mMeasuredWidth;
				lp.height = mScroller.getCurrY();
				setLayoutParams(lp);
				if (mMode == CollapseMode.PullOut) {
					scrollTo(0, mMeasuredHeight - mScroller.getCurrY());
				} else {
					scrollTo(0, 0);
				}
				post(this);
			} else {
				if (mCollapseListener != null) {
					if (mScroller.getCurrY() == 0) {
						mCollapseListener.onCloseComplete();
					} else {
						mCollapseListener.onOpenComplete();
					}
				}
			}
		}
	};
	
	private Runnable mHorizontalAnim = new Runnable() {
		
		@Override
		public void run() {
			if (mScroller.computeScrollOffset()) {
				android.view.ViewGroup.LayoutParams lp = getLayoutParams();
				lp.width = mScroller.getCurrX();
				lp.height = mMeasuredHeight;
				setLayoutParams(lp);
				if (mMode == CollapseMode.PullOut) {
					scrollTo(mMeasuredWidth - mScroller.getCurrX(), 0);
				} else {
					scrollTo(0, 0);
				}
				post(this);
			} else {
				if (mCollapseListener != null) {
					if (mScroller.getCurrX() == 0) {
						mCollapseListener.onCloseComplete();
					} else {
						mCollapseListener.onOpenComplete();
					}
				}
			}
		}
	};
	
	public CollapseLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CollapseLayout, defStyle, 0);
		setCollapseOrientation(Orientation.values()[a.getInt(R.styleable.CollapseLayout_collapseOrientation, 0)]);
		setCollapseMode(CollapseMode.values()[a.getInt(R.styleable.CollapseLayout_collapseMode, 0)]);
		state = State.values()[a.getInt(R.styleable.CollapseLayout_state, 0)];
		setAnimateDuration(a.getInt(R.styleable.CollapseLayout_collapseDuration, DEFAULT_ANIM_TIME));
		a.recycle();
		setInterpolator(DEFAULT_INTERPOLATOR);
		setFocusable(true);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        setWillNotDraw(false);
	}

	public CollapseLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CollapseLayout(Context context) {
		this(context, null);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (mIsFirstLayout) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			mMeasuredWidth = getMeasuredWidth();
			mMeasuredHeight = getMeasuredHeight();
			if (state == State.Close) {
				if (mCollapseOrientation == Orientation.Horizontal) {
					setMeasuredDimension(0, mMeasuredHeight);
				} else {
					setMeasuredDimension(mMeasuredWidth, 0);
				}
			}
		} else {
			setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
		}
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (!mScroller.isFinished()) {
			mScroller.abortAnimation();
		}
		mIsFirstLayout = true;
	}
	
    public void setInterpolator(Interpolator interpolator) {
    	if (interpolator == null) {
    		interpolator = DEFAULT_INTERPOLATOR;
    	}
    	if (interpolator == this.mInterpolator) {
    		return;
    	}
    	this.mInterpolator = interpolator;
    	mScroller = new Scroller(getContext(), interpolator);
    }
    
    public void setAnimateDuration(int duration) {
    	if (duration <= 0) {
    		duration = DEFAULT_ANIM_TIME;
    	}
    	mAnimTime = duration;
    }
    
    public void setCollapseMode(CollapseMode mode) {
    	if (mode == null) {
    		mode = CollapseMode.PullOut;
    	}
    	mMode = mode;
    }
    
    public void setCollapseOrientation(Orientation orientation) {
    	if (orientation == null) {
    		orientation = Orientation.Vertical;
    	}
    	mCollapseOrientation = orientation;
    }
    
    public void setCollapseListener(CollapseListener listener) {
    	mCollapseListener = listener;
    }
    
    public State getState() {
		return state;
	}
    
    public void setState(State state) {
    	if (state == null || this.state == state) {
    		return;
    	}
    	this.state = state;
    	mIsFirstLayout = true;
    	requestLayout();
    }
    
	public void close() {
		mIsFirstLayout = false;
		switch (mCollapseOrientation) {
		case Vertical:
			verticalClose();
			break;
		case Horizontal:
			horizontalClose();
			break;
		}
	}
	
	private void verticalClose() {
		if (mMeasuredHeight > 0) {
			if (state == State.Close) {
				return;
			}
			state = State.Close;
			if (mScroller.isFinished()) {
				mScroller.startScroll(0, mMeasuredHeight, 0, -mMeasuredHeight, mAnimTime);
			} else {
				removeAnims();
				int currY = mScroller.getCurrY();
				mScroller.abortAnimation();
				mScroller.startScroll(0, currY, 0, -currY, mAnimTime * currY / mMeasuredHeight);
			}
			post(mVerticalAnim);
		}
	}
	
	private void horizontalClose() {
		if (mMeasuredWidth > 0) {
			if (state == State.Close) {
				return;
			}
			state = State.Close;
			if (mScroller.isFinished()) {
				mScroller.startScroll(mMeasuredWidth, 0, -mMeasuredWidth, 0, mAnimTime);
			} else {
				removeAnims();
				int currX = mScroller.getCurrX();
				mScroller.abortAnimation();
				mScroller.startScroll(currX, 0, -currX, 0, mAnimTime * currX / mMeasuredWidth);
			}
			post(mHorizontalAnim);
		}
	}
	
	public void open() {
		mIsFirstLayout = false;
		switch (mCollapseOrientation) {
		case Vertical:
			verticalOpen();
			break;
		case Horizontal:
			horizontalOpen();
			break;
		}
	}
	
	private void verticalOpen() {
		if (mMeasuredHeight > 0) {
			if (state == State.Open) {
				return;
			}
			state = State.Open;
			if (mScroller.isFinished()) {
				mScroller.startScroll(0, 0, 0, mMeasuredHeight, mAnimTime);
			} else {
				removeAnims();
				int currY = mScroller.getCurrY();
				mScroller.abortAnimation();
				mScroller.startScroll(0, currY, 0, mMeasuredHeight - currY, mAnimTime * (mMeasuredHeight - currY) / mMeasuredHeight);
			}
			post(mVerticalAnim);
		}
	}
	
	private void horizontalOpen() {
		if (mMeasuredWidth > 0) {
			if (state == State.Open) {
				return;
			}
			state = State.Open;
			if (mScroller.isFinished()) {
				mScroller.startScroll(0, 0, mMeasuredWidth, 0, mAnimTime);
			} else {
				removeAnims();
				int currX = mScroller.getCurrX();
				mScroller.abortAnimation();
				mScroller.startScroll(currX, 0, mMeasuredWidth - currX, 0, mAnimTime * (mMeasuredWidth - currX) / mMeasuredWidth);
			}
			post(mHorizontalAnim);
		}
	}
	
	private void removeAnims() {
		removeCallbacks(mHorizontalAnim);
		removeCallbacks(mVerticalAnim);
	}
	
	public enum CollapseMode {
		PullOut,
		LayDown;
	}
	
	public enum Orientation {
		Vertical,
		Horizontal;
	}
	
	public enum State {
		Open,
		Close;
	}
	
	public interface CollapseListener {
		void onOpenComplete();
		void onCloseComplete();
	}
}
