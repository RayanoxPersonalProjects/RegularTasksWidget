package com.opencdk.appwidget.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.opencdk.appwidget.GConstants;
import com.opencdk.appwidget.activity.NewsListActivity;
import com.opencdk.appwidget.clients.GoogleTaskMediationClient;
import com.opencdk.appwidget.model.Task;
import com.opencdk.appwidget.utils.DataProvider;

/**
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2016-3-22
 * @Modify 2016-3-22
 */
public class NewsAppWidgetProvider extends AppWidgetProvider {

	// Lors de la creation du widget, on obtiens la sequence suivante de reception d'intents:
	//
	//      1) android.appwidget.action.APPWIDGET_ENABLED
	//      2) android.appwidget.action.APPWIDGET_UPDATE
	//      3) android.appwidget.action.APPWIDGET_REFRESH_AUTO


	private static final String TAG = "NewsAppWidgetProvider";

	public static final String ACTION_REFRESH_MANUAL = "com.opencdk.appwidget.action.APPWIDGET_REFRESH_MANUAL";
	public static final String ACTION_REFRESH_AUTO = "com.opencdk.appwidget.action.APPWIDGET_REFRESH_AUTO";
	public static final String ACTION_JUMP_LISTITEM = "com.opencdk.appwidget.action.APPWIDGET_JUMP_LISTITEM";
	public static final String ACTION_JUMP_LOGO = "com.opencdk.appwidget.action.APPWIDGET_JUMP_LOGO";

	public static final String KEY_TASK_NAME = "com.opencdk.appwidget.action.APPWIDGET_JUMP_LOGO";

	/** 扩展信息 */
	public static final String EXT_DATA = "ext_data";

	private GoogleTaskMediationClient googleTaskApiClient;


	@Override
	public void onReceive(final Context context, final Intent intent) {

		System.out.println("RAYANE - RECEPTION dans NewsAppWidgetProvider .");

		if (ACTION_JUMP_LOGO.equalsIgnoreCase(intent.getAction())) { // Inutile pour mon cas mais qui pourrait m'être utile à l'avenir
			Intent newsListIntent = new Intent(context, NewsListActivity.class);
			newsListIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(newsListIntent);
		} else if (ACTION_REFRESH_MANUAL.equals(intent.getAction())) {
			Log.d(TAG, "-- APPWIDGET_REFRESH_MANUAL --");

			NewsRemoteViews remoteViews = new NewsRemoteViews(context);
			remoteViews.loading();

			//Run a thread that will synchronize the datas
			new SynchronizerThread(context).start();

		} else if (ACTION_REFRESH_AUTO.equals(intent.getAction())) {
			Log.d(TAG, "-- APPWIDGET_REFRESH_AUTO --");

			/*
			try {
				GoogleTaskApiClient.GetAllTasksOfCurrentDay(context);
				//TODO Appel de test au WS methode 'GetAllTasksOfCurrentDay' <-> Ca ne sera pas un appel définitif (sera à retirer)
			} catch (Exception e) {
				e.printStackTrace();
			}*/



			NewsRemoteViews remoteViews = new NewsRemoteViews(context);
			remoteViews.setOnLogoClickPendingIntent();
			remoteViews.setOnRefreshClickPendingIntent();
			remoteViews.bindListViewAdapter();

			remoteViews.notifyAppWidgetViewDataChanged();

		} else if (ACTION_JUMP_LISTITEM.equals(intent.getAction())) { // Click sur un item du widget
			Log.d(TAG, "-- ACTION_JUMP_LISTITEM --");

			if(intent.getExtras() != null) {
				String jsonString = intent.getExtras().getString(GConstants.SCHEME_DATA_KEY);
				Task task = Task.toObject(jsonString);

				System.out.println("Le nom de la task cliquée = " + task.getName());


				//On met à jour la task sur Google task + dans la liste de données du widget
				DataProvider.UpdateTask(task, context);

				NewsRemoteViews remoteViews = new NewsRemoteViews(context);
				remoteViews.notifyAppWidgetViewDataChanged();

			}else {
				System.out.println("/!\\ RAYANE - Absence de bundle lors de la réception ... /!\\");
			}




/*
			Bundle extras = intent.getExtras();
			if (extras == null) {
				return;
			}
			Intent newIntent = new Intent();
			Uri data = Uri.parse(GConstants.SCHEME_HOST + "?className=com.opencdk.appwidget.activity.NewsDetailActivity");
			newIntent.setData(data);
			newIntent.putExtras(extras);
			newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(newIntent);*/
		} else {
			System.out.println("Unknown Recevicer: " + intent.getAction());
		}

		super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		Log.d(TAG, "onUpdate()");

		if (android.os.Build.VERSION.SDK_INT < 14) {
			Log.e(TAG, "Not support version less than 14!!!");
			return;
		}

		NewsRemoteViews newsRemoteViews = new NewsRemoteViews(context);
		newsRemoteViews.setOnLogoClickPendingIntent();
		newsRemoteViews.setOnRefreshClickPendingIntent();
		newsRemoteViews.bindListViewAdapter();

		// 更新所有的widget
		appWidgetManager.updateAppWidget(appWidgetIds, newsRemoteViews);

		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);

		//googleTaskApiClient = new GoogleTaskMediationClient();

	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
	}





	private class SynchronizerThread extends Thread {

		Context context;

		public SynchronizerThread(Context context) {
			this.context = context;
		}

		@Override
		public void run() {
			super.run();

			DataProvider.getAllTasksOfDay(context, true); // Alimente le dataProvider des valeurs du serveur.

			NewsRemoteViews remoteViews = new NewsRemoteViews(context);
			remoteViews.notifyAppWidgetViewDataChanged();
		}
	}



}
