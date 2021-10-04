package montanez.alexander.socioinfonavit.ui.home

import android.content.Context
import android.graphics.Color
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import montanez.alexander.socioinfonavit.R
import montanez.alexander.socioinfonavit.model.entity.Benevit


class RVBenevitAdapter(arrayList: ArrayList<Benevit>, mContext: Context) :
    RecyclerView.Adapter<RVBenevitAdapter.MyViewHolder>() {
    private var benevits: ArrayList<Benevit> = arrayList
    var context: Context = mContext

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.benevit_title)
        var miniLogo: ImageView = itemView.findViewById(R.id.benevit_mini_logo)
        var banner: LinearLayout = itemView.findViewById(R.id.topBanner)
        var vector: ImageView = itemView.findViewById(R.id.benevit_vector)
        var addButton: Button = itemView.findViewById(R.id.benevit_add)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_benevit, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem: Benevit = benevits[position]
        holder.title.text = currentItem.title
        if (!currentItem.locked!!){
            holder.banner.visibility = View.VISIBLE
            holder.title.visibility = View.VISIBLE
            holder.miniLogo.visibility = View.VISIBLE
            holder.vector.visibility = View.GONE
            holder.addButton.visibility = View.GONE
            Picasso.get().load(currentItem.ally.logo).into(holder.miniLogo)
            try {
                holder.banner.setBackgroundColor(Color.parseColor(currentItem.color))
            } catch (e:Exception){
                holder.banner.setBackgroundColor(Color.WHITE)
            }

        } else {
            holder.vector.visibility = View.VISIBLE
            holder.addButton.visibility = View.VISIBLE
            holder.banner.visibility = View.GONE
            holder.miniLogo.visibility = View.GONE
            holder.title.visibility = View.GONE
            Picasso.get().load(currentItem.vector).into(holder.vector)
        }
    }

    override fun getItemCount(): Int {
        return benevits.size
    }

}
