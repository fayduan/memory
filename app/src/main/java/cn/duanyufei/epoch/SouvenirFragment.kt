package cn.duanyufei.epoch

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.duanyufei.db.DBDao
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
class SouvenirFragment : Fragment() {

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
    }

    override fun onResume() {
        super.onResume()
        val list = dao.findAllMemory()
        if (list.isNullOrEmpty()) {
            (activity as MemoryActivity).showSnackBar()
        } else {
            adapter.addData(list)
        }
    }

    class SouvenirAdapter(private var context: Activity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        var data = ArrayList<Memory>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val rootView = LayoutInflater.from(context).inflate(R.layout.item_souvenir, parent, false)
            return ViewHolder(rootView)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val data = data[position]
            holder.itemView.txt_title.text = data.text
            holder.itemView.txt_date.text = DateUtil.toDateString(data.date)
            holder.itemView.txt_number.text = data.number.toString()
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
    }

    class ViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView)
}