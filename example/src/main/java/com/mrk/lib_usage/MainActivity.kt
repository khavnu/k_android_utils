package com.mrk.lib_usage

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.khapv.search_view.ExpandableSearchView
import com.mrk.circle_image_view.RotateAbleDiscShapeImageView
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
        testRotateImage()
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

    private fun testRotateImage() {
        var rotateAbleDiscShapeImageView = findViewById<RotateAbleDiscShapeImageView>(R.id.rotate_image_view)
        var imgToggleAnim = findViewById<ImageView>(R.id.toggle_play);
        var imgStopAnim = findViewById<ImageView>(R.id.img_stop_anim)

        imgToggleAnim?.setOnClickListener{
            if(rotateAbleDiscShapeImageView.isAnimPausedOrStop) {
                rotateAbleDiscShapeImageView.startOrResumeAnim()
                imgToggleAnim.setImageResource(R.drawable.baseline_pause_circle_outline_24)
            } else {
                rotateAbleDiscShapeImageView.pauseAnim()
                imgToggleAnim.setImageResource(R.drawable.baseline_play_circle_outline_24)
            }
        }

        imgStopAnim.setOnClickListener{
            rotateAbleDiscShapeImageView.cancelAnim()
        }

        imgToggleAnim?.performClick()
    }
}