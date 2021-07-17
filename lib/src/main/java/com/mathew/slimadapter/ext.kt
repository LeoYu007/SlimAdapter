package com.mathew.slimadapter

import android.content.Context
import com.mathew.slimadapter.ex.SlimAdapterWrapper


fun <T> SlimAdapter<T>.exWrapper(
    context: Context,
    exBlock: SlimAdapterWrapper<T>.() -> Unit
): SlimAdapterWrapper<T> {
    return SlimAdapterWrapper(context, this).apply(exBlock)
}