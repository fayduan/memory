package cn.duanyufei.epoch

import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.duanyufei.memory.R
import cn.duanyufei.model.Plan
import cn.duanyufei.util.DateUtil
import kotlinx.android.synthetic.main.item_plan.view.*
import android.graphics.Rect
import android.view.TouchDelegate
import android.widget.ImageView


/**
 * 计划fragment
 * Created by fayduan on 2019/1/14.
 */
class PlanFragment : BaseFragment() {

    companion object {
        fun newInstance() = PlanFragment()
    }

    override fun loadData() {
        val list = dao.findAllPlan()
        if (list.isNullOrEmpty()) {
            (activity as MemoryActivity).showSnackBar()
        } else {
            adapter?.setData(list)
            adapter?.notifyDataSetChanged()
        }
    }

    override fun getAdapterMethod(): OnAdapter {
        return onAdapter
    }

    private val onAdapter = object : OnAdapter {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val rootView = LayoutInflater.from(context).inflate(R.layout.item_plan, parent, false)
            rootView.checkbox.setOnClickListener(this@PlanFragment)
            expandViewTouchDelegate(rootView.checkbox, 30, 30, 30, 30)
            rootView.setOnClickListener(this@PlanFragment)
            return ViewHolder(rootView)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, data: Any) {
            data as Plan
            if (data.isDone) {
                holder.itemView.checkbox.setImageResource(R.mipmap.btn_item_checked)
                holder.itemView.txt_plan_title.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG
            } else {
                holder.itemView.checkbox.setImageResource(R.mipmap.btn_item_unchecked)
                holder.itemView.txt_plan_title.paint.flags = 0
            }
            holder.itemView.txt_plan_title.text = data.text
            holder.itemView.txt_plan_date.text = DateUtil.toDateString(data.date)
            holder.itemView.tag = position
            holder.itemView.checkbox.tag = position
        }
    }

    override fun onClick(v: View?) {
        if (v is ImageView) {
            val pos = v.tag as Int
            val data = adapter!!.data[pos] as Plan
            data.isDone = !data.isDone
            adapter!!.notifyDataSetChanged()
            dao.updatePlan(data)
        } else {
            super.onClick(v)
        }
    }

    fun expandViewTouchDelegate(touchView: View?, top: Int, bottom: Int, left: Int, right: Int) {
        if (touchView == null) {
            return
        }
        (touchView.parent as View).post {
            val bounds = Rect()
            touchView.isEnabled = true
            touchView.getHitRect(bounds)

            bounds.top -= top
            bounds.bottom += bottom
            bounds.left -= left
            bounds.right += right

            val touchDelegate = TouchDelegate(bounds, touchView)
            if (View::class.java.isInstance(touchView.parent)) {
                (touchView.parent as View).touchDelegate = touchDelegate
            }
        }
    }
}