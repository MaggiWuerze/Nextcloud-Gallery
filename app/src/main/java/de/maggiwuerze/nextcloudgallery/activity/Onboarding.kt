package de.maggiwuerze.nextcloudgallery.activity

import android.animation.ArgbEvaluator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import androidx.viewpager.widget.ViewPager
import com.thegrizzlylabs.sardineandroid.impl.SardineException
import de.maggiwuerze.nextcloudgallery.R
import de.maggiwuerze.nextcloudgallery.adapter.OnboardingPagerAdapter
import de.maggiwuerze.nextcloudgallery.extensions.getBitmapFromDrawable
import de.maggiwuerze.nextcloudgallery.model.account.Account
import de.maggiwuerze.nextcloudgallery.persistence.model.DatabaseManager
import de.maggiwuerze.nextcloudgallery.util.extensions.doAfterDelay
import de.maggiwuerze.nextcloudgallery.util.extensions.showToastShort
import de.maggiwuerze.nextcloudgallery.viewmodel.OnboardingViewModel
import kotlinx.android.synthetic.main.activity_onboarding.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException


class Onboarding : AppCompatActivity() {

    var page = 0
    private var indicators: Array<ImageView>? = null
    private lateinit var onboardingViewModel: OnboardingViewModel

    val evaluator: ArgbEvaluator = ArgbEvaluator()

    val errorHandler = CoroutineExceptionHandler { _, error ->
        MainScope().launch {
//            .error = true
            Log.e("LoginErrorHandler", error.localizedMessage)
            when (error) {
                is SocketTimeoutException -> {
                    showToastShort(this@Onboarding, "Timeout")
                }
                is SardineException -> {
                    when (error.statusCode) {
                        401 -> showToastShort(this@Onboarding, "Wrong Credentials")
                        else -> {
                            showToastShort(this@Onboarding, "Unknown Error : ${error.statusCode}")
                        }
                    }

                }
                else -> {
                    showToastShort(this@Onboarding, "Unknown Error : ${error.cause}")
                }
            }

            login_btn.doneLoadingAnimation(
                R.color.red,
                getBitmapFromDrawable(
                    this@Onboarding,
                    R.drawable.ic_close_black_24dp
                )!!
            )
            doAfterDelay({
                login_btn.revertAnimation()
            }, 1000L)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_onboarding)

        onboardingViewModel = ViewModelProviders.of(this).get(OnboardingViewModel::class.java)
        indicators = arrayOf(intro_indicator_0, intro_indicator_1, intro_indicator_2)

        onboarding_pager.adapter =
            OnboardingPagerAdapter(
                this,
                supportFragmentManager
            )
        moveToPage(page, true)

        val color1 = ContextCompat.getColor(this, R.color.blue)
        val color2 = ContextCompat.getColor(this, R.color.orange)
        val color3 = ContextCompat.getColor(this, R.color.green)
        val colorList = intArrayOf(color1, color2, color3, color1)

        onboarding_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                val colorUpdate = evaluator.evaluate(
                    positionOffset,
                    colorList[position],
                    colorList[if (position == 3) position else position + 1]
                ) as Int

                when (position) {
                    1 -> intro_btn_next.visibility = View.VISIBLE
                    else -> intro_btn_next.visibility = View.INVISIBLE
                }

                if (position == 3) {
                    intro_indicator_container.visibility = View.GONE
                    intro_indicator_divider.visibility = View.GONE
                }

                onboarding_pager.setBackgroundColor(colorUpdate)
            }

            override fun onPageSelected(position: Int) {
                page = position
                updateIndicators(page)

                when (position) {
                    0 -> onboarding_pager.setBackgroundColor(color1)
                    1 -> onboarding_pager.setBackgroundColor(color2)
                    2 -> onboarding_pager.setBackgroundColor(color3)
                    3 -> onboarding_pager.setBackgroundColor(color1)
                }

                intro_btn_next.visibility = if (position == 2) View.GONE else View.VISIBLE
                intro_btn_finish.visibility = if (position == 2) View.VISIBLE else View.GONE
            }


        })

        intro_btn_next.setOnClickListener {
            page += 1
            onboarding_pager.setCurrentItem(page, true)
        }

        intro_btn_finish.setOnClickListener {
            page += 1
            onboarding_pager.setCurrentItem(page, true)
        }


    }


    fun moveToPage(position: Int, smoothScrolling: Boolean) {
        onboarding_pager.setCurrentItem(position, smoothScrolling)
        updateIndicators(position)
    }

    fun updateIndicators(position: Int) {
        for (i in indicators!!.indices) {
            indicators!![i].setBackgroundResource(
                if (i == position) R.drawable.indicator_selected else R.drawable.indicator_unselected
            )
        }
    }

}