package com.xinlan.Kaoyan.Activity;

import java.util.List;

import cn.domob.android.ads.DomobAdView;

import com.xinlan.Kaoyan.Data.Constants;
import com.xinlan.Kaoyan.Data.DatabaseService;
import com.xinlan.Kaoyan.Data.ReadFileService;
import com.xinlan.Kaoyan.Data.Settings;
import com.xinlan.Kaoyan.Utils.Randoms;
import com.xinlan.Kaoyan.Utils.RegExpValidator;
import com.xinlan.Kaoyan.model.Item;
import com.xinlan.Kaoyan.model.Word;
import com.xinlan.Kaoyan.thread.LoadItemsThread;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

public class MainActivity extends Activity implements OnGestureListener {
	KaoyanApp app;
	private GestureDetector gestureDetector;// ����ʶ��
	private TextView txtEngOne;// ��ʾӢ�����1
	private TextView txtChOne;// ��ʾ���ķ������1
	private TextView txtEngTwo;// ��ʾӢ�����2
	private TextView txtChTwo;// ��ʾ���ķ������2
	private RelativeLayout mainLayout;// ������
	private EditText wordEdit;// �µ���
	private EditText chEdit;// �·���

	private Button remberBtn;
	private Button hiddenTranslateBtn;
	private Button doAddBtn;
	private Button doClearBtn;
	private Button gotoWordListBtn;
	private Button doTestBtn;

	private boolean splashFlag = true;
	private boolean isTranslateHiiden = false;// ��־�����Ƿ�����
	private int currentIndex = 0;// ��ǰ��ʾ��Ŀ����

	private Integer[] BACKGROUND_ARRAY = Constants.backgrounds;// ����ͼ����
	private static int SPALSH_DURATION = 500;// �����ٶȲ���
	private static int TRANSLATE_DISPLAY_DURING = 600;// �������� ��ʾ��ʱ
	private static final int SPLASH_TO_LEFT = 0;// ���󻬶�
	private static final int SPLASH_TO_RIGHT = 1;// ���һ���

	private static final int MENU_RESTORE_SHOWALL = 2;// �ָ���ʾ���о���
	private static final int MENU_SETBACKGROUND = 1;
	private static final int MENU_EXIT = 0;

	public DatabaseService dbService;// ���ݿ������
	private Settings settings;
	private List<Item> dataList;// �����б�

	RelativeLayout adContainer;
	DomobAdView mAdview;

	public void addDombAD() {
		adContainer = (RelativeLayout) this.findViewById(R.id.mainAdContainer);
		mAdview = new DomobAdView(this, getResources().getString(
				R.string.adIdTest), DomobAdView.INLINE_SIZE_320X50);
		adContainer.addView(mAdview);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (gestureDetector.onTouchEvent(event))
			return true;
		else
			return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Toast.makeText(this, "���һ����л�����", Toast.LENGTH_LONG).show();// ��ʾ��������
		init();

		addDombAD();
	}

	/**
	 * ���ÿؼ�����
	 */
	private void setData() {
		if (dbService.getItemsNum() != 100)// �״����������ݿ��л�û������
		{
			ReadFileService readFileService = new ReadFileService(this);
			dataList = readFileService.readOriginFile();// ���ļ��ж�ȡ����
			new LoadItemsThread(dbService, dataList).start();// �������������߳�
			remberBtn.setEnabled(false);
			remberBtn.setVisibility(View.INVISIBLE);
			doTestBtn.setVisibility(View.INVISIBLE);
		} else {
			// �����ݿ��ȡ����
			dataList = dbService.selectItemListAlive();
		}
		currentIndex = settings.getLastItemIndexValue();
		Item item = dataList.get(currentIndex);
		txtEngOne.setText(item.getEnglish());
		txtChOne.setText(item.getTranslate());
	}

	@Override
	protected void onDestroy() {
		settings.setLastItemIndexValue(currentIndex); // ����ITEMֵ
		super.onDestroy();
	}

	/**
	 * ��ʼ��
	 */
	private void init() {
		setTitle("����Ӣ��100��");
		initService();
		findViews();
		addEventListener();
		setData();
	}

	/**
	 * ע�������Ŀ
	 */
	private void initService() {
		settings = new Settings(this);
		dbService = new DatabaseService(this);
		app = (KaoyanApp) getApplication(); // ����Զ����Ӧ�ó���MyApp
		app.setDbService(dbService);
	}

	/**
	 * ���ñ���
	 */
	private void setBackground() {
		int index = settings.getBackgroundIndexValue();
		if (index < 0 || index >= BACKGROUND_ARRAY.length) {
			index = Randoms.genInt(0, BACKGROUND_ARRAY.length - 1);
		}
		mainLayout.setBackgroundResource(BACKGROUND_ARRAY[index]);
	}

	@Override
	protected void onResume() {
		setBackground();
		super.onResume();
	}

	/**
	 * ��ȡ�ؼ�
	 */
	private void findViews() {
		mainLayout = (RelativeLayout) findViewById(R.id.mainId);
		txtEngOne = (TextView) findViewById(R.id.txtEngOne);
		txtChOne = (TextView) findViewById(R.id.txtChOne);
		txtEngTwo = (TextView) findViewById(R.id.txtEngTwo);
		txtChTwo = (TextView) findViewById(R.id.txtChTwo);

		chEdit = (EditText) findViewById(R.id.addNewChBtn);
		wordEdit = (EditText) findViewById(R.id.addNewWordBtn);

		doAddBtn = (Button) findViewById(R.id.doAddBtn);
		doClearBtn = (Button) findViewById(R.id.doClearBtn);
		remberBtn = (Button) findViewById(R.id.remberBtn);
		hiddenTranslateBtn = (Button) findViewById(R.id.hidenTranslateBtn);
		gotoWordListBtn = (Button) findViewById(R.id.gotoWordList);
		doTestBtn = (Button) findViewById(R.id.doTestBtn);
	}

	/**
	 * ¼�뵥��
	 * 
	 * @param wordContent
	 * @param ch
	 * @return
	 */
	private long addNewWord(String wordContent, String ch) {
		Word word = dbService.selectWordByEng(wordContent);// ���ݵ��ʲ�ѯ
		long ret = -1;
		if (word == null)// ԭ���ݿ���û����ص�����
		{
			// ����������
			ret = dbService.addNewWordToData(wordContent, ch);
		} else {
			// ����ص���
			ret = dbService.updateWordChData(ch, word);
		}
		return ret;
	}

	/**
	 * ע���¼�����
	 */
	private void addEventListener() {
		gestureDetector = new GestureDetector(this);

		doTestBtn.setOnClickListener(new OnClickListener() {// ת������
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClass(MainActivity.this, DoTestActivity.class);
						MainActivity.this.startActivity(intent);
					}
				});

		gotoWordListBtn.setOnClickListener(new OnClickListener() {// ת�������б�
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClass(MainActivity.this,
								WordListActivity.class);
						MainActivity.this.startActivity(intent);

					}
				});

		remberBtn.setOnClickListener(new OnClickListener() {// ��ס�˾䰴ť
					public void onClick(View v) {
						Item item = dataList.get(currentIndex);
						dbService.deleteItemById(item.getId());
						dataList.remove(item);
						if (currentIndex >= dataList.size()) {
							currentIndex = dataList.size() - 1;
						}
						splashFlag = !splashFlag;
						setComeInTextViewsContent();
						playAnimation(SPLASH_TO_LEFT);
					}
				});

		doAddBtn.setOnClickListener(new OnClickListener() // �����µ���
		{
			public void onClick(View v) {
				String word = wordEdit.getText().toString().trim();
				String ch = chEdit.getText().toString().trim();
				if (RegExpValidator.IsEnglishWord(word)) {
					if (addNewWord(word, ch) > 0) {
						Toast.makeText(MainActivity.this, "��¼�ɹ�!",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(MainActivity.this, "���뵥�ʲ���ȷ",
							Toast.LENGTH_SHORT).show();
					wordEdit.setText("");
				}
			}
		});

		doClearBtn.setOnClickListener(new OnClickListener() // ���¼������
				{
					public void onClick(View v) {
						wordEdit.setText("");
						chEdit.setText("");
					}
				});

		hiddenTranslateBtn.setOnClickListener(new OnClickListener()// ���� �� ��ʾ����
				{
					public void onClick(View v) {
						isTranslateHiiden = !isTranslateHiiden;
						if (isTranslateHiiden)// ���ط���
						{
							AlphaAnimation alpha = new AlphaAnimation(1.0f, 0);
							alpha.setDuration(TRANSLATE_DISPLAY_DURING);
							TextView textView = getComeOutChView();
							textView.setAnimation(alpha);
							textView.setVisibility(View.INVISIBLE);
							hiddenTranslateBtn.setText("��ʾ����");
						} else// ��ʾ����
						{
							AlphaAnimation alpha = new AlphaAnimation(0, 1.0f);
							alpha.setDuration(TRANSLATE_DISPLAY_DURING);
							TextView textView = getComeOutChView();
							textView.setAnimation(alpha);
							textView.setVisibility(View.VISIBLE);
							hiddenTranslateBtn.setText("���ط���");
						}
					}
				});
	}

	/**
	 * ��ȡ���� �������ʾ�ؼ�
	 * 
	 * @return
	 */
	private TextView[] getComeInTextViews() {
		TextView[] retViews = new TextView[2];
		retViews[0] = splashFlag ? txtEngOne : txtEngTwo;
		retViews[1] = splashFlag ? txtChOne : txtChTwo;
		return retViews;
	}

	private TextView getComeOutChView() {
		return splashFlag ? txtChOne : txtChTwo;
	}

	/**
	 * ��ȡ�Ƴ� �������ʾ�ؼ�
	 * 
	 * @return
	 */
	private TextView[] getComeOutTextViews() {
		TextView[] retViews = new TextView[2];
		retViews[0] = splashFlag ? txtEngTwo : txtEngOne;
		retViews[1] = splashFlag ? txtChTwo : txtChOne;
		return retViews;
	}

	private void playAnimation(int splashType) {
		float[] LEFT = { 0f, -1.0f, 1.0f, 0f }, RIGHT = { 0f, 1.0f, -1.0f, 0f }, direct = LEFT;
		if (splashType == SPLASH_TO_LEFT) {
			direct = LEFT;
		} else if (splashType == SPLASH_TO_RIGHT) {
			direct = RIGHT;
		}
		TextView[] leaveTextViews = getComeOutTextViews();// ��ȡ�Ƴ��Ŀؼ�
		TextView[] comeTextViews = getComeInTextViews();// ��ȡ����Ŀؼ�
		int num = isTranslateHiiden ? 1 : 2;
		for (int i = 0; i < num; i++) {
			// �Ƴ�
			TextView leaveView = leaveTextViews[i];
			TranslateAnimation translateLeave = new TranslateAnimation(
					Animation.RELATIVE_TO_PARENT, direct[0],
					Animation.RELATIVE_TO_PARENT, direct[1],
					Animation.RELATIVE_TO_PARENT, 0,
					Animation.RELATIVE_TO_PARENT, 0);
			translateLeave.setDuration(SPALSH_DURATION);
			leaveView.setAnimation(translateLeave);
			leaveView.setVisibility(View.INVISIBLE);// ���ÿؼ�Ϊ���ɼ�
			// ����
			TextView comeView = comeTextViews[i];
			TranslateAnimation translateCome = new TranslateAnimation(
					Animation.RELATIVE_TO_PARENT, direct[2],
					Animation.RELATIVE_TO_PARENT, direct[3],
					Animation.RELATIVE_TO_PARENT, 0,
					Animation.RELATIVE_TO_PARENT, 0);
			translateCome.setDuration(SPALSH_DURATION);
			comeView.setAnimation(translateCome);
			comeView.setVisibility(View.VISIBLE);// ���ÿؼ��ɼ�
		}// end for
	}

	/**
	 * ������ָ�����¼�
	 */
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() - e2.getX() > 30)// ��һ��
		{
			currentIndex++;
			if (currentIndex >= dataList.size()) {
				currentIndex = dataList.size() - 1;
				Toast.makeText(this, "�Ѿ������һ��", Toast.LENGTH_SHORT).show();
				return true;
			}
			splashFlag = !splashFlag;
			setComeInTextViewsContent();
			playAnimation(SPLASH_TO_LEFT);
			return true;
		} else if (e1.getX() - e2.getX() < -30) {
			currentIndex--;
			if (currentIndex < 0) {
				currentIndex = 0;
				Toast.makeText(this, "�Ѿ��ǵ�һ��", Toast.LENGTH_SHORT).show();
				return true;
			}
			splashFlag = !splashFlag;
			setComeInTextViewsContent();
			playAnimation(SPLASH_TO_RIGHT);
			return true;
		}

		return false;
	}

	private void setComeInTextViewsContent() {
		TextView[] textViews = getComeInTextViews();
		for (int i = 0; i < textViews.length; i++) {
			if (i == 0) {
				textViews[i].setText(dataList.get(currentIndex).getEnglish());
			} else if (i == 1) {
				textViews[i].setText(dataList.get(currentIndex).getTranslate());
			}
		}// end for
	}

	/**
	 * ��Ӳ˵�����
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(1, MENU_EXIT, 1, "�˳�");
		menu.add(1, MENU_SETBACKGROUND, 1, "���ñ���");
		menu.add(1, MENU_RESTORE_SHOWALL, 1, "�ָ���ʾ���о���");
		return true;
	}

	/**
	 * �˵����¼���Ӧ
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == MENU_SETBACKGROUND)// ���ñ���
		{
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, SetBackgroundActivity.class);
			this.startActivity(intent);
		} else if (item.getItemId() == MENU_RESTORE_SHOWALL)// �ָ���ʾ���о���
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(
					MainActivity.this);
			builder.setTitle("ȷ���ָ�����ʾ���о�����?");
			builder.setPositiveButton("ȷ��",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							dbService.restoreAllItems();// �ָ����ݿ�
							dataList = dbService.selectItemListAlive();// ����List����
							setComeInTextViewsContent();
						}
					});
			builder.setNegativeButton("ȡ��", null);
			builder.create().show();
		} else if (item.getItemId() == MENU_EXIT)// �˳�����
		{
			System.exit(0);
		}

		return true;
	}

	public boolean onDown(MotionEvent e) {
		return false;
	}

	public void onLongPress(MotionEvent e) {

	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	public void onShowPress(MotionEvent e) {
	}

	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

}// end class
