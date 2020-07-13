package com.ypp.docsreader.ui

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.ypp.docsreader.R
import com.ypp.docsreader.model.MultiListItem

class MultiListAdapter(
    private val ctx: Context,
    private val resource: Int,
    private var itemList: ArrayList<MultiListItem>
) : ArrayAdapter<MultiListItem>(ctx, resource) {

    private var filtedList: ArrayList<MultiListItem> = arrayListOf()

    init {
        filterList()
    }

    override fun getCount(): Int {
        return filtedList.size
    }

    override fun getItem(position: Int): MultiListItem? {
        return filtedList.get(position)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View = convertView ?: LayoutInflater.from(ctx).inflate(resource, parent, false)
        var item = filtedList[position]
        val icon: ImageView = view.findViewOften(R.id.item_icon)
        icon.setImageResource(if (item.isChecked) R.drawable.arrow_down else R.drawable.arrow_right)
        val text: TextView = view.findViewOften(R.id.item_text)
        text.text = item.itemText
        view.setPadding(50 * item.level, 0, 0, 0)
        return view
    }

    fun <T : View> View.findViewOften(viewId: Int): T {
        var viewHolder: SparseArray<View> = tag as? SparseArray<View> ?: SparseArray()
        tag = viewHolder
        var childView: View? = viewHolder.get(viewId)
        if (null == childView) {
            childView = findViewById(viewId)
            viewHolder.put(viewId, childView)
        }
        return childView as T
    }

    fun filterList() {
        filtedList.clear()
        for (item in itemList) {
            filtedList.add(item)
            if (item.isChecked) {
                filtedList.addAll(itemList.indexOf(item) + 1, item.children)
            }
        }
        notifyDataSetChanged()
    }
}