package com.xinlan.Kaoyan.Data;

import java.util.ArrayList;
import java.util.List;

import com.xinlan.Kaoyan.model.AtomWord;
import com.xinlan.Kaoyan.model.Item;
import com.xinlan.Kaoyan.model.Word;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 对外提供数据库服务
 * @author Administrator
 *
 */
public class DatabaseService
{
	private SqliteOpen sqliteOpen;	
	public SQLiteDatabase getWritableDatabase()
	{
		return  sqliteOpen.getWritableDatabase();
	}
	
	public SQLiteDatabase getReadableDatabase()
	{
		return sqliteOpen.getReadableDatabase();
	}
	
	public DatabaseService(Context context)
	{
		sqliteOpen=new SqliteOpen(context,"kaoyandb", null,1);
		sqliteOpen.getReadableDatabase();
	}
	
	/**
	 * 查询ITEMS表记录数量
	 * @return
	 */
	public int getItemsNum()
	{
		SQLiteDatabase db=getReadableDatabase();
		Cursor cursor=db.query("ITEMS",new String[]{"count(ID)"},null,null, null, null,null);
		cursor.moveToNext();
		int ret=cursor.getInt(cursor.getColumnIndex("count(ID)"));
		cursor.close();
		db.close();
		return ret;
	}
	
	/**
	 * 清空ITEMS表
	 */
	public void clearItemsData()
	{
		SQLiteDatabase db=getWritableDatabase();
		db.delete("ITEMS", null, null);
		db.close();
	}
	
	/**
	 * 插入一条Item
	 */
	public long insertItems(Item item)
	{
		SQLiteDatabase db=getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("NUMBER", item.getNumber());
		values.put("ENGLISH", item.getEnglish());
		values.put("TRANSLATE", item.getTranslate());
		values.put("FLAG", item.getFlag());
		long ret=db.insert("ITEMS", null, values);
		db.close();
		return ret;
	}
	
	
	/**
	 * 逻辑上删除指定ID的ITEM项
	 * @param id
	 * @return
	 */
	public int deleteItemById(int id){
		SQLiteDatabase db=getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("FLAG", Constants.ITEM_FLAG_DELETE);
		int ret=db.update("ITEMS", values, "ID=?",new String[]{id+""} );
		db.close();
		return ret;
	}
	
	/**
	 * 查询item列表
	 * @return
	 */
	public List<Item> selectItemListAlive()
	{
		SQLiteDatabase db=getWritableDatabase();
		List<Item> list=new ArrayList<Item>();
		Cursor cursor=db.query("ITEMS", new String[]{"ID","NUMBER","ENGLISH",
				"TRANSLATE","FLAG","EXTERN"},"FLAG="+Constants.ITEM_FLAG_NORMAL
				,null,null,null,"ID asc");
		while(cursor.moveToNext())
		{
			Item item=new Item();
			item.setId(cursor.getInt(cursor.getColumnIndex("ID")));
			item.setNumber(cursor.getInt(cursor.getColumnIndex("NUMBER")));
			item.setExtern(cursor.getString(cursor.getColumnIndex("EXTERN")));
			item.setFlag(cursor.getInt(cursor.getColumnIndex("FLAG")));
			item.setEnglish(cursor.getString(cursor.getColumnIndex("ENGLISH")));
			item.setTranslate(cursor.getString(cursor.getColumnIndex("TRANSLATE")));
			list.add(item);
		}//end while
		cursor.close();
		db.close();
		return list;
	}
	
	/**
	 * 恢复所有句子
	 * @return
	 */
	public int restoreAllItems()
	{
		SQLiteDatabase db=getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("FLAG", Constants.ITEM_FLAG_NORMAL);
		int ret=db.update("ITEMS", values, "FLAG=?",new String[]{Constants.ITEM_FLAG_DELETE+""} );
		db.close();
		return ret;
	}
	
	public int selectWordByCotent(String wordCotent)
	{
		SQLiteDatabase db=getReadableDatabase();
		Cursor cursor=db.query("WORDS", new String[]{"count(ID)"},"FLAG="+Constants.WORD_FLAG_NORMAL,null,null,null,null);
		cursor.moveToNext();
		int ret=cursor.getInt(cursor.getColumnIndex("count(ID)"));
		cursor.close();
		db.close();
		return ret;
	}
	
	/**
	 * 插入WORD表数据
	 * @param word
	 * @param ch
	 * @return
	 */
	public long addNewWordToData(String word,String ch)
	{
		SQLiteDatabase db=getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("WORD",word);
		values.put("CH", ch);
		values.put("FLAG", Constants.WORD_FLAG_NORMAL);
		values.put("EXTERN", "");
		long ret=db.insert("WORDS", null, values);
		db.close();
		
		return ret;
	}
	
	public Word selectWordByEng(String content)
	{
		SQLiteDatabase db=getReadableDatabase();
		Word word=null;
		Cursor cursor=db.query("WORDS", new String[]{"ID","WORD","CH","FLAG","EXTERN"}, "WORD=? and FLAG=?",
				new String[]{content+"",Constants.WORD_FLAG_NORMAL+""},null, null, null, "1");
		if(cursor.moveToNext())//查找到数据
		{
			word=getWordFromCursor(cursor);//获得WORD数据
		}
		return word;
	}
	
	/**
	 * 更新指定WORD翻译 数据
	 * @param record
	 * @return
	 */
	public long updateWordData(Word record)
	{
		SQLiteDatabase db=getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("CH", record.getCh());
		long ret=db.update("WORDS", values, "ID=? and FLAG=?", new String[]{record.getId()+"",Constants.WORD_FLAG_NORMAL+""});
		db.close();
		
		return ret;
	}
	
	/**
	 * 更新指定单词英文翻译
	 * @param chContent
	 * @param word
	 * @return
	 */
	public long updateWordChData(String chContent,Word word)
	{
		String ch=word.getCh();
		ch=ch+"#"+chContent;
		word.setCh(ch);
		
		return updateWordData(word);
	}
	
	/**
	 * 根据ID查询WORD项目
	 * @param id
	 * @return
	 */
	public Word selectWordById(int id)
	{
		SQLiteDatabase db=getReadableDatabase();
		Word word=null;
		Cursor cursor=db.query("WORDS", new String[]{"ID","WORD","CH","FLAG","EXTERN"}, "ID=? and FLAG=?",
				new String[]{id+"",Constants.WORD_FLAG_NORMAL+""},null, null, null, "1");
		if(cursor.moveToNext())//查找到数据
		{
			 word=getWordFromCursor(cursor);
		}
		cursor.close();
		db.close();
		return word;
	}
	
	/**
	 * 从Cursor中获得Word对象
	 * @param cursor
	 * @return
	 */
	private Word getWordFromCursor(Cursor cursor)
	{
		 Word word=new Word();
		 word.setId(cursor.getInt(cursor.getColumnIndex("ID")));
		 String chContent=cursor.getString(cursor.getColumnIndex("WORD"));
		 StringBuffer sb=new StringBuffer();
		 String[] subCh=chContent.split("#");
		 for(int i=0;i<subCh.length;i++)
		 {
			 sb.append((i+1)+"."+subCh[i]+"\n");
		 }//end for
		 word.setWord(sb.toString());
		 word.setCh(cursor.getString(cursor.getColumnIndex("CH")));
		 word.setFlag(cursor.getInt(cursor.getColumnIndex("FLAG")));
		 word.setExtern(cursor.getString(cursor.getColumnIndex("EXTERN")));
		 
		 return word;
	}
	
	/**
	 * 读取录入单词列表
	 * @return
	 */
	public List<Word> selectAllWords()
	{
		List<Word> list=new ArrayList<Word>();
		SQLiteDatabase db=getReadableDatabase();
		Cursor cursor=db.query("WORDS", new String[]{"ID","WORD","CH","FLAG","EXTERN"}, "FLAG=?",
				new String[]{Constants.WORD_FLAG_NORMAL+""},null, null, "ID desc");
		while(cursor.moveToNext())
		{
			Word word=new Word();
			word.setId(cursor.getInt(cursor.getColumnIndex("ID")));
			word.setWord(cursor.getString(cursor.getColumnIndex("WORD")));
			String[] strs=cursor.getString(cursor.getColumnIndex("CH")).split("#");
			StringBuffer sb=new StringBuffer();
			for(int i=0;i<strs.length;i++)
			{
				sb.append(strs[i]+"   \t");
			}//end for
			word.setCh(sb.toString());
			word.setFlag(cursor.getInt(cursor.getColumnIndex("FLAG")));
			word.setExtern(cursor.getString(cursor.getColumnIndex("EXTERN")));
			list.add(word);
		}//end while
		cursor.close();
		db.close();
		return list;
	}
	
	public List<AtomWord> selectAtomWordsList()
	{
		List<AtomWord> list=new ArrayList<AtomWord>();
		SQLiteDatabase db=getReadableDatabase();
		Cursor cursor=db.query("WORDS", new String[]{"ID","WORD","CH","FLAG","EXTERN"}, "FLAG=?",
				new String[]{Constants.WORD_FLAG_NORMAL+""},null, null, "ID desc");
		int index=0;
		while(cursor.moveToNext())
		{
			String word=cursor.getString(cursor.getColumnIndex("WORD"));
			String[] strs=cursor.getString(cursor.getColumnIndex("CH")).split("#");
			for(int i=0;i<strs.length;i++)
			{
				AtomWord atomWord=new AtomWord();
				atomWord.setNumber(index++);
				atomWord.setWord(word);
				atomWord.setCh(strs[i]);
				list.add(atomWord);
			}//end for
		}//end while
		cursor.close();
		db.close();
		return list;
	}
	
}//end class
