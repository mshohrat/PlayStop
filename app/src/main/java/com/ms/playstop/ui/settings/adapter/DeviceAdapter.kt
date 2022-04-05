package com.ms.playstop.ui.settings.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ms.playstop.R
import com.ms.playstop.extension.getJalaliDateTime
import com.ms.playstop.model.Device
import com.ms.playstop.utils.DayNightModeAwareAdapter
import com.ms.playstop.utils.DayNightModeAwareViewHolder
import kotlinx.android.synthetic.main.item_device_layout.view.*

class DeviceAdapter(
        private val items: MutableList<Device>? = null,
        private val itemClickListener: ItemClickListener
    ): RecyclerView.Adapter<DeviceAdapter.ViewHolder>(), DayNightModeAwareAdapter {

    private val boundViewHolders = mutableListOf<ViewHolder>()
    private var recycler: RecyclerView? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if(items == null) {
            LoadingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_device_place_holder_layout,parent,false))
        } else {
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_device_layout,parent,false))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val device = items?.get(position)
        device?.let {
            holder.titleTv?.text = device.model
            holder.appInfoTv?.text = device.platform.plus(" ").plus(device.platformVersion)
            holder.lastConnectedTimeTv?.text = String.format(holder.rootView.context.getString(R.string.last_connection_x),
                getJalaliDateTime(device.lastUsageDate)
            )
            holder.lastConnectedTimeTv?.setTextColor(
                ContextCompat.getColor(
                    holder.rootView.context,
                    R.color.pure_orange_dark
                )
            )
            holder.revokeBtn?.visibility = View.VISIBLE
            holder.revokeBtn?.setOnClickListener {
                itemClickListener.onItemClick(device)
            }
            if(position == items?.lastIndex) {
                holder.divider?.visibility = View.GONE
            } else {
                holder.divider?.visibility = View.VISIBLE
            }
            boundViewHolders.takeIf { it.contains(holder).not() }?.add(holder)
        }
    }

    override fun getItemCount(): Int {
        return items?.size ?: 3
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

    fun setLoading(device: Device,isLoading: Boolean) {
        val pos = items?.indexOf(device)
        if(pos != null && pos != -1) {
            val holder = recycler?.findViewHolderForLayoutPosition(pos) as? ViewHolder
            if(isLoading) {
                holder?.revokeBtn?.text = ""
                holder?.revokeBtnLoading?.visibility = View.VISIBLE
            } else {
                holder?.revokeBtnLoading?.visibility = View.INVISIBLE
                holder?.revokeBtn?.setText(R.string.remove)
            }
        }
    }

    fun removeDevice(device: Device) {
        if(items?.contains(device) == true) {
            items.remove(device)
            notifyDataSetChanged()
        }
    }

    fun clearAll() {
        items?.clear()
        notifyDataSetChanged()
    }

    open inner class ViewHolder(protected val itemView: View): RecyclerView.ViewHolder(itemView), DayNightModeAwareViewHolder {
        val rootView = itemView
        val iconIv = itemView.device_iv
        val titleTv = itemView.device_title_tv
        val appInfoTv = itemView.device_app_version_tv
        val lastConnectedTimeTv = itemView.device_last_connected_time_tv
        val revokeBtn = itemView.device_revoke_btn
        val revokeBtnLoading = itemView.device_revoke_loading
        val divider = itemView.device_bottom_divider

        override fun onDayNightModeChanged(type: Int) {
            itemView.context?.let { ctx ->
                iconIv?.setImageDrawable(AppCompatResources.getDrawable(ctx,R.drawable.ic_device))
                titleTv?.setTextColor(ContextCompat.getColor(ctx,R.color.white))
                appInfoTv?.setTextColor(ContextCompat.getColor(ctx,R.color.white))
                revokeBtn?.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(ctx,R.color.redLight))
                divider?.setBackgroundColor(ContextCompat.getColor(ctx,R.color.grayLight))
            }
        }
    }

    inner class LoadingViewHolder(val view: View): ViewHolder(view) {

    }

    interface ItemClickListener {
        fun onItemClick(device: Device)
    }

    override fun onDayNightModeChanged(type: Int) {
        for (item in boundViewHolders) {
            item.onDayNightModeChanged(type)
        }
    }
}