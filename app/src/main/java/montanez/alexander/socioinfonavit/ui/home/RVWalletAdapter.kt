package montanez.alexander.socioinfonavit.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import montanez.alexander.socioinfonavit.R
import montanez.alexander.socioinfonavit.model.entity.Benevit
import montanez.alexander.socioinfonavit.model.entity.Wallet


class RVWalletAdapter(arrayList: ArrayList<Wallet>, mContext: Context) :
    RecyclerView.Adapter<RVWalletAdapter.MyViewHolder>() {
    private var wallets: ArrayList<Wallet> = arrayList
    var context: Context = mContext

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.wallet_title)
        var walletCards: RecyclerView = itemView.findViewById(R.id.wallet_rv_cards)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_wallet, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = wallets[position]
        holder.title.text = currentItem.displayText
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        holder.walletCards.layoutManager = layoutManager
        holder.walletCards.setHasFixedSize(true)

        val benevitsRVAdapter = RVBenevitAdapter(wallets[position].benevits as ArrayList<Benevit>, holder.walletCards.context)
        holder.walletCards.setAdapter(benevitsRVAdapter)

    }

    override fun getItemCount(): Int {
        return wallets.size
    }

}