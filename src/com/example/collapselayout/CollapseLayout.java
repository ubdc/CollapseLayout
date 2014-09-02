package com.example.collapselayout;

import com.example.expendlayout.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

public class CollapseLayout extends FrameLayout {
	private static final Interpolator DEFAULT_INTERPOLATOR = new AccelerateDecelerateInterpolator();
	private static final int DEFAULT_ANIM_TIME = 600;
	private static final int STATE_NONE = 0;
	public static final int STATE_OPEN = STATE_NONE + 1;
	public static final int STATE_CLOSE = STATE_OPEN + 1;
	private int state = STATE_NONE;
	private Scroller mScroller;
	private int mMeasuredWidth;
	private int mMeasuredHeight;
	private Interpolator mInterpolator;
	private int mAnimTime = DEFAULT_ANIM_TIME;
	private Mode mMode = Mode.PullOut;
	private Orientation mCollapseOrientation = Orientation.VERTICAL;
	private CollapseListener mCollapseListener;
	private boolean isStartWithClosedState;
	
	private Runnable mVerticalAnim = new Runnable() {

		@Override
		public void run() {
			if (mScroller.computeScrollOffset()) {
				android.view.ViewGroup.LayoutParams lp = getLayoutParams();
				lp.width = mMeasuredWidth;
				lp.height = mScroller.getCurrY();
				setLayoutParams(lp);
				if (mMode == Mode.PullOut) {
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
				if (mMode == Mode.PullOut) {
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
		int collapseOrientation = a.getInt(R.styleable.CollapseLayout_collapseOrientation, 0);
		if (collapseOrientation == 0) {
			setCollapseOrientation(Orientation.VERTICAL);
		} else {
			setCollapseOrientation(Orientation.HORIZONTAL);
		}
		int collapseMode = a.getInt(R.styleable.CollapseLayout_collapseMode, 0);
		if (collapseMode == 0) {
			setCollapseMode(Mode.PullOut);
		} else {
			setCollapseMode(Mode.LayDown);
		}
		int initialCollapseState = a.getInt(R.styleable.CollapseLayout_initialCollapseState, 0);
		isStartWithClosedState = (initialCollapseState != 0);
		int collapseDuration = a.getInt(R.styleable.CollapseLayout_collapseDuration, DEFAULT_ANIM_TIME);
		setAnimateDuration(collapseDuration);
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
		if (state == STATE_NONE) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			mMeasuredWidth = getMeasuredWidth();
			mMeasuredHeight = getMeasuredHeight();
			
			if (isStartWithClosedState) {
				if (mCollapseOrientation == Orientation.HORIZONTAL) {
					setMeasuredDimension(0, mMeasuredHeight);
				} else {
					setMeasuredDimension(mMeasuredWidth, 0);
				}
			}
		} else {
			setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
		}
	}
//	
//    @Override
//    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed,
//            int parentHeightMeasureSpec, int heightUsed) {
//        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
//
//        final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
//                getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin
//                        + widthUsed, lp.width);
//        int childHeightMeasureSpec = 0;
//        if (lp.height > 0) {
//        	childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
//                    lp.topMargin + lp.bottomMargin + lp.height, MeasureSpec.EXACTLY);
//        } else {
//        	childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
//                lp.topMargin + lp.bottomMargin, MeasureSpec.UNSPECIFIED);
//        }
//
//        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
//    }
    
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
    
    public void setCollapseMode(Mode mode) {
    	if (mode == null) {
    		mode = Mode.PullOut;
    	}
    	mMode = mode;
    }
    
    public void setCollapseOrientation(Orientation orientation) {
    	if (orientation == null) {
    		orientation = Orientation.VERTICAL;
    	}
    	mCollapseOrientation = orientation;
    }
    
    public void setCollapseListener(CollapseListener listener) {
    	mCollapseListener = listener;
    }
    
    public int getState() {
    	if (isStartWithClosedState) {
    		return STATE_CLOSE;
    	} else {
    		if (state == STATE_NONE) {
    			return STATE_OPEN;
    		} else {
    			return state;
    		}
    	}
	}
    
	public void close() {
		switch (mCollapseOrientation) {
		case VERTICAL:
			verticalClose();
			break;
		case HORIZONTAL:
			horizontalClose();
			break;
		}
	}
	
	private void verticalClose() {
		if (mMeasuredHeight > 0) {
			if (getState() == STATE_CLOSE) {
				return;
			}
			state = STATE_CLOSE;
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
			if (getState() == STATE_CLOSE) {
				return;
			}
			state = STATE_CLOSE;
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
		if (isStartWithClosedState) {
			isStartWithClosedState = false;
			state = STATE_CLOSE;
		}
		switch (mCollapseOrientation) {
		case VERTICAL:
			verticalOpen();
			break;
		case HORIZONTAL:
			horizontalOpen();
			break;
		}
	}
	
	private void verticalOpen() {
		if (mMeasuredHeight > 0) {
			if (getState() == STATE_OPEN) {
				return;
			}
			state = STATE_OPEN;
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
			if (getState() == STATE_OPEN) {
				return;
			}
			state = STATE_OPEN;
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
	
	public enum Mode {
		LayDown,
		PullOut
	}
	
	public enum Orientation {
		HORIZONTAL,
		VERTICAL
	}
	
	public interface CollapseListener {
		void onOpenComplete();
		void onCloseComplete();
	}
}
