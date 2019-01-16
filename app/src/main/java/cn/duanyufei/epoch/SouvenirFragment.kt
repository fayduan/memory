package cn.duanyufei.epoch

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import cn.duanyufei.db.DBDao
import cn.duanyufei.memory.AddActivity
import cn.duanyufei.memory.R
import cn.duanyufei.model.Memory
import cn.duanyufei.util.DateUtil
import kotlinx.android.synthetic.main.fragment_souvenir.*
import kotlinx.android.synthetic.main.item_souvenir.view.*
import java.util.*
import kotlin.properties.Delegates

/**
 * 纪念日fragment
 * Created by fayduan on 2019/1/14.
 */
class SouvenirFragment : BaseFragment() {

    companion object {
        fun newInstance() = SouvenirFragment()
    }

    private var dao = DBDao.getInstance()
    private var adapter by Delegates.notNull<SouvenirAdapter>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_souvenir, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recycler_view.layoutManager = LinearLayoutManager(activity)
        adapter = SouvenirAdapter(activity!!)
        recycler_view.adapter = adapter
        val itemTouchHelper = DragItemTouchHelper(DragItemTouchHelpCallback(onItemTouchCallbackListener))
        itemTouchHelper.attachToRecyclerView(recycler_view);
        itemTouchHelper.setDragEnable(true);
        itemTouchHelper.setSwipeEnable(true);
        loadData()
    }

    private fun loadData() {
        val list = dao.findAllMemory()
        if (list.isNullOrEmpty()) {
            (activity as MemoryActivity).showSnackBar()
        } else {
            adapter.setData(list)
        }
    }

    override fun doResume() {
        loadData()
    }

    override fun doPause() {

    }

    private val onItemTouchCallbackListener = object : DragItemTouchHelpCallback.OnItemTouchCallbackListener {
        override fun onSwiped(adapterPosition: Int) {
            if (!adapter.data.isNullOrEmpty()) {
                AlertDialog.Builder(context)
                        .setTitle("某人要弹出来的")
                        .setMessage("是手抖不？")
                        .setPositiveButton("嗯嗯是的", delCancel)
                        .setNegativeButton("显然不是", delOk)
                        .show()
                delPos = adapterPosition
            }
        }

        override fun onMove(srcPosition: Int, targetPosition: Int): Boolean {
            if (!adapter.data.isNullOrEmpty()) {
                // 更换数据源中的数据Item的位置
                Collections.swap(adapter.data, srcPosition, targetPosition)
                // 更新UI中的Item的位置，主要是给用户看到交互效果
                adapter.notifyItemMoved(srcPosition, targetPosition)
                return true
            }
            return false
        }
    }

    var delPos: Int = 0

    var delCancel: DialogInterface.OnClickListener = DialogInterface.OnClickListener { arg0, arg1 ->
        Toast.makeText(context, "手残了吧...", Toast.LENGTH_SHORT).show()
        adapter.notifyDataSetChanged()
    }
    var delOk: DialogInterface.OnClickListener = DialogInterface.OnClickListener { arg0, arg1 ->
        val m = adapter.data.removeAt(delPos)
        dao.deleteMemory(m.id)
        Toast.makeText(context, "已删除~", Toast.LENGTH_SHORT).show()
        adapter.notifyItemRemoved(delPos)
    }

    class SouvenirAdapter(private var context: Activity) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnClickListener {

        var data = ArrayList<Memory>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val rootView = LayoutInflater.from(context).inflate(R.layout.item_souvenir, parent, false)
            rootView.setOnClickListener(this)
            return ViewHolder(rootView)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val data = data[position]
            holder.itemView.txt_title.text = data.text
            holder.itemView.txt_date.text = DateUtil.toDateString(data.date)
            holder.itemView.txt_number.text = data.number.toString()
            holder.itemView.tag = position
            if (data.type == 0) {
                holder.itemView.txt_number.setTextColor(context.resources.getColor(R.color.red_80, null))
            }
        }

        fun setData(data: List<Memory>) {
            this.data.clear()
            this.data.addAll(data)
        }

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onClick(v: View?) {
            val pos = v?.tag as Int
            val data = data[pos]
            val changeIntent = Intent()
            changeIntent.setClass(context, AddActivity::class.java)
            changeIntent.putExtra("id", data.id)
            context.startActivity(changeIntent)
        }
    }

    class ViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView)
}