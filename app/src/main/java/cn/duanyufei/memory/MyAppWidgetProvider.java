package com.minor.memory;

import com.minor.db.DBDao;
import com.minor.model.Memory;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;

public class MyAppWidgetProvider extends AppWidgetProvider {

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		super.onDisabled(context);
	}

	@Override
	public void onEnabled(Context context) {
		// TODO Auto-generated method stub
		super.onEnabled(context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
		
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		
		//Intent intent = getIntent();
		int n = appWidgetIds.length;
		for (int i = 0; i < n; i++)
		{
			int mId = ConfigActivity.getID(context, appWidgetIds[i]);

			
			Message message = new Message();
			message.arg1 = appWidgetIds[i];
			message.arg2 = mId;
			updateView(context, appWidgetManager, message);
		}
	}

	private static Handler getHandler(final Context context,
			final AppWidgetManager appWidgetManager)
	{
		Handler handler = new Handler()
		{
			public void handleMessage(Message msg)
			{

				updateView(context, appWidgetManager, msg);
				super.handleMessage(msg);
			}
		};
		return handler;
	}
	
	public static void sendMsg(final Context context,
			AppWidgetManager appWidgetManager, final int appWidgetId,
			final int mId)
	{

		final Handler handler = getHandler(context, appWidgetManager);

		Message message = new Message();
		message.arg1 = appWidgetId;
		message.arg2 = mId;
		handler.sendMessage(message);
		Log.i("minor", "2sendmsg,appWidgetID="+appWidgetId+",MID="+mId);
	}
	
	public static void updateView(Context context,
			AppWidgetManager appWidgetManager, Message msg)
	{
		Log.i("minor", "update,appWidgetID="+msg.arg1+",MID="+msg.arg2);
		try
		{
			DBDao dao = new DBDao(context);
			RemoteViews views = null;
			//
			int mAppWidgetID = msg.arg1;
			int mID = msg.arg2;
			Memory m = dao.find(mID);
			views = new RemoteViews(context.getPackageName(),
					R.layout.widget_view_light);
			views.setTextViewText(R.id.widget_text, m.getText());
			views.setTextViewText(R.id.widget_number, m.getNumber()+"");

			appWidgetManager.updateAppWidget(mAppWidgetID, views);
		}

		catch (Exception e)
		{

		}

	}
}
