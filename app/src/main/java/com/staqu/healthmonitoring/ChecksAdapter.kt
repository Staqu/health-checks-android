package com.staqu.healthmonitoring


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.staqu.healthmonitoring.network.model.Check


class ChecksAdapter(private val context: Context, private val checksList: List<Check>) :
        RecyclerView.Adapter<ChecksAdapter.MyViewHolder>() {

    var actionListener: RecyclerViewActionListener? = null

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var nameTv: TextView? = null
        var pingUrlTv: TextView? = null
        var statusIv: ImageView? = null
        var copy: ImageView? = null;

        init {
            nameTv = view.findViewById(R.id.name)
            pingUrlTv = view.findViewById(R.id.pingUrl)
            statusIv = view.findViewById(R.id.status)
            copy = view.findViewById(R.id.copy)

            copy?.setOnClickListener {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("ping_url", checksList[adapterPosition].ping_url)
                clipboard.primaryClip = clip
                actionListener?.pingUrlCopied(context.getString(R.string.ping_url_copied))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.checks_list_item, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val note = checksList[position]

        holder.nameTv!!.text = note.name
        holder.pingUrlTv!!.text = note.ping_url

        if (note.status == "new")
            holder.statusIv?.setImageResource(R.drawable.twotone_check_circle_24)

    }

    override fun getItemCount(): Int {
        return checksList.size
    }


    /* private fun getRandomMaterialColor(typeColor: String): Int {
         var returnColor = Color.GRAY
         val arrayId = context.resources.getIdentifier("mdcolor_$typeColor", "array", context.packageName)

         if (arrayId != 0) {
             val colors = context.resources.obtainTypedArray(arrayId)
             val index = (Math.random() * colors.length()).toInt()
             returnColor = colors.getColor(index, Color.GRAY)
             colors.recycle()
         }
         return returnColor
     }*/


}