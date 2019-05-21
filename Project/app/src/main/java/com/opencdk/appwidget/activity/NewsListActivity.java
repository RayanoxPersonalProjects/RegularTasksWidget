package com.opencdk.appwidget.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.opencdk.appwidget.BaseActivity;
import com.opencdk.appwidget.GConstants;
import com.opencdk.appwidget.R;
import com.opencdk.appwidget.model.Task;
import com.opencdk.appwidget.utils.DataProvider;
import com.opencdk.appwidget.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

public class NewsListActivity extends BaseActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_news_list_main);

        listView = (ListView) findViewById(R.id.listView);
        List<Task> tasks = DataProvider.getRandomNews();
        NewsListAdapter adapter = new NewsListAdapter(this, tasks);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = (Task) parent.getAdapter().getItem(position);

                Intent fillInIntent = new Intent(NewsListActivity.this, NewsDetailActivity.class);
                Bundle extras = new Bundle();
                extras.putString(GConstants.SCHEME_DATA_KEY, task.toJSON().toString());
                fillInIntent.putExtras(extras);
                fillInIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(fillInIntent);
            }
        });

        setTitle(R.string.news_main_title);
    }

    public static class NewsListAdapter extends BaseAdapter {

        private Context mContext = null;
        private List<Task> tasks = null;

        public NewsListAdapter(Context context, List<Task> tasks) {
            if (tasks == null) {
                tasks = new ArrayList<Task>();
            }

            this.mContext = context;
            this.tasks = tasks;
        }

        @Override
        public int getCount() {
            return this.tasks.size();
        }

        @Override
        public Task getItem(int position) {
            return this.tasks.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_widget_news_list_item, parent, false);
            }

            TextView tv_title = ViewUtils.get(convertView, R.id.tv_title);

            Task task = getItem(position);
            tv_title.setText(task.getTitle());

            return convertView;
        }

    }

}
