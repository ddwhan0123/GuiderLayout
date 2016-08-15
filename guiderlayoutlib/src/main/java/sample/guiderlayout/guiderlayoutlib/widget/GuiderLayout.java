package sample.guiderlayout.guiderlayoutlib.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import sample.guiderlayout.guiderlayoutlib.Tools.ViewUtil;

/**
 * Created by jiajiewang on 16/8/12.
 */
public class GuiderLayout extends RelativeLayout {

    public static final int CLTP_NONE = 0;
    public static final int CLTP_RECT = 1;
    public static final int CLTP_CIRCLE = 2;

    private String mCurrTag = null;
    private Matrix mViewMatrix = null;
    private Matrix mScreenMatrix = null;
    private Matrix mGuiderToScreenMatrix = null;
    private Matrix mGuiderMatrix = null;
    private Matrix mViewToGuiderMatrix = null;

    //默认没有
    private int mClipMode = CLTP_NONE;
    private Path mClipPath = null;
    private List<View> mCurrViews = new ArrayList<>();


    private ArrayList<KV> mTagViewList = new ArrayList<>();

    private static class KV {
        public String tag;
        public View view;

        public KV(String tag, View view) {
            this.tag = tag;
            this.view = view;
        }
    }

    public GuiderLayout(Context context) {
        super(context);
        init();
    }

    public GuiderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GuiderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        this.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                view.removeOnLayoutChangeListener(this);
                updateTagList();
                updateGuiderToScreenMatrix();

            }
        });
    }

    //处理 Tag标签
    private void updateTagList() {
        mTagViewList.clear();
        int c = this.getChildCount();
        for (int k = 0; k < c; k++) {
            View child = this.getChildAt(k);
            if (child.getTag() == null) {
                continue;
            }
            if (child.getTag() instanceof String) {
                String tag = (String) child.getTag();
                String[] subTags = tag.split(",");
                if (subTags.length > 0) {
                    child.setTag(subTags);
                    mTagViewList.add(new KV(subTags[0], child));
                }
            }
        }
        updateCurrView();
    }


    private void updateScreenMatrix(View view) {
        mViewMatrix = ViewUtil.computeViewFrameMatrix(view);
        mScreenMatrix = ViewUtil.computeScreenFrameMatrix(view);
        updateGuiderMat();
    }

    private void updateGuiderToScreenMatrix() {
        mGuiderToScreenMatrix = ViewUtil.computeV2SFrameMatrix(this);
        updateGuiderMat();
    }

    private void updateGuiderMat() {
        if (mScreenMatrix == null || mGuiderToScreenMatrix == null) {
            return;
        }

        Matrix screenToGuiderMatrix = new Matrix();
        mGuiderToScreenMatrix.invert(screenToGuiderMatrix);
        mGuiderMatrix = new Matrix(mScreenMatrix);
        mGuiderMatrix.postConcat(screenToGuiderMatrix);

        Matrix m = new Matrix();
        mViewMatrix.invert(m);
        mViewToGuiderMatrix = new Matrix(mGuiderMatrix);
        mViewToGuiderMatrix.preConcat(m);

        updateClipPath();
        updateCurrViewLayout();
    }


    private void perFormUpdateLayout() {
        if (mCurrViews.size() == 0 || mGuiderMatrix == null) {
            return;
        }

        for (View view : mCurrViews) {
            String[] subTags = (String[]) view.getTag();
            float[] guiderMatrixValues = new float[9];
            mGuiderMatrix.getValues(guiderMatrixValues);
            int l = Math.round(guiderMatrixValues[0]);
            int r = Math.round(guiderMatrixValues[1]);
            int t = Math.round(guiderMatrixValues[3]);
            int b = Math.round(guiderMatrixValues[5]);
            int v1 = view.getLeft();
            int vh = view.getHeight();

            if (subTags[1].equals("below")) {
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view.getLayoutParams();
                lp.leftMargin = v1;
                lp.topMargin = b;
                view.setLayoutParams(lp);
            } else if (subTags[1].equals("above")) {
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view.getLayoutParams();
                lp.leftMargin = v1;
                lp.topMargin = t - vh;
                view.setLayoutParams(lp);
            } else if (subTags[1].equals("right-top")) {
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view.getLayoutParams();
                lp.leftMargin = r;
                lp.topMargin = t - vh;
                view.setLayoutParams(lp);
            }
        }
    }

    //画圆
    private void updateClipPath() {
        if (mViewMatrix == null || mViewToGuiderMatrix == null) {
            return;
        }
        float[] values = new float[9];
        mViewMatrix.getValues(values);
        RectF rectF = new RectF(values[0], values[3], values[1], values[5]);
        Path path = new Path();
        if (mClipMode == CLTP_RECT) {
            path.addRect(rectF, Path.Direction.CCW);
        } else if (mClipMode == CLTP_CIRCLE) {
            path.addCircle((rectF.left + rectF.right) * 0.5f, (rectF.top + rectF.bottom) * 0.5f,
                    (rectF.right - rectF.left+40) * 0.5f, Path.Direction.CCW);
        }
        path.transform(mViewToGuiderMatrix);
        mClipPath = path;
    }

    @Override
    public void draw(Canvas canvas) {
        if (mClipPath != null) {
            canvas.clipPath(mClipPath, Region.Op.DIFFERENCE);
        }
        super.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    private OnLayoutChangeListener onLayoutChangeListener = new OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
            removeOnLayoutChangeListener(this);
            perFormUpdateLayout();
        }
    };

    private void updateCurrViewLayout() {
        if (mCurrViews.size() == 0 || mGuiderMatrix == null) {
            return;

        }
        for (View view : mCurrViews) {
            view.setVisibility(View.VISIBLE);
        }
        perFormUpdateLayout();
    }

    private void updateCurrView() {
        List<View> lastViews = new ArrayList<>(mCurrViews);
        mCurrViews.clear();

        if (mCurrTag != null) {
            for (KV kv : mTagViewList) {
                if (kv.tag.equals(mCurrTag)) {
                    mCurrViews.add(kv.view);
                }
            }
        }

        for (View lastView : lastViews) {
            if (!mCurrViews.contains(lastView)) {
                lastView.setVisibility(View.INVISIBLE);
            }
        }
        updateCurrViewLayout();
    }

    private void updateCurrTag(String tag) {
        mCurrTag = tag;
        updateCurrView();
    }

    public void showNoGuide() {
        for (View v : mCurrViews) {
            v.setVisibility(View.INVISIBLE);
        }
        mCurrTag = null;
        mCurrViews.clear();
        mScreenMatrix = null;
        mGuiderMatrix = null;
        mViewMatrix = null;
        mViewToGuiderMatrix = null;
        mClipPath = null;
        this.invalidate();
    }

    public void hideGuider() {
        showNoGuide();
        this.setVisibility(View.INVISIBLE);
    }

    public void showGuider(View view, final String tag, int clipMode) {
        if (view == null) {
            showNoGuide();
            return;
        }
        mClipMode = clipMode;
        setVisibility(View.VISIBLE);
        updateCurrTag(tag);
        if (!view.isLayoutRequested()) {
            updateScreenMatrix(view);
        } else {
            view.addOnLayoutChangeListener(new OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                    view.removeOnLayoutChangeListener(this);
                    updateScreenMatrix(view);
                }
            });
        }

    }

}
