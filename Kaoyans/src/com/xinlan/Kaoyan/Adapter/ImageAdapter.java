package com.xinlan.Kaoyan.Adapter;

import com.xinlan.Kaoyan.Data.Constants;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

/**
 * 背景图片适配
 * 
 * @author Administrator
 * 
 */
public class ImageAdapter extends BaseAdapter {
	private Context mContext;

	private Integer[] imageIds = Constants.backgrounds;
	private int SCREEN_HEIGHT;
	private int SCREEN_WIDTH;

	public ImageAdapter(Context c) {
		mContext = c;
		// 获取屏幕高度与宽度
		WindowManager wm = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		SCREEN_WIDTH = wm.getDefaultDisplay().getWidth();
		SCREEN_HEIGHT = wm.getDefaultDisplay().getHeight();
	}

	public int getCount() {
		return imageIds.length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView i = new ImageView(mContext);
		i.setImageResource(imageIds[position]);// set resource for the imageView
		i.setLayoutParams(new Gallery.LayoutParams(SCREEN_WIDTH,
				SCREEN_HEIGHT / 2));// 显示图片高度与宽度
		i.setScaleType(ImageView.ScaleType.FIT_XY);// set scale type
		return i;
	}

}// end class
