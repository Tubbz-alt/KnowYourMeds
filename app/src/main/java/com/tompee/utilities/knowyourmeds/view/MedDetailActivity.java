package com.tompee.utilities.knowyourmeds.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.tompee.utilities.knowyourmeds.R;
import com.tompee.utilities.knowyourmeds.view.base.BaseActivity;
import com.tompee.utilities.knowyourmeds.view.dialog.ProcessingDialog;

public class MedDetailActivity extends BaseActivity {
    public static final String TAG_NAME = "name";
    public static final String TAG_ID = "id";

    private ProcessingDialog mDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_detail);
        setToolbar(R.id.toolbar, true);

        Intent intent = getIntent();
        TextView title = (TextView) findViewById(R.id.toolbar_text);
        title.setText(intent.getStringExtra(TAG_NAME));

        mDialog = new ProcessingDialog(this, getString(R.string.fetch_details));
        mDialog.show();
    }

}
