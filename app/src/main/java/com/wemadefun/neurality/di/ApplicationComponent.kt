package com.wemadefun.neurality.di

import com.wemadefun.neurality.MainActivity
import com.wemadefun.neurality.ui.EnergyFragment
import com.wemadefun.neurality.ui.dialogs.FeedbackDialog
import com.wemadefun.neurality.ui.dialogs.LowEnergyDialog
import com.wemadefun.neurality.ui.dialogs.RatingDialog
import com.wemadefun.neurality.ui.dialogs.RewardedEnergyDialog
import com.wemadefun.neurality.ui.eventgameover.GameOverFragment
import com.wemadefun.neurality.ui.eventgamepause.GamePausedFragment
import com.wemadefun.neurality.ui.games.GameFragment
import com.wemadefun.neurality.ui.others.OthersFragment
import com.wemadefun.neurality.ui.others.OthersFragmentOld
import com.wemadefun.neurality.ui.signin.SignInFlowFragment
import com.wemadefun.neurality.ui.signin.SignInFragment
import com.wemadefun.neurality.ui.stats.StatsFragment
import com.wemadefun.neurality.ui.subscription.SubscriptionFragment
import com.wemadefun.neurality.ui.train.TrainFragment
import com.wemadefun.neurality.ui.traindetails.TrainDetailsFragment
import com.wemadefun.neurality.ui.utilfragments.NonAnonymousFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, ActivityModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(subscriptionFragment: SubscriptionFragment)
    fun inject(preferencesFragment: OthersFragmentOld)
    fun inject(preferencesFragment: OthersFragment)
    fun inject(trainFragment: TrainFragment)
    fun inject(trainDetailsFragment: TrainDetailsFragment)
    fun inject(signInFragment: SignInFragment)
    fun inject(signInFlowFragment: SignInFlowFragment)
    fun inject(statsFragment: StatsFragment)
    fun inject(pauseFragment: GamePausedFragment)
    fun inject(gameOverFragment: GameOverFragment)
    fun inject(gameFragment: GameFragment)

    fun inject(energyFragment: EnergyFragment)
    fun inject(nonAnonFragment: NonAnonymousFragment)

    fun inject(lowEnergyDialog: LowEnergyDialog)
    fun inject(rewardedEnergyDialog: RewardedEnergyDialog)
    fun inject(ratingDialog: RatingDialog)
    fun inject(feedbackDialog: FeedbackDialog)

    fun inject(activity: MainActivity)
}