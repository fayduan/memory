package cn.duanyufei.epoch

import android.support.v7.widget.helper.ItemTouchHelper

/**
 * Created by fayduan on 2019/1/15.
 */
class DragItemTouchHelper(private val callback: DragItemTouchHelpCallback) : ItemTouchHelper(callback) {

    fun setDragEnable(canDrag: Boolean) {
        callback.isCanDrag = canDrag
    }

    fun setSwipeEnable(canSwipe: Boolean) {
        callback.isCanSwipe = canSwipe
    }
}