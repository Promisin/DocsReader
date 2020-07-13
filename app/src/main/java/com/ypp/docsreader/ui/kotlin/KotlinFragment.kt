package com.ypp.docsreader.ui.kotlin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.ListFragment
import androidx.lifecycle.ViewModelProviders
import com.ypp.docsreader.R
import com.ypp.docsreader.model.MultiListItem
import com.ypp.docsreader.ui.MultiListAdapter
import com.ypp.docsreader.ui.WebViewActivity
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class KotlinFragment : ListFragment(), CoroutineScope by MainScope() {
    private val KOTLIN_URL = "https://www.kotlincn.net/docs/reference/"
    private var nodes: ArrayList<MultiListItem> = arrayListOf()
    private lateinit var multiListAdapter: MultiListAdapter

    private lateinit var kotlinViewModel: KotlinViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        kotlinViewModel =
            ViewModelProviders.of(this).get(KotlinViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_kotlin, container, false)
        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launch(Dispatchers.IO) {
            val doc: Document = Jsoup.connect(KOTLIN_URL)
                .timeout(10000)
                .userAgent("Mozilla/5.0")
                .get()
            val elements: Elements = doc.getElementsByClass("tree-branch")
            for (element in elements) {
                val titleItem = element.child(0)
                val subItems = element.children()
                subItems.removeAt(0)
                val pid = nodes.size
                nodes.add(MultiListItem(0, -1, false, titleItem.child(1).text()))
                for (item in subItems) {
                    nodes[pid].children.add(
                        MultiListItem(
                            1,
                            pid,
                            false,
                            item.child(0).child(1).text(),
                            item.child(0).attr("href")
                        )
                    )
                }
                withContext(Dispatchers.Main) {
                    multiListAdapter =
                        context?.let { MultiListAdapter(it, R.layout.multi_list_item, nodes) }!!
                    listAdapter = multiListAdapter
                }
            }
        }
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        var item = multiListAdapter.getItem(position) ?: MultiListItem()
        if (item.children.size > 0) {
            item.isChecked = !item.isChecked
            multiListAdapter.filterList()
        } else {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra("targetUrl", item.target)
            intent.putExtra("type", "kotlin")
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}
