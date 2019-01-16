package cn.duanyufei.memory

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.os.Bundle
import android.support.annotation.IdRes
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.DatePicker
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast

import java.util.Calendar

import cn.duanyufei.db.DBDao
import kotlinx.android.synthetic.main.activity_add.*

class AddActivity : Activity(), View.OnClickListener {

    private var mname: EditText? = null
    private var dp: DatePicker? = null
    private var dao = DBDao.getInstance()
    private var calendar: Calendar? = null
    private var selId: Long = 0
    private var type: Int = 0
    private var context: Context? = null
    private var planType: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        selId = intent.getLongExtra("id", -1L)
        type = intent.getIntExtra("type", 0)

        setContentView(R.layout.activity_add)

        btn_back.setOnClickListener(this)
        btn_add.setOnClickListener(this)

        mname = findViewById<View>(R.id.mname) as EditText
        mname!!.inputType = EditorInfo.TYPE_CLASS_TEXT
        dp = findViewById<View>(R.id.add_dp) as DatePicker
        calendar = Calendar.getInstance()
        rg_pos.setOnCheckedChangeListener{ _, checkedId ->
            when (checkedId) {
                R.id.rb_short -> planType = 0
                R.id.rb_middle -> planType = 1
                R.id.rb_far -> planType = 2
            }
        }
        if (type == 0) {
            // memory
            if (selId != -1L) {
                val m = dao!!.findMemory(selId)
                calendar!!.time = m.date
                mname!!.setText(m.text)
                title_name.text = getText(R.string.change_memory)
            } else {
                title_name.text = getText(R.string.add_memory)
            }
            rg_pos.visibility = View.GONE
        } else {
            // plan
            txt_date_desc.text = "不知道咋的日期"
            if (selId != -1L) {
                val p = dao!!.findPlan(selId)
                calendar!!.time = p.date
                mname!!.setText(p.text)
                planType = p.type
                when (planType) {
                    0 -> rg_pos.check(R.id.rb_short)
                    1 -> rg_pos.check(R.id.rb_middle)
                    2 -> rg_pos.check(R.id.rb_far)
                }
                title_name.text = getText(R.string.change_plan)
            } else {
                title_name.text = getText(R.string.add_plan)
                rg_pos.check(R.id.rb_short)
            }
        }
        val year = calendar!!.get(Calendar.YEAR)
        val monthOfYear = calendar!!.get(Calendar.MONTH)
        val dayOfMonth = calendar!!.get(Calendar.DAY_OF_MONTH)
        dp!!.init(year, monthOfYear, dayOfMonth) { view, year, monthOfYear, dayOfMonth ->
            //Toast.makeText(getApplicationContext(), year+"."+(monthOfYear+1)+"."+dayOfMonth, Toast.LENGTH_SHORT).show();
            calendar!!.set(year, monthOfYear, dayOfMonth)
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_add -> {
                val text = mname!!.text.toString()
                if (!text.isEmpty()) {
                    if (type == 0) {
                        if (selId != -1L) {
                            //Toast.makeText(getApplicationContext(), selId+text+calendar.toString(), Toast.LENGTH_SHORT).show();
                            dao.updateMemory(selId, text, calendar!!.time)
                            val appWidgetId = ConfigActivity.getAwID(context!!, selId)
                            val awm = AppWidgetManager.getInstance(context)
                            MyAppWidgetProvider.sendMsg(context, awm, appWidgetId, selId)
                            Log.i("minor", "1sendmsg,appWidgetID=$appWidgetId,MID=$selId")
                        } else {
                            dao.addMemory(text, calendar!!.time)

                            val datas = dao.findAllMemory()
                            datas.forEachIndexed { index, memory ->
                                dao.updateMemory(memory, index)
                            }
                        }
                    } else {
                        if (selId != -1L) {
                            dao.updatePlan(selId, text, calendar!!.time, planType)
                        } else {
                            dao.addPlan(text, calendar!!.time, planType)
                            val datas = dao.findAllPlanByType(planType)
                            datas.forEachIndexed { index, plan ->
                                dao.updatePlan(plan, index)
                            }
                        }
                    }

                    this.finish()
                } else {
                    Toast.makeText(applicationContext, R.string.empty, Toast.LENGTH_SHORT).show()
                }
            }
            R.id.btn_back -> {
                finish()
            }
        }
    }
}
