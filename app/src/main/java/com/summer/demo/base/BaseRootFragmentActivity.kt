package com.nufang.fsxq.base.summer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.FragmentActivity
import android.view.View
import com.nufang.fsxq.api.ApiConstants
import com.nufang.fsxq.base.activity.swipe.SwipeBackActivityBase
import com.nufang.fsxq.base.activity.swipe.SwipeBackActivityHelper
import com.nufang.fsxq.base.activity.swipe.SwipeBackLayout
import com.nufang.fsxq.base.activity.swipe.Utils
import com.nufang.fsxq.ui.main.activity.MainActivity
import com.nufang.fsxq.utils.NewReceiverUtils
import com.nufang.nfhelper.server.SummerParameter
import com.nufang.nfhelper.utils.BitmapUtils
import com.nufang.nfhelper.utils.Logs
import com.nufang.nfhelper.web.ActivitysManager
import rx.Observable
import java.lang.ref.WeakReference
import rx.android.schedulers.AndroidSchedulers
import rx.internal.operators.OperatorReplay.observeOn



/**
 * FragmentActivity基类
 * @author xiastars@vip.qq.com
 */
abstract class BaseRootFragmentActivity : FragmentActivity(), SwipeBackActivityBase {

    protected var context: Context? = null
    protected var myHandlder: MyHandler? = null
    private lateinit var baseHelper: BaseHelper //用来处理请求接口
    private lateinit var receiverUtils: NewReceiverUtils
    private var isRefresh: Boolean = false
    private lateinit var mHelper: SwipeBackActivityHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        ActivitysManager.Add(this.javaClass.simpleName, this)
        myHandlder = MyHandler(this)
        baseHelper = BaseHelper(context, myHandlder)
        baseHelper.setMIUIStatusBarDarkMode(this)
        if (this !is MainActivity) {
            mHelper = SwipeBackActivityHelper(this)
            mHelper!!.onActivityCreate()
        }
    }

    /**
     * 初始通知广播
     */
    protected fun initBroadcast(vararg action: String) {
        receiverUtils = NewReceiverUtils(this)
        receiverUtils.setActionsAndRegister(*action)
        receiverUtils.setOnReceiverListener({ action, intent ->
            if (context != null) {
                onMsgReceiver(action, intent)
            }
        })
    }

    /**
     * 子类重载该方法，用来处理相关通知
     */
    open fun onMsgReceiver(type: String, intent: Intent) {

    }

    override fun onDestroy() {
        super.onDestroy()
        if (receiverUtils != null) {
            receiverUtils.unRegisterReceiver()
        }
        BitmapUtils.getInstance().clearBitmaps(javaClass.simpleName)
        context = null
    }

    class MyHandler(activity: BaseRootFragmentActivity) : Handler() {
        private val mActivity: WeakReference<BaseRootFragmentActivity> = WeakReference(activity)

        override fun handleMessage(msg: Message) {
            val activity = mActivity.get()
            if (null != activity) {
                when (msg.what) {
                    BaseHelper.MSG_SUCCEED -> {
                        activity.handleData(msg.arg1, msg.obj)
                        activity.cancelLoading()
                    }
                    BaseHelper.MSG_FINISHLOAD -> activity.finishLoad()
                    BaseHelper.MSG_CACHE -> activity.handleData(msg.arg1, msg.obj)
                    BaseHelper.MSG_ERRO -> {
                        Logs.i("requestCode:" + msg.arg1 + ",,," + msg.arg2)
                        activity.dealErrors(msg.arg1, msg.arg2.toString() + "", msg.obj as String, false)
                        activity.finishLoad()
                    }
                    else -> activity.handleMsg(msg.what, msg.obj)
                }
            }
        }
    }

    /**
     * 1.2接口put数据
     *
     * @param requestCode
     * @param className
     * @param params
     * @param url
     */
    open fun putDataTwo(requestCode: Int, className: Class<*>, params: SummerParameter, url: String) {
        if (baseHelper == null) {
            return
        }
        baseHelper.putData(requestCode, ApiConstants.getHostVersion2(), className, params, url)
    }

    open fun requestData(className: Class<*>, params: SummerParameter, url: String, post: Boolean) {
        requestData(0, className, params, url, post)
    }

    open fun requestData(requestCode: Int, className: Class<*>, params: SummerParameter, url: String, post: Boolean) {
        requestData(requestCode, className, params, url, post, false)
    }

    open fun requestData(requestCode: Int, className: Class<*>, params: SummerParameter, url: String, post: Boolean, isArray: Boolean) {
        baseHelper.setIsRefresh(isRefresh)
        baseHelper.requestData(requestCode, 0, className, params, url, post, isArray)
    }

    open fun requestData(requestCode: Int, limiteTime: Int, className: Class<*>, params: SummerParameter, url: String, post: Boolean) {
        baseHelper.setIsRefresh(isRefresh)
        baseHelper.requestData(requestCode, limiteTime, className, params, url, post)
    }

    open fun requestData(requestCode: Int, limiteTime: Int, className: Class<*>, params: SummerParameter, url: String, post: Boolean, isArray: Boolean) {
        baseHelper.setIsRefresh(isRefresh)
        baseHelper.requestData(requestCode, limiteTime, className, params, url, post, isArray)
    }

    /**
     * 1.2接口put数据
     *
     * @param requestCode
     * @param className
     * @param params
     * @param url
     */
    open fun getDataTwo(requestCode: Int, className: Class<*>, params: SummerParameter, url: String) {
        if (baseHelper == null) {
            return
        }
        baseHelper.requestData(requestCode, ApiConstants.getHostVersion2(), 0, className, params, url, 0, false)
    }

    open fun cancelLoading() {
        baseHelper.cancelLoading()
    }

    /**
     * 处理错误
     *
     * @param requstCode  请求数据标识码
     * @param requestType 返回错误码，根据requestCode判断是返回的Code还是@ErroCode
     * @param errString   错误信息
     * @param requestCode 如果是返回的Code则为true
     */
    protected fun dealErrors(requstCode: Int, requestType: String, errString: String, requestCode: Boolean) {
        if (baseHelper != null) {
            baseHelper.cancelLoading()
            finishLoad()
        }
    }

    /**
     * 处理Handler数据
     */
    protected fun handleMsg(position: Int, `object`: Any) {}

    /**
     * 处理后端返回的数据
     */
    private fun handleData(requestCode: Int, `object`: Any) {
        if (this.isFinishing) {
            return
        }
        dealDatas(requestCode, `object`)
    }

    /**
     * 请求数据
     */
    protected abstract fun loadData()


    abstract fun getContainerView(): View

    /**
     * 分页加载时结束加载
     */
    protected abstract fun finishLoad()

    /**
     * 处理返回的数据
     */
    protected abstract fun dealDatas(requestCode: Int, obj: Any)

    /**
     * 设置当前界面标题
     */
    protected abstract fun setTitleId(): Int

    /**
     * 设置当前界面主体内容
     */
    protected abstract fun setContentView(): Int

    /**
     * 初始化界面与数据
     */
    protected abstract fun initData()

    override fun getSwipeBackLayout(): SwipeBackLayout {
        return mHelper.swipeBackLayout
    }


    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (mHelper != null && context != null) {
            mHelper.onPostCreate()
        }
    }

    override fun setSwipeBackEnable(enable: Boolean) {
        swipeBackLayout.setEnableGesture(enable)
    }

    override fun scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this)
        swipeBackLayout.scrollToFinishActivity()
    }
}