package com.example.store2.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.store2.data.Product
import com.example.store2.databinding.BestDealsRvItemBinding
import com.example.store2.databinding.ProductRvItemBinding
import com.example.store2.databinding.SpecialRvItemBinding

class BestProductAdapter: RecyclerView.Adapter<BestProductAdapter.BestProductViewHolder>() {

    inner class BestProductViewHolder(private val binding: ProductRvItemBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.apply {
                Glide.with(itemView).load(product.images[0]).into(bestImage)
                BestName.text = product.name  // Asigură-te că `name` este non-null
                if (product.offerPercentage != null) {  // Dacă există o ofertă
                    val remainingOffer = 1f - product.offerPercentage
                    val priceAfterOffer = remainingOffer * product.price  // Calculul prețului după ofertă
                    priceOld.text = "${String.format("%.2f", product.price)} mdl"  // Prețul original cu strikethrough
                    priceOld.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    priceNew.visibility = View.VISIBLE
                    priceNew.text = "${String.format("%.2f", priceAfterOffer)} mdl"  // Prețul după ofertă
                } else {  // Dacă nu există ofertă
                    priceOld.paintFlags = 0  // Elimină strikethrough
                    priceOld.text = "${String.format("%.2f", product.price)} mdl"  // Prețul original
                    priceNew.visibility = View.INVISIBLE  // Ascunde prețul nou
                }
            }
        }
    }

    // Callback pentru diferența dintre itemi și conținut
    val diffCallback = object : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id  // Comparație după ID
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem  // Comparație după conținut
        }
    }

    // AsyncListDiffer pentru actualizări eficiente
    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ProductRvItemBinding.inflate(inflater, parent, false)
        return BestProductViewHolder(binding)  // Returnează noul ViewHolder
    }

    override fun getItemCount(): Int {
        return differ.currentList.size  // Numărul de itemi în listă
    }

    override fun onBindViewHolder(holder: BestProductViewHolder, position: Int) {
        val product = differ.currentList[position]  // Obține produsul din poziția curentă
        holder.bind(product)  // Leagă produsul de ViewHolder

        holder.itemView.setOnClickListener {
            onClick?.invoke(product)
        }
    }

    var onClick: ((Product) -> Unit)? = null
}

