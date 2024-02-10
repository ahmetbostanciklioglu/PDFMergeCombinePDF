package com.ahmetbostancikli.combine.pdf.presentation.ui.advertise

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.ahmetbostancikli.combine.pdf.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.wada811.databindingktx.BuildConfig

class Advertise(private val activity: Activity? = null) {


    //Variables for functions which are the Ads functions.
    private  val interstitialAdUnitId = if (BuildConfig.DEBUG) activity?.getString(R.string.interstitial_test_id)
    else activity?.getString(R.string.interstitial_id)
    private  val rewardedAdAdUnitId = if (BuildConfig.DEBUG) activity?.getString(R.string.rewarded_test_id)
    else activity?.getString(R.string.rewarded_id)
    private var mInterstitialAd: InterstitialAd? = null
    private var adRequest: AdRequest? = null
    private var rewardedAd: RewardedAd? = null


    init {
        activity?.apply {
            adRequest = AdRequest.Builder().build()
        }
    }


    //This Function shows RewardedAd
    fun showRewardedAd() {
        adRequest?.let { adRequest ->
            if (activity != null) {
                RewardedAd.load(activity, rewardedAdAdUnitId!!, adRequest, object : RewardedAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        rewardedAd = null
                    }

                    override fun onAdLoaded(ad: RewardedAd) {
                        rewardedAd = ad
                        ad.show(activity, OnUserEarnedRewardListener { rewardItem ->
                            // Handle the reward.
                            val rewardAmount = rewardItem.amount
                            val rewardType = rewardItem.type
                        })

                    }
                })
            }
        }

    }



    //This Function shows InterstitialAd
    fun showInterstitialAd(){
        if (activity != null) {
            adRequest?.let { adRequest ->
                InterstitialAd.load(
                    activity,
                    interstitialAdUnitId!!,
                    adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            mInterstitialAd = null
                        }
                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            mInterstitialAd = interstitialAd
                            mInterstitialAd?.show(activity)
                        }
                    }
                )
            }
        }
    }

    //This function makes Banner Ad View
    @Composable
    fun BannerAdView() {
        val bannerUnitId = if (BuildConfig.DEBUG)
            stringResource(id = R.string.banner_test_id)
        else stringResource(id = R.string.banner_id)

        AndroidView(
            factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize.FULL_BANNER)
                    adUnitId = bannerUnitId
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }








}
