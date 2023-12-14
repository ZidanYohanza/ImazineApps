package com.himatifunpad.imazine.util

import com.himatifunpad.imazine.core.domain.model.Post
import org.jsoup.Jsoup

fun Post.scrapeContent(): String {
  val result = StringBuilder()
  val document = Jsoup.parse(content)

  val elements = document.select("body > *")
  for (element in elements) {
    val tag = element.tagName()
    if (tag == "p") {
      result.append(element.wholeText())
      result.append("<br><br>")
    } else if (tag == "figure") {
      val imgUrl = element.select("img").first()?.attr("src") ?: ""
      result.append("<img src=\"$imgUrl\"/>")
      result.append("<br><br>")
    } else if (tag == "blockquote") {
      val innerParagraphs = element.select("p")
      for (p in innerParagraphs) {
        result.append(p.wholeText())
      }
    }
  }
  return result.toString()
}
