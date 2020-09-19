package com.andruid.magic.helpfulsense.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import com.andruid.magic.helpfulsense.databinding.LayoutSpinnerBinding
import com.andruid.magic.helpfulsense.model.Category
import com.andruid.magic.helpfulsense.ui.fragment
        .ActionDialogFragment

/**
 * Adapter for showing dialog with list of categories [ActionDialogFragment]
 * @property categories list of all categories
 */
class MySpinnerAdapter(val categories: List<Category>) : BaseAdapter() {
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val (binding, view) =
                convertView?.let {
                    requireNotNull(DataBindingUtil.findBinding<LayoutSpinnerBinding>(convertView),
                            { "Spinner layout binding null" }) to
                            convertView
                } ?: run {
                    val binding = LayoutSpinnerBinding.inflate(LayoutInflater.from(requireNotNull(parent).context),
                            parent, false)
                    binding to binding.root
                }
        binding.category = categories[position]
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        return getView(position, convertView, parent)
    }

    override fun getItem(position: Int) = categories[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getCount() = categories.size
}