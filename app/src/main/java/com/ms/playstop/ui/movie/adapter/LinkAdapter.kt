package com.ms.playstop.ui.movie.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ms.playstop.R
import com.ms.playstop.model.Url
import kotlinx.android.synthetic.main.item_link_layout.view.*

class LinkAdapter(private val urls: List<Url>): RecyclerView.Adapter<LinkAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_link_layout,parent,false))
    }

    override fun getItemCount(): Int {
         return urls.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(urls.size > position) {
            val url = urls[position]
            holder.bind(url)
        }
    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val rootView = itemView
        val nameTv = itemView.link_name_tv
        val volumeTv = itemView.link_volume_tv
        fun bind(url: Url) {
            nameTv?.text = String.format(itemView.context.getString(R.string.quality_x),url.quality?.name ?: "")
            rootView.context?.let { ctx ->
                volumeTv?.text = getVolumeText(ctx,url.volume)
            }
            rootView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(Uri.parse(url.link),"file/*")
                val resolveInfo = rootView.context?.packageManager?.queryIntentActivities(intent,0)
                resolveInfo?.takeIf { it.isNotEmpty() }?.let {
                    rootView.context?.startActivity(
                        Intent.createChooser(intent,rootView.context.getString(R.string.receive_by))
                    )
                } ?: kotlin.run {
                    intent.data = Uri.parse(url.link)
                    rootView.context?.startActivity(
                        Intent.createChooser(intent,rootView.context.getString(R.string.receive_by))
                    )
                }
            }
        }
    }

    private fun getVolumeText(context: Context,volume: Int): String {
        return if(volume == 0) {
            ""
        } else {
            if(volume >= 1000) {
                String.format(context.getString(R.string.x_GB),volume.toFloat().div(1000).toString())
            } else {
                String.format(context.getString(R.string.x_MB),volume.toString())
            }
        }
    }
}