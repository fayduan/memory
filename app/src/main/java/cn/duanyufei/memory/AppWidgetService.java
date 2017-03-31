package cn.duanyufei.memory;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;

public class AppWidgetService extends Service {

    final static String TAG = "AppWidgetService";

    private static final int ALARM_DURATION = 4000;// * 60 * 60 * 1000; // service 自启间隔
    private static final int UPDATE_DURATION = 4000;// * 60 * 60 * 1000;     // Widget 更新间隔 4h
    private static final int UPDATE_MESSAGE = 1000;

    private UpdateHandler updateHandler; // 更新 Widget 的 Handler

    public AppWidgetService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 每个 ALARM_DURATION 自启一次
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(getBaseContext(), AppWidgetService.class);
        PendingIntent pendingIntent = PendingIntent.getService(getBaseContext(), 0,
                alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + ALARM_DURATION, pendingIntent);

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Message message = Message.obtain();
        message.what = UPDATE_MESSAGE;
        updateHandler = new UpdateHandler();
        updateHandler.sendMessageDelayed(message, UPDATE_DURATION);
    }

    private void updateWidget() {
        // 更新 Widget
        SettingsActivity.updateWidget(this);

        // 发送下次更新的消息
        Message message = updateHandler.obtainMessage();
        message.what = UPDATE_MESSAGE;
        updateHandler.sendMessageDelayed(message, UPDATE_DURATION);
    }

    protected final class UpdateHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_MESSAGE:
                    updateWidget();
                    break;
                default:
                    break;
            }
        }
    }
}
