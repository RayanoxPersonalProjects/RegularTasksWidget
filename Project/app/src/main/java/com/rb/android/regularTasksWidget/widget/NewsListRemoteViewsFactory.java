package com.rb.android.regularTasksWidget.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.TypedValue;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.rb.android.regularTasksWidget.model.Task;
import com.rb.android.regularTasksWidget.utils.DataProvider;

import java.util.ArrayList;
import java.util.List;


class NewsListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final int VIEW_TYPE_COUNT = 1;

    private List<Task> mTaskList = new ArrayList<Task>();

    private Context mContext;

    private boolean firstRetrieve = true;


    public NewsListRemoteViewsFactory(Context context, Intent intent) {
        this.mContext = context;
    }

    @Override
    public void onCreate() {

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

    private Task getTasks(int index) {
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

        Task taskItem = getTasks(position);

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
        mTaskList.clear(); //use less because the object is abandoned then to welcome a new instance

        SystemClock.sleep(2000);

        mTaskList = getTasks();
    }


    private List<Task> getTasks() {
        List<Task> results = DataProvider.getAllTasksOfDay(mContext, false);
        firstRetrieve = false;
        return results;
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