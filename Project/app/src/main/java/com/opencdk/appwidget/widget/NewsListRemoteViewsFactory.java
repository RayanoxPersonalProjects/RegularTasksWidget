package com.opencdk.appwidget.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.util.TypedValue;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.opencdk.appwidget.R;
import com.opencdk.appwidget.model.Task;
import com.opencdk.appwidget.utils.DataProvider;

import java.util.ArrayList;
import java.util.List;

class NewsListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = "NewsListRemoteViewsFactory";

    private static final int VIEW_TYPE_COUNT = 1;

    private List<Task> mTaskList = new ArrayList<Task>();

    private Context mContext;

    public NewsListRemoteViewsFactory(Context context, Intent intent) {
        this.mContext = context;
    }

    @Override
    public void onCreate() {
        // TODO: C'est ici que je dois coder la recuperation complete de la liste des tasks du jour
    }

    @Override
    public int getCount() {
        if (mTaskList == null) {
            return 0;
        }

        return mTaskList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private Task getNews(int index) {
        return mTaskList.get(index);
    }

    @Override
    public RemoteViews getLoadingView() {
        // System.out.println("getLoadingView");
        return null;
    }

    @SuppressLint("NewApi")
    @Override
    public RemoteViews getViewAt(int position) {
        if (getCount() == 0) {
            return null;
        }

        System.out.println("getViewAt");

        NewsRemoteViews newsRemoteViews = new NewsRemoteViews(mContext);
        newsRemoteViews.loadComplete();

        Task taskItem = getNews(position);

        taskItem.setCompleted(position % 2 == 0); // TODO: A virer


        return newsRemoteViews.applyNewsView(taskItem);
    }

    /**
     * 设置字体大小
     *
     * @param views
     * @param viewId
     * @param textSize
     */
    @SuppressLint("NewApi")
    private void setRemoteViewsTextSize(RemoteViews views, int viewId, int textSize) {
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            // 低版本不支持字体适配
            views.setTextViewTextSize(viewId, TypedValue.COMPLEX_UNIT_SP, textSize / 2);
        }
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDataSetChanged() {
        mTaskList.clear();

        SystemClock.sleep(2000);
        mTaskList = getNews();
    }

    private List<Task> getNews() {
        return DataProvider.getRandomNews();
    }

    @Override
    public void onDestroy() {
        mTaskList.clear();
    }

//	private void setNetImage(final Context context) {
//		// 对图片的特殊处理逻辑
//		Bitmap src = imageManager.obtainBitmapRemoteViews(item.img);
//		if (src != null) {
//			item.bitmap = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Config.ARGB_8888);
//			Canvas c = new Canvas(item.bitmap);
//			c.drawBitmap(src, new Matrix(), null);
//		}
//	}

}