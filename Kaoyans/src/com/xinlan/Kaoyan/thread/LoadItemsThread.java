package com.xinlan.Kaoyan.thread;

import java.util.List;

import com.xinlan.Kaoyan.Data.DatabaseService;
import com.xinlan.Kaoyan.model.Item;

/**
 * ���������߳�
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
	 * ��������
	 */
	private void loadData()
	{
		dbService.clearItemsData();//���ԭ������
		for(int i=0;i<dataList.size();i++)//��������
		{
			dbService.insertItems(dataList.get(i));
		}//end for
	}
}//end class






