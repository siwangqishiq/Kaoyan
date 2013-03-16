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
	private GestureDetector gestureDetector;// 手势识别
	private TextView txtEngOne;// 显示英文面板1
	private TextView txtChOne;// 显示中文翻译面板1
	private TextView txtEngTwo;// 显示英文面板2
	private TextView txtChTwo;// 显示中文翻译面板2
	private RelativeLayout mainLayout;// 主界面
	private EditText wordEdit;// 新单词
	private EditText chEdit;// 新翻译

	private Button remberBtn;
	private Button hiddenTranslateBtn;
	private Button doAddBtn;
	private Button doClearBtn;
	private Button gotoWordListBtn;
	private Button doTestBtn;

	private boolean splashFlag = true;
	private boolean isTranslateHiiden = false;// 标志翻译是否隐藏
	private int currentIndex = 0;// 当前显示项目索引

	private Integer[] BACKGROUND_ARRAY = Constants.backgrounds;// 背景图数列
	private static int SPALSH_DURATION = 500;// 滑动速度参数
	private static int TRANSLATE_DISPLAY_DURING = 600;// 翻译隐藏 显示延时
	private static final int SPLASH_TO_LEFT = 0;// 向左滑动
	private static final int SPLASH_TO_RIGHT = 1;// 向右滑动

	private static final int MENU_RESTORE_SHOWALL = 2;// 恢复显示所有句子
	private static final int MENU_SETBACKGROUND = 1;
	private static final int MENU_EXIT = 0;

	public DatabaseService dbService;// 数据库服务类
	private Settings settings;
	private List<Item> dataList;// 数据列表

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
		Toast.makeText(this, "左右滑动切换句子", Toast.LENGTH_LONG).show();// 提示操作文字
		init();

		addDombAD();
	}

	/**
	 * 设置控件数据
	 */
	private void setData() {
		if (dbService.getItemsNum() != 100)// 首次启动，数据库中还没有数据
		{
			ReadFileService readFileService = new ReadFileService(this);
			dataList = readFileService.readOriginFile();// 从文件中读取数据
			new LoadItemsThread(dbService, dataList).start();// 启动导入数据线程
			remberBtn.setEnabled(false);
			remberBtn.setVisibility(View.INVISIBLE);
			doTestBtn.setVisibility(View.INVISIBLE);
		} else {
			// 从数据库读取数据
			dataList = dbService.selectItemListAlive();
		}
		currentIndex = settings.getLastItemIndexValue();
		Item item = dataList.get(currentIndex);
		txtEngOne.setText(item.getEnglish());
		txtChOne.setText(item.getTranslate());
	}

	@Override
	protected void onDestroy() {
		settings.setLastItemIndexValue(currentIndex); // 存入ITEM值
		super.onDestroy();
	}

	/**
	 * 初始化
	 */
	private void init() {
		setTitle("考研英语100句");
		initService();
		findViews();
		addEventListener();
		setData();
	}

	/**
	 * 注入服务项目
	 */
	private void initService() {
		settings = new Settings(this);
		dbService = new DatabaseService(this);
		app = (KaoyanApp) getApplication(); // 获得自定义的应用程序MyApp
		app.setDbService(dbService);
	}

	/**
	 * 设置背景
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
	 * 获取控件
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
	 * 录入单词
	 * 
	 * @param wordContent
	 * @param ch
	 * @return
	 */
	private long addNewWord(String wordContent, String ch) {
		Word word = dbService.selectWordByEng(wordContent);// 根据单词查询
		long ret = -1;
		if (word == null)// 原数据库中没有相关的数据
		{
			// 插入新数据
			ret = dbService.addNewWordToData(wordContent, ch);
		} else {
			// 有相关单词
			ret = dbService.updateWordChData(ch, word);
		}
		return ret;
	}

	/**
	 * 注册事件监听
	 */
	private void addEventListener() {
		gestureDetector = new GestureDetector(this);

		doTestBtn.setOnClickListener(new OnClickListener() {// 转到测试
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClass(MainActivity.this, DoTestActivity.class);
						MainActivity.this.startActivity(intent);
					}
				});

		gotoWordListBtn.setOnClickListener(new OnClickListener() {// 转到单词列表
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClass(MainActivity.this,
								WordListActivity.class);
						MainActivity.this.startActivity(intent);

					}
				});

		remberBtn.setOnClickListener(new OnClickListener() {// 记住此句按钮
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

		doAddBtn.setOnClickListener(new OnClickListener() // 加入新单词
		{
			public void onClick(View v) {
				String word = wordEdit.getText().toString().trim();
				String ch = chEdit.getText().toString().trim();
				if (RegExpValidator.IsEnglishWord(word)) {
					if (addNewWord(word, ch) > 0) {
						Toast.makeText(MainActivity.this, "记录成功!",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(MainActivity.this, "输入单词不正确",
							Toast.LENGTH_SHORT).show();
					wordEdit.setText("");
				}
			}
		});

		doClearBtn.setOnClickListener(new OnClickListener() // 清空录入内容
				{
					public void onClick(View v) {
						wordEdit.setText("");
						chEdit.setText("");
					}
				});

		hiddenTranslateBtn.setOnClickListener(new OnClickListener()// 隐藏 或 显示翻译
				{
					public void onClick(View v) {
						isTranslateHiiden = !isTranslateHiiden;
						if (isTranslateHiiden)// 隐藏翻译
						{
							AlphaAnimation alpha = new AlphaAnimation(1.0f, 0);
							alpha.setDuration(TRANSLATE_DISPLAY_DURING);
							TextView textView = getComeOutChView();
							textView.setAnimation(alpha);
							textView.setVisibility(View.INVISIBLE);
							hiddenTranslateBtn.setText("显示翻译");
						} else// 显示翻译
						{
							AlphaAnimation alpha = new AlphaAnimation(0, 1.0f);
							alpha.setDuration(TRANSLATE_DISPLAY_DURING);
							TextView textView = getComeOutChView();
							textView.setAnimation(alpha);
							textView.setVisibility(View.VISIBLE);
							hiddenTranslateBtn.setText("隐藏翻译");
						}
					}
				});
	}

	/**
	 * 获取移入 画面的显示控件
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
	 * 获取移出 画面的显示控件
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
		TextView[] leaveTextViews = getComeOutTextViews();// 获取移出的控件
		TextView[] comeTextViews = getComeInTextViews();// 获取移入的控件
		int num = isTranslateHiiden ? 1 : 2;
		for (int i = 0; i < num; i++) {
			// 移出
			TextView leaveView = leaveTextViews[i];
			TranslateAnimation translateLeave = new TranslateAnimation(
					Animation.RELATIVE_TO_PARENT, direct[0],
					Animation.RELATIVE_TO_PARENT, direct[1],
					Animation.RELATIVE_TO_PARENT, 0,
					Animation.RELATIVE_TO_PARENT, 0);
			translateLeave.setDuration(SPALSH_DURATION);
			leaveView.setAnimation(translateLeave);
			leaveView.setVisibility(View.INVISIBLE);// 设置控件为不可见
			// 移入
			TextView comeView = comeTextViews[i];
			TranslateAnimation translateCome = new TranslateAnimation(
					Animation.RELATIVE_TO_PARENT, direct[2],
					Animation.RELATIVE_TO_PARENT, direct[3],
					Animation.RELATIVE_TO_PARENT, 0,
					Animation.RELATIVE_TO_PARENT, 0);
			translateCome.setDuration(SPALSH_DURATION);
			comeView.setAnimation(translateCome);
			comeView.setVisibility(View.VISIBLE);// 设置控件可见
		}// end for
	}

	/**
	 * 监听手指滑动事件
	 */
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() - e2.getX() > 30)// 后一句
		{
			currentIndex++;
			if (currentIndex >= dataList.size()) {
				currentIndex = dataList.size() - 1;
				Toast.makeText(this, "已经是最后一句", Toast.LENGTH_SHORT).show();
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
				Toast.makeText(this, "已经是第一句", Toast.LENGTH_SHORT).show();
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
	 * 添加菜单内容
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(1, MENU_EXIT, 1, "退出");
		menu.add(1, MENU_SETBACKGROUND, 1, "设置背景");
		menu.add(1, MENU_RESTORE_SHOWALL, 1, "恢复显示所有句子");
		return true;
	}

	/**
	 * 菜单的事件响应
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == MENU_SETBACKGROUND)// 设置背景
		{
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, SetBackgroundActivity.class);
			this.startActivity(intent);
		} else if (item.getItemId() == MENU_RESTORE_SHOWALL)// 恢复显示所有句子
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(
					MainActivity.this);
			builder.setTitle("确定恢复，显示所有句子吗?");
			builder.setPositiveButton("确认",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							dbService.restoreAllItems();// 恢复数据库
							dataList = dbService.selectItemListAlive();// 更新List数据
							setComeInTextViewsContent();
						}
					});
			builder.setNegativeButton("取消", null);
			builder.create().show();
		} else if (item.getItemId() == MENU_EXIT)// 退出程序
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
