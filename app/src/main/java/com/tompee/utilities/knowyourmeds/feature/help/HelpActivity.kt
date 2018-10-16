package com.tompee.utilities.knowyourmeds.feature.help

import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Html
import android.text.method.LinkMovementMethod
import com.tompee.utilities.knowyourmeds.BuildConfig
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.base.BaseActivity
import com.tompee.utilities.knowyourmeds.core.asset.AssetManager
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.activity_license.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

@Suppress("DEPRECATION")
class HelpActivity : BaseActivity() {
    companion object {
        const val TAG_MODE = "mode"
        const val ABOUT = 0
        const val LICENSE = 1
        const val PRIVACY = 2
    }

    @Inject
    lateinit var assetManager: AssetManager

    //region HelpActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        val mode = intent.getIntExtra(TAG_MODE, ABOUT)
        setContentView(if (mode == ABOUT) R.layout.activity_about else R.layout.activity_license)
        setToolbar(toolbar, true)
        toolbar_bg.setBackgroundColor(ContextCompat.getColor(this, R.color.launcher_color))
        when (mode) {
            ABOUT -> {
                toolbar_text.setText(R.string.title_about)
                version.text = String.format(getString(R.string.version), BuildConfig.VERSION_NAME)
            }
            LICENSE -> {
                toolbar_text.setText(R.string.title_license)
                header.setText(R.string.title_license)
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    content.text = Html.fromHtml(assetManager.getStringFromAsset("opensource.html"),
                            Html.FROM_HTML_MODE_LEGACY)
                } else {
                    content.text = Html.fromHtml(assetManager.getStringFromAsset("opensource.html"))
                }
                content.movementMethod = LinkMovementMethod.getInstance()
            }
            else -> {
                toolbar_text.setText(R.string.title_policy)
                header.setText(R.string.title_policy)
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    content.text = Html.fromHtml(assetManager.getStringFromAsset("policy.html"),
                            Html.FROM_HTML_MODE_LEGACY)
                } else {
                    content.text = Html.fromHtml(assetManager.getStringFromAsset("policy.html"))
                }
                content.movementMethod = LinkMovementMethod.getInstance()
            }
        }
    }
    //endregion
}