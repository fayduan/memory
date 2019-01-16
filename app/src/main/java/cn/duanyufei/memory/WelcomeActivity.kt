package cn.duanyufei.memory

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import cn.duanyufei.db.DBDao
import cn.duanyufei.epoch.MemoryActivity
import cn.duanyufei.util.UpdateTask

//import com.idescout.sql.SqlScoutServer;

class WelcomeActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_welcome)

        UpdateTask(this).update()
        val appWidgetIntent = Intent(this, AppWidgetService::class.java)
        startService(appWidgetIntent)
        SettingsActivity.updateWidget(this)

        val dao = DBDao.getInstance()
        val datas = dao.findAllMemory()
        datas.forEachIndexed { index, memory ->
            dao.updateMemory(memory, index)
        }

        Handler().postDelayed({
            val intent = Intent(this@WelcomeActivity, MemoryActivity::class.java)
            startActivity(intent)
            this@WelcomeActivity.finish()
        }, 700)

        //        SqlScoutServer.create(this, getPackageName());
    }

}
