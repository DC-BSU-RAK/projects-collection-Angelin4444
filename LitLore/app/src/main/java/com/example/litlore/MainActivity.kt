package com.example.litlore

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {

    // Keeping track of the first selected scent. Also the second one.
    private var selection1ImageRes: Int? = null
    private var selection2ImageRes: Int? = null

    // Knowing if a popup is visible right now.
    private var isShowingInstructionsPage = false
    private var isShowingMixResult = false

    // Music is playing by default.
    private var isMusicPlaying = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Making the app go edge to edge.
        enableEdgeToEdge()

        // Connecting the layout file.
        setContentView(R.layout.activity_main)

        // Fixing padding so the layout fits perfectly. Preventing system bars from covering things up.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Grabbing all the UI elements from the layout.
        val mainScrollView = findViewById<ScrollView>(R.id.mainScrollView)
        val btnInfo = findViewById<ImageView>(R.id.btnInfo)
        val btnMix = findViewById<ImageView>(R.id.btnMix)
        val bottomCard1 = findViewById<MaterialCardView>(R.id.bottomCard1)
        val bottomCard2 = findViewById<MaterialCardView>(R.id.bottomCard2)
        val bottomImg1 = findViewById<ImageView>(R.id.bottomImg1)
        val bottomImg2 = findViewById<ImageView>(R.id.bottomImg2)

        val popupOverlay = findViewById<FrameLayout>(R.id.popupOverlay)
        val imgPopupBg = findViewById<ImageView>(R.id.imgPopupBg)
        val btnPopupAction = findViewById<ImageView>(R.id.btnPopupAction)

        val cardSoundControl = findViewById<MaterialCardView>(R.id.cardSoundControl)
        val imgSoundControl = findViewById<ImageView>(R.id.imgSoundControl)

        // Grouping all six flavor cards with their specific images.
        val cards = listOf(
            findViewById<MaterialCardView>(R.id.card1) to R.drawable.flavor_1,
            findViewById<MaterialCardView>(R.id.card2) to R.drawable.flavor_2,
            findViewById<MaterialCardView>(R.id.card3) to R.drawable.flavor_3,
            findViewById<MaterialCardView>(R.id.card4) to R.drawable.flavor_4,
            findViewById<MaterialCardView>(R.id.card5) to R.drawable.flavor_5,
            findViewById<MaterialCardView>(R.id.card6) to R.drawable.flavor_6
        )

        // The master dictionary!
        // It links a pair of flavors to the final mixed result image.
        val mixCombinations = mapOf(
            setOf(R.drawable.flavor_1, R.drawable.flavor_3) to R.drawable.mix_rose_vanilla,
            setOf(R.drawable.flavor_1, R.drawable.flavor_5) to R.drawable.mix_rose_jasmine,
            setOf(R.drawable.flavor_1, R.drawable.flavor_6) to R.drawable.mix_rose_lavender,

            setOf(R.drawable.flavor_6, R.drawable.flavor_3) to R.drawable.mix_lavender_vanilla,
            setOf(R.drawable.flavor_6, R.drawable.flavor_2) to R.drawable.mix_lavender_sandalwood,
            setOf(R.drawable.flavor_6, R.drawable.flavor_5) to R.drawable.mix_lavender_jasmine,

            setOf(R.drawable.flavor_4, R.drawable.flavor_5) to R.drawable.mix_citrus_jasmine,
            setOf(R.drawable.flavor_4, R.drawable.flavor_6) to R.drawable.mix_citrus_lavender,
            setOf(R.drawable.flavor_4, R.drawable.flavor_3) to R.drawable.mix_citrus_vanilla,

            setOf(R.drawable.flavor_2, R.drawable.flavor_3) to R.drawable.mix_sandalwood_vanilla,
            setOf(R.drawable.flavor_2, R.drawable.flavor_1) to R.drawable.mix_sandalwood_rose,
            setOf(R.drawable.flavor_2, R.drawable.flavor_5) to R.drawable.mix_sandalwood_jasmine,

            setOf(R.drawable.flavor_3, R.drawable.flavor_5) to R.drawable.mix_vanilla_jasmine,
            setOf(R.drawable.flavor_4, R.drawable.flavor_2) to R.drawable.mix_citrus_sandalwood,
            setOf(R.drawable.flavor_1, R.drawable.flavor_4) to R.drawable.mix_rose_citrus
        )

        // A quick function to check if both slots are full.
        // If they are full, make the mix button clickable.
        // Otherwise, dim it out.
        fun updateMixButton() {
            if (selection1ImageRes != null && selection2ImageRes != null) {
                btnMix.alpha = 1.0f
                btnMix.isClickable = true
            } else {
                btnMix.alpha = 0.5f
                btnMix.isClickable = false
            }
        }

        // Looping through each flavor card to set up click events.
        for ((cardView, imageResId) in cards) {
            cardView.setOnClickListener {

                // If the user clicks a card they already picked, undo the selection.
                if (selection1ImageRes == imageResId) {
                    selection1ImageRes = null
                    bottomCard1.visibility = View.INVISIBLE
                    cardView.alpha = 1.0f
                } else if (selection2ImageRes == imageResId) {
                    selection2ImageRes = null
                    bottomCard2.visibility = View.INVISIBLE
                    cardView.alpha = 1.0f
                } else {

                    // If the first slot is empty, put this flavor in slot one.
                    // Show it in the bottom bar.
                    // Dim the card so it looks selected.
                    if (selection1ImageRes == null) {
                        selection1ImageRes = imageResId
                        bottomImg1.setImageResource(imageResId)
                        bottomCard1.visibility = View.VISIBLE
                        cardView.alpha = 0.6f

                        // If slot one is full, try slot two.
                    } else if (selection2ImageRes == null) {
                        selection2ImageRes = imageResId
                        bottomImg2.setImageResource(imageResId)
                        bottomCard2.visibility = View.VISIBLE
                        cardView.alpha = 0.6f

                        // Both slots are full! Tell the user they cannot pick more.
                    } else {
                        Toast.makeText(this, "You can only pick 2 flavors!", Toast.LENGTH_SHORT).show()
                    }
                }
                updateMixButton()
            }
        }

        updateMixButton()

        // Time to mix!
        btnMix.setOnClickListener {
            if (selection1ImageRes != null && selection2ImageRes != null) {

                // Grab the two selected flavors.
                val selectedSet = setOf(selection1ImageRes!!, selection2ImageRes!!)

                // Look up the match in the dictionary above.
                val resultImage = mixCombinations[selectedSet]

                // Show the beautiful result popup!
                if (resultImage != null) {
                    isShowingMixResult = true
                    imgPopupBg.setImageResource(resultImage)
                    btnPopupAction.setImageResource(R.drawable.btn_back)
                    popupOverlay.visibility = View.VISIBLE
                }
            }
        }

        // ==========================================
        // THE PERFECT FLAWLESS SOUND LOGIC
        // ==========================================
        cardSoundControl.setOnClickListener {
            if (isMusicPlaying) {
                // Music is ON. The user clicked it to MUTE.
                MusicManager.pauseMusic()
                imgSoundControl.setImageResource(R.drawable.ic_sound_pause) // Show the 'X'
                isMusicPlaying = false
            } else {
                // Music is OFF. The user clicked it to UNMUTE.
                MusicManager.resumeMusic()
                imgSoundControl.setImageResource(R.drawable.ic_sound_play) // Show the 'Waves'
                isMusicPlaying = true
            }
        }

        // When scrolling down past 150 pixels, fade in the floating sound button.
        mainScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            if (scrollY > 150) {
                if (cardSoundControl.visibility == View.GONE) {
                    cardSoundControl.visibility = View.VISIBLE
                    cardSoundControl.animate().alpha(1f).setDuration(300).start()
                }
            } else {
                // When scrolling back up to the top, fade it out.
                if (cardSoundControl.visibility == View.VISIBLE) {
                    cardSoundControl.animate().alpha(0f).setDuration(300)
                        .withEndAction { cardSoundControl.visibility = View.GONE }
                        .start()
                }
            }
        }

        // Clicking the info button shows the intro popup.
        btnInfo.setOnClickListener {
            isShowingInstructionsPage = false
            isShowingMixResult = false
            imgPopupBg.setImageResource(R.drawable.popup_intro)
            btnPopupAction.setImageResource(R.drawable.btn_next)
            popupOverlay.visibility = View.VISIBLE
        }

        // This button does different things depending on what popup is visible.
        btnPopupAction.setOnClickListener {

            // If showing a result, clear everything to start fresh.
            if (isShowingMixResult) {
                popupOverlay.visibility = View.GONE
                isShowingMixResult = false
                selection1ImageRes = null
                selection2ImageRes = null
                bottomCard1.visibility = View.INVISIBLE
                bottomCard2.visibility = View.INVISIBLE
                for ((cardView, _) in cards) {
                    cardView.alpha = 1.0f
                }
                updateMixButton()

                // If showing the intro, switch to the instructions page.
            } else if (!isShowingInstructionsPage) {
                isShowingInstructionsPage = true
                imgPopupBg.setImageResource(R.drawable.popup_instructions)
                btnPopupAction.setImageResource(R.drawable.btn_exit)

                // Otherwise, just close the popup completely.
            } else {
                popupOverlay.visibility = View.GONE
            }
        }

        // Clicking the dark background closes the popup.
        popupOverlay.setOnClickListener {
            popupOverlay.visibility = View.GONE
        }

        // Doing nothing here. This stops clicks from passing through the popup image.
        imgPopupBg.setOnClickListener { }
    }

    // Stop the music forever when closing the app.
    override fun onDestroy() {
        super.onDestroy()
        MusicManager.stopMusic()
    }
}