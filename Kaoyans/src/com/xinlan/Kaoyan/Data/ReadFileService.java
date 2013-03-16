package com.xinlan.Kaoyan.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.xinlan.Kaoyan.model.Item;

import android.content.Context;
import android.content.res.AssetManager;

/**
 * 读取文件
 * @author Administrator
 *
 */
public class ReadFileService
{
	private Context context;
	public ReadFileService(Context context)
	{
		this.context=context;
	}
	
	/**
	 * 读取原始文件到列表中去
	 * @return
	 */
	public List<Item> readOriginFile()
	{
		List<Item> list=new ArrayList<Item>();
		AssetManager assetManager=context.getAssets();
		InputStream inputStream=null;
		BufferedReader buffer=null;
		try
		{
			inputStream=assetManager.open("origin/kaoyanwords.lan");
			buffer = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));
			String line=null;
			int flag=1;
			Item item=null;
			while ((line = buffer.readLine()) != null)
			{
				if(flag%2!=0)
				{
					item=new Item();
					item.setNumber((flag+1)/2);
					item.setFlag(Constants.ITEM_FLAG_NORMAL);
					item.setEnglish(line);
				}
				else
				{
					item.setTranslate(line);
					list.add(item);
					item=null;
				}
				flag++;
			}//end while
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			if(inputStream!=null)
			{
				try 
				{
					inputStream.close();
				} 
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		return list;
	}
}
