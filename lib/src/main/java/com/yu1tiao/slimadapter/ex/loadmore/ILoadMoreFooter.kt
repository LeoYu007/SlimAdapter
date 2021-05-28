package com.yu1tiao.slimadapter.ex.loadmore

import android.view.View

/**
 * @author yu
 * @date 2018/1/12
 */
interface ILoadMoreFooter {

    enum class Status {
        LOADING, COMPLETED, NO_MORE, ERROR, DISABLE
    }

    var status: Status

    val view: View
}