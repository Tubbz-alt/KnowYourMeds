package com.tompee.utilities.knowyourmeds.view.fragment;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.controller.AnimationUtility;
import com.tompee.utilities.knowyourmeds.controller.Utilities;
import com.tompee.utilities.knowyourmeds.controller.task.SearchTask;
import com.tompee.utilities.knowyourmeds.model.Medicine;
import com.tompee.utilities.knowyourmeds.view.adapter.StringListAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements FloatingSearchView.OnSearchListener,
        SearchTask.SearchListener, AdapterView.OnItemClickListener {
    private static final int VERTICAL_POSITION_BOUNCE = 85;
    private static final int ANIMATION_DURATION = 700;

    private ViewSwitcher mViewSwitcher;
    private FloatingActionButton mCheckout;
    private FloatingSearchView mSearchView;

    private List<Medicine> mMedList;

    private SearchTask mTask;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        mSearchView = (FloatingSearchView) view.findViewById(R.id.floating_search_view);
        mSearchView.setOnSearchListener(this);
        mViewSwitcher = (ViewSwitcher) view.findViewById(R.id.view_switcher);
        mCheckout = (FloatingActionButton) view.findViewById(R.id.checkout);
        mCheckout.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.
                getColor(getContext(), R.color.light_text)));
        return view;
    }

    private void searchFromDatabase() {
//        mResultBar.setVisibility(View.VISIBLE);
//        mResultText.setText(R.string.search_results);
//        DatabaseHelper db = DatabaseHelper.getInstance(getContext());
//        List<Medicine> medList = db.getAllShortEntriesByName(mEditText.getText().toString());
//        if (medList.isEmpty()) {
//            mListView.setVisibility(View.GONE);
//            mNoResultsView.setVisibility(View.VISIBLE);
//            return;
//        }
//        mListView.setVisibility(View.VISIBLE);
//        mNoResultsView.setVisibility(View.GONE);
//        boolean isFullLayoutSupported = ((MainActivity) getActivity()).isFullLayoutSupported();
//        MainListAdapter adapter = new MainListAdapter(getContext(), medList, isFullLayoutSupported,
//                true, false);
//        mListView.setAdapter(adapter);
//        mMedList = medList;
    }

    private void startSearch(String string) {
        if (mTask == null) {
            mTask = new SearchTask(getContext(), this);
            mTask.execute(string);
        }
    }

    @Override
    public void onSearchSuccess(List<Medicine> medList) {
        mTask = null;
        if (medList.size() > 0) {
            mMedList = medList;
            TextView textView = (TextView) mViewSwitcher.findViewById(R.id.header);
            textView.setText(mMedList.get(0).getName());
            textView = (TextView) mViewSwitcher.findViewById(R.id.rx_type);
            textView.setText(mMedList.get(0).isPrescribable() ? R.string.property_prescribable_yes :
                    R.string.property_prescribable_no);
            textView = (TextView) mViewSwitcher.findViewById(R.id.tty);
            textView.setText(Utilities.getTtyString(getContext(), mMedList.get(0).getTty()));

            showViewSwitcher(true);
            mCheckout.setVisibility(View.VISIBLE);
            AnimationUtility.animateVerticalPosition(mCheckout, Utilities.
                            convertDPtoPixel(getContext(), VERTICAL_POSITION_BOUNCE),
                    ANIMATION_DURATION, new BounceInterpolator());
        }
    }

    @Override
    public void onSearchFailed(List<Medicine> suggestedMed) {
        mTask = null;
        if (suggestedMed.size() > 0) {
            mMedList = suggestedMed;
            resetCheckoutButton();
            showViewSwitcher(false);
            TextView suggestionText = (TextView) mViewSwitcher.findViewById(R.id.suggestion_header);
            suggestionText.setText(String.format(getString(R.string.suggestions_results),
                    mSearchView.getQuery()));
            List<String> list = new ArrayList<>();
            for (Medicine med : suggestedMed) {
                list.add(med.getName());
            }
            StringListAdapter adapter = new StringListAdapter(getContext(), list, 0);
            ListView listView = (ListView) mViewSwitcher.findViewById(R.id.suggestion_list);
            listView.setOnItemClickListener(this);
            listView.setAdapter(adapter);
        } else {
            SuperActivityToast.create(getActivity(), new Style(), Style.TYPE_BUTTON)
                    .setProgressBarColor(Color.WHITE)
                    .setText(getString(R.string.no_suggestions, mSearchView.getQuery()))
                    .setDuration(Style.DURATION_LONG)
                    .setFrame(Style.FRAME_LOLLIPOP)
                    .setColor(ContextCompat.getColor(getContext(), R.color.cardAlpha))
                    .setAnimations(Style.ANIMATIONS_POP).show();
        }
    }

    @Override
    public void onConnectionError() {
        mTask = null;
        Toast.makeText(getContext(), getString(R.string.toast_no_internet),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String medName = mMedList.get(position).getName();
        mSearchView.setSearchText(medName);
        startSearch(medName);
        mViewSwitcher.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser && mViewSwitcher != null) {
            mViewSwitcher.setVisibility(View.INVISIBLE);
        }
        resetCheckoutButton();
    }

    @Override
    public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
    }

    @Override
    public void onSearchAction(String currentQuery) {
        startSearch(currentQuery);
    }

    private void resetCheckoutButton() {
        if (mCheckout != null) {
            mCheckout.setVisibility(View.INVISIBLE);
        }
        if (mCheckout != null) {
            AnimationUtility.animateVerticalPosition(mCheckout, -Utilities.
                    convertDPtoPixel(getContext(), VERTICAL_POSITION_BOUNCE), 0);
        }
    }

    private void showViewSwitcher(boolean isFirst) {
        if (isFirst && mViewSwitcher.getDisplayedChild() != 0) {
            mViewSwitcher.showPrevious();
        } else if (!isFirst && mViewSwitcher.getDisplayedChild() == 0) {
            mViewSwitcher.showNext();
        }
        mViewSwitcher.setVisibility(View.VISIBLE);
    }
}
