package com.nullexcom.indicator

import android.animation.ValueAnimator
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.abs
import kotlin.math.max

class MainActivity : AppCompatActivity() {

    private var currentIndex = 0
    private var offset = 0.3f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager.adapter = Adapter()
        indicator.attachToViewPager(viewPager)
    }

    private inner class Adapter : PagerAdapter() {
        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun getCount(): Int {
            return 6
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view =  View(this@MainActivity).apply {
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                setBackgroundColor(if (position % 2 == 0) Color.RED else Color.BLUE)
            }
            container.addView(view)
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            if (`object` is View) {
                container.removeView(`object`)
            }
        }

    }
}