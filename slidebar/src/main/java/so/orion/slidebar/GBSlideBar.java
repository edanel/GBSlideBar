package so.orion.slidebar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;


/**
 * 项目名称：GBSlideBar
 * 类描述：
 * 创建人：Edanel
 * 创建时间：16/1/14 下午4:48
 * 修改人：Edanel
 * 修改时间：16/1/14 下午4:48
 * 修改备注：
 */
public class GBSlideBar extends View {

    private static final String TAG = "edanelx";

    private RectF mBackgroundPaddingRect;
    private Drawable mBackgroundDrawable;
    private boolean mFirstDraw = true;
    private GBSlideBarAdapter mAdapter;
    private int[][] mAnchor;
    private boolean mModIsHorizontal = true;
    private int mCurrentX, mCurrentY, mPivotX, mPivotY;
    private boolean mSlide = false;

    private static final int[] STATE_NORMAL = new int[]{};
    private static final int[] STATE_SELECTED = new int[]{android.R.attr.state_selected};
    private static final int[] STATE_PRESS = new int[]{android.R.attr.state_pressed};
    private int[] mState = STATE_SELECTED;
    private int mCurrentItem;

    private int mAnchorWidth, mAnchorHeight;

    private int mPlaceHolderWidth, mPlaceHolderHeight;
    private int mTextMargin;
    private int mType;

    private Paint mPaint;
    private int mTextSize;
    private int mTextColor;

    private int mLastX;
    private int mSlideX, mSlideY;

    private int mAbsoluteY;

    private int mSelectedX;
    private boolean mIsStartAnimation = false, mIsEndAnimation = false;
    private ValueAnimator mStartAnim, mEndAnim;
    private boolean mIsFirstSelect = true, mCanSelect = true;

    private GBSlideBarListener gbSlideBarListener;


    public GBSlideBar(Context context) {
        super(context);
        init(null, 0);
    }

    public GBSlideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public GBSlideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attributeSet, int defStyleAttr) {
        mBackgroundPaddingRect = new RectF();
        TypedArray a = getContext().obtainStyledAttributes(attributeSet, R.styleable.GBSlideBar, defStyleAttr, 0);
        mBackgroundPaddingRect.left = a.getDimension(R.styleable.GBSlideBar_gbs_paddingLeft, 0.0f);
        mBackgroundPaddingRect.top = a.getDimension(R.styleable.GBSlideBar_gbs_paddingTop, 0.0f);
        mBackgroundPaddingRect.right = a.getDimension(R.styleable.GBSlideBar_gbs_paddingRight, 0.0f);
        mBackgroundPaddingRect.bottom = a.getDimension(R.styleable.GBSlideBar_gbs_paddingBottom, 0.0f);

        mAnchorWidth = (int) a.getDimension(R.styleable.GBSlideBar_gbs_anchor_width, 50.0f);
        mAnchorHeight = (int) a.getDimension(R.styleable.GBSlideBar_gbs_anchor_height, 50.0f);

        mPlaceHolderWidth = (int) a.getDimension(R.styleable.GBSlideBar_gbs_placeholder_width, 20.0f);
        mPlaceHolderHeight = (int) a.getDimension(R.styleable.GBSlideBar_gbs_placeholder_height, 20.0f);

        mType = a.getInt(R.styleable.GBSlideBar_gbs_type, 1);

        mBackgroundDrawable = a.getDrawable(R.styleable.GBSlideBar_gbs_background);

        mTextSize = a.getDimensionPixelSize(R.styleable.GBSlideBar_gbs_textSize, 28);
        mTextColor = a.getColor(R.styleable.GBSlideBar_gbs_textColor, Color.BLACK);

        mTextMargin = (int) a.getDimension(R.styleable.GBSlideBar_gbs_text_margin, 0.0f);
        a.recycle();
    }

    private void drawBackground() {

        Rect rect = new Rect((int) mBackgroundPaddingRect.left + mAnchorWidth,
                (int) mBackgroundPaddingRect.top,
                (int) (getWidth() - mBackgroundPaddingRect.right - mAnchorWidth),
                (int) (getHeight() - mBackgroundPaddingRect.bottom));
        mBackgroundDrawable.setBounds(rect);

        mAbsoluteY = (int) (mBackgroundPaddingRect.top - mBackgroundPaddingRect.bottom);

        Log.d(TAG, "mAbsoluteY:" + mBackgroundPaddingRect.top + " : " + mBackgroundPaddingRect.bottom + " : " + (mBackgroundPaddingRect.top - mBackgroundPaddingRect.bottom));

        mCurrentX = mPivotX = getWidth() / 2;
        mCurrentY = mPivotY = getHeight() / 2;

        int widthBase = rect.width() / getCount();
        int widthHalf = widthBase / 2;
        int heightBase = rect.height() / getCount();
        int heightHalf = heightBase / 2;


        mAnchor = new int[getCount()][2];
        for (int i = 0, j = 1; i < getCount(); i++, j++) {
//            if (mType == 1) {
            if (i == 0) {
                mAnchor[i][0] = mModIsHorizontal ? rect.left : mPivotX;
            } else if (i == getCount() - 1) {
                mAnchor[i][0] = mModIsHorizontal ? rect.right : mPivotX;
            } else {
                mAnchor[i][0] = mModIsHorizontal ? widthBase * j - widthHalf + rect.left : mPivotX;
            }
            mAnchor[i][1] = !mModIsHorizontal ? heightBase * j - heightHalf + rect.top : mPivotY + mAbsoluteY / 2;
//            }

        }

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(mTextColor);
        mPaint.setTextAlign(Paint.Align.CENTER);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mFirstDraw) drawBackground();
        if (mBackgroundDrawable != null) mBackgroundDrawable.draw(canvas);
        if (isInEditMode()) return;

        Drawable itemDefault, itemSlide;
        StateListDrawable stateListDrawable;

        if (!mSlide) {
            int distance, minIndex = 0, minDistance = Integer.MAX_VALUE;
            for (int i = 0; i < getCount(); i++) {
                distance = Math.abs(mModIsHorizontal ? mAnchor[i][0] - mCurrentX : mAnchor[i][1] - mCurrentY);
                if (minDistance > distance) {
                    minIndex = i;
                    minDistance = distance;
                }
            }

            setCurrentItem(minIndex);
            stateListDrawable = mAdapter.getItem(minIndex);


        } else {
            mSlide = false;
            mCurrentX = mAnchor[mCurrentItem][0];
            mCurrentY = mAnchor[mCurrentItem][1];
            if (mFirstDraw) {
                mSlideX = mLastX = mCurrentX;
            }
            stateListDrawable = mAdapter.getItem(mCurrentItem);

            mIsFirstSelect = true;


        }
        stateListDrawable.setState(mState);
        itemDefault = stateListDrawable.getCurrent();


        for (int i = 0; i < getCount(); i++) {
            if (i == mCurrentItem) {
//                continue; //
                mPaint.setColor(mAdapter.getTextColor(mCurrentItem));
                canvas.drawText(mAdapter.getText(i), mAnchor[i][0], mAnchor[i][1] + mAnchorHeight * 3 / 2 + mTextMargin, mPaint);
            }else {
                mPaint.setColor(mTextColor);
                canvas.drawText(mAdapter.getText(i), mAnchor[i][0], mAnchor[i][1] + mAnchorHeight * 3 / 2 + mTextMargin, mPaint);
            }
            stateListDrawable = mAdapter.getItem(i);
            stateListDrawable.setState(STATE_NORMAL);
            itemSlide = stateListDrawable.getCurrent();
            itemSlide.setBounds(
                    mAnchor[i][0] - mPlaceHolderWidth,
                    mAnchor[i][1] - mPlaceHolderHeight,
                    mAnchor[i][0] + mPlaceHolderWidth,
                    mAnchor[i][1] + mPlaceHolderHeight
            );
            itemSlide.draw(canvas);

        }


        itemDefault.setBounds(
                mSlideX - mAnchorWidth,
                mPivotY + mAbsoluteY / 2 - mAnchorHeight,
                mSlideX + mAnchorWidth,
                mPivotY + mAbsoluteY / 2 + mAnchorHeight
        );

        itemDefault.draw(canvas);

        setFirstDraw(false);

    }


    private void endSlide() {
        if (mIsEndAnimation == false && mSlide) {
            mIsEndAnimation = true;
            mEndAnim = ValueAnimator.ofFloat(0.0f, 1.0f);
            mEndAnim.setDuration(200);
            mEndAnim.setInterpolator(new LinearInterpolator());
            mEndAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mSlideX = (int) ((mCurrentX - mLastX) * animation.getAnimatedFraction() + mLastX);
                    mSlideY = (int) (mCurrentY * animation.getAnimatedFraction());
                    invalidate();
                }
            });
            mEndAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mIsStartAnimation = false;
                    mLastX = mCurrentX;
                    mIsEndAnimation = false;
                    mCanSelect = true;
                    invalidate();
                }
            });
            mEndAnim.start();
        } else {

            mLastX = mCurrentX;
            mSlideX = mCurrentX;
            invalidate();
        }
    }

    private void startSlide() {
        if (mIsStartAnimation == false && !mSlide && mCanSelect) {

            mIsStartAnimation = true;
            mStartAnim = ValueAnimator.ofFloat(0.0f, 1.0f);
            mStartAnim.setDuration(200);
            mStartAnim.setInterpolator(new LinearInterpolator());
            mStartAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mSlideX = (int) ((mCurrentX - mLastX) * animation.getAnimatedFraction() + mLastX);
                    mSlideY = (int) (mCurrentY * animation.getAnimatedFraction());

                    invalidate();
                }
            });
            mStartAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {

                    mLastX = mCurrentX;
                    mIsStartAnimation = false;
                    mCanSelect = true;
                    invalidate();
                }
            });
            mStartAnim.start();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mCanSelect) {
            int action = event.getAction();
            //获取当前坐标
            mCurrentX = mModIsHorizontal ? getNormalizedX(event) : mPivotX;
            mCurrentY = !mModIsHorizontal ? (int) event.getY() : mPivotY;

            mSlide = action == MotionEvent.ACTION_UP;

            if (!mSlide && mIsFirstSelect) {
                startSlide();
                mIsFirstSelect = false;

            } else if (mIsStartAnimation == false && mIsEndAnimation == false) {
                endSlide();
            }


            mState = action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL ? STATE_SELECTED : STATE_PRESS;

            switch (action) {
                case MotionEvent.ACTION_MOVE:
//                if (BuildConfig.DEBUG) Log.d(TAG, "Move " + event.getX());
                    return true;
                case MotionEvent.ACTION_DOWN:
                    Log.d(TAG, "Down " + event.getX());
                    return true;
                case MotionEvent.ACTION_UP:
                    Log.d(TAG, "Up " + event.getX());
                    mCanSelect = false;
                    invalidate();
                    return true;
            }
        }

        return super.onTouchEvent(event);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(event);
    }

    private int getNormalizedX(MotionEvent event) {
        return Math.min(Math.max((int) event.getX(), mAnchorWidth), getWidth() - mAnchorWidth);
    }

    private void setFirstDraw(boolean firstDraw) {
        mFirstDraw = firstDraw;
    }

    private int getCount() {
        return isInEditMode() ? 3 : mAdapter.getCount();
    }

    private void setCurrentItem(int currentItem) {
        if (mCurrentItem != currentItem && gbSlideBarListener != null) {
            gbSlideBarListener.onPositionSelected(currentItem);
        }
        mCurrentItem = currentItem;
    }

    public void setAdapter(GBSlideBarAdapter adapter) {
        mAdapter = adapter;
    }

    public void setPosition(int position) {
        position = position < 0 ? 0 : position;
        position = position > mAdapter.getCount() ? mAdapter.getCount() - 1 : position;
        mCurrentItem = position;
        mSlide = true;
        invalidate();
    }

    public void setOnGbSlideBarListener(GBSlideBarListener listener) {
        this.gbSlideBarListener = listener;
    }
}
