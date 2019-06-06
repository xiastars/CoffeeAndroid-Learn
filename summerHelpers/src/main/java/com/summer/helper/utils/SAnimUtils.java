package com.summer.helper.utils;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.view.View;

import com.summer.helper.listener.OnAnimEndListener;

public class SAnimUtils {
	
	/**
	 * 向左偏移
	 * @param view
	 * @param offset
	 */
	public static void hideTop(View view,float offset){
		showPropertyAnim(view,View.GONE, "translationY",offset,0f,0f,300);
	}
	
	/**
	 * 向左偏移
	 * @param view
	 * @param offset
	 */
	public static void hideTop(View view,float offset,final OnAnimEndListener listener){
		showPropertyAnim(true,view,View.GONE, "translationY",0,offset,offset,300,listener);
	}
	
	/**
	 * 向左偏移
	 * @param view
	 * @param offset
	 */
	public static void hideBottom(View view,float offset,final OnAnimEndListener listener){
		showPropertyAnim(true,view,View.GONE, "translationY",0,0,offset,300,listener);
	}
	
	/**
	 * 向左偏移
	 * @param view
	 * @param offset
	 */
	public static void moveLeftHide(View view,float offset,final OnAnimEndListener listener){
		showPropertyAnim(true,view,View.GONE, "translationX",0f,offset,offset,300,listener);
	}
	
	/**
	 * 向左偏移
	 * @param view
	 * @param offset
	 */
	public static void moveLeftShow(View view,float offset){
		showPropertyAnim(view,View.VISIBLE, "translationX",offset,0,0,300);
	}
	
	/**
	 * 向左偏移
	 * @param view
	 * @param offset
	 */
	public static void moveLeftShowInvisible(View view,float offset){
		showPropertyAnim(view,View.VISIBLE, "translationX",0,offset,offset,300);
	}
	
	/**
	 * 向左偏移
	 * @param view
	 * @param offset
	 */
	public static void moveLeftShow(View view,float offset,final OnAnimEndListener listener){
		showPropertyAnim(true,view,View.VISIBLE, "translationX",0,offset,offset,300,listener);
	}
	
	/**
	 * 向下偏移
	 * @param view
	 * @param offset
	 */
	public static void moveDownShow(View view,float offset){
		showPropertyAnim(view,View.VISIBLE, "translationY",offset,0f,0f,300);
	}
	
	/**
	 * 向上偏移
	 * @param view
	 * @param offset
	 */
	public static void moveUpHide(View view,float offset){
		showPropertyAnim(view,View.GONE, "translationY",0f,offset,offset,300);
	}
	
	/**
	 * 向上偏移
	 * @param view
	 * @param offset
	 */
	public static void moveUpHide(View view,float offset,long time){
		showPropertyAnim(view,View.GONE, "translationY",0f,offset,offset,time);
	}

	/**
	 * 物体动画 
	 * @param view
	 * @param visible
	 * @param animAction
	 * @param x1
	 * @param x2
	 * @param x3
	 */
	public static void showPropertyAnim(View view,int visible,String animAction,float x1,float x2,float x3,long time){
		showPropertyAnim(true,view,visible,animAction,x1,x2,x3,time,null);
	}
	
	/**
	 * 物体动画 
	 * @param view
	 * @param visible
	 * @param animAction
	 * @param x1
	 * @param x2
	 * @param x3
	 */
	public static void showPropertyAnim(boolean anim,View view,int visible,String animAction,float x1,float x2,float x3){
		showPropertyAnim(anim,view,visible,animAction,x1,x2,x3,300,null);
	}
	
	/**
	 * 物体动画 
	 * @param view
	 * @param visible
	 * @param animAction
	 * @param x1
	 * @param x2
	 * @param x3
	 */
	public static void showPropertyAnim(boolean anim,View view,int visible,String animAction,float x1,float x2,float x3,long time){
		showPropertyAnim(anim,view,visible,animAction,x1,x2,x3,time,null);
	}
	
	public static void scale(View view,boolean clearAnim,float width,float height){
		circleScaleAnim(view,width,height,0,300,clearAnim,null);
	}
	
	public static void scale(View view,boolean clearAnim){
		circleScaleAnim(view,0,0,0,300,clearAnim,null);
	}
	
	public static void scaleY(View view){
	}
	
	/**
	 * 物体动画 
	 * @param view
	 * @param visible
	 * @param animAction
	 * @param x1
	 * @param x2
	 * @param x3
	 */
	public static void showPropertyAnim(boolean anim,final View view,final int visible,String animAction,float x1,float x2,float x3,long time,final OnAnimEndListener listener){
		if(!anim){
			if(listener != null){
				listener.onEnd();
			}
			return;
		}
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, animAction, x1, x2, x3);
		if(visible == View.VISIBLE){
			if(view.getVisibility() == View.GONE){
				  view.setVisibility(View.VISIBLE);
			}
		}
		animator.setDuration(time).start();
        animator.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				view.setTranslationX(0);
				view.setTranslationY(0);
				view.setAlpha(1.0f);
				view.clearAnimation();
				if(listener != null){
					listener.onEnd();
				}
				if(visible == View.GONE){
					view.setVisibility(visible);
				}else{
					view.setVisibility(visible);
				}
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
			}
		});
	}
	
	/**
	 * 放大缩小动画 
	 * @param view
	 * @param listener
	 */
	public static void circleScaleAnim(final View view,float width,float height,int repeatcount,int time,boolean clearAnim,final OnAnimEndListener listener){
		if(view == null)return;
		if(width == 0){
			view.setPivotX(view.getWidth()/2);
		}else{
			view.setPivotX(width);
		}
		if(height == 0){
			view.setPivotY(view.getHeight()/2);
		}else{
			view.setPivotY(height);
		}
		float endX = 1.1f; 
		float endY = 1.1f;
		float startX = 1f; 
		float startY = 1f;
		PropertyValuesHolder valuesHolder1 = PropertyValuesHolder.ofFloat("scaleX", startX,endX,startX);
		if(!clearAnim){
			 valuesHolder1 = PropertyValuesHolder.ofFloat("scaleX", startX,endX,endX);
		}
        PropertyValuesHolder valuesHolder2 = PropertyValuesHolder.ofFloat("scaleY",  startY,endY,startY);
        if(!clearAnim){
        	valuesHolder2 = PropertyValuesHolder.ofFloat("scaleY",  startY,endY,endY);
        }

        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, valuesHolder1, valuesHolder2);
        objectAnimator.setRepeatCount(repeatcount);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.setDuration(time).start();
		objectAnimator.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				view.clearAnimation();
				if(listener != null){
					listener.onEnd();
				}
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
			}
		});
	}

	public static void removeScale(View view) {
		float x = 1.0f;
		PropertyValuesHolder valuesHolder1 = PropertyValuesHolder.ofFloat("scaleX", x, x, x);
		PropertyValuesHolder valuesHolder2 = PropertyValuesHolder.ofFloat("scaleY", x, x, x);

		ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, valuesHolder1, valuesHolder2);
		objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
		objectAnimator.setDuration(0).start();
	}
	
}
