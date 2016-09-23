package com.tompee.utilities.knowyourmeds.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.view.adapter.StringListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TtyFragment extends Fragment {
    private static final String KEY_LIST = "key_list";
    private static final String KEY_TAB_NAME = "key_tab_name";

    public static TtyFragment newInstance(ArrayList<String> infoList, String tabName) {
        TtyFragment fragment = new TtyFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(KEY_LIST, infoList);
        bundle.putString(KEY_TAB_NAME, tabName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tty, container, false);
        TextView tabName = (TextView) view.findViewById(R.id.tab_name);
        tabName.setText(getArguments().getString(KEY_TAB_NAME));
        TextView emptySource = (TextView) view.findViewById(R.id.tv_no_items);
        List<String> infoList = getArguments().getStringArrayList(KEY_LIST);
        if (infoList == null || infoList.isEmpty()) {
            emptySource.setText(String.format(getString(R.string.tty_no_items),
                    getArguments().getString(KEY_TAB_NAME)));
            emptySource.setVisibility(View.VISIBLE);
        } else {
            Collections.sort(infoList);
            ListView listView = (ListView) view.findViewById(R.id.list);
            listView.setAdapter(new StringListAdapter(getContext(), infoList));
            emptySource.setVisibility(View.GONE);
            TextView countView = (TextView) view.findViewById(R.id.count);
            countView.setText(String.valueOf(infoList.size()));
        }
        return view;
    }
}
