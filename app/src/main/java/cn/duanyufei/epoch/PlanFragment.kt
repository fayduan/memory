package cn.duanyufei.epoch

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.duanyufei.memory.R

/**
 * 计划fragment
 * Created by fayduan on 2019/1/14.
 */
class PlanFragment : BaseFragment() {

    companion object {
        fun newInstance() = PlanFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_plan, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun doResume() {

    }

    override fun doPause() {

    }
}