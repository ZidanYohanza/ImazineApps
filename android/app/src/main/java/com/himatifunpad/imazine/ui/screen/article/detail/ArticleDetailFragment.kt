package com.himatifunpad.imazine.ui.screen.article.detail

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.request.ImageRequest
import com.himatifunpad.imazine.R
import com.himatifunpad.imazine.core.data.toModel
import com.himatifunpad.imazine.core.domain.model.Post
import com.himatifunpad.imazine.databinding.FragmentArticleDetailBinding
import com.himatifunpad.imazine.ui.ext.imazineDateFormatter
import com.himatifunpad.imazine.ui.ext.parseHtmlWithImage
import com.himatifunpad.imazine.ui.ext.viewBinding
import com.himatifunpad.imazine.util.base.BaseFragment
import com.himatifunpad.imazine.util.scrapeContent
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ArticleDetailFragment : BaseFragment(R.layout.fragment_article_detail) {
  private val binding by viewBinding<FragmentArticleDetailBinding>()
  private val args by navArgs<ArticleDetailFragmentArgs>()

  private val toolbar get() = binding.toolbar
  private val img get() = binding.ivPostImg
  private val title get() = binding.tvTitle
  private val content get() = binding.tvContent
  private val date get() = binding.tvDate

  override fun setupView() {
    toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
    displayPost(args.post.toModel())
  }

  private fun displayPost(post: Post) {
    img.apply {
      val imgData = ImageRequest.Builder(this.context)
        .data(post.cover)
        .target(this)
        .allowHardware(true)
        .build()
      imageLoader.enqueue(imgData)
    }
    title.text = post.title
    date.text =
      imazineDateFormatter.format(LocalDate.parse(post.date, DateTimeFormatter.ISO_DATE_TIME))
    content.parseHtmlWithImage(post.scrapeContent())
  }
}
