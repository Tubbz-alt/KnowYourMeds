package com.tompee.utilities.knowyourmeds.view.fragment;

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

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.controller.task.SearchTask;
import com.tompee.utilities.knowyourmeds.model.Medicine;
import com.tompee.utilities.knowyourmeds.view.adapter.SearchResultAdapter;
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
    private ListView mListView;
    private List<Medicine> mMedList;

    private SearchTask mTask;
    private ProcessingDialog mDialog;

    public static SearchFragment newInstance() {
        Bundle args = new Bundle();
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
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
            startSearch();
            return true;
        }
        return false;
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
            SearchResultAdapter adapter = new SearchResultAdapter(getContext(),
                    R.layout.list_search_result, medList, true);
            mListView.setAdapter(adapter);
        }
        mDialog.dismiss();
        mDialog = null;
    }

    @Override
    public void onSearchFailed(List<Medicine> suggestedMed) {
        mTask = null;
        if (suggestedMed.size() > 0) {
            mMedList = suggestedMed;
            mResultText.setText(String.format(getString(R.string.suggestions_results),
                    mEditText.getText().toString()));
            mResultBar.setVisibility(View.VISIBLE);
            SearchResultAdapter adapter = new SearchResultAdapter(getContext(),
                    R.layout.list_search_result, suggestedMed, false);
            mListView.setAdapter(adapter);
        }
        mDialog.dismiss();
        mDialog = null;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (mMedList.get(position).getRxnormId() == null) {
            mEditText.setText(mMedList.get(position).getName());
            startSearch();
        }
    }
}
