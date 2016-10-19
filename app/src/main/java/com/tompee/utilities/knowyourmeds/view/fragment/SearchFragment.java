package com.tompee.utilities.knowyourmeds.view.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.controller.Utilities;
import com.tompee.utilities.knowyourmeds.controller.networkinterface.RxNavWrapper;
import com.tompee.utilities.knowyourmeds.controller.task.SearchTask;
import com.tompee.utilities.knowyourmeds.model.Medicine;
import com.tompee.utilities.knowyourmeds.view.MainActivity;
import com.tompee.utilities.knowyourmeds.view.adapter.MainListAdapter;
import com.tompee.utilities.knowyourmeds.view.dialog.ProcessingDialog;

import java.util.List;

public class SearchFragment extends Fragment implements FloatingSearchView.OnSearchListener,
        SearchTask.SearchListener, AdapterView.OnItemClickListener {
    private View mResultBar;

    private List<Medicine> mMedList;

    private View mNoResultsView;
    private ListView mListView;
    private SharedPreferences mSharedPreferences;
    private View mListHeader;


    private SearchTask mTask;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = getContext().getSharedPreferences(MainActivity.SHARED_PREF,
                Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        FloatingSearchView searchView = (FloatingSearchView) view.findViewById(R.id.floating_search_view);
        searchView.setOnSearchListener(this);
        mResultBar = view.findViewById(R.id.result_bar);

//        mListView = (ListView) view.findViewById(R.id.list_view_search);
//        mListView.setOnItemClickListener(this);
//        mNoResultsView = view.findViewById(R.id.text_no_items);
//        mListHeader = view.findViewById(R.id.list_header);
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
            TextView textView = (TextView) mResultBar.findViewById(R.id.header);
            textView.setText(mMedList.get(0).getName());
            mResultBar.setVisibility(View.VISIBLE);
            textView = (TextView) mResultBar.findViewById(R.id.rx_type);
            textView.setText(mMedList.get(0).isPrescribable() ? R.string.property_prescribable_yes :
                    R.string.property_prescribable_no);
            textView = (TextView) mResultBar.findViewById(R.id.tty);
            textView.setText(Utilities.getTtyString(getContext(), mMedList.get(0).getTty()));
        }
    }

    @Override
    public void onSearchFailed(List<Medicine> suggestedMed) {
        mTask = null;
        mResultBar.setVisibility(View.VISIBLE);
//        if (suggestedMed.size() > 0) {
//            mMedList = suggestedMed;
//            mResultText.setText(String.format(getString(R.string.suggestions_results),
//                    mEditText.getText().toString()));
//            mResultBar.setVisibility(View.VISIBLE);
//            List<String> list = new ArrayList<>();
//            for (Medicine med : suggestedMed) {
//                list.add(med.getName());
//            }
//            StringListAdapter adapter = new StringListAdapter(getContext(), list, 0);
//            mListView.setAdapter(adapter);
//            mListView.setVisibility(View.VISIBLE);
//            mNoResultsView.setVisibility(View.GONE);
//        } else {
//            mListView.setVisibility(View.GONE);
//            mNoResultsView.setVisibility(View.VISIBLE);
//        }
//        if (mListHeader != null) {
//            mListHeader.setVisibility(View.GONE);
//        }
    }

    @Override
    public void onConnectionError() {
        mTask = null;
        Toast.makeText(getContext(), getString(R.string.toast_no_internet),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//        Medicine med = mMedList.get(position);
//        if (med.getRxnormId() == null) {
//            mEditText.setText(med.getName());
//            startSearch();
//        } else {
//            Intent intent = new Intent(getContext(), MedDetailActivity.class);
//            intent.putExtra(MedDetailActivity.TAG_NAME, med.getName());
//            intent.putExtra(MedDetailActivity.TAG_ID, med.getRxnormId());
//            intent.putExtra(MedDetailActivity.TAG_ORIGIN, MedDetailActivity.FROM_SEARCH);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//            InterstitialAd ad = ((MainActivity) getActivity()).getInterstitialAd();
//            if (ad.isLoaded()) {
//                ad.show();
//            }
//        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser && mResultBar != null) {
            mResultBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
    }

    @Override
    public void onSearchAction(String currentQuery) {
        startSearch(currentQuery);
    }
}
