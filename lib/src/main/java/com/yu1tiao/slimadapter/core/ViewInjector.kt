package com.yu1tiao.slimadapter.core

import androidx.annotation.LayoutRes
import java.lang.reflect.ParameterizedType

/**
 * @author yu
 * @date 2018/1/11
 */
abstract class ViewInjector<T>(@LayoutRes val layoutId: Int) {

    /**
     * 绑定item的数据
     *
     * @param holder   ViewHolder
     * @param item     实体对象
     * @param position 角标
     */
    abstract fun bind(holder: ViewHolder, item: T, position: Int)

//    fun getType(): Class<T> {
//        val type = this::class.java.genericSuperclass
//        if (type is ParameterizedType) {
//            val actType = type.actualTypeArguments[0]
//            return actType as Class<T>
//        }
//        throw RuntimeException("未获取到泛型参数")
//    }
}