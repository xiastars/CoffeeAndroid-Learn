package com.summer.demo.ui.course.skotlin

import android.os.Build
import android.support.annotation.RequiresApi
import android.view.View
import com.summer.demo.R
import com.summer.demo.module.base.BaseFragment
import com.summer.helper.utils.Logs
import java.util.*

/**
 *
 * @Description: 学习kotlin
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/11/12 15:46
 */

class LearnCkotlinFragment : BaseFragment(){

    fun main(){

    }

    var width = 100//声明一个变量

    val height = 200//声明一个不可更改的变量，忽略类型

    var h : Int = 100//声明一个不可更改的变量



    @RequiresApi(Build.VERSION_CODES.N)
    override fun initView(view: View?) {
        var messagge: String//如果没有个初始值 ，则需指定类型
        messagge = if (width == 100 ) "summer" else "spring"
        //在字符串中通过$来引用局部变量
        var name = "你好$messagge,转义：\$x"
        //在字符串通过$来引用数组
        var args :ArrayList<String> = ArrayList()
        name = "获取第一个${args[0]}"
        //在字符串里引用函数
        name = "${if(width == 100) "summer" else "spring"}!"
        //集合
        val systemUsers : MutableList<Int> = mutableListOf(1,2,3)
        val sudoers : List<Int> = systemUsers
        systemUsers.add(0,12)
        sudoers.forEach{
            i -> Logs.i(i.toString())
        }
        systemUsers.filter { x -> x > 0 }
        val openIssues: MutableSet<String> = mutableSetOf("uniqueDesc","haha")
        var mapDatas :MutableMap<Int,Int> = mutableMapOf(1 to 100,2 to 100,3 to 100)
        mapDatas.forEach { k,v -> Logs.i("k"+k+",,"+v)  }



    }

    override fun dealDatas(requestType: Int, obj: Any?) {
    }

    override fun setContentView(): Int {
        return R.layout.view_container;
    }

    //声明一个有返回值的函数
    fun max(a:Int,b : Int): Int{
        return if(a > b) a else b
    }

    //进一步简化
    fun min(a:Int,b:Int):Int = if(a > b) a else b

    //进下简化
    fun middle( a:Int,b:Int) = if(a > b ) a else b



}