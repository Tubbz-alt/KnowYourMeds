package com.tompee.utilities.knowyourmeds.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.controller.task.SearchTask;
import com.tompee.utilities.knowyourmeds.model.Medicine;
import com.tompee.utilities.knowyourmeds.view.dialog.ProcessingDialog;

public class SearchFragment extends Fragment implements TextWatcher, View.OnFocusChangeListener,
        View.OnClickListener, TextView.OnEditorActionListener, SearchTask.SearchListener {
    private EditText mEditText;
    private View mEditIcon;
    private View mClearIcon;

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
            if (mTask == null) {
                if (mDialog == null) {
                    mDialog = new ProcessingDialog(getContext(),
                            getString(R.string.process_search));
                    mDialog.show();
                }
                mTask = new SearchTask(getContext(), this);
                mTask.execute(mEditText.getText().toString());
            }
            return true;
        }
        return false;
    }

    @Override
    public void onSearchSuccess(Medicine medicine) {
        mTask = null;
        mDialog.dismiss();
        mDialog = null;
    }
}
