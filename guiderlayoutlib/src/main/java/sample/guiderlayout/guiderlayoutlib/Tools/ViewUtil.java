package sample.guiderlayout.guiderlayoutlib.Tools;

import android.graphics.Matrix;
import android.view.View;

/**
 * Created by jiajiewang on 16/8/12.
 */
public class ViewUtil {

    public static Matrix computeViewFrameMatrix(View view) {
        int w = view.getWidth();
        int h = view.getHeight();
        float[] m1 = new float[]{
                0, w - 1, 0, 0, 0, h - 1, 1, 1, 1,
        };
        Matrix ml = new Matrix();
        ml.setValues(m1);
        return ml;

    }

    public static Matrix computeScreenFrameMatrix(View view) {
        int orgL = view.getLeft();
        int orgT = view.getTop();
        int orgR = view.getRight();
        int orgB = view.getBottom();
        float w = view.getWidth();
        float h = view.getHeight();
        float[] lt = new float[]{0, 0};
        float[] rt = new float[]{w - 1, 0};
        float[] lb = new float[]{0, h - 1};


        Matrix v2pMat = new Matrix(view.getMatrix());
        v2pMat.postTranslate(view.getLeft(), view.getTop());
        v2pMat.mapPoints(lt);
        v2pMat.mapPoints(rt);
        v2pMat.mapPoints(lb);

        int[] slt = new int[2];
        int[] srt = new int[2];
        int[] slb = new int[2];

        view.layout(Math.round(lt[0]),
                Math.round(lt[1]),
                Math.round(lt[0]), Math.round(lt[1]));
        view.getLocationOnScreen(slt);

        view.layout(Math.round(rt[0]),
                Math.round(rt[1]),
                Math.round(rt[0]), Math.round(rt[1]));
        view.getLocationOnScreen(srt);

        view.layout(Math.round(lb[0]),
                Math.round(lb[1]),
                Math.round(lb[0]), Math.round(lb[1]));
        view.getLocationOnScreen(slb);

        Matrix screenMat = new Matrix();
        float[] screenMatValue = new float[]{
                slt[0], srt[0], slb[0], slt[1], srt[1], slb[1], 1, 1, 1
        };
        screenMat.setValues(screenMatValue);
        view.layout(orgL, orgT, orgR, orgB);
        return screenMat;
    }

    public static Matrix computeV2SFrameMatrix(View view) {
        Matrix viewMat = computeViewFrameMatrix(view);
        Matrix screenMat = computeScreenFrameMatrix(view);

        Matrix inViewMat = new Matrix();
        Matrix v2sMat = new Matrix(screenMat);
        viewMat.invert(inViewMat);
        v2sMat.preConcat(inViewMat);
        return v2sMat;
    }
}
