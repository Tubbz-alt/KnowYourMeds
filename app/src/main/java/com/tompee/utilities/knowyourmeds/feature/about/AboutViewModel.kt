package com.tompee.utilities.knowyourmeds.feature.about

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tompee.utilities.knowyourmeds.BuildConfig
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.base.BaseViewModel
import com.tompee.utilities.knowyourmeds.interactor.HelpInteractor
import com.tompee.utilities.knowyourmeds.model.SchedulerPool

class AboutViewModel private constructor(helpInteractor: HelpInteractor,
                                         schedulerPool: SchedulerPool,
                                         private val context: Context) :
        BaseViewModel<HelpInteractor>(helpInteractor, schedulerPool) {

    class Factory(private val helpInteractor: HelpInteractor,
                  private val schedulerPool: SchedulerPool,
                  private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return AboutViewModel(helpInteractor, schedulerPool, context) as T
        }
    }

    val title = MutableLiveData<String>()
    val version = MutableLiveData<String>()

    init {
        title.postValue(context.getString(R.string.label_about))
        version.postValue(BuildConfig.VERSION_NAME)
    }
}