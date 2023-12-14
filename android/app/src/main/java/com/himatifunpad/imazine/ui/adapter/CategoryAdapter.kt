package com.himatifunpad.imazine.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.himatifunpad.imazine.core.domain.model.Category
import com.himatifunpad.imazine.databinding.ItemCategoryBinding
import com.himatifunpad.imazine.ui.adapter.CategoryAdapter.CategoryViewHolder
import com.himatifunpad.imazine.ui.ext.viewBinding
import com.himatifunpad.imazine.util.CATEGORY_DIFF

class CategoryAdapter(
  private val onClick: (Int, String) -> Unit
) : ListAdapter<Category, CategoryViewHolder>(CATEGORY_DIFF) {

  inner class CategoryViewHolder(private val binding: ItemCategoryBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Category?) {
      if (data == null) return
      with(binding) {
        root.setOnClickListener { onClick(data.id, data.name) }
        tvName.text = data.name
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
    CategoryViewHolder(parent.viewBinding(ItemCategoryBinding::inflate))

  override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
    val data = getItem(position)
    holder.bind(data)
  }
}
