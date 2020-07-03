package com.ypp.docsreader.ui.kotlin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.ListFragment
import androidx.lifecycle.ViewModelProviders
import com.ypp.docsreader.R
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class KotlinFragment : ListFragment(), CoroutineScope by MainScope() {
    private val KOTLIN_URL = "https://www.kotlincn.net/docs/reference/"
    private var nodes: ArrayList<String> = arrayListOf()
    private var children: Map<String, String> = mapOf()
    private var targetUrls: Map<String, String> = mapOf()
    private lateinit var adapter: ArrayAdapter<String>

    private lateinit var kotlinViewModel: KotlinViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        kotlinViewModel =
            ViewModelProviders.of(this).get(KotlinViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_kotlin, container, false)
//        kotlinViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launch(Dispatchers.IO) {
            val doc: Document = Jsoup.connect("https://www.kotlincn.net/docs/reference/")
                .timeout(10000)
                .userAgent("Mozilla/5.0")
                .get()
            val elements: Elements = doc.getElementsByClass("tree-branch")
            for (element in elements) {
                val item: String = element.child(0).child(1).text()
                Log.d("tree_item", "item:$item")
                nodes.add(item)
                withContext(Dispatchers.Main) {
                    listAdapter =
                        context?.let {
                            ArrayAdapter<String>(
                                it,
                                android.R.layout.simple_list_item_1,
                                nodes
                            )
                        }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}
