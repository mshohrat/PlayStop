package com.ms.playstop.ui.payment

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ms.playstop.MainActivity
import com.ms.playstop.R
import com.ms.playstop.base.BaseFragment
import com.ms.playstop.extension.hide
import com.ms.playstop.extension.show
import com.ms.playstop.model.Host
import com.ms.playstop.model.PathType
import com.ms.playstop.model.Product
import com.ms.playstop.model.Scheme
import com.ms.playstop.network.model.GeneralResponse
import com.ms.playstop.ui.payment.adapter.ProductAdapter
import com.ms.playstop.utils.LoadingDialog
import kotlinx.android.synthetic.main.fragment_payment.*


class PaymentFragment : BaseFragment(), ProductAdapter.OnItemClickListener {

    companion object {
        fun newInstance() = PaymentFragment()
        const val TAG = "Payment Fragment"
    }

    private lateinit var viewModel: PaymentViewModel
    private var loadingDialog: LoadingDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_payment, container, false)
    }

    override fun tag(): String {
        return TAG
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PaymentViewModel::class.java)
        initViews()
        subscribeToViewEvents()
        subscribeToViewModel()
    }

    private fun initViews() {
        activity?.let { ctx ->
            payment_recycler?.layoutManager = LinearLayoutManager(ctx,RecyclerView.VERTICAL,false)
            initializeProductRecycler()
        }
    }

    private fun initializeProductRecycler() {
        payment_recycler?.adapter = ProductAdapter(onItemClickListener = this)
    }

    private fun subscribeToViewModel() {
        viewModel.products.observe(viewLifecycleOwner, Observer {
            payment_refresh_layout?.isRefreshing = false
            it.takeIf { it.isNotEmpty() }?.let { data ->
                payment_recycler?.adapter?.takeIf { it is ProductAdapter }?.let {
                    (it as ProductAdapter).submitList(data)
                } ?: kotlin.run {
                    payment_recycler?.adapter = ProductAdapter(data,this)
                }
            } ?: kotlin.run {
                payment_recycler?.hide()
                payment_no_products_tv?.show()
            }
        })

        viewModel.productsError.observe(viewLifecycleOwner, Observer {
            payment_refresh_layout?.isRefreshing = false
            showToast(it)
        })

        viewModel.paymentUrl.observe(viewLifecycleOwner, Observer { pair->
            payment_recycler?.adapter?.takeIf { it is ProductAdapter }?.let {
                (it as ProductAdapter).setLoadingForPosition(pair.first,false)
            }
            payment_refresh_layout?.isEnabled = true
            val startPaymentIntent = Intent(Intent.ACTION_VIEW)
            startPaymentIntent.data = Uri.parse(pair.second)
            activity?.startActivity(startPaymentIntent)
        })

        viewModel.paymentUrlError.observe(viewLifecycleOwner, Observer {
            payment_refresh_layout?.isEnabled = true
            showToast(it)
        })

        viewModel.verifyPayment.observe(viewLifecycleOwner, Observer {
            dismissLoadingDialog()
            showToast(it)
            payment_back_btn?.callOnClick()
        })

        viewModel.verifyPaymentError.observe(viewLifecycleOwner, Observer {
            dismissLoadingDialog()
            showToast(it)
        })
    }

    private fun subscribeToViewEvents() {
        payment_back_btn?.setOnClickListener {
            activity?.onBackPressed()
        }
        payment_refresh_layout?.setOnRefreshListener {
            payment_no_products_tv?.hide()
            payment_recycler?.show()
            initializeProductRecycler()
            viewModel.fetchProducts()
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

    override fun onItemClick(position: Int, product: Product) {
        payment_refresh_layout?.isEnabled = false
        viewModel.startPayment(position,product.id)
    }

    override fun onHandleDeepLink() {
        super.onHandleDeepLink()
        activity?.takeIf { it is MainActivity }?.let { act ->
            (act as MainActivity).deepLink?.takeIf {
                it.scheme == Scheme.PlayStop
                        && it.host == Host.Open
                        && it.path1?.pathType == PathType.Payment}?.let {
                it.path1?.value?.let {
                    act.consumeDeepLink()
                    showLoadingDialog()
                    viewModel.verifyPayment(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dismissLoadingDialog()
    }

    private fun showLoadingDialog() {
        activity?.let { ctx ->
            loadingDialog = LoadingDialog(ctx)
            loadingDialog?.show()
        }
    }

    private fun dismissLoadingDialog() {
        loadingDialog?.takeIf { it.isShowing }?.dismiss()
        loadingDialog?.cancel()
    }

}