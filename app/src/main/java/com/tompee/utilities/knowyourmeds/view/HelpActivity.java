package com.tompee.utilities.knowyourmeds.view;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.tompee.utilities.knowyourmeds.BuildConfig;
import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.controller.Utilities;
import com.tompee.utilities.knowyourmeds.view.base.BaseActivity;

public class HelpActivity extends BaseActivity {
    public static final String TAG_MODE = "mode";
    public static final int ABOUT = 0;
    public static final int LICENSE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        int mode = getIntent().getIntExtra(TAG_MODE, ABOUT);
        if (mode == ABOUT) {
            setContentView(R.layout.activity_about);
            setToolbar(R.id.toolbar, true);
            TextView title = (TextView) findViewById(R.id.toolbar_text);
            title.setText(R.string.title_about);

            ImageView toolbar = (ImageView) findViewById(R.id.toolbar_bg);
            toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.launcher_color));

            TextView version = (TextView) findViewById(R.id.version);
            version.setText(String.format(getString(R.string.version), BuildConfig.VERSION_NAME));
        } else if (mode == LICENSE) {
            setContentView(R.layout.activity_license);
            setToolbar(R.id.toolbar, true);

            ImageView toolbar = (ImageView) findViewById(R.id.toolbar_bg);
            toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.launcher_color));

            TextView title = (TextView) findViewById(R.id.toolbar_text);
            title.setText(R.string.title_license);

            TextView content = (TextView) findViewById(R.id.content);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                content.setText(Html.fromHtml(Utilities.getStringFromAsset(this, "opensource.html"),
                        Html.FROM_HTML_MODE_LEGACY));
            } else {
                content.setText(Html.fromHtml(Utilities.getStringFromAsset(this, "opensource.html")));
            }
            content.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
}
