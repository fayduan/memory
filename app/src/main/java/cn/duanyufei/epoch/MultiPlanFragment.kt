package cn.duanyufei.epoch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.duanyufei.memory.R
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import cn.duanyufei.custom.SectionsPagerAdapter
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import kotlinx.android.synthetic.main.fragment_multi_list.*

/**
 * 多个计划fragment
 * Created by fayduan on 2019/1/14.
 */
class MultiPlanFragment : Fragment(), BottomNavigationBar.OnTabSelectedListener, ViewPager.OnPageChangeListener {

    companion object {
        fun newInstance() = MultiPlanFragment()
    }

    private val fragments = ArrayList<Fragment>()
    private var curIndex = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_multi_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bottom_bar.clearAll()
        bottom_bar.setMode(BottomNavigationBar.MODE_FIXED)
        bottom_bar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
        bottom_bar
                .addItem(BottomNavigationItem(R.mipmap.ic_forward_5_white_36dp, getString(R.string.short_plan)))
                .addItem(BottomNavigationItem(R.mipmap.ic_forward_10_white_36dp, getString(R.string.middle_plan)))
                .addItem(BottomNavigationItem(R.mipmap.ic_forward_30_white_36dp, getString(R.string.far_plan)))
                .initialise()
        bottom_bar.setTabSelectedListener(this)

        fragments.add(PlanFragment.newInstance(0))
        fragments.add(PlanFragment.newInstance(1))
        fragments.add(PlanFragment.newInstance(2))

        view_pager.adapter = SectionsPagerAdapter(childFragmentManager, fragments)
        view_pager.addOnPageChangeListener(this)
        view_pager.currentItem = 0
    }

    override fun onTabReselected(position: Int) {

    }

    override fun onTabSelected(position: Int) {
        curIndex = position
        view_pager.currentItem = position
    }

    override fun onTabUnselected(position: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        curIndex = position
        (fragments[position] as PlanFragment).loadData()
        bottom_bar.selectTab(position)
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    fun loadData() {
        if (fragments.isNotEmpty()) {
            (fragments[curIndex] as PlanFragment).loadData()
        }
    }

}