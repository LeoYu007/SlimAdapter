package com.yu1tiao.slimadapter

import com.yu1tiao.slimadapter.core.InjectorFinder
import com.yu1tiao.slimadapter.core.ViewInjector


/**
 * @author yuli
 * @date 2021/5/20
 * @description SlimAdapter
 */
open class HeaderAdapter(
    injectors: List<ViewInjector<Any>>,
    override var injectorFinder: InjectorFinder?
) : SlimAdapter<Any>() {

    init {
        injectors.forEach {
            register(it)
        }
    }
}

class FooterAdapter(
    injectors: List<ViewInjector<Any>>,
    injectorFinder: InjectorFinder?
) : HeaderAdapter(injectors, injectorFinder)

