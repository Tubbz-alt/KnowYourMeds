package com.tompee.utilities.knowyourmeds.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.InterstitialAd;
import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.controller.database.DatabaseHelper;
import com.tompee.utilities.knowyourmeds.controller.task.SearchTask;
import com.tompee.utilities.knowyourmeds.model.Medicine;
import com.tompee.utilities.knowyourmeds.view.MainActivity;
import com.tompee.utilities.knowyourmeds.view.MedDetailActivity;
import com.tompee.utilities.knowyourmeds.view.adapter.MainListAdapter;
import com.tompee.utilities.knowyourmeds.view.adapter.StringListAdapter;
import com.tompee.utilities.knowyourmeds.view.dialog.ProcessingDialog;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements TextWatcher, View.OnFocusChangeListener,
        View.OnClickListener, TextView.OnEditorActionListener, SearchTask.SearchListener,
        AdapterView.OnItemClickListener {
    private EditText mEditText;
    private TextView mResultText;
    private View mEditIcon;
    private View mClearIcon;
    private View mResultBar;
    private View mNoResultsView;
    private ListView mListView;
    private List<Medicine> mMedList;
    private SharedPreferences mSharedPreferences;
    private View mListHeader;

    private SearchTask mTask;
    private ProcessingDialog mDialog;

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
        mEditText = (EditText) view.findViewById(R.id.edit_text_search);
        mEditIcon = view.findViewById(R.id.icon_write);
        mClearIcon = view.findViewById(R.id.icon_delete);
        mClearIcon.setOnClickListener(this);
        mEditText.addTextChangedListener(this);
        mEditText.setOnFocusChangeListener(this);
        mEditText.setOnEditorActionListener(this);
        mListView = (ListView) view.findViewById(R.id.list_view_search);
        mListView.setOnItemClickListener(this);
        mResultBar = view.findViewById(R.id.result_bar);
        mResultText = (TextView) view.findViewById(R.id.text_result);
        mNoResultsView = view.findViewById(R.id.text_no_items);
        mListHeader = view.findViewById(R.id.list_header);
        return view;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        updateClearIcon();
    }

    private void updateClearIcon() {
        if (mEditText.getText().length() > 0 && mEditText.hasFocus()) {
            mClearIcon.setVisibility(View.VISIBLE);
        } else {
            mClearIcon.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            mEditIcon.setVisibility(View.GONE);
        } else {
            mEditIcon.setVisibility(View.VISIBLE);
        }
        updateClearIcon();
    }

    @Override
    public void onClick(View v) {
        mEditText.setText("");
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            mEditText.clearFocus();
            if (mSharedPreferences.getBoolean(SettingsFragment.TAG_OFFLINE_MODE_CB, false)) {
                searchFromDatabase();
            } else {
                startSearch();
            }
            return true;
        }
        return false;
    }

    private void searchFromDatabase() {
        mResultBar.setVisibility(View.VISIBLE);
        mResultText.setText(R.string.search_results);
        DatabaseHelper db = DatabaseHelper.getInstance(getContext());
        List<Medicine> medList = db.getAllShortEntriesByName(mEditText.getText().toString());
        if (medList.isEmpty()) {
            mListView.setVisibility(View.GONE);
            mNoResultsView.setVisibility(View.VISIBLE);
            return;
        }
        mListView.setVisibility(View.VISIBLE);
        mNoResultsView.setVisibility(View.GONE);
        boolean isFullLayoutSupported = ((MainActivity) getActivity()).isFullLayoutSupported();
        MainListAdapter adapter = new MainListAdapter(getContext(), medList, isFullLayoutSupported,
                true, false);
        mListView.setAdapter(adapter);
        mMedList = medList;
    }

    private void startSearch() {
        if (mTask == null) {
            if (mDialog == null) {
                mDialog = new ProcessingDialog(getContext(),
                        getString(R.string.process_search));
                mDialog.show();
            }
            mTask = new SearchTask(getContext(), this);
            mTask.execute(mEditText.getText().toString());
        }
    }

    @Override
    public void onSearchSuccess(List<Medicine> medList) {
        mTask = null;
        if (medList.size() > 0) {
            mMedList = medList;
            mResultText.setText(R.string.search_results);
            mResultBar.setVisibility(View.VISIBLE);
            boolean isFullLayoutSupported = ((MainActivity) getActivity()).isFullLayoutSupported();
            MainListAdapter adapter = new MainListAdapter(getContext(), medList, isFullLayoutSupported,
                    true, false);
            mListView.setAdapter(adapter);
            if (mListHeader != null) {
                mListHeader.setVisibility(View.VISIBLE);
            }
        }
        mDialog.dismiss();
        mDialog = null;
    }

    @Override
    public void onSearchFailed(List<Medicine> suggestedMed) {
        mTask = null;
        mResultBar.setVisibility(View.VISIBLE);
        if (suggestedMed.size() > 0) {
            mMedList = suggestedMed;
            mResultText.setText(String.format(getString(R.string.suggestions_results),
                    mEditText.getText().toString()));
            mResultBar.setVisibility(View.VISIBLE);
            List<String> list = new ArrayList<>();
            for (Medicine med : suggestedMed) {
                list.add(med.getName());
            }
            StringListAdapter adapter = new StringListAdapter(getContext(), list, 0);
            mListView.setAdapter(adapter);
            mListView.setVisibility(View.VISIBLE);
            mNoResultsView.setVisibility(View.GONE);
        } else {
            mListView.setVisibility(View.GONE);
            mNoResultsView.setVisibility(View.VISIBLE);
        }
        if (mListHeader != null) {
            mListHeader.setVisibility(View.GONE);
        }
        mDialog.dismiss();
        mDialog = null;
    }

    @Override
    public void onConnectionError() {
        mTask = null;
        mDialog.dismiss();
        mDialog = null;
        Toast.makeText(getContext(), getString(R.string.toast_no_internet),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Medicine med = mMedList.get(position);
        if (med.getRxnormId() == null) {
            mEditText.setText(med.getName());
            startSearch();
        } else {
            Intent intent = new Intent(getContext(), MedDetailActivity.class);
            intent.putExtra(MedDetailActivity.TAG_NAME, med.getName());
            intent.putExtra(MedDetailActivity.TAG_ID, med.getRxnormId());
            intent.putExtra(MedDetailActivity.TAG_ORIGIN, MedDetailActivity.FROM_SEARCH);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            InterstitialAd ad = ((MainActivity) getActivity()).getInterstitialAd();
            if (ad.isLoaded()) {
                ad.show();
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser && mResultBar != null) {
            mResultBar.setVisibility(View.GONE);
        }
    }
}
