package com.example.view;


import com.example.myctrip.R;
import com.example.utils.ScreenUtils;

import android.R.menu;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class SlidingMenu extends HorizontalScrollView {

	public SlidingMenu(Context context) {
		this(context, null);
	}

	public SlidingMenu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mScreenWidth = ScreenUtils.getScreenWidth(context);
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SlidingMenu, defStyle, 0);
		int n = a.getIndexCount();
		for(int i = 0;i<n;i++){
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.SlidingMenu_rightPadding:
				mMenuRightPadding = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f, getResources().getDisplayMetrics()));
				break;
			}
		}
		a.recycle();
	}

	
	private int mScreenWidth ;//��Ļ���
	private int mMenuRightPadding = 80;
	private int mMenuWidth;//�˵����
	private int mHalfMenuWidth;
	
	private boolean once;
	private boolean isOpen;//�Ƿ�򿪲˵�
	private ViewGroup mMenu;
	private ViewGroup mContent;
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (!once) {
			LinearLayout wrapper = (LinearLayout) getChildAt(0);
			mMenu = (ViewGroup) wrapper.getChildAt(0);
			mContent = (ViewGroup) wrapper.getChildAt(1);
			mMenuRightPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mMenuRightPadding, mContent.getResources().getDisplayMetrics());
			mMenuWidth = mScreenWidth-mMenuRightPadding;
			mHalfMenuWidth = mMenuWidth/2;
			mMenu.getLayoutParams().width = mMenuWidth;
			mContent.getLayoutParams().width = mScreenWidth;
		}
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			this.scrollTo(mMenuWidth, 0);
			once = true;
		}
		super.onLayout(changed, l, t, r, b);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_UP:
			int scrollX = getScrollX();
			if (scrollX>mHalfMenuWidth) {
				this.smoothScrollTo(mMenuWidth, 0);
				isOpen = false;
			}else {
				this.smoothScrollTo(0, 0);
				isOpen = true;
			}
			return true;
		}
		return super.onTouchEvent(ev);
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		float scale = 1*1.0f/mMenuWidth;
		float leftScale = 1-0.3f*scale;
		float rightScale = 0.8f+scale*0.2f;
		/*
		ViewHelper.setScaleX(mMenu, leftScale);
		ViewHelper.setScaleY(mMenu, leftScale);
		ViewHelper.setAlpha(mMenu, 0.6f+0.4f*1-scale);
		ViewHelper.setTranslationX(mMenu, mMenuWidth*scale*0.6f);
		
		ViewHelper.setPivotX(mContent, 0);  
        ViewHelper.setPivotY(mContent, mContent.getHeight() / 2);  
        ViewHelper.setScaleX(mContent, rightScale);  
        ViewHelper.setScaleY(mContent, rightScale); */
		
		//ViewHelper.setTranslationX(mMenu, mMenuWidth*scale);
		
		
	}
	
	public void openMenu() {
		if (isOpen) {
			return;
		}
		this.smoothScrollTo(0, 0);
		isOpen = true;
	}
	
	public void closeMenu(){
		if (isOpen) {
			this.smoothScrollTo(mMenuWidth, 0);
			isOpen = false;
		}
	}
	
	/**
	 * �л��˵�״̬
	 */
	public void toggle() {
		if (isOpen) {
			closeMenu();
		}else {
			openMenu();
		}
	}
	
	
}
