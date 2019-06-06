package com.summer.demo.anim;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
/**
 * 拥有四边框的ImageView
 * @author @xiastars@vip.qq.com
 *
 */
public class ReframeView extends View{
	int left;
	int right;
	int top;
	int bottom;
	
	Paint linePaint;
	
	
	public ReframeView(Context context) {
		super(context);
	}
	
	public ReframeView(Context context,AttributeSet atri) {
		super(context,atri);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		dragLine(canvas);
		super.onDraw(canvas);
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		Log.i("xia", left+",,"+top+",,"+right);
	}
	
	private void dragLine(Canvas canvas){
		if(linePaint == null){
			linePaint = new Paint();
			linePaint.setStyle(Paint.Style.STROKE);
			linePaint.setStrokeWidth(5);
			linePaint.setMaskFilter(new BlurMaskFilter(100, BlurMaskFilter.Blur.SOLID));
			linePaint.setColor(Color.parseColor("#2b6a9b"));	
		}
		Log.i("xia", getLeft()+",,");
        Path path = new Path();
        path.moveTo(left - 10, top - 10);
        path.lineTo(right + 10,top - 10);
        //虚线效果
//        PathEffect effects = new DashPathEffect(new float[]{5,5,5,5},1);
//        linePaint.setPathEffect(effects);
        canvas.drawPath(path, linePaint);
        path.moveTo(right + 10, top - 10);
        path.lineTo(right + 10,bottom - 10);
        canvas.drawPath(path, linePaint);
        path.moveTo(left - 10, bottom + 10);
        path.lineTo(right + 10,bottom + 10);
        canvas.drawPath(path, linePaint);
        path.moveTo(left - 10, top - 10);
        path.lineTo(left - 10,bottom + 10);
        canvas.drawPath(path, linePaint);
	}

	public void setData(int left2, int right2, int top2, int bottom2) {
		this.left = left2;
		this.right = right2;
		this.top = top2;
		this.bottom = bottom2;
		invalidate();
		requestLayout();
	}
	
}
