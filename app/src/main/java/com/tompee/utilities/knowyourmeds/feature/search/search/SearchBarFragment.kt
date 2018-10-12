package com.tompee.utilities.knowyourmeds.feature.search.search

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import android.view.animation.BounceInterpolator
import android.widget.AdapterView
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.jakewharton.rxbinding2.view.RxView
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.base.BaseFragment
import com.tompee.utilities.knowyourmeds.controller.Utilities
import com.tompee.utilities.knowyourmeds.core.helper.AnimationHelper
import com.tompee.utilities.knowyourmeds.feature.detail.DetailActivity
import com.tompee.utilities.knowyourmeds.feature.search.SearchActivity
import com.tompee.utilities.knowyourmeds.model.Medicine2
import com.tompee.utilities.knowyourmeds.view.adapter.StringListAdapter
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject

class SearchBarFragment : BaseFragment(), SearchBarView, FloatingSearchView.OnSearchListener,
        AdapterView.OnItemClickListener {

    companion object {
        private const val ANIMATION_DURATION = 700
        private const val VERTICAL_POSITION_BOUNCE = 152
        fun newInstance(): SearchBarFragment = SearchBarFragment()
    }

    private val searchSubject = BehaviorSubject.create<String>()

    @Inject
    lateinit var searchBarPresenter: SearchBarPresenter

    //region SearchBarFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchView.setOnSearchListener(this)
        searchBarPresenter.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        searchBarPresenter.detachView()
    }
    //endregion

    //region BaseFragment

    override fun setupComponent() {
        SearchActivity[activity!!].component.inject(this)
    }

    override fun layoutId(): Int = R.layout.fragment_search
    //endregion

    //region OnSearchListener

    override fun onSearchAction(currentQuery: String?) {
        searchSubject.onNext(currentQuery!!)
    }

    override fun onSuggestionClicked(searchSuggestion: SearchSuggestion?) {
    }
    //endregion

    //region SearchBarView

    override fun searchString(): Observable<String> = searchSubject

    override fun detailedSearchRequest(): Observable<Any> = RxView.clicks(checkout)

    override fun showInvalidSearchString() {
        YoYo.with(Techniques.Shake)
                .duration(ANIMATION_DURATION.toLong())
                .playOn(searchView)
    }

    @SuppressLint("RestrictedApi")
    override fun showStartSearchSequence() {
        viewSwitcher.visibility = View.INVISIBLE
        checkout.visibility = View.INVISIBLE
        AnimationHelper.animateVerticalPosition(checkout,
                -(Utilities.convertDPtoPixel(context!!, VERTICAL_POSITION_BOUNCE)).toFloat(), 0)
        searchProgress.show()
    }

    override fun showEndSearchSequence() {
        searchProgress.hide()
    }

    @SuppressLint("RestrictedApi")
    override fun showSearchResults(list: List<Medicine2>) {
        when {
            list.size > 1 -> {
                showViewSwitcher(false)
                suggestionHeader.text = String.format(getString(R.string.suggestions_results), searchView.query)

                val adapter = StringListAdapter(context, list.map { it.name }, false, "")
                suggestionList.onItemClickListener = this
                suggestionList.adapter = adapter
            }
            list.any() -> {
                header.text = list[0].name
                type.setText(if (list[0].isPrescribable) R.string.property_prescribable_yes else R.string.property_prescribable_no)
                tty.text = list[0].tty?.name(context!!)

                showViewSwitcher(true)
                checkout.visibility = View.VISIBLE
                AnimationHelper.animateVerticalPosition(checkout,
                        (Utilities.convertDPtoPixel(context!!, VERTICAL_POSITION_BOUNCE)).toFloat(),
                        ANIMATION_DURATION, BounceInterpolator())
            }
            else -> Snackbar.make(activity?.findViewById(android.R.id.content)!!, getString(R.string.no_suggestions,
                    searchView.query), Snackbar.LENGTH_LONG).show()
        }
    }

    private fun showViewSwitcher(isFirst: Boolean) {
        if (isFirst && viewSwitcher.displayedChild != 0) {
            viewSwitcher.showPrevious()
        } else if (!isFirst && viewSwitcher.displayedChild == 0) {
            viewSwitcher.showNext()
        }
        viewSwitcher.visibility = View.VISIBLE
    }

    override fun moveToDetailActivity() {
        val intent = Intent(context, DetailActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    //endregion

    //region OnItemClickListener
    override fun onItemClick(adapter: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        val string = adapter?.adapter?.getItem(position) as String
        searchView.setSearchText(string)
        searchSubject.onNext(string)
    }
    //endregion
}