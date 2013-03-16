package com.xinlan.Kaoyan.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.domob.android.ads.DomobAdView;

import com.xinlan.Kaoyan.Adapter.WordListAdapter;
import com.xinlan.Kaoyan.Data.DatabaseService;
import com.xinlan.Kaoyan.model.Word;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/**
 * 单词列表
 * @author Panyi
 *
 */
public class WordListActivity extends ListActivity
{
	private DatabaseService dbService;
	private List<Word> wordList;
	KaoyanApp app;
	
	RelativeLayout adContainer;
	DomobAdView mAdview; 
	
	public void addDombAD()
	{
		adContainer=(RelativeLayout)this.findViewById(R.id.listAdr);
		mAdview = new DomobAdView(this,getResources().getString(R.string.adIdTest), DomobAdView.INLINE_SIZE_320X50);
		adContainer.addView(mAdview);
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
		setTitle("记录单词列表");
        super.onCreate(savedInstanceState);
        injectService();
        setContentView(R.layout.word_list);
        init();
        addDombAD();
    }
	
	private void injectService()
	{
		// TODO 注入服务
		app = (KaoyanApp) getApplication(); //获得自定义的应用程序MyApp 
		dbService=app.getDbService();
	}

	private void init()
	{
		wordList=dbService.selectAllWords();//获取Word数据
		if(wordList.size()==0)
		{
			Toast.makeText(this, "您还未录入单词", Toast.LENGTH_SHORT).show();
			return;
		}
		ArrayList<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
		for(int i=0;i<wordList.size();i++)
		{
			Word word=wordList.get(i);
			HashMap<String,String> map=new HashMap<String,String>();
			map.put("english", (i+1)+". "+word.getWord());
	        map.put("ch",word.getCh());
	        list.add(map);
		}//end for
		SimpleAdapter listAdapter=new WordListAdapter(this,list,R.layout.word_item,new String[]{"english","ch"},
				new int[]{R.id.wordEnglish,R.id.wordChinese});
		setListAdapter(listAdapter);
	}
}//end class
