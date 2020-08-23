package com.ms.playstop.base

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    abstract fun tag() : String

    open fun handleBack() : Boolean {
        return false
    }

    @IdRes open fun containerId() : Int {
        return 0
    }
}
