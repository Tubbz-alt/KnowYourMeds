package com.tompee.utilities.knowyourmeds.feature.detail.type

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.feature.common.ListViewModel
import com.tompee.utilities.knowyourmeds.interactor.DetailInteractor
import com.tompee.utilities.knowyourmeds.model.Brand
import com.tompee.utilities.knowyourmeds.model.BrandedDoseFormGroup
import com.tompee.utilities.knowyourmeds.model.BrandedDrugComponent
import com.tompee.utilities.knowyourmeds.model.BrandedDrugPack
import com.tompee.utilities.knowyourmeds.model.ClinicalDoseFormGroup
import com.tompee.utilities.knowyourmeds.model.ClinicalDrugComponent
import com.tompee.utilities.knowyourmeds.model.ClinicalDrugPack
import com.tompee.utilities.knowyourmeds.model.Medicine
import com.tompee.utilities.knowyourmeds.model.MedicineType
import com.tompee.utilities.knowyourmeds.model.SchedulerPool
import io.reactivex.Completable
import io.reactivex.rxkotlin.plusAssign

class TypeViewModel private constructor(detailInteractor: DetailInteractor,
                                        schedulerPool: SchedulerPool,
                                        private val context: Context) :
        ListViewModel<DetailInteractor, Medicine>(detailInteractor, schedulerPool) {
    private lateinit var type: MedicineType

    class Factory(private val detailInteractor: DetailInteractor,
                  private val schedulerPool: SchedulerPool,
                  private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return TypeViewModel(detailInteractor, schedulerPool, context) as T
        }
    }

    fun onLoad(type: MedicineType) {
        this.type = type
        determineTitle(context, type)

        subscriptions += Completable.fromAction {
            showSearchButton.postValue(false)
            searching.postValue(true)
        }.andThen(interactor.getCachedMedicineType(type))
                .doFinally { searching.postValue(false) }
                .subscribeOn(schedulerPool.io)
                .subscribe(::postList) {
                    showSearchButton.postValue(true)
                }
    }

    override fun search() {
        super.search()
        subscriptions += Completable.fromAction { searching.postValue(true) }
                .andThen(interactor.getMedicineType(type))
                .doOnSuccess { count.postValue(it.size) }
                .doFinally { searching.postValue(false) }
                .subscribeOn(schedulerPool.io)
                .subscribe(::postList) { isListEmpty.postValue(true) }
    }

    private fun determineTitle(context: Context, type: MedicineType) {
        title.postValue(context.getString(
                when (type) {
                    Brand -> R.string.label_brand_name
                    BrandedDrugComponent -> R.string.label_sbdc
                    BrandedDrugPack -> R.string.label_sbd
                    BrandedDoseFormGroup -> R.string.label_sbdg
                    ClinicalDrugComponent -> R.string.label_scdc
                    ClinicalDrugPack -> R.string.label_scd
                    ClinicalDoseFormGroup -> R.string.label_scdg
                    else -> R.string.label_brand_name
                }))
    }
}