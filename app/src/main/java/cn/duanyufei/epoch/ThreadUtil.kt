package cn.duanyufei.epoch

import android.os.Handler
import android.os.HandlerThread
import kotlin.properties.Delegates

/**
 * Thread
 * Created by fayduan on 2019/1/14.
 */
class ThreadUtil private constructor() {

    companion object {
        val instance = ThreadUtilHolder.holder
    }

    private object ThreadUtilHolder {
        val holder = ThreadUtil()
    }

    private val dbThread = HandlerThread("db")
    private val subThread = HandlerThread("sub")
    private val netThread = HandlerThread("net")
    private var dbHandler by Delegates.notNull<Handler>()
    private var subHandler by Delegates.notNull<Handler>()
    private var netHandler by Delegates.notNull<Handler>()

    init {
        dbThread.start()
        subThread.start()
        netThread.start()
        dbHandler = Handler(dbThread.looper)
        subHandler = Handler(subThread.looper)
        netHandler = Handler(netThread.looper)
    }

    fun executeDb(runnable: Runnable) {
        dbHandler.post(runnable)
    }

    fun destroy() {
        dbHandler.removeCallbacksAndMessages(null)
        subHandler.removeCallbacksAndMessages(null)
        netHandler.removeCallbacksAndMessages(null)
        dbThread.quitSafely()
        subThread.quitSafely()
        netThread.quitSafely()
    }
}