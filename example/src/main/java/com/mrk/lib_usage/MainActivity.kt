package com.mrk.lib_usage

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.khapv.search_view.ExpandableSearchView
import com.mrk.kutils.R
import com.mrk.view.indicator_seekbar.IndicatorSeekBar

class MainActivity : AppCompatActivity() {


    private var expandableSearchView: ExpandableSearchView? = null

    private var seekBar: IndicatorSeekBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        testExpandableSearchView()

        testSeekBar()
    }

    private fun testExpandableSearchView() {
        expandableSearchView = findViewById(R.id.expandable_searchView)

        var imageView2 = ImageView(this)
        imageView2.setImageResource(R.drawable.baseline_add_24)
        imageView2.setPadding(20, 20, 20, 20);
        imageView2.setBackgroundColor(Color.BLUE)
        imageView2.setOnClickListener {

        }

        expandableSearchView?.apply {
            addButton(imageView2)
            setButtonContainerPadding(20, 0, 20, 0)
        }
    }


    private fun testSeekBar() {
        seekBar = findViewById(R.id.indicator_seekbar)
        val array_ends = arrayOf(
            "0", "", "", "", "", "", "", "", "", "", "50", "", "", "", "", "", "", "", "", "", "100"
        )
        seekBar?.apply {
            tickCount = array_ends.size
            customTickTexts(array_ends)
        }
    }
}