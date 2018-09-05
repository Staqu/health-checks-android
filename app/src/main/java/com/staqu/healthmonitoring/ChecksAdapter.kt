package com.staqu.healthmonitoring


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.staqu.healthmonitoring.network.model.Check


class ChecksAdapter(private val context: Context, private val checksList: List<Check>) :
        RecyclerView.Adapter<ChecksAdapter.MyViewHolder>() {

    var actionListener: RecyclerViewActionListener? = null

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var nameTv: TextView? = null
        var scheduleGrace: TextView? = null
        var statusIv: ImageView? = null
        var tagChips: ChipGroup? = null
        //var copy: ImageView? = null

        init {
            nameTv = view.findViewById(R.id.name)
            scheduleGrace = view.findViewById(R.id.scheduleGrace)
            statusIv = view.findViewById(R.id.status)
            tagChips = view.findViewById(R.id.tagChips)
            //copy = view.findViewById(R.id.copy)

            /*copy?.setOnClickListener {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("ping_url", checksList[adapterPosition].ping_url)
                clipboard.primaryClip = clip
                actionListener?.pingUrlCopied(context.getString(R.string.ping_url_copied))
            }*/
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.checks_list_item, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val check = checksList[position]

        holder.nameTv!!.text = check.name
        holder.scheduleGrace!!.text = check.schedule+"\n"+check.grace

        when {
            check.status == "new" -> {
                holder.statusIv?.setImageResource(R.drawable.twotone_check_circle_new)
                holder.statusIv?.alpha = 0.5f
            }
            check.status == "up" -> {
                holder.statusIv?.setImageResource(R.drawable.twotone_check_circle_up)
                holder.statusIv?.alpha = 1.0f
            }

            check.status == "grace" -> {
                holder.statusIv?.setImageResource(R.drawable.twotone_error_24)
                holder.statusIv?.alpha = 1.0f
            }

            check.status == "down" -> {
                holder.statusIv?.setImageResource(R.drawable.twotone_notification_down)
                holder.statusIv?.alpha = 1.0f
            }


            else -> holder.statusIv?.alpha = 0.0f
        }


        holder.tagChips?.removeAllViews()

        if(check.tags != null){
            val tagsList: List<String>? = check.tags?.split(" ")
            if (tagsList != null) {
                for (i in 0 until tagsList.size){
                    val chip = Chip(context)
                    chip.text = tagsList[i]
                    chip.isCheckable = false
                    holder.tagChips?.addView(chip,i)
                }
            }
        }

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