package com.tompee.utilities.knowyourmeds.feature.search.search

import com.tompee.utilities.knowyourmeds.base.BaseView
import com.tompee.utilities.knowyourmeds.model.Medicine2
import io.reactivex.Observable

interface SearchBarView : BaseView {
    fun searchString(): Observable<String>
    fun detailedSearchRequest(): Observable<Any>
    fun showInvalidSearchString()
    fun showStartSearchSequence()
    fun showEndSearchSequence()
    fun showSearchResults(list: List<Medicine2>)
    fun moveToDetailActivity()
}