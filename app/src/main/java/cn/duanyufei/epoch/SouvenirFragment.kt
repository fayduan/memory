package cn.duanyufei.epoch

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
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
import kotlin.properties.Delegates

/**
 * 纪念日fragment
 * Created by fayduan on 2019/1/14.
 */
class SouvenirFragment : Fragment(), Handler.Callback {

    companion object {
        fun newInstance() = SouvenirFragment()
        private const val MSG_DELETE = 1
    }

    private var dao = DBDao.getInstance()
    private var adapter by Delegates.notNull<SouvenirAdapter>()
    private val handler = Handler(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_souvenir, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recycler_view.layoutManager = LinearLayoutManager(activity)
        adapter = SouvenirAdapter(activity!!)
        adapter.deleteListener = deleteListener
        recycler_view.adapter = adapter
        loadData()
    }

    private fun loadData() {
        val list = dao.findAllMemory()
        if (list.isNullOrEmpty()) {
            (activity as MemoryActivity).showSnackBar()
        } else {
            adapter.addData(list)
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(hidden){
            //pause
        }else{
            //resume
            loadData()
        }
    }

    override fun handleMessage(msg: Message?): Boolean {
        when (msg?.what) {
            MSG_DELETE -> {
                Toast.makeText(context, "已删除~", Toast.LENGTH_SHORT).show()
                loadData()
                adapter.notifyDataSetChanged()
            }
        }
        return true
    }

    private val deleteListener = object : SouvenirAdapter.OnDeleteListener {
        override fun delete(delId: Long) {
            dao.deleteMemory(delId)
            handler.sendEmptyMessage(MSG_DELETE)
        }
    }

    class SouvenirAdapter(private var context: Activity) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnClickListener, View.OnLongClickListener {

        var data = ArrayList<Memory>()
        var deleteListener: OnDeleteListener? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val rootView = LayoutInflater.from(context).inflate(R.layout.item_souvenir, parent, false)
            rootView.setOnClickListener(this)
            rootView.setOnLongClickListener(this)
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

        fun addData(data: List<Memory>) {
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

        override fun onLongClick(v: View?): Boolean {
            val pos = v?.tag as Int
            delId = data[pos].id
            AlertDialog.Builder(context)
                    .setTitle("某人要弹出来的")
                    .setMessage("是手抖不？")
                    .setPositiveButton("嗯嗯是的", del_cancel)
                    .setNegativeButton("显然不是", del_ok)
                    .show()
            return true
        }

        var delId: Long = 0

        var del_cancel: DialogInterface.OnClickListener = DialogInterface.OnClickListener { arg0, arg1 -> Toast.makeText(context, "手残了吧...", Toast.LENGTH_SHORT).show() }
        var del_ok: DialogInterface.OnClickListener = DialogInterface.OnClickListener { arg0, arg1 ->
            deleteListener?.delete(delId)
        }

        interface OnDeleteListener {
            fun delete(delId: Long)
        }
    }

    class ViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView)
}