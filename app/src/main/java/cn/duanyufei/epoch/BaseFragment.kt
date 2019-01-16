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
import cn.duanyufei.model.Plan
import kotlinx.android.synthetic.main.fragment_list.*
import java.util.*
import kotlin.properties.Delegates

/**
 * Created by fayduan on 2019/1/15.
 */
abstract class BaseFragment : Fragment(), View.OnClickListener {

    protected var dao = DBDao.getInstance()!!

    protected var adapter: RecyclerAdapter? = null

    protected var delPos: Int = 0

    abstract fun loadData()
    abstract fun getAdapterMethod(): OnAdapter

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            // do pause
        } else {
            // do resume
            loadData()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recycler_view.layoutManager = LinearLayoutManager(activity)
        adapter = RecyclerAdapter(activity!!)
        adapter?.onAdapter = getAdapterMethod()
        recycler_view.adapter = adapter
        val itemTouchHelper = DragItemTouchHelper(DragItemTouchHelpCallback(onItemTouchCallbackListener))
        itemTouchHelper.attachToRecyclerView(recycler_view)
        itemTouchHelper.setDragEnable(true)
        itemTouchHelper.setSwipeEnable(true)
        loadData()
    }

    override fun onClick(v: View?) {
        val pos = v?.tag as Int
        val data = adapter!!.data[pos]
        var type = -1
        var id = -1L
        when (data) {
            is Memory -> {
                id = data.id
                type = 0
            }
            is Plan -> {
                id = data.id
                type = 1
            }
        }
        val changeIntent = Intent()
        changeIntent.setClass(activity!!, AddActivity::class.java)
        changeIntent.putExtra("id", id)
        changeIntent.putExtra("type", type)
        activity!!.startActivity(changeIntent)
    }

    interface OnAdapter {
        fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
        fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, data: Any)
    }

    class RecyclerAdapter(private var context: Activity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        var data = ArrayList<Any>()
        var onAdapter by Delegates.notNull<OnAdapter>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return onAdapter.onCreateViewHolder(parent, viewType)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            onAdapter.onBindViewHolder(holder, position, data[position])
        }

        fun setData(data: List<Any>) {
            this.data.clear()
            this.data.addAll(data)
        }

        override fun getItemCount(): Int {
            return data.size
        }
    }

    class ViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView)

    var delCancel: DialogInterface.OnClickListener = DialogInterface.OnClickListener { arg0, arg1 ->
        Toast.makeText(context, "手残了吧...", Toast.LENGTH_SHORT).show()
        adapter!!.notifyDataSetChanged()
    }
    var delOk: DialogInterface.OnClickListener = DialogInterface.OnClickListener { arg0, arg1 ->
        val data = adapter!!.data.removeAt(delPos)
        when (data) {
            is Memory -> dao.deleteMemory(data.id)
            is Plan -> dao.deletePlan(data.id)
        }
        Toast.makeText(context, "已删除~", Toast.LENGTH_SHORT).show()
        adapter!!.notifyDataSetChanged()

    }

    private val onItemTouchCallbackListener = object : DragItemTouchHelpCallback.OnItemTouchCallbackListener {
        override fun onSwiped(adapterPosition: Int) {
            if (!adapter!!.data.isNullOrEmpty()) {
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
            if (!adapter!!.data.isNullOrEmpty()) {
                // 更换数据源中的数据Item的位置
                Collections.swap(adapter!!.data, srcPosition, targetPosition)
                // 更新UI中的Item的位置，主要是给用户看到交互效果
                adapter!!.notifyItemMoved(srcPosition, targetPosition)
                when (adapter!!.data[srcPosition]) {
                    is Memory -> {
                        dao.updateMemory(adapter!!.data[srcPosition] as Memory, srcPosition)
                        dao.updateMemory(adapter!!.data[targetPosition] as Memory, targetPosition)
                    }
                    is Plan -> {
                        dao.updatePlan(adapter!!.data[srcPosition] as Plan, srcPosition)
                        dao.updatePlan(adapter!!.data[targetPosition] as Plan, targetPosition)
                    }
                }
                return true
            }
            return false
        }
    }
}