package cn.duanyufei.memory

import android.support.v4.app.FragmentTransaction
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_memory.*

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memory)
        header.setOnClickListener(this)
        curFragment = TAG_SOUVENIR
        switchFragment(TAG_SOUVENIR)
    }

    private fun switchFragment(fragmentTag: String) {
        val fragment = buildFragment(fragmentTag)
        val transaction = supportFragmentManager.beginTransaction()
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
        val fragmentManager = super.getSupportFragmentManager()
        if (null != fragmentManager) {
            val fragments = fragmentManager.fragments
            if (fragments.size > 0) {
                fragments.forEach {
                    if (it.isVisible) {
                        transaction.hide(it)
                        it.onPause()
                    }
                }
            }
        }
    }

    private fun buildFragment(fragmentTag: String): Fragment {
        var fragment = super.getSupportFragmentManager().findFragmentByTag(fragmentTag)
        if (fragment == null) {
            fragment = SouvenirFragment.newInstance()
            if (TAG_PLAN == fragmentTag) {
                fragment = PlanFragment.newInstance()
            }
        }
        return fragment
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
        }
    }
}