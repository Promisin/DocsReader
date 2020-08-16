package com.ypp.docsreader.ui.java

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ListView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.ypp.docsreader.R
import com.ypp.docsreader.model.MultiListItem
import com.ypp.docsreader.ui.MultiListAdapter
import com.ypp.docsreader.ui.WebViewActivity
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class JavaFragment : Fragment(), CoroutineScope by MainScope() {
    private val JAVA_URL = "https://docs.oracle.com/javase/8/docs/api/allclasses-frame.html"
    private var visibleNodes: ArrayList<MultiListItem> = arrayListOf()
    private var cacheNodes: ArrayList<MultiListItem> = arrayListOf()
    private lateinit var multiListAdapter: MultiListAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_java, container, false)
        val listView = root.findViewById<ListView>(R.id.lv_nodes)
        multiListAdapter = MultiListAdapter(context, R.layout.multi_list_item, visibleNodes)
        listView.adapter = multiListAdapter
        launch(Dispatchers.IO) {
            val doc: Document = Jsoup.connect(JAVA_URL)
                .timeout(10000)
                .userAgent("Mozilla/5.0")
                .get()
            val elements: Elements = doc.getElementsByClass("indexContainer")[0].child(0).children()
            for (element in elements) {
                val item = element.child(0)
                cacheNodes.add(MultiListItem(0, -1, false, item.text(), item.attr("href")))
            }
            visibleNodes.addAll(cacheNodes)
            withContext(Dispatchers.Main) {
                multiListAdapter.filterList()
            }
        }
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val item = multiListAdapter.getItem(position) ?: MultiListItem()
                if (item.children.size > 0) {
                    item.isChecked = !item.isChecked
                    multiListAdapter.filterList()
                } else {
                    val intent = Intent(context, WebViewActivity::class.java)
                    intent.putExtra(
                        "targetUrl",
                        "https://docs.oracle.com/javase/8/docs/api/" + item.target
                    )
                    intent.putExtra("type", "java")
                    startActivity(intent)
                }
            }
        val llSearch = root.findViewById<LinearLayout>(R.id.ll_search)
        val buttonCancel = root.findViewById<Button>(R.id.btn_cancel)
        buttonCancel.setOnClickListener {
            llSearch.visibility = View.GONE
            multiListAdapter.filterList()
        }
        val sv = root.findViewById<SearchView>(R.id.sv)
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { multiListAdapter.filterList(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { multiListAdapter.filterList(it) }
                return false
            }
        })
        return root
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}