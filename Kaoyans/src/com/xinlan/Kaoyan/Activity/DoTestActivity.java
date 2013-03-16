package com.xinlan.Kaoyan.Activity;

import java.util.List;

import cn.domob.android.ads.DomobAdView;

import com.xinlan.Kaoyan.Data.DatabaseService;
import com.xinlan.Kaoyan.Utils.Randoms;
import com.xinlan.Kaoyan.model.AtomWord;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ����ģ��
 * @author Panyis
 *
 */
public class DoTestActivity extends Activity
{	
	KaoyanApp app;
	private DatabaseService dbService;
	private List<AtomWord> atomWordsList;
	private Button submitBtn;
	private EditText wordContentText;
	private TextView wordTxt;
	private AtomWord curWord;
	
	public static final int SPALSH_DURATION=400;
	
	RelativeLayout adContainer;
	DomobAdView mAdview; 
	
	public void addDombAD()
	{
		adContainer=(RelativeLayout)this.findViewById(R.id.doTestAdContainer);
		mAdview = new DomobAdView(this,getResources().getString(R.string.adIdTest), DomobAdView.INLINE_SIZE_320X50);
		adContainer.addView(mAdview);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);     
		setContentView(R.layout.do_test);
		setTitle("����Ӣ��100��");
		 injectService();
		 addListener();
		 initData();
		 
		 addDombAD();
	}

	private void addListener()
	{
		submitBtn=(Button)this.findViewById(R.id.testSubmitBtn);
		wordContentText=(EditText)this.findViewById(R.id.testWordContent);
		submitBtn.setOnClickListener(new OnClickListener(){//�ύ��ť
			public void onClick(View v)
			{
				if(atomWordsList.size()<=0)
				{
					return;
				}
				
				String wordContent=wordContentText.getText().toString().trim();
				if(curWord.getWord().equals(wordContent))
				{
					Toast.makeText(DoTestActivity.this, "����Ĭд��ȷ!^_^", Toast.LENGTH_SHORT).show();//��ʾ��������
				}
				else
				{
					Toast.makeText(DoTestActivity.this, "����Ĭд����!>_<", Toast.LENGTH_SHORT).show();//��ʾ��������
				}
			}
		});
		
		wordTxt=(TextView)this.findViewById(R.id.testMainText);
		wordTxt.setOnClickListener(new OnClickListener(){//��� �л�����
			public void onClick(View v)
			{
				gotoNextWord();
			}
		});
	}
	
	private int gotoNextWord()
	{
		wordContentText.setText("");//��������
		//��ִ�пؼ����ƵĶ���
		TranslateAnimation riseAnimation=new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0,
				Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,-1);
		riseAnimation.setDuration(SPALSH_DURATION);
		riseAnimation.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {}
			public void onAnimationRepeat(Animation animation) {}
			public void onAnimationEnd(Animation animation) {
				nextWord();
				TranslateAnimation downAnimation=new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0,
						Animation.RELATIVE_TO_SELF,-1,Animation.RELATIVE_TO_SELF,0);
				downAnimation.setDuration(SPALSH_DURATION);
				downAnimation.setFillAfter(true);
				wordTxt.setAnimation(downAnimation);
			}
		});
		wordTxt.setAnimation(riseAnimation);
		
		return 0;
	}

	private void injectService()
	{
		app = (KaoyanApp) getApplication(); //����Զ����Ӧ�ó���MyApp 
		dbService=app.getDbService();
	}
	
	private void initData()
	{
		atomWordsList=dbService.selectAtomWordsList();
		Toast.makeText(this, "�л���������������ʾ��", Toast.LENGTH_LONG).show();//��ʾ��������
		nextWord();
	}
	
	private void nextWord()
	{
		if(atomWordsList.size()<=0)
		{
			Toast.makeText(this, "����δ¼�뵥��Ŷ���������ڲ�����д", Toast.LENGTH_LONG).show();//��ʾ��������
			
			return;
		}
		curWord=getNextAtomWord(atomWordsList);
		wordTxt.setText("��д����������ĵ���:\n \t"+curWord.getCh());
	}
	
	/**
	 * ����õ�һ����д�ĵ���
	 * @return
	 */
	private AtomWord getNextAtomWord(List<AtomWord> atomWordsList)
	{
		return atomWordsList.get(Randoms.genInt(0, atomWordsList.size()));
	}
	
//	private List<AtomWord> switchWordsList(List<Word> list)
// 	{
//		List<AtomWord> retList=new ArrayList<AtomWord>();
//		int index=0;
//		for(Word word:list)
//		{
//			String[] chArrays=word.getCh().split("#");
//			for(String eachCh:chArrays)
//			{
//				AtomWord atomWord=new AtomWord();
//				atomWord.setNumber(index++);
//				atomWord.setWord(word.getWord());
//				atomWord.setCh(eachCh);
//				retList.add(atomWord);
//			}//end for
//			
//		}//end for
//		return retList;
//	}
}//end class
