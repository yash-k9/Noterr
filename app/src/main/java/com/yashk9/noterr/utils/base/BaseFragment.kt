package com.yashk9.noterr.utils.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB: ViewBinding>: Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getBinding(inflater, container)
        return binding?.root
    }

    abstract fun getBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    fun applicationContext(): Context = requireActivity().applicationContext

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}