package com.xinlan.Kaoyan.Activity;

import com.xinlan.Kaoyan.Adapter.ImageAdapter;
import com.xinlan.Kaoyan.Data.Settings;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Gallery;

/**
 * ���ñ���Activity
 * @author Panyi
 *
 */
public class SetBackgroundActivity extends Activity
{
	
	private Gallery gallery;
	private Button setBackgroundBtn;
	
	private Settings settings;//���ö�д����
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);     
		setContentView(R.layout.setbackground);
		init();
	}
	
	private void getComponents()
	{
		gallery=(Gallery)findViewById(R.id.gallery);
		setBackgroundBtn=(Button)findViewById(R.id.setBackgroundBtn);
	}
	
	private void setAction()
	{
		gallery.setAdapter(new ImageAdapter(this));//��������������
		setBackgroundBtn.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				int id=(int)gallery.getSelectedItemId();
				settings.setBackgroundIndexValue(id);
				SetBackgroundActivity.this.finish();//�رմ�Activity
			}
		});
	}
	
	private void init()
	{
		setTitle("����Ӣ��100��");
		initService();
		getComponents();
		setAction();
	}

	private void initService()
	{
		settings=new Settings(this);
	}
}//end class
