package com.hackerrank.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hackerrank.android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val dealtCards = mutableListOf(
        R.drawable.ten_clubs,
        R.drawable.nine_clubs,
        R.drawable.eight_clubs,
        R.drawable.seven_clubs,
        R.drawable.six_clubs,
        R.drawable.five_clubs
    )

    private val deck = mutableListOf(
        R.drawable.king_of_clubs,
        R.drawable.king_of_diamonds,
        R.drawable.king_of_hearts,
        R.drawable.king_of_spades,
        R.drawable.ace_of_clubs,
        R.drawable.ace_of_diamonds,
        R.drawable.ace_of_hearts,
        R.drawable.ace_of_spades,
        R.drawable.king_of_clubs,
        R.drawable.king_of_diamonds,
        R.drawable.king_of_hearts,
        R.drawable.king_of_spades,
        R.drawable.ace_of_clubs,
        R.drawable.ace_of_diamonds,
        R.drawable.ace_of_hearts,
        R.drawable.ace_of_spades
    )

    private var selectedDealt = UNDEFINED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupShuffle()
        refreshDealt()
        refreshDeck()
    }

    private fun setupShuffle() {
        binding.shuffleBtn.setOnClickListener {
            dealtCards.shuffle()
            selectedDealt = UNDEFINED
            refreshDealt()
        }
    }

    private fun refreshDealt() {
        binding.dealtCards.adapter =
            DealtCardsAdapter(this, dealtCards, selectedDealt) { position ->
                swapDealtCards(position)
            }

        binding.dealtCards.setOnItemClickListener { _, _, position, _ ->
            selectedDealt =
                if (selectedDealt == position) UNDEFINED else position
            refreshDealt()
        }
    }

    private fun swapDealtCards(position: Int) {
        if (selectedDealt == UNDEFINED) {
            selectedDealt = position
        } else if (selectedDealt != position) {
            val tmp = dealtCards[selectedDealt]
            dealtCards[selectedDealt] = dealtCards[position]
            dealtCards[position] = tmp
            selectedDealt = UNDEFINED
        } else {
            selectedDealt = UNDEFINED
        }
        refreshDealt()
    }

    private fun refreshDeck() {
        binding.deck.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.deck.setHasFixedSize(false)

        binding.deck.adapter =
            DeckAdapter(deck) { deckIndex ->
                if (selectedDealt == UNDEFINED) return@DeckAdapter

                val tmp = dealtCards[selectedDealt]
                dealtCards[selectedDealt] = deck[deckIndex]
                deck[deckIndex] = tmp

                selectedDealt = UNDEFINED
                refreshDealt()
                refreshDeck()
            }

        binding.deck.addItemDecoration(HorizontalDecoration(10))
    }

    companion object {
        private const val UNDEFINED = -1
    }
}