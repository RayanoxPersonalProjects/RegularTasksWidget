package widget.regularTasks.vue;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

import widget.regularTasks.R;

public class ListeRemoteFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private int mAppWidgetId;

    private ArrayList<String> listeTaches;


    public ListeRemoteFactory (Context context, Intent intent){
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        this.listeTaches = new ArrayList<String>();
        this.listeTaches.add("Faire une randonnée");
        this.listeTaches.add("Jouer à saute mouton");
        this.listeTaches.add("Se rouler par terre");
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {
        this.listeTaches.clear();
    }

    @Override
    public int getCount() {
        return this.listeTaches.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        String taskName = this.listeTaches.get(position);

        RemoteViews viewLine = new RemoteViews(mContext.getPackageName(), R.layout.row_of_list);

        viewLine.setTextViewText(R.id.rowTask, this.listeTaches.get(position));

        return viewLine;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null; //Retourne la vue utilisée durant le chargement
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
