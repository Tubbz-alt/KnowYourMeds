package com.tompee.utilities.knowyourmeds.view;

import android.os.Bundle;
import android.widget.TextView;

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.view.base.BaseActivity;

public class SearchResultActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        setToolbar(R.id.toolbar, true);
        TextView title = (TextView) findViewById(R.id.toolbar_text);
        title.setText(R.string.app_name);
    }
}
