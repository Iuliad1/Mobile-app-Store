package com.example.store2.adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.store2.R
import com.example.store2.data.order.Order
import com.example.store2.data.order.OrderStatus
import com.example.store2.data.order.getOrderStatus
import com.example.store2.databinding.OrderItemBinding


class AllOrdersAdapter:  RecyclerView.Adapter<AllOrdersAdapter.OrdersViewHolder>() {


    inner class OrdersViewHolder(private val binding: OrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.apply {
                tvOrderId.text = order.orderId.toString()
                tvOrderDate.text = order.date
                val resources = itemView.resources

                val colorDrawable = when (getOrderStatus(order.orderStatus)) {
                    is OrderStatus.Ordered -> {
                        ColorDrawable(resources.getColor(R.color.dustyBlue))
                    }
                    is OrderStatus.Confirmed -> {
                        ColorDrawable(resources.getColor(R.color.paleGreen))
                    }
                    is OrderStatus.Delivered -> {
                        ColorDrawable(resources.getColor(R.color.mutedTeal))
                    }
                    is OrderStatus.Shipped -> {
                        ColorDrawable(resources.getColor(R.color.sageGreen))
                    }
                    is OrderStatus.Canceled -> {
                        ColorDrawable(resources.getColor(R.color.lightCoral))
                    }
                    is OrderStatus.Returned -> {
                        ColorDrawable(resources.getColor(R.color.softOlive))
                    }
                }
                imageOrderState.setImageDrawable(colorDrawable)
                }
            }
        }

        private val diffCallback = object : DiffUtil.ItemCallback<Order>() {
            override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
                return oldItem == newItem
            }
        }

            val differ = AsyncListDiffer(this, diffCallback)

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
                return OrdersViewHolder(
                    OrderItemBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }

            override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
                val order = differ.currentList[position]
                holder.bind(order)

                holder.itemView.setOnClickListener {
                    onClick?.invoke(order)
                }
            }

            override fun getItemCount(): Int {
                return differ.currentList.size
            }

            var onClick: ((Order) -> Unit)? = null
        }
