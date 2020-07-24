package com.ypp.docsreader.ui

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import com.ypp.docsreader.R
import com.ypp.docsreader.model.MultiListItem
import java.util.*

class MultiListAdapter(
    private val ctx: Context?,
    private val resource: Int,
    private var itemList: ArrayList<MultiListItem>
) : ArrayAdapter<MultiListItem>(ctx!!, resource), Filterable {

    private var filteredList: ArrayList<MultiListItem> = arrayListOf()

    init {
        filterList()
    }

    override fun getCount(): Int {
        return filteredList.size
    }

    override fun getItem(position: Int): MultiListItem? {
        return filteredList.get(position)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: LayoutInflater.from(ctx).inflate(resource, parent, false)
        val item = filteredList[position]
        val icon: ImageView = view.findViewOften(R.id.item_icon)
        if (item.hasChildren()) {
            icon.visibility = View.VISIBLE
            icon.setImageResource(if (item.isChecked) R.drawable.arrow_down else R.drawable.arrow_right)
        } else {
            icon.visibility = View.INVISIBLE
        }
        val text: TextView = view.findViewOften(R.id.item_text)
        text.text = item.itemText
        view.setPadding(50 * item.level, 0, 0, 0)
        return view
    }

    fun <T : View> View.findViewOften(viewId: Int): T {
        val viewHolder: SparseArray<View> = tag as? SparseArray<View> ?: SparseArray()
        tag = viewHolder
        var childView: View? = viewHolder.get(viewId)
        if (null == childView) {
            childView = findViewById(viewId)
            viewHolder.put(viewId, childView)
        }
        return childView as T
    }

    fun filterList() {
        filteredList.clear()
        for (item in itemList) {
            filteredList.add(item)
            if (item.isChecked) {
                filteredList.addAll(itemList.indexOf(item) + 1, item.children)
            }
        }
        notifyDataSetChanged()
    }

    fun filterList(key: String) {
        filteredList.clear()
        for (item in itemList) {
            if (item.itemText.toLowerCase(Locale.getDefault())
                    .contains(key.toLowerCase(Locale.getDefault()))
            ) {
                filteredList.add(item)
            }
        }
        notifyDataSetChanged()
    }

}