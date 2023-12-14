package com.himatifunpad.imazine.util.coil

import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Html
import android.util.TypedValue
import android.widget.TextView
import coil.request.ImageRequest
import com.himatifunpad.imazine.core.di.entrypoint.ImageLoaderEntryPoint
import dagger.hilt.android.EntryPointAccessors
import logcat.logcat
import kotlin.math.roundToInt

// from https://github.com/Commit451/coil-imagegetter

class CoilImageGetter(
  private val textView: TextView
) : Html.ImageGetter {
  companion object {
    private const val MARGIN_SIZE = 32f
  }
  override fun getDrawable(source: String): Drawable {
    logcat { source }

    val drawablePlaceholder = DrawablePlaceHolder()
    val entryPoint =
      EntryPointAccessors.fromApplication(
        textView.context,
        ImageLoaderEntryPoint::class.java
      )
    entryPoint.coilLoader().enqueue(
      ImageRequest.Builder(textView.context)
        .data(source)
        .apply {
          val metrics = textView.resources.displayMetrics
          val margin = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            MARGIN_SIZE,
            metrics
          ).roundToInt()
          size(metrics.widthPixels - margin)
          target { drawable ->
            drawablePlaceholder.updateDrawable(drawable)
            // invalidating the drawable doesn't seem to be enough...
            textView.text = textView.text
          }
        }.build()
    )
    // Since this loads async, we return a "blank" drawable, which we update
    // later
    return drawablePlaceholder
  }

  @Suppress("DEPRECATION")
  inner class DrawablePlaceHolder : BitmapDrawable() {

    private var drawable: Drawable? = null

    override fun draw(canvas: Canvas) {
      drawable?.draw(canvas)
    }

    fun updateDrawable(drawable: Drawable) {
      this.drawable = drawable

      val width = drawable.intrinsicWidth
      val height = drawable.intrinsicHeight

      drawable.setBounds(0, 0, width, height)
      setBounds(0, 0, width, height)
    }
  }
}
