package com.wemadefun.neurality.ui.signin

import com.wemadefun.neurality.data.userdata.modules.source.UserPreferencesWrapper
import javax.inject.Inject

class SignInUtils @Inject constructor(private val preferencesWrapper: UserPreferencesWrapper)
{
    private val aboveEighteenLocator = "signin_above_eighteen"

    enum class AgeStatus {
        ABOVE_EIGHTEEN, BELOW_EIGHTEEN, NULL
    }

    fun getUserAboveEighteen(): AgeStatus {
        return when (preferencesWrapper.getString(aboveEighteenLocator, "null")) {
            "true" -> AgeStatus.ABOVE_EIGHTEEN
            "false" -> AgeStatus.BELOW_EIGHTEEN
            else -> AgeStatus.NULL
        }
    }

    fun setUserAboveEighteen(isAboveEighteen: Boolean) {
        preferencesWrapper.putString(aboveEighteenLocator, isAboveEighteen.toString())
    }

    fun isAgeSelected(): Boolean {
        return getUserAboveEighteen() != AgeStatus.NULL
    }

}