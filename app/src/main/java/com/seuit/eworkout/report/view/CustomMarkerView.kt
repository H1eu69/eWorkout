package com.seuit.eworkout.report.view

import android.content.Context
import android.graphics.Canvas
import android.widget.TextView
import com.seuit.eworkout.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF


class CustomMarkerView(
    context: Context,
    layoutResource: Int
) : MarkerView(context, layoutResource) {

    private var tvContent: TextView? = null
    private var mOffset: MPPointF? = null

    init {
        tvContent = findViewById(R.id.tvContent)
    }


    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        tvContent?.text = e?.y.toString()
        // this will perform necessary layouting
        super.refreshContent(e, highlight)
    }
    override fun getOffset(): MPPointF {
        if (mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
        }
        return mOffset!!
    }


}