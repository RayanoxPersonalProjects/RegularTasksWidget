package com.rb.android.regularTasksWidget.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

import com.rb.android.regularTasksWidget.GConstants;
import com.rb.android.regularTasksWidget.R;
import com.rb.android.regularTasksWidget.model.Task;

/**
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2016-3-22
 * @Modify 2016-3-22
 */
public class NewsRemoteViews extends RemoteViews {

	private Context mContext;

	private AppWidgetManager mAppWidgetManager;

	private int[] mAppWidgetIds;

	public NewsRemoteViews(Context context) {
		super(context.getPackageName(), R.layout.layout_widget_news_list);
		init(context);
	}

	private void init(Context context) {
		this.mContext = context;
		this.mAppWidgetManager = AppWidgetManager.getInstance(mContext);
		this.mAppWidgetIds = getAppWidgetIds();
	}

	private Class<? extends AppWidgetProvider> getAppWidgetProvider() {
		return NewsAppWidgetProvider.class;
	}

	private Intent getProviderIntent() {
		return new Intent(mContext, getAppWidgetProvider());
	}

	public int[] getAppWidgetIds() {
		ComponentName provider = new ComponentName(mContext, getAppWidgetProvider());
		return mAppWidgetManager.getAppWidgetIds(provider);
	}

	public void loading() {
		final int widgetLoading = R.id.widget_loading;
		final int widgetRefresh = R.id.widget_refresh;
		setViewVisibility(widgetLoading, View.VISIBLE);
		setViewVisibility(widgetRefresh, View.GONE);
		mAppWidgetManager.updateAppWidget(mAppWidgetIds, this);
	}

	public void loadComplete() {
		final int widgetLoading = R.id.widget_loading;
		final int widgetRefresh = R.id.widget_refresh;
		setViewVisibility(widgetLoading, View.GONE);
		setViewVisibility(widgetRefresh, View.VISIBLE);
		mAppWidgetManager.updateAppWidget(mAppWidgetIds, this);
	}

	public void setOnLogoClickPendingIntent() {
		Intent intent = getProviderIntent();
		intent.setAction(NewsAppWidgetProvider.ACTION_JUMP_LOGO);
		PendingIntent logoPendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);
		setOnClickPendingIntent(R.id.widget_logo, logoPendingIntent);
	}

	public void setOnRefreshClickPendingIntent() {
		Intent intent = getProviderIntent();
		intent.setAction(NewsAppWidgetProvider.ACTION_REFRESH_MANUAL);
		PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);
		setOnClickPendingIntent(R.id.widget_refresh, refreshPendingIntent);
	}

	public void setOnFutureRefreshClickPendingIntent() {
		Intent intent = getProviderIntent();
		intent.setAction(NewsAppWidgetProvider.ACTION_REFRESH_FUTURE_MANUAL);
		PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);
		setOnClickPendingIntent(R.id.widget_refresh_future, refreshPendingIntent);
	}
	
	public void bindListViewAdapter() {
		int listViewResId = R.id.listView;
		Intent serviceIntent = new Intent(mContext, NewsWidgetService.class);
		serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
		// rv.setEmptyView(listViewResId, R.id.tv_empty);
		setRemoteAdapter(listViewResId, serviceIntent);


		Intent listItemIntent = getProviderIntent();
		listItemIntent.setAction(NewsAppWidgetProvider.ACTION_JUMP_LISTITEM);
		listItemIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, listViewResId);
		PendingIntent pendingIntentTemplate = PendingIntent.getBroadcast(mContext, 0, listItemIntent, 0);

		setPendingIntentTemplate(listViewResId, pendingIntentTemplate);
	}

	public void notifyAppWidgetViewDataChanged() {
		int[] appIds = getAppWidgetIds();

		mAppWidgetManager.notifyAppWidgetViewDataChanged(appIds, R.id.listView);
	}

	/**
	 * 
	 * @param task
	 * @return
	 */
	public RemoteViews applyNewsView(final Task task) {
		if (task == null) {
			return null;
		}

		RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.layout_widget_news_list_item);
		views.setViewVisibility(R.id.tv_title, View.VISIBLE);

		//TODO : Mettre la donnée du contenu de la ligne de la liste dans l'intent
		views.setTextViewText(R.id.tv_title, task.getName());
		views.setTextColor(R.id.tv_title, task.getIsFuture() ? Color.BLUE : task.getIsCompleted() ? Color.GREEN : Color.RED);

		Intent fillInIntent = new Intent();
		Bundle extras = new Bundle();
		extras.putString(GConstants.SCHEME_DATA_KEY, task.toJSON().toString());
		fillInIntent.putExtras(extras);

		views.setOnClickFillInIntent(R.id.news_container, fillInIntent);

		return views;
	}

}
