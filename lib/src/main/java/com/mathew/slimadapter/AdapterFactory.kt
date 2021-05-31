package com.mathew.slimadapter

/**
 * Copyright (c) 2021 $ All rights reserved.
 *
 * @author mathew
 * @date 2021/5/31
 * @description AdapterFactory
 */
interface AdapterFactory<T> {

    // 内容页面
    fun createContentAdapter(): SlimAdapter<T>

    // 包装类，通过它来做header、footer等扩展功能
    fun createWrapper(): SlimAdapterWrapper<T>
}