package com.summer.demo.ui.mine.release.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.text.method.ArrowKeyMovementMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.demo.ui.mine.release.bean.SubjectInfo;
import com.summer.helper.listener.OnReturnObjectClickListener;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiastars on 2017/10/17.
 */

public class TagGroup extends ViewGroup {
    private final int default_border_color = Color.rgb(0x49, 0xC1, 0x20);
    private final int default_text_color = Color.rgb(0x49, 0xC1, 0x20);
    private final int default_background_color = Color.WHITE;
    private final int default_dash_border_color = Color.rgb(0xAA, 0xAA, 0xAA);
    private final int default_input_hint_color = Color.argb(0x80, 0x00, 0x00, 0x00);
    private final int default_input_text_color = Color.argb(0xDE, 0x00, 0x00, 0x00);
    private final int default_checked_border_color = Color.rgb(0x49, 0xC1, 0x20);
    private final int default_checked_text_color = Color.WHITE;
    private final int default_checked_marker_color = Color.WHITE;
    private final int default_checked_background_color = Color.rgb(0x49, 0xC1, 0x20);
    private final int default_pressed_background_color = Color.rgb(0xED, 0xED, 0xED);
    private final float default_border_stroke_width;
    private final float default_text_size;
    private final float default_horizontal_spacing;
    private final float default_vertical_spacing;
    private final float default_horizontal_padding;
    private final float default_vertical_padding;

    /**
     * Indicates whether this TagGroup is set up to APPEND mode or DISPLAY mode. Default is false.
     */
    private boolean isAppendMode;

    /**
     * The text to be displayed when the text of the INPUT tag is empty.
     */
    private CharSequence inputHint;

    /**
     * The tag outline border color.
     */
    private int borderColor;

    /**
     * The tag text color.
     */
    private int textColor;

    /**
     * The tag background color.
     */
    private int backgroundColor;

    /**
     * The dash outline border color.
     */
    private int dashBorderColor;

    /**
     * The  input tag hint text color.
     */
    private int inputHintColor;

    /**
     * The input tag type text color.
     */
    private int inputTextColor;

    /**
     * The checked tag outline border color.
     */
    private int checkedBorderColor;

    /**
     * The check text color
     */
    private int checkedTextColor;

    /**
     * The checked marker color.
     */
    private int checkedMarkerColor;

    /**
     * The checked tag background color.
     */
    private int checkedBackgroundColor;

    /**
     * The tag background color, when the tag is being pressed.
     */
    private int pressedBackgroundColor;

    /**
     * The tag outline border stroke width, default is 0.5dp.
     */
    private float borderStrokeWidth;

    /**
     * The tag text size, default is 13sp.
     */
    private float textSize;

    /**
     * The horizontal tag spacing, default is 8.0dp.
     */
    private int horizontalSpacing;

    /**
     * The vertical tag spacing, default is 4.0dp.
     */
    private int verticalSpacing;

    /**
     * The horizontal tag padding, default is 12.0dp.
     */
    private int horizontalPadding;

    /**
     * The vertical tag padding, default is 3.0dp.
     */
    private int verticalPadding;

    /**
     * Listener used to dispatch tag change event.
     */
    private OnTagChangeListener mOnTagChangeListener;

    float deleteViewHeight;

    //是否支持点击
    boolean eanbleClick;

    //设置粗体
    boolean isTextBold;

    List<SubjectInfo> mTags;

    int mNormalBackground = R.drawable.so_grey93_45;
    int mSelectedBackground = R.drawable.so_greyc5_45;

    private int mTextNormalColor = Color.parseColor("#4A4A4A");
    private int mTextSelectedColor = Color.parseColor("#C5C4CA");

    //是否是横向模式
    boolean isHorizontalMode = false;

    private View titleView;//标题view，排在第一个

    /**
     * Listener used to dispatch tag click event.
     */
    private OnTagClickListener mOnTagClickListener;

    OnReturnObjectClickListener onObjectListener;

    private int mShowRows = Integer.MAX_VALUE;//可显示的行数

    boolean isAppendTagMark = true;//是否前后加#
    boolean shouldChangeStatus = true;//点击是否更改状态

    int viewHeight,viewWidth;

    public TagGroup(Context context) {
        this(context, null);
    }

    public TagGroup(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.tagGroupStyle);
    }

    public TagGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        default_border_stroke_width = dp2px(0.5f);
        default_text_size = sp2px(13.0f);
        default_horizontal_spacing = dp2px(0f);
        default_vertical_spacing = dp2px(0f);
        default_horizontal_padding = dp2px(2.0f);
        default_vertical_padding = dp2px(3.0f);
        deleteViewHeight = dp2px(7.5f);

        // Load styled attributes.
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TagGroup, defStyleAttr, R.style.TagGroup);
        try {
            isAppendMode = a.getBoolean(R.styleable.TagGroup_atg_isAppendMode, false);
            eanbleClick = a.getBoolean(R.styleable.TagGroup_atg_enable, true);
            inputHint = a.getText(R.styleable.TagGroup_atg_inputHint);
            borderColor = a.getColor(R.styleable.TagGroup_atg_borderColor, default_border_color);
            textColor = a.getColor(R.styleable.TagGroup_atg_textColor, default_text_color);
            backgroundColor = a.getColor(R.styleable.TagGroup_atg_backgroundColor, default_background_color);
            dashBorderColor = a.getColor(R.styleable.TagGroup_atg_dashBorderColor, default_dash_border_color);
            inputHintColor = a.getColor(R.styleable.TagGroup_atg_inputHintColor, default_input_hint_color);
            inputTextColor = a.getColor(R.styleable.TagGroup_atg_inputTextColor, default_input_text_color);
            checkedBorderColor = a.getColor(R.styleable.TagGroup_atg_checkedBorderColor, default_checked_border_color);
            checkedTextColor = a.getColor(R.styleable.TagGroup_atg_checkedTextColor, default_checked_text_color);
            checkedMarkerColor = a.getColor(R.styleable.TagGroup_atg_checkedMarkerColor, default_checked_marker_color);
            checkedBackgroundColor = a.getColor(R.styleable.TagGroup_atg_checkedBackgroundColor, default_checked_background_color);
            pressedBackgroundColor = a.getColor(R.styleable.TagGroup_atg_pressedBackgroundColor, default_pressed_background_color);
            borderStrokeWidth = a.getDimension(R.styleable.TagGroup_atg_borderStrokeWidth, default_border_stroke_width);
            textSize = a.getDimension(R.styleable.TagGroup_atg_textSize, default_text_size);
            horizontalSpacing = (int) a.getDimension(R.styleable.TagGroup_atg_horizontalSpacing, default_horizontal_spacing);
            verticalSpacing = (int) a.getDimension(R.styleable.TagGroup_atg_verticalSpacing, default_vertical_spacing);
            horizontalPadding = (int) a.getDimension(R.styleable.TagGroup_atg_horizontalPadding, default_horizontal_padding);
            verticalPadding = (int) a.getDimension(R.styleable.TagGroup_atg_verticalPadding, default_vertical_padding);
        } finally {
            a.recycle();
        }

        if (isAppendMode) {
            // Append the initial INPUT tag.
            appendInputTag();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int width = 0;
        int height = 0;

        int row = 0; // The row counter.
        int rowWidth = 0; // Calc the current row width.
        int rowMaxHeight = 0; // Calc the max tag height, in current row.

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            final int childWidth = child.getMeasuredWidth();
            final int childHeight = child.getMeasuredHeight();
            if (height == 0) {
                height = childHeight;
            }
            if (child.getVisibility() != GONE) {
                rowWidth += childWidth;

                if (rowWidth > widthSize && !isHorizontalMode) { // Next line.

                    rowWidth = childWidth; // The next row width.
                    height += rowMaxHeight + verticalSpacing;
                    rowMaxHeight = childHeight; // The next row max height.
                    if (++row == mShowRows)
                        break;
                } else { // This line.
                    rowMaxHeight = Math.max(rowMaxHeight, childHeight);
                }
                rowWidth += horizontalSpacing;
            }
            viewWidth = childWidth;
        }
     /*   if (addMoreRow && getTags() != null && getTags().size() < 5) {
            height += SUtils.getDip(getContext(), 20);
        }*/
        // Account for the last row height.

        // Account for the padding too.
        height += getPaddingTop() + getPaddingBottom();
        viewHeight = height;
        // If the tags grouped in one row, set the width to wrap the tags.
        if (row == 0) {
            width = rowWidth;
            width += getPaddingLeft() + getPaddingRight();
        } else {// If the tags grouped exceed one line, set the width to match the parent.
            width = widthSize;
        }

        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize : width,
                heightMode == MeasureSpec.EXACTLY ? heightSize : height);
    }

    boolean addMoreRow;

    public void setAddMoreRow() {
        addMoreRow = true;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int parentLeft = getPaddingLeft();
        final int parentRight = r - l - getPaddingRight();
        final int parentTop = getPaddingTop();
        final int parentBottom = b - t - getPaddingBottom();

        int childLeft = 0;
        int childTop = parentTop;

        int rowMaxHeight = 0;

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            final int width = child.getMeasuredWidth();
            final int height = child.getMeasuredHeight();

            if (child.getVisibility() != GONE) {
                if (childLeft + width > parentRight) { // Next line
                    childLeft = parentLeft;
                    childTop += rowMaxHeight + verticalSpacing;
                    rowMaxHeight = height;
                } else {
                    rowMaxHeight = Math.max(rowMaxHeight, height);
                }
                child.layout(childLeft, childTop, childLeft + width, childTop + height);

                childLeft += width + horizontalSpacing;
            }
        }
    }

    /**
     * Returns the INPUT tag view in this group.
     *
     * @return the INPUT state tag view or null if not exists
     */
    protected TagView getInputTag() {
        if (isAppendMode) {
            final int inputTagIndex = getChildCount() - 1;
            final TagView inputTag = getTagAt(inputTagIndex);
            if (inputTag != null && inputTag.mState == TagView.STATE_INPUT) {
                return inputTag;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Return the last NORMAL state tag view in this group.
     *
     * @return the last NORMAL state tag view or null if not exists
     */
    protected TagView getLastNormalTagView() {
        final int lastNormalTagIndex = isAppendMode ? getChildCount() - 2 : getChildCount() - 1;
        TagView lastNormalTagView = getTagAt(lastNormalTagIndex);
        return lastNormalTagView;
    }

    public void setTitleView(View view) {
        if (titleView != null) {
            removeView(titleView);
        }
        titleView = view;
        addView(view, 0);
    }

    public void removeTitleView() {
        if (titleView != null) {
            removeView(titleView);
            titleView = null;
        }
    }

    public void setTags(List<SubjectInfo> tags) {
        if (titleView == null) {
            removeAllViews();
        } else {
            removeViews(1, getChildCount() - 1);
        }
        if (tags == null) {
            return;
        }
        this.mTags = tags;
        for (SubjectInfo tag : tags) {
            appendTag(tag);
        }

        if (isAppendMode) {
            appendInputTag();
        }
    }

    public List<SubjectInfo> getTags() {
        return mTags;
    }

    public void setTagTitles(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return;
        }
        List<SubjectInfo> tagInfos = new ArrayList<>();
        for (String str : tags) {
            SubjectInfo info = new SubjectInfo();
            info.setTitle(str);
            tagInfos.add(info);
        }
        setTags(tagInfos);
    }

    public void setTagTitles(String[] tags) {
        if (tags == null) {
            return;
        }
        List<SubjectInfo> tagInfos = new ArrayList<>();
        for (String str : tags) {
            SubjectInfo info = new SubjectInfo();
            info.setTitle(str);
            tagInfos.add(info);
        }
        setTags(tagInfos);
    }


    /**
     * Returns the tag view at the specified position in the group.
     *
     * @param index the position at which to get the tag view from.
     * @return the tag view at the specified position or null if the position
     * does not exists within this group.
     */
    protected TagView getTagAt(int index) {
        return (TagView) getChildAt(index);
    }

    /**
     * Returns the checked tag view in the group.
     *
     * @return the checked tag view or null if not exists.
     */
    protected TagView getCheckedTag() {
        final int checkedTagIndex = getCheckedTagIndex();
        if (checkedTagIndex != -1) {
            return getTagAt(checkedTagIndex);
        }
        return null;
    }

    /**
     * Return the checked tag index.
     *
     * @return the checked tag index, or -1 if not exists.
     */
    protected int getCheckedTagIndex() {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final TagView tag = getTagAt(i);
            if (tag.isChecked) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Register a callback to be invoked when this tag group is changed.
     *
     * @param l the callback that will run
     */
    public void setOnTagChangeListener(OnTagChangeListener l) {
        mOnTagChangeListener = l;
    }

    protected void appendInputTag() {
        appendInputTag(null);
    }

    protected void appendInputTag(SubjectInfo tag) {
        final TagView previousInputTag = getInputTag();
        if (previousInputTag != null) {
            throw new IllegalStateException("Already has a INPUT tag in group.");
        }
        int index = titleView == null ? getChildCount() : getChildCount() - 1;
        final TagView newInputTag = new TagView(getContext(), TagView.STATE_INPUT, tag, index);
        addView(newInputTag);
    }

    /**
     * Append tag to this group.
     *
     * @param tag the tag to append.
     */
    protected void appendTag(SubjectInfo tag) {
        int index = titleView == null ? getChildCount() : getChildCount() - 1;
        final TagView newTag = new TagView(getContext(), TagView.STATE_NORMAL, tag, index);
        addView(newTag);
    }

    public float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    public float sp2px(float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                getResources().getDisplayMetrics());
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new TagGroup.LayoutParams(getContext(), attrs);
    }

    /**
     * 设置显示的行数
     *
     * @param rows
     */
    public void setShowRows(int rows) {
        if (mShowRows != rows)
            mShowRows = rows;
        requestLayout();
    }

    public void setTextNormalColor(int color) {
        mTextNormalColor = color;
    }

    public void setTextSelectedColor(int color) {
        mTextSelectedColor = color;
    }

    /**
     * 设置统一的默认背景
     *
     * @param resBg
     */
    public void setNormalBackground(int resBg) {
        this.mNormalBackground = resBg;
    }

    /**
     * 设置统一的选中背景
     *
     * @param resBg
     */
    public void setSelectedBackground(int resBg) {
        this.mSelectedBackground = resBg;
    }

    public int getVerticalPadding() {
        return verticalPadding;
    }

    public void setVerticalPadding(int verticalPadding) {
        this.verticalPadding = verticalPadding;
    }

    public void addOnClickListener(OnReturnObjectClickListener listener) {
        this.onObjectListener = listener;
    }

    /**
     * Register a callback to be invoked when a tag is clicked.
     *
     * @param l the callback that will run.
     */
    public void setOnTagClickListener(OnTagClickListener l) {
        mOnTagClickListener = l;
    }

    public void setHorizontalMode(boolean isHorizontalMode) {
        this.isHorizontalMode = isHorizontalMode;
    }

    public boolean isHorizontalMode(){
        return isHorizontalMode;
    }

    public void setTextBold(boolean b) {
        this.isTextBold = b;
    }

    /**
     * Interface definition for a callback to be invoked when a tag group is changed.
     */
    public interface OnTagChangeListener {
        void onDelete(SubjectInfo info);
    }

    /**
     * Interface definition for a callback to be invoked when a tag is clicked.
     */
    public interface OnTagClickListener {
        /**
         * Called when a tag has been clicked.
         *
         * @param tag The tag text of the tag that was clicked.
         */
        void onTagClick(int position, String tag);
    }

    /**
     * Per-child layout information for layouts.
     */
    public static class LayoutParams extends ViewGroup.LayoutParams {
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }
    }

    /**
     * The tag view which has two states can be either NORMAL or INPUT.
     */
    class TagView extends RelativeLayout {
        public static final int STATE_NORMAL = 1;
        public static final int STATE_INPUT = 2;

        /**
         * The offset to the text.
         */
        private static final int CHECKED_MARKER_OFFSET = 3;

        /**
         * The stroke width of the checked marker
         */
        private static final int CHECKED_MARKER_STROKE_WIDTH = 4;

        /**
         * The current state.
         */
        private int mState;

        /**
         * Indicates the tag if checked.
         */
        private boolean isChecked = false;

        /**
         * Indicates the tag if pressed.
         */
        private boolean isPressed = false;

        private Paint mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        private Paint mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        private Paint mCheckedMarkerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        /**
         * The rect for the tag's left corner drawing.
         */
        private RectF mLeftCornerRectF = new RectF();

        /**
         * The rect for the tag's right corner drawing.
         */
        private RectF mRightCornerRectF = new RectF();

        /**
         * The rect for the tag's horizontal blank fill area.
         */
        private RectF mHorizontalBlankFillRectF = new RectF();

        /**
         * The rect for the tag's vertical blank fill area.
         */
        private RectF mVerticalBlankFillRectF = new RectF();

        /**
         * The rect for the checked mark draw bound.
         */
        private RectF mCheckedMarkerBound = new RectF();

        /**
         * Used to detect the touch event.
         */
        private Rect mOutRect = new Rect();

        /**
         * The path for draw the tag's outline border.
         */
        private Path mBorderPath = new Path();

        TextView tvContent;

        /**
         * The path effect provide draw the dash border.
         */
        private PathEffect mPathEffect = new DashPathEffect(new float[]{10, 5}, 0);

        {
            mBorderPaint.setStyle(Paint.Style.STROKE);
            mBorderPaint.setStrokeWidth(borderStrokeWidth);
            mBackgroundPaint.setStyle(Paint.Style.FILL);
            mCheckedMarkerPaint.setStyle(Paint.Style.FILL);
            mCheckedMarkerPaint.setStrokeWidth(CHECKED_MARKER_STROKE_WIDTH);
            mCheckedMarkerPaint.setColor(checkedMarkerColor);
        }

        int borderColor, textColor, backgroundColor;

        public TagView(final Context context, final int state, final SubjectInfo tagInfo, final int index) {
            super(context);
            //this.borderColor = tagInfo.isSelect()  ? context.getResources().getColor(R.color.red_d4) : tagInfo.getBorderColor();
            this.textColor = tagInfo.isSelect() ? context.getResources().getColor(R.color.grey_c5) : context.getResources().getColor(R.color.grey_4a);
            //this.backgroundColor = tagInfo.isSelect() ? context.getResources().getDrawable(R.drawable.so_greycc_45) : context.getResources().getDrawable(R.drawable.so_grey93_45);
            setLayoutParams(new TagGroup.LayoutParams(
                    TagGroup.LayoutParams.WRAP_CONTENT,
                    TagGroup.LayoutParams.WRAP_CONTENT));
            this.setPadding(0, verticalPadding, verticalPadding, 0);
            tvContent = new TextView(context);
            tvContent.setMaxLines(1);
            tvContent.getPaint().setFakeBoldText(isTextBold);
            tvContent.setEllipsize(TextUtils.TruncateAt.END);
            tvContent.setId(R.id.tv_content);
            this.addView(tvContent);
            this.setClipChildren(false);
            this.setClipToPadding(false);
            setGravity(Gravity.CENTER);
            if (isAppendTagMark) {
                tvContent.setText("#" + tagInfo.getTitle() + "#");
            } else {
                tvContent.setText(tagInfo.getTitle());
            }
            tvContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            mState = state;
            tvContent.setHint(state == STATE_INPUT ? inputHint : null);
            tvContent.setMovementMethod(state == STATE_INPUT ? ArrowKeyMovementMethod.getInstance() : null);


            //设置文字自定义大小
            if (tagInfo.getTextSize() != 0) {
                tvContent.setTextSize(tagInfo.getTextSize());
            }
            tvContent.setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding);
            if (tagInfo.isAdd()) {
                tagInfo.setSelect(false);
            }
            if (!tagInfo.isMoreView()) {

            }
            setChecked(tagInfo.isSelect(), tagInfo.isHistory());
            //设置文字自定义颜色
            if (tagInfo.getTextColor() != 0) {
                tvContent.setTextColor(tagInfo.getTextColor());
            }
            //ivDelete.setVisibility(tagInfo.isDeletable() ? View.VISIBLE : View.GONE);
            // Interrupted long click event to avoid PAUSE popup.
            Logs.i("tagInfp:"+tagInfo.isAdd());
            //设置添加标签的样式
            if (tagInfo.isAdd()) {
                ImageView ivAdd = new ImageView(context);
                this.addView(ivAdd);
                SUtils.setPicResource(ivAdd, R.drawable.home_star_add_icon);
                RelativeLayout.LayoutParams addparams = (RelativeLayout.LayoutParams) ivAdd.getLayoutParams();
                addparams.width = SUtils.getDip(context, 12);
                addparams.height = SUtils.getDip(context, 12);
                addparams.leftMargin = (int) (horizontalPadding*1.55f);
                addparams.topMargin = (int) (verticalPadding*1.8);

            }

            setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return state != STATE_INPUT;
                }
            });
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Logs.i(tagInfo.isSelect() + "");
                    if (mOnTagClickListener != null) {
                        if (shouldChangeStatus) {
                            setChecked(tagInfo.isSelect(), tagInfo.isHistory());
                        }
                        mOnTagClickListener.onTagClick(index, tagInfo.getTitle());
                    }

                    if (onObjectListener != null && !tagInfo.isSelect()) {
                        if (!tagInfo.isAdd() && !tagInfo.isMoreView() && shouldChangeStatus) {
                            tagInfo.setSelect(!tagInfo.isSelect());
                        }
                        if (shouldChangeStatus) {
                            setChecked(tagInfo.isSelect(), tagInfo.isHistory());
                        }
                        onObjectListener.onClick(tagInfo);
                    }
                }
            });

        }

        public void setChecked(boolean checked, boolean isHistory) {
            Resources resources = getContext().getResources();
            Logs.i("checked::" + checked + ",,," + isHistory);
            if (checked) {
                tvContent.setTextColor(mTextSelectedColor);
                if (!isHistory) {
                    tvContent.setBackgroundResource(mSelectedBackground);
                }
            } else {

                if (!isHistory) {
                    tvContent.setBackgroundResource(mNormalBackground);
                }
                tvContent.setTextColor(mTextNormalColor);
            }
        }

        public TextView getTextView() {
            return tvContent;
        }

        /**
         * Call this method to end this tag's INPUT state.
         */
        public void endInput() {
            // Make the view not focusable.
            setFocusable(false);
            setFocusableInTouchMode(false);
            // Set the hint empty, make the TextView measure correctly.
            getTextView().setHint(null);
            // Take away the cursor.
            getTextView().setMovementMethod(null);

            mState = STATE_NORMAL;
            requestLayout();
        }

       /* private void invalidatePaint() {
            if (isAppendMode) {
                if (mState == STATE_INPUT) {
                    mBorderPaint.setColor(dashBorderColor);
                    mBorderPaint.setPathEffect(mPathEffect);
                    mBackgroundPaint.setColor(backgroundColor);
                    getTextView().setHintTextColor(inputHintColor);
                } else {
                    mBorderPaint.setPathEffect(null);
                    if (isChecked) {
                        mBorderPaint.setColor(checkedBorderColor);
                        mBackgroundPaint.setColor(checkedBackgroundColor);
                    } else {
                        mBorderPaint.setColor(borderColor);
                    }
                }
            } else {
                mBorderPaint.setColor(borderColor);
                mBackgroundPaint.setColor(backgroundColor);
            }

            if (isPressed) {
                mBackgroundPaint.setColor(pressedBackgroundColor);
            }
        }*/

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            int left = (int) borderStrokeWidth;
            int top = (int) borderStrokeWidth;
            int right = (int) (left + w - borderStrokeWidth * 2);
            int bottom = (int) (top + h - borderStrokeWidth * 2);

            int d = 0;//bottom - top;

            mLeftCornerRectF.set(left, top, left + d, top + d);
            mRightCornerRectF.set(right - d, top, right, top + d);

            mBorderPath.reset();
            //mBorderPath.addArc(mLeftCornerRectF, -180, 90);
            //mBorderPath.addArc(mLeftCornerRectF, -270, 90);
            //mBorderPath.addArc(mRightCornerRectF, -90, 90);
            //mBorderPath.addArc(mRightCornerRectF, 0, 90);

            int l = (int) (d / 2.0f);
            mBorderPath.moveTo(left + l, top);
            mBorderPath.lineTo(right - l, top);

            mBorderPath.moveTo(left + l, bottom);
            mBorderPath.lineTo(right - l, bottom);

            mBorderPath.moveTo(left, top + l);
            mBorderPath.lineTo(left, bottom - l);

            mBorderPath.moveTo(right, top + l);
            mBorderPath.lineTo(right, bottom - l);

            mHorizontalBlankFillRectF.set(left, top + l, right, bottom - l);
            mVerticalBlankFillRectF.set(left + l, top, right - l, bottom);

            int m = (int) (h / 2.5f);
            h = bottom - top;
            mCheckedMarkerBound.set(right - m - horizontalPadding + CHECKED_MARKER_OFFSET,
                    top + h / 2 - m / 2,
                    right - horizontalPadding + CHECKED_MARKER_OFFSET,
                    bottom - h / 2 + m / 2);

            // Ensure the checked mark drawing region is correct across screen orientation changes.
            if (isChecked) {
                setPadding(horizontalPadding,
                        verticalPadding,
                        (int) (horizontalPadding + h / 2.5f + CHECKED_MARKER_OFFSET),
                        verticalPadding);
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (mState == STATE_INPUT) {
                // The INPUT tag doesn't change background color on the touch event.
                return super.onTouchEvent(event);
            }

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    getDrawingRect(mOutRect);
                    isPressed = true;
                    invalidate();
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    if (!mOutRect.contains((int) event.getX(), (int) event.getY())) {
                        isPressed = false;
                        invalidate();
                    }
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    isPressed = false;
                    invalidate();
                    break;
                }
            }
            return super.onTouchEvent(event);
        }

        @Override
        public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
            return new ZanyInputConnection(super.onCreateInputConnection(outAttrs), true);
        }

        /**
         * Solve edit text delete(backspace) key detect, see<a href="http://stackoverflow.com/a/14561345/3790554">
         * Android: Backspace in WebView/BaseInputConnection</a>
         */
        private class ZanyInputConnection extends InputConnectionWrapper {
            public ZanyInputConnection(InputConnection target, boolean mutable) {
                super(target, mutable);
            }

            @Override
            public boolean deleteSurroundingText(int beforeLength, int afterLength) {
                // magic: in latest Android, deleteSurroundingText(1, 0) will be called for backspace
                if (beforeLength == 1 && afterLength == 0) {
                    // backspace
                    return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
                            && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
                }
                return super.deleteSurroundingText(beforeLength, afterLength);
            }
        }
    }

    public void setAppendTagMark(boolean appendTagMark) {
        isAppendTagMark = appendTagMark;
    }

    public void setShouldChangeStatus(boolean shouldChangeStatus) {
        this.shouldChangeStatus = shouldChangeStatus;
    }
}
