package com.ms.playstop.ui.payment.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.ms.playstop.R
import com.ms.playstop.extension.convertToPersianNumber
import com.ms.playstop.extension.hide
import com.ms.playstop.extension.show
import com.ms.playstop.model.Product
import com.ms.playstop.utils.DayNightModeAwareAdapter
import com.ms.playstop.utils.DayNightModeAwareViewHolder
import kotlinx.android.synthetic.main.item_product_layout.view.*

class ProductAdapter(private var products: List<Product> = listOf(), private val onItemClickListener: OnItemClickListener? = null)
    : RecyclerView.Adapter<ProductAdapter.ViewHolder>(), DayNightModeAwareAdapter {

    private var recycler: RecyclerView? = null
    private var boundViewHolders = mutableListOf<ViewHolder>()
    companion object {
        private const val VIEW_TYPE_LOADING = 0
        private const val VIEW_TYPE_PRODUCT = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when(viewType) {
            VIEW_TYPE_LOADING -> LoadingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_product_shimmer_layout,parent,false))
            else -> ProductViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_product_layout,parent,false))
        }
    }

    fun submitList(products: List<Product>) {
        this.products = products
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if(products.isEmpty()) VIEW_TYPE_LOADING else VIEW_TYPE_PRODUCT
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(holder is ProductViewHolder) {
            if (products.isEmpty() || products.size == position) {
                return
            }
            val product = products[position]
            holder.bind(product)
            holder.root.setOnClickListener {
                holder.isLoading = true
                onItemClickListener?.onItemClick(position, product)
            }
            boundViewHolders.takeIf { it.contains(holder).not() }?.add(holder)
        }
    }

    override fun getItemCount(): Int {
        return products.takeIf { it.isNotEmpty() }?.size ?: 3
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recycler = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recycler = null
        this.boundViewHolders.clear()
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        boundViewHolders.takeIf { it.contains(holder) }?.remove(holder)
    }

    fun setLoadingForPosition(position: Int,isLoading: Boolean) {
        val viewHolder = recycler?.findViewHolderForLayoutPosition(position) as? ProductViewHolder
        viewHolder?.isLoading = isLoading
    }

    abstract class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), DayNightModeAwareViewHolder

    class LoadingViewHolder(itemView: View): ViewHolder(itemView) {
        override fun onDayNightModeChanged(type: Int) {

        }
    }

    class ProductViewHolder(itemView: View): ViewHolder(itemView) {
        val root = itemView
        val nameTv = itemView.product_name_tv
        val priceTv = itemView.product_price_tv
        val iconIv = itemView.product_icon_iv
        val progress = itemView.product_progress
        var isLoading = false
        set(value) {
            field = value
            if(value) {
                root.isClickable = false
                iconIv?.hide()
                progress?.show()
            } else {
                root.isClickable = true
                progress?.hide()
                iconIv?.show()
            }
        }

        fun bind(product: Product) {
            nameTv?.text = product.name.convertToPersianNumber()
            priceTv?.text = root.context?.getString(R.string.x_toman)?.let {
                String.format(it,String.format("%,d", product.price).convertToPersianNumber())
            }
        }

        override fun onDayNightModeChanged(type: Int) {
            root.context?.let { ctx ->
                with(ContextCompat.getColor(ctx,R.color.colorAccentDark)){
                    iconIv?.let {
                        ImageViewCompat.setImageTintList(it, ColorStateList.valueOf(this@with))
                    }
                    (root as MaterialCardView).strokeColor = this
                    progress?.setColor(this)
                    root.setCardBackgroundColor(ColorStateList.valueOf(this))
                }
                nameTv?.setTextColor(ContextCompat.getColor(ctx,R.color.white))
                priceTv?.setTextColor(ContextCompat.getColor(ctx,R.color.purple_new))
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position:Int,product: Product)
    }

    override fun onDayNightModeChanged(type: Int) {
        for (item in boundViewHolders) {
            item.onDayNightModeChanged(type)
        }
    }
}