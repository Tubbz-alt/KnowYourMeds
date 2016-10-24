package com.tompee.utilities.knowyourmeds.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.view.MedDetailActivity;
import com.tompee.utilities.knowyourmeds.view.adapter.StringListAdapter;

import java.util.ArrayList;
import java.util.Collections;

public class TtyFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String KEY_LIST = "key_list";
    private static final String KEY_TAB_NAME = "key_tab_name";
    private ArrayList<String> mList;

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
        mList = getArguments().getStringArrayList(KEY_LIST);
        if (mList == null || mList.isEmpty()) {
            emptySource.setText(String.format(getString(R.string.tty_no_items),
                    getArguments().getString(KEY_TAB_NAME)));
            emptySource.setVisibility(View.VISIBLE);
        } else {
            Collections.sort(mList);
            ListView listView = (ListView) view.findViewById(R.id.list);
            listView.setAdapter(new StringListAdapter(getContext(), mList, ""));
            listView.setOnItemClickListener(this);
            emptySource.setVisibility(View.GONE);
            TextView countView = (TextView) view.findViewById(R.id.count);
            countView.setText(String.valueOf(mList.size()));
        }
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(String.format(getString(R.string.search_item_message), mList.get(position)));
        builder.setNegativeButton(R.string.control_cancel, null);
        builder.setPositiveButton(R.string.control_search, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getContext(), MedDetailActivity.class);
                intent.putExtra(MedDetailActivity.TAG_NAME, mList.get(position));
                intent.putExtra(MedDetailActivity.TAG_ID, "");
                intent.putExtra(MedDetailActivity.TAG_ORIGIN, MedDetailActivity.FROM_SEARCH);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        builder.create().show();
    }
}
