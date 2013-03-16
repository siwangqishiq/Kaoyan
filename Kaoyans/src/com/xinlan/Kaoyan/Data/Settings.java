package com.xinlan.Kaoyan.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Settings
{
	private Context mContext;
	private SharedPreferences sharePreferences;
	
	private String BACKGROUND_INDEX="background_index";
	private String LAST_ITEM_INDEX="last_item_index";
	public Settings(Context context)
	{
		mContext=context;
		sharePreferences= mContext.getSharedPreferences(Constants.NAMESPACE,Context.MODE_PRIVATE);
	}
	
	/**
	 * 获取背景图索引值
	 * @return
	 */
	public int getBackgroundIndexValue()
	{
		return sharePreferences.getInt(BACKGROUND_INDEX, -1);
	}
	
	/**
	 * 设置背景图索引
	 * @param index
	 */
	public void setBackgroundIndexValue(int index)
	{
		Editor	edit=sharePreferences.edit();
		edit.putInt(BACKGROUND_INDEX, index);
		edit.commit();
	}
	
	/**
	 * 取得上次ITEM的编号
	 * @return
	 */
	public int getLastItemIndexValue()
	{
		return sharePreferences.getInt(LAST_ITEM_INDEX, 0);
	}
	
	/**
	 * 存入上次ITEM值
	 * @param value
	 */
	public void setLastItemIndexValue(int value)
	{
		Editor	edit=sharePreferences.edit();
		edit.putInt(LAST_ITEM_INDEX, value);
		edit.commit();
	}
}//end class
