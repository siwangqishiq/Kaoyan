package com.xinlan.Kaoyan.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteOpen extends SQLiteOpenHelper
{

	public SqliteOpen(Context context, String name, CursorFactory factory,int version) 
	{
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL("create table ITEMS (ID INTEGER primary key autoincrement," +
				"NUMBER int,ENGLISH varchar(200),TRANSLATE varchar(200),FLAG int,EXTERN varchar(150))");//创建ITEMS表
		db.execSQL("create table WORDS (ID INTEGER primary key autoincrement," +
				"WORD varchar(50),CH varchar(100),FLAG int,EXTERN varchar(150))");//创建WORDS表
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		
	}

}
