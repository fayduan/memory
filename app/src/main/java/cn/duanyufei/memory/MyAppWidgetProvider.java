package cn.duanyufei.memory;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.widget.RemoteViews;

import cn.duanyufei.db.DBDao;
import cn.duanyufei.model.Memory;
import cn.duanyufei.util.ColorUtil;

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
        for (int i = 0; i < n; i++) {
            int mId = (int) ConfigActivity.getID(context, appWidgetIds[i]);


            Message message = new Message();
            message.arg1 = appWidgetIds[i];
            message.arg2 = mId;
            updateView(context, appWidgetManager, message);
        }
    }

    private static Handler getHandler(final Context context,
                                      final AppWidgetManager appWidgetManager) {
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {

                updateView(context, appWidgetManager, msg);
                super.handleMessage(msg);
            }
        };
        return handler;
    }

    public static void sendMsg(final Context context,
                               AppWidgetManager appWidgetManager, final int appWidgetId,
                               final long mId) {

        final Handler handler = getHandler(context, appWidgetManager);

        Message message = new Message();
        message.arg1 = appWidgetId;
        message.arg2 = (int)mId;
        handler.sendMessage(message);
//        Log.i("minor", "2sendmsg,appWidgetID=" + appWidgetId + ",MID=" + mId);
    }

    public static void updateView(Context context,
                                  AppWidgetManager appWidgetManager, Message msg) {
//        Log.i("minor", "updateMemory,appWidgetID=" + msg.arg1 + ",MID=" + msg.arg2);
        try {
            DBDao dao = DBDao.getInstance();
            RemoteViews views = null;
            //
            int mAppWidgetID = msg.arg1;
            int mID = msg.arg2;
            Memory m = dao.findMemory(mID);
            views = new RemoteViews(context.getPackageName(),
                    R.layout.widget_view_light);
            Intent launchIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.ll_widget, pendingIntent);
            views.setTextViewText(R.id.widget_text, m.getText());
            views.setTextViewText(R.id.widget_number, m.getNumber() + "");
            if (m.getType() == 0) {
                views.setTextColor(R.id.widget_number, ContextCompat.getColor(context, R.color.red));
            }
            views.setTextColor(R.id.widget_text, ColorUtil.getColor(context));
            views.setTextColor(R.id.widget_day, ColorUtil.getColor(context));


            appWidgetManager.updateAppWidget(mAppWidgetID, views);
        } catch (Exception e) {

        }

    }


}
