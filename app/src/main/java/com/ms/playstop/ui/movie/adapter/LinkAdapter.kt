package com.ms.playstop.ui.movie.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textview.MaterialTextView
import com.ms.playstop.R
import com.ms.playstop.extension.isPlayable
import com.ms.playstop.model.Subtitle
import com.ms.playstop.model.Url
import com.ms.playstop.ui.playVideo.PlayVideoActivity
import com.ms.playstop.utils.MenuAdapter
import kotlinx.android.synthetic.main.item_link_layout.view.*
import java.util.ArrayList

class LinkAdapter(private val urls: List<Url>,private val subtitles: List<Subtitle>? = null): RecyclerView.Adapter<LinkAdapter.ViewHolder>() {

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
                if(url.link.isPlayable()) {
                    showBottomSheetDialogForPlayOrDownload(rootView.context, url)
                }
                else {
                    tryToDownload(rootView.context,url)
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

    private fun showBottomSheetDialogForPlayOrDownload(context: Context?,url: Url) {
        context?.let { ctx ->
            val items = arrayListOf<Pair<Int,Int>>()
            items.add(MenuAdapter.ITEM_TYPE_PLAY to R.string.play)
            items.add(MenuAdapter.ITEM_TYPE_DOWNLOAD to R.string.download)
            val dialog = BottomSheetDialog(ctx)
            dialog.setContentView(R.layout.layout_bottom_sheet_recycler)
            val titleTv = dialog.findViewById<MaterialTextView>(R.id.bottom_sheet_recycler_title_tv)
            val closeIb = dialog.findViewById<AppCompatImageButton>(R.id.bottom_sheet_recycler_close_ib)
            val recycler = dialog.findViewById<RecyclerView>(R.id.bottom_sheet_recycler_recycler)
            titleTv?.setText(R.string.please_choose_play_or_download)
            closeIb?.setOnClickListener {
                dialog.takeIf { it.isShowing }?.dismiss()
                dialog.cancel()
            }
            val lm = LinearLayoutManager(ctx,RecyclerView.VERTICAL,false)
            recycler?.layoutManager = lm
            val adapter = MenuAdapter(items,object : MenuAdapter.OnItemClickListener {
                override fun onItemClick(position: Int, itemType: Int) {
                    when(itemType) {
                        MenuAdapter.ITEM_TYPE_PLAY -> {
                            tryToPlay(context,url)
                        }
                        MenuAdapter.ITEM_TYPE_DOWNLOAD -> {
                            tryToDownload(context,url)
                        }
                        else -> {}
                    }
                    dialog.takeIf { it.isShowing }?.dismiss()
                    dialog.cancel()
                }
            })
            recycler?.adapter = adapter
            dialog.show()
        }
    }

    private fun tryToPlay(context: Context?,url: Url) {
        context?.let {
            val intent = Intent(context,PlayVideoActivity::class.java)
            intent.putExtra(PlayVideoActivity.PLAY_VIDEO_URL,url.link)
            val subtitles = subtitles?.let { list ->
                ArrayList(list.map { it.link })
            }
            intent.putStringArrayListExtra(PlayVideoActivity.PLAY_VIDEO_SUBTITLES, subtitles)
            context.startActivity(intent)
        }
    }

    private fun tryToDownload(context: Context?,url: Url) {
         val intent = Intent(Intent.ACTION_VIEW)
         if(url.link.isPlayable()) {
             intent.setDataAndType(Uri.parse(url.link), "file/*")
         } else {
             intent.data = Uri.parse(url.link)
         }
         val resolveInfo = context?.packageManager?.queryIntentActivities(intent,0)
         resolveInfo?.takeIf { it.isNotEmpty() }?.let {
             context.startActivity(
                 Intent.createChooser(intent,context.getString(R.string.receive_by))
             )
         } ?: kotlin.run {
                    intent.data = Uri.parse(url.link)
                    context?.startActivity(
                        Intent.createChooser(intent,context.getString(R.string.receive_by))
                    )
         }
    }
}