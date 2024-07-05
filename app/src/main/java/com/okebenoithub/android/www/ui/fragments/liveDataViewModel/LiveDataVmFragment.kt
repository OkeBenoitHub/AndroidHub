package com.okebenoithub.android.www.ui.fragments.liveDataViewModel

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.okebenoithub.android.www.R
import com.okebenoithub.android.www.databinding.FragmentLiveDataVmBinding
import com.okebenoithub.android.www.utils.CountDownListener
import com.okebenoithub.android.www.utils.CountDownUtil

class LiveDataVmFragment : Fragment() {
    private val viewModel: LiveDataVmViewModel by viewModels()
    private var _binding: FragmentLiveDataVmBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLiveDataVmBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // observe for winner or tight between the 2 teams
        viewModel.winnerOrTight.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { _ ->
                binding.winScoreLabel.visibility = View.VISIBLE
                binding.loseScoreLabel.visibility = View.VISIBLE
            }
        }

        val countDownUtil = CountDownUtil()

        /**
         * Observe the stop game live data
         */
        viewModel.stopGame.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { isStopped ->
                 if (isStopped) {
                     // stop countdown timer
                     countDownUtil.cancelCountdown()
                     binding.startGameBtn.isEnabled = true
                     binding.startGameBtn.text = getString(R.string.start)
                     // check winner or tight between the 2 teams
                     viewModel.checkWinnerOrTight()
                 }
            }
        }

        /**
         * Observe the start game live data
         */
        viewModel.startGame.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { isStarted ->
                if (isStarted) {
                    binding.winScoreLabel.visibility = View.INVISIBLE
                    binding.loseScoreLabel.visibility = View.INVISIBLE

                    // start countdown timer
                    countDownUtil.startCountdown(object : CountDownListener {
                        override fun onCountDownFinished() {
                            // implement an interface here
                            binding.startGameBtn.text = getString(R.string.start)
                            // enable the button again
                            binding.startGameBtn.isEnabled = true
                            // check winner or tight between the 2 teams
                            viewModel.checkWinnerOrTight()
                        }

                        override fun onCountDownTick(timeLeft: String) {
                            // disable the button while timer is ticking
                            binding.startGameBtn.isEnabled = false
                            binding.startGameBtn.text = timeLeft
                        }
                    })
                }
            }
        }

        return binding.root
    }
}