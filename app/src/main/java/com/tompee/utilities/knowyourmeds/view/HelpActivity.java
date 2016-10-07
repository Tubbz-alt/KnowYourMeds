package com.tompee.utilities.knowyourmeds.view;

import android.os.Bundle;
import android.widget.TextView;

import com.tompee.utilities.knowyourmeds.BuildConfig;
import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.view.base.BaseActivity;

public class HelpActivity extends BaseActivity {
    public static final String TAG_MODE = "mode";
    public static int ABOUT = 0;
    public static int LICENSE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        int mode = getIntent().getIntExtra(TAG_MODE, ABOUT);
        if(mode == ABOUT) {
            setContentView(R.layout.activity_about);
            setToolbar(R.id.toolbar, true);
            TextView title = (TextView) findViewById(R.id.toolbar_text);
            title.setText(R.string.title_about);

            TextView version = (TextView) findViewById(R.id.version);
            version.setText(String.format(getString(R.string.version), BuildConfig.VERSION_NAME));
        } else if (mode == LICENSE) {
            setContentView(R.layout.activity_license);
            setToolbar(R.id.toolbar, true);
            TextView title = (TextView) findViewById(R.id.toolbar_text);
            title.setText(R.string.title_license);
        }
    }
}
