package com.xinlan.Kaoyan.Activity;

import com.xinlan.Kaoyan.Data.DatabaseService;

import android.app.Application;

public class KaoyanApp extends Application
{
	private DatabaseService dbService;

	public DatabaseService getDbService() 
	{
		return dbService;
	}

	public void setDbService(DatabaseService dbService)
	{
		this.dbService = dbService;
	}
}
