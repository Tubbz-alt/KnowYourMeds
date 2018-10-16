package com.tompee.utilities.knowyourmeds.feature.search.persist

import android.os.Bundle
import android.view.View
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_recent_favorite.*

class RecentFavoriteFragment : BaseFragment() {

    companion object {
        private const val IS_RECENT = "is_recent"

        fun newInstance(isRecent: Boolean): RecentFavoriteFragment {
            val fragment = RecentFavoriteFragment()
            val bundle = Bundle()
            bundle.putBoolean(IS_RECENT, isRecent)
            fragment.arguments = bundle
            return fragment
        }
    }

    //region RecentFavoriteFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val isRecent = arguments?.getBoolean(IS_RECENT) ?: false
        header.setText(if (isRecent) R.string.recent else R.string.favorites)
    }
    //endregion

    //region BaseFragment
    override fun layoutId(): Int = R.layout.fragment_recent_favorite
    //endregion
}