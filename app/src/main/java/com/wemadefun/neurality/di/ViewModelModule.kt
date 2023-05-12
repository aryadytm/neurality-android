package com.wemadefun.neurality.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wemadefun.neurality.ui.EnergyViewModel
import com.wemadefun.neurality.ui.eventgameover.GameOverViewModel
import com.wemadefun.neurality.ui.signin.SignInViewModel
import com.wemadefun.neurality.ui.stats.StatsViewModel
import com.wemadefun.neurality.ui.train.TrainViewModel
import com.wemadefun.neurality.ui.traindetails.TrainDetailsViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.reflect.KClass

@Singleton
class ViewModelFactory @Inject constructor(private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModels[modelClass]?.get() as T
}

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    // Add ViewModels below ...

    @Binds
    @IntoMap
    @ViewModelKey(SignInViewModel::class)
    internal abstract fun signInViewModel(viewModel: SignInViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EnergyViewModel::class)
    internal abstract fun energyViewModel(viewModel: EnergyViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TrainViewModel::class)
    internal abstract fun trainViewModel(viewModel: TrainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StatsViewModel::class)
    internal abstract fun statsViewModel(viewModel: StatsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TrainDetailsViewModel::class)
    internal abstract fun trainDetailsViewModel(viewModel: TrainDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GameOverViewModel::class)
    internal abstract fun gameOverViewModel(viewModel: GameOverViewModel): ViewModel

}