package com.xinlan.Kaoyan.Adapter;

import java.util.List;
import java.util.Map;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

public class WordListAdapter extends SimpleAdapter 
{
	private int[] colors = new int[] { 0x30ff2020, 0x30808080};

	public WordListAdapter(Context context,List<? extends Map<String, ?>> data,
			int resource, String[] from,int[] to) 
	{
		super(context, data, resource, from, to);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View view = super.getView(position, convertView, parent);
		if(position%2==0)
		{
			view.setBackgroundColor(colors[0]);
		}
		else
		{
			view.setBackgroundColor(colors[1]);
		}
		return view;
	}


}//end class
