package com.tompee.utilities.knowyourmeds.view.fragment;

import android.content.Intent;
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
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.controller.Utilities;
import com.tompee.utilities.knowyourmeds.controller.task.SearchTask;
import com.tompee.utilities.knowyourmeds.core.helper.AnimationHelper;
import com.tompee.utilities.knowyourmeds.model.Medicine;
import com.tompee.utilities.knowyourmeds.view.MedDetailActivity;
import com.tompee.utilities.knowyourmeds.view.adapter.StringListAdapter;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements FloatingSearchView.OnSearchListener,
        SearchTask.SearchListener, AdapterView.OnItemClickListener, View.OnClickListener {
    private static final int VERTICAL_POSITION_BOUNCE = 82;
    private static final int ANIMATION_DURATION = 700;

    private ViewSwitcher mViewSwitcher;
    private FloatingActionButton mCheckout;
    private FloatingSearchView mSearchView;
    private AVLoadingIndicatorView mSearchAnimation;

    private List<Medicine> mMedList;
    private SearchTask mTask;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        mSearchView = (FloatingSearchView) view.findViewById(R.id.searchView);
        mSearchView.setOnSearchListener(this);
        mViewSwitcher = (ViewSwitcher) view.findViewById(R.id.viewSwitcher);
        mCheckout = (FloatingActionButton) view.findViewById(R.id.checkout);
        mCheckout.setOnClickListener(this);
        mSearchAnimation = (AVLoadingIndicatorView) view.findViewById(R.id.searchProgress);
        return view;
    }

    private void startSearch(String string) {
        if (string.isEmpty()) {
            YoYo.with(Techniques.Shake)
                    .duration(ANIMATION_DURATION)
                    .playOn(mSearchView);
            return;
        }
        mViewSwitcher.setVisibility(View.INVISIBLE);
        resetCheckoutButton();
        if (mTask == null) {
            mSearchAnimation.show();
            mTask = new SearchTask(getContext(), this);
            mTask.execute(string);
        }
    }

    @Override
    public void onSearchSuccess(List<Medicine> medList) {
        mTask = null;
        mSearchAnimation.hide();
        if (medList.size() > 0) {
            mMedList = medList;
            TextView textView = (TextView) mViewSwitcher.findViewById(R.id.header);
            textView.setText(mMedList.get(0).getName());
            textView = (TextView) mViewSwitcher.findViewById(R.id.type);
            textView.setText(mMedList.get(0).isPrescribable() ? R.string.property_prescribable_yes :
                    R.string.property_prescribable_no);
            textView = (TextView) mViewSwitcher.findViewById(R.id.tty);
            textView.setText(Utilities.getTtyString(getContext(), mMedList.get(0).getTty()));

            showViewSwitcher(true);
            mCheckout.setVisibility(View.VISIBLE);
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
    public void onSearchFailed(List<Medicine> suggestedMed) {
        mTask = null;
        mSearchAnimation.hide();
        if (suggestedMed.size() > 0) {
            mMedList = suggestedMed;
            resetCheckoutButton();
            showViewSwitcher(false);
            TextView suggestionText = (TextView) mViewSwitcher.findViewById(R.id.suggestionHeader);
            suggestionText.setText(String.format(getString(R.string.suggestions_results),
                    mSearchView.getQuery()));
            List<String> list = new ArrayList<>();
            for (Medicine med : suggestedMed) {
                list.add(med.getName());
            }
            StringListAdapter adapter = new StringListAdapter(getContext(), list, false, "");
            ListView listView = (ListView) mViewSwitcher.findViewById(R.id.suggestionList);
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
        mSearchAnimation.hide();
        Toast.makeText(getContext(), getString(R.string.toast_no_internet),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String medName = mMedList.get(position).getName();
        mSearchView.setSearchText(medName);
        startSearch(medName);
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

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getContext(), MedDetailActivity.class);
        intent.putExtra(MedDetailActivity.TAG_NAME, mMedList.get(0).getName());
        intent.putExtra(MedDetailActivity.TAG_ID, mMedList.get(0).getRxnormId());
        intent.putExtra(MedDetailActivity.TAG_ORIGIN, MedDetailActivity.FROM_SEARCH);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
