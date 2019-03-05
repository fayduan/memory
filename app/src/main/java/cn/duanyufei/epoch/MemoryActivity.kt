package cn.duanyufei.epoch

import android.content.Intent
import android.support.v4.app.FragmentTransaction
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.View
import cn.duanyufei.memory.AddActivity
import cn.duanyufei.memory.AppWidgetService
import cn.duanyufei.memory.R
import cn.duanyufei.memory.SettingsActivity
import cn.duanyufei.util.UpdateTask
import kotlinx.android.synthetic.main.activity_memory.*
import kotlin.properties.Delegates

/**
 *
 * Created by fayduan on 2019/1/14.
 */
class MemoryActivity : FragmentActivity(), View.OnClickListener {

    companion object {
        private const val TAG_SOUVENIR = "souvenir"
        private const val TAG_PLAN = "plan"
    }

    private var curFragment = ""
    private var snackBar: Snackbar? = null
    private var souvenirFragment: SouvenirFragment? = null
    private var multiPlanFragment: MultiPlanFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memory)
        header.setOnClickListener(this)
        curFragment = TAG_SOUVENIR
        switchFragment(TAG_SOUVENIR)
        snackBar = Snackbar.make(fab, R.string.msg_nolist, Snackbar.LENGTH_INDEFINITE).setAction(R.string.button_ok, { snackBar?.dismiss() })
        fab.setOnClickListener(this)
        UpdateTask(this).update()
        val appWidgetIntent = Intent(this, AppWidgetService::class.java)
        startService(appWidgetIntent)
        SettingsActivity.updateWidget(this)
    }

    override fun onResume() {
        super.onResume()
        souvenirFragment?.loadData()
        multiPlanFragment?.loadData()
    }

    fun showSnackBar() {
        snackBar?.show()
    }

    private fun switchFragment(fragmentTag: String) {
        tab_name.text = if (fragmentTag == TAG_SOUVENIR) {
            getText(R.string.app_name)
        } else {
            getText(R.string.tab_plan)
        }
        snackBar?.dismiss()
        val fragment = buildFragment(fragmentTag)
        val transaction = supportFragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        hideCurrentFragment(transaction)
        if (!fragment.isAdded) {
            transaction.add(R.id.fragment_container, fragment, fragmentTag)
        } else {
            transaction.show(fragment)
            fragment.onResume()
        }
        transaction.commitAllowingStateLoss()
    }

    private fun hideCurrentFragment(transaction: FragmentTransaction) {
        supportFragmentManager?.let {
            val fragments = supportFragmentManager.fragments
            if (fragments.size > 0) {
                fragments.forEach { f ->
                    if (f.isVisible) {
                        transaction.hide(f)
                        f.onPause()
                    }
                }
            }
        }
    }

    private fun buildFragment(fragmentTag: String): Fragment {
        val fragment = supportFragmentManager.findFragmentByTag(fragmentTag)
        if (fragment == null) {
            souvenirFragment = SouvenirFragment.newInstance()
            if (TAG_PLAN == fragmentTag) {
                multiPlanFragment = MultiPlanFragment.newInstance()
            }
        }
        return if (fragmentTag == TAG_PLAN) {
            multiPlanFragment!!
        } else {
            souvenirFragment!!
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.header -> {
                curFragment = if (curFragment == TAG_SOUVENIR) {
                    TAG_PLAN
                } else {
                    TAG_SOUVENIR
                }
                switchFragment(curFragment)
            }
            R.id.fab -> {
                val addIntent = Intent()
                addIntent.setClass(this, AddActivity::class.java)
                val type = if (curFragment == TAG_SOUVENIR) {
                    0
                } else {
                    1
                }
                addIntent.putExtra("type", type)
                this.startActivity(addIntent)
            }
        }
    }
}