package com.summer.demo.ui.course.skotlin

/**
 *
 * 支持将多个类放到同一个文件里
 *
 * @Description:
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/11/12 16:34
 */
class Person(val height:Int,val width:Int) {
    val isSquare:Boolean
        get(){
            return height == width
        }

}

class Doleros(val height:Int,val width: Int){

}

enum class Color(val r:Int,val g:Int,val b:Int){
    RED(255,0,0),
    ORANGE(255,255,0),
    YELLOW(255,255,0),
    GREEN(0,255,0),
    BLUE(0,0,255),
    INDIGO(75,0,130),
    VIOLET(238,130,238)
}