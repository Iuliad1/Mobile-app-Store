package com.example.store2.adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.store2.data.CartProduct
import com.example.store2.data.Product
import com.example.store2.databinding.CartLayoutBinding
import com.example.store2.helper.getProductPrice


class CartProductAdapter: RecyclerView.Adapter<CartProductAdapter.CartProductsViewHolder>() {

    inner class CartProductsViewHolder( val binding: CartLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cartProduct: CartProduct) {
            binding.apply {
                Glide.with(itemView).load(cartProduct.product.images[0]).into(ImageCartProd)
                ProdCartName.text = cartProduct.product.name
                ProdCartQuantity.text = cartProduct.quantity.toString()

                val priceOffer = cartProduct.product.offerPercentage.getProductPrice(cartProduct.product.price)
                ProdCartPrice.text = "${String.format("%.2f", priceOffer)} mdl"

                ColorCart.setImageDrawable(ColorDrawable(cartProduct.selectedColor?: Color.TRANSPARENT))
                TextSize.text = cartProduct.selectedSize?:""
                    .also { SizeCart.setImageDrawable(ColorDrawable(Color.TRANSPARENT)) }
                }

            }
        }

    private val diffCallback = object : DiffUtil.ItemCallback<CartProduct>(){
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.product.id == newItem.product.id
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductsViewHolder {
        return  CartProductsViewHolder(
            CartLayoutBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }


    override fun onBindViewHolder(holder: CartProductsViewHolder, position: Int) {
        val cartProduct = differ.currentList[position]
        holder.bind(cartProduct)

        holder.itemView.setOnClickListener{
            onProductClick?.invoke(cartProduct)
        }

        holder.binding.plusIcon.setOnClickListener {
            onPlusClick?.invoke(cartProduct)
        }
        holder.binding.minusIcon.setOnClickListener {
            onMinusClick?.invoke(cartProduct)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onProductClick: ((CartProduct) -> Unit)? = null
    var onPlusClick: ((CartProduct) -> Unit)? = null
    var onMinusClick: ((CartProduct) -> Unit)? = null


}