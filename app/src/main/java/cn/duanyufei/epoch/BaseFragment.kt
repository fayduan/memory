package cn.duanyufei.epoch

import android.support.v4.app.Fragment

/**
 * Created by fayduan on 2019/1/15.
 */
abstract class BaseFragment : Fragment() {

    abstract fun doResume()
    abstract fun doPause()

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            doPause()
        } else {
            doResume()
        }
    }
}