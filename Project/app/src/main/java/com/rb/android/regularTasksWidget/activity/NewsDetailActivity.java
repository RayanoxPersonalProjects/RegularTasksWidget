package com.rb.android.regularTasksWidget.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.rb.android.regularTasksWidget.BaseActivity;
import com.rb.android.regularTasksWidget.GConstants;
import com.rb.android.regularTasksWidget.R;
import com.rb.android.regularTasksWidget.model.Task;


public class NewsDetailActivity extends BaseActivity {

	private TextView newsTitle;
	private TextView newsMessage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_news_detail_main);

		setTitle(R.string.news_detail_title);

		newsTitle = (TextView) findViewById(R.id.news_title);
		newsMessage = (TextView) findViewById(R.id.news_message);

		Intent intent = getIntent();
		if (intent != null) {
			Bundle extras = intent.getExtras();
			if (extras != null) {
				String jsonString = extras.getString(GConstants.SCHEME_DATA_KEY);
				if (!TextUtils.isEmpty(jsonString)) {
					handleNewsDetail(jsonString);
				}
			}
		}
	}

	private void handleNewsDetail(String jsonString) {
		Task task = Task.toObject(jsonString);
		if (task == null) {
			return;
		}

		newsTitle.setText(task.getName());
		newsMessage.setText("所有新闻内容, 均属于测试数据!!!");
	}

}
