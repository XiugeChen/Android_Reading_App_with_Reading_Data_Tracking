package com.xiugechen.reading_app.Presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.xiugechen.reading_app.R

class TxtHoriReadingViewPagerAdapter(val mContext: Context, val mData: ArrayList<String>, val mViewPager2: ViewPager2)
    : RecyclerView.Adapter<TxtHoriReadingViewPagerAdapter.ViewHolder>() {

    private val mInflater: LayoutInflater = LayoutInflater.from(mContext)

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var myTextView = itemView.findViewById<TextView>(R.id.hori_slide_text)
        var relativeLayout = itemView.findViewById<RelativeLayout>(R.id.hori_slide_layout)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.txt_hori_slide_fragment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item = mData[position]
        holder.myTextView.text = item
    }
}