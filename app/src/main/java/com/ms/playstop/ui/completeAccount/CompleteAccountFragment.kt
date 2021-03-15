package com.ms.playstop.ui.completeAccount

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.ms.playstop.R
import com.ms.playstop.base.BaseFragment
import com.ms.playstop.extension.*
import com.ms.playstop.network.model.GeneralResponse
import kotlinx.android.synthetic.main.fragment_complete_account.*

class CompleteAccountFragment : BaseFragment() {

    companion object {
        fun newInstance() = CompleteAccountFragment()
        const val COMPLETE_ACCOUNT_STATE = "Complete Account State"
        const val COMPLETE_ACCOUNT_STATE_ADD = 11
        const val COMPLETE_ACCOUNT_STATE_EDIT = 12
    }

    private lateinit var viewModel: CompleteAccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_complete_account, container, false)
    }

    override fun tag(): String {
        return "Complete Account Fragment"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CompleteAccountViewModel::class.java)
        handleArguments()
        subscribeToViewModel()
        subscribeToViewEvents()
    }

    private fun handleArguments() {
        arguments?.let {
            if(it.containsKey(COMPLETE_ACCOUNT_STATE)) {
                val state = it.getInt(
                    COMPLETE_ACCOUNT_STATE,
                    COMPLETE_ACCOUNT_STATE_ADD
                )
                if(state == COMPLETE_ACCOUNT_STATE_EDIT) {
                    complete_account_skip_btn?.hide()
                    complete_account_btn_divider?.hide()
                    complete_account_toolbar_name_tv?.setText(R.string.edit_account_data)
                }
            }
        }
    }

    private fun subscribeToViewModel() {

        viewModel.account.observe(viewLifecycleOwner, Observer {
            complete_account_name_et?.setText(it?.name)
            complete_account_email_et?.setText(it?.email)
        })

        viewModel.completeAccount.observe(viewLifecycleOwner, Observer {
            hideButtonLoading()
            showToast(it)
            removeFromParent(this)
        })

        viewModel.completeAccountError.observe(viewLifecycleOwner, Observer {
            hideButtonLoading()
            showToast(it)
        })
    }

    private fun subscribeToViewEvents() {
        complete_account_back_btn?.setOnClickListener {
            activity?.onBackPressed()
        }

        complete_account_btn?.setOnClickListener {
            showButtonLoading()
            viewModel.submit(
                complete_account_name_et?.text?.toString(),
                complete_account_email_et?.text?.toString()
            )
        }

        complete_account_root?.setOnClickListener {
            complete_account_name_et?.hideSoftKeyboard()
        }

        complete_account_skip_btn?.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun showToast(response: GeneralResponse) {
        response.message?.takeIf { it.isNotEmpty() }?.let {
            Toast.makeText(activity,it, Toast.LENGTH_SHORT).show()
        } ?: kotlin.run {
            response.messageResId?.let {
                Toast.makeText(activity,it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showButtonLoading() {
        complete_account_btn?.text = ""
        complete_account_btn?.isEnabled = false
        complete_account_btn_loading?.show()
    }

    private fun hideButtonLoading() {
        complete_account_btn_loading?.hide()
        complete_account_btn?.setText(R.string.submit)
        complete_account_btn?.isEnabled = true
    }

}