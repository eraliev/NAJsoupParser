package ru.nowandroid.youtube.nowjsoupandroid.list

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.list_news_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import ru.nowandroid.youtube.nowjsoupandroid.R
import java.io.IOException

class ListNewsFragment : Fragment() {

    private val BASE_URL = "http://guobdd.kg"
    private val listNews = mutableListOf<News>()
    private lateinit var adapter: DataAdapter

    companion object {
        fun newInstance() = ListNewsFragment()
    }

    private lateinit var viewModel: ListNewsViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_news_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ListNewsViewModel::class.java)
        // TODO: Use the ViewModel

        adapter = DataAdapter()
        val llm = LinearLayoutManager(this.context)
        rv.layoutManager = llm
        rv.adapter = adapter

        GlobalScope.launch {
            getData()
        }
    }

    private fun getData() {
        try {
            val document = Jsoup
                    .connect(BASE_URL + "/allnews/category/NEWS")
                    .get()

            val elements = document.select("article.one-news")

            elements.forEach { element ->

                val title = element.select("a").text()
                val description = element.select("p").text()
                             val linkImage = BASE_URL + "/" + element.select("a.one-news__image").first().attr("style").substringAfter("/").dropLast(2)
                val additionalInfo = element.select("div.news-date").text()
                        val linkDetails = BASE_URL + element.select("a.one-news__link").attr("href")

              listNews.add(News(title, description, linkImage, additionalInfo, linkDetails))
            }
            GlobalScope.launch(Dispatchers.Main) {
                adapter.set(listNews)
            }
        } catch (e: IOException) {
            Log.e("TEST) exception", e.message.toString())
        }
    }

}
