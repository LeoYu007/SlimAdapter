package com.yu1tiao.slimadapter.core

/**
 * 主要作用是：
 * 注册了多个ViewInjector时，通过这个接口返回当前item具体使用哪个ViewInjector
 * @author yu
 * @date 2018/1/11
 */
interface InjectorFinder {
    /**
     * @return layoutId 返回ViewInjector的layoutId
     */
    fun layoutId(item: Any, position: Int, itemCount: Int): Int
}