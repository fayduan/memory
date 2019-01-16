package cn.duanyufei.epoch

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import cn.duanyufei.memory.R
import cn.duanyufei.model.Memory
import cn.duanyufei.util.DateUtil
import kotlinx.android.synthetic.main.item_souvenir.view.*

/**
 * 纪念日fragment
 * Created by fayduan on 2019/1/14.
 */
class SouvenirFragment : BaseFragment() {

    companion object {
        fun newInstance() = SouvenirFragment()
    }

    override fun loadData() {
        val list = dao.findAllMemory()
        if (list.isNullOrEmpty()) {
            (activity as MemoryActivity).showSnackBar()
        } else {
            adapter.setData(list)
            adapter.notifyDataSetChanged()
        }
    }

    override fun getAdapterMethod(): OnAdapter {
        return onAdapter
    }

    private val onAdapter = object : OnAdapter {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val rootView = LayoutInflater.from(context).inflate(R.layout.item_souvenir, parent, false)
            rootView.setOnClickListener(this@SouvenirFragment)
            return ViewHolder(rootView)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, data: Any) {
            data as Memory
            holder.itemView.txt_title.text = data.text
            holder.itemView.txt_date.text = DateUtil.toDateString(data.date)
            holder.itemView.txt_number.text = data.number.toString()
            holder.itemView.tag = position
            if (data.type == 0) {
                holder.itemView.txt_number.setTextColor(context!!.resources.getColor(R.color.red_80, null))
            }
        }
    }

}