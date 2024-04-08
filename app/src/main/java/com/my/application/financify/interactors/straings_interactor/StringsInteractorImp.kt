package com.my.application.financify.interactors.straings_interactor

import android.content.Context
import com.my.application.financify.R

class StringsInteractorImp(private val context: Context):StringsInteractor {
    override val textInternet: String
        get() = context.getString(R.string.check_internet)
    override val textError: String
        get() = context.getString(R.string.error)
}