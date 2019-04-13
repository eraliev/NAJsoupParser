package ru.nowandroid.youtube.nowjsoupandroid.details

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.details_news_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import ru.nowandroid.youtube.nowjsoupandroid.R
import java.io.IOException
import kotlin.coroutines.CoroutineContext

class DetailsNewsFragment : Fragment(), CoroutineScope {
  private val BASE_URL = "http://guobdd.kg/"
  private var job = Job()
  override val coroutineContext: CoroutineContext = Dispatchers.Main + job

  private lateinit var viewModel: DetailsNewsViewModel

  override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
                           ): View? {
    return inflater.inflate(R.layout.details_news_fragment, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this).get(DetailsNewsViewModel::class.java)

    job = launch(Dispatchers.IO) {
      getData()
    }
  }

  private fun getData() {
    try {

        //оригинальный метод       val document = Jsoup.connect(arguments?.getString("link")).get()
        // но так вообще ничего не показывает s

      val document = Jsoup
              .connect(BASE_URL )
              .get()

      val elements = document.select("div.content-wrapper")

      elements.forEach { element ->

       val title = element.select("h2.panel-title no-margin-bottom no-padding-bottom").attr("h2")
        val description = element.select("p").text()
               val linkImage = BASE_URL + "/" + element.select("a.one-news__image").first().attr("style").substringAfter("/").dropLast(2)

     //  val additionalInfo = element.select("div.news-date").text()
    //    val linkDetails = BASE_URL + element.select("a.one-news__link").attr("href")





        job = launch {
         det_title.text = title.toString()
          det_description.text = description.toString()
          Picasso.with(activity)
                  .load(linkImage)
                  .into(det_main_photo)
        }
      }

    } catch (e: IOException) {
      Log.e("TEST) exception", e.message.toString())
    }
  }

}


