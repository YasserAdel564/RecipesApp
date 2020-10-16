package com.recipe.app.ui.splash

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.recipe.app.R
import com.recipe.app.utils.viewAnimation
import kotlinx.android.synthetic.main.fragment_splash.*


class SplashFragment : Fragment() {

    private var timer: CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        startAnimation()
        startApp()
    }

    private fun startApp() {
        timer = object : CountDownTimer(3000, 500) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                findNavController().navigate(
                    R.id.RecipesFragment,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.SplashFragment, true).build()
                )
            }
        }
        timer!!.start()
    }

    private fun startAnimation() {
        requireActivity().viewAnimation(splash_icon)
    }

    override fun onStop() {
        super.onStop()
        timer?.cancel()
    }
}