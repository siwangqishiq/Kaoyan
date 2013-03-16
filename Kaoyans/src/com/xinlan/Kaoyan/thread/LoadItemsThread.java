package com.xinlan.Kaoyan.thread;

import java.util.List;

import com.xinlan.Kaoyan.Data.DatabaseService;
import com.xinlan.Kaoyan.model.Item;

/**
 * 导入数据线程
 * @author Panyi
 *
 */
public class LoadItemsThread extends Thread
{
	private DatabaseService dbService;
	private List<Item> dataList;
	public LoadItemsThread(DatabaseService dbService,List<Item> dataList)
	{
		this.dbService=dbService;
		this.dataList=dataList;
	}
	
	@Override
	public void run()
	{
		loadData();
	}
	
	/**
	 * 导入数据
	 */
	private void loadData()
	{
		dbService.clearItemsData();//清空原有数据
		for(int i=0;i<dataList.size();i++)//插入数据
		{
			dbService.insertItems(dataList.get(i));
		}//end for
	}
}//end class






