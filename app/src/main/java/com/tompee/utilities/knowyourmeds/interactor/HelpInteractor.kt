package com.tompee.utilities.knowyourmeds.interactor

import android.os.Build
import android.text.Html
import android.text.Spanned
import com.tompee.utilities.knowyourmeds.base.BaseInteractor
import com.tompee.utilities.knowyourmeds.core.asset.AssetManager
import io.reactivex.Single

class HelpInteractor(private val assetManager: AssetManager) : BaseInteractor {

    fun getStringFromAsset(assetName: String): Single<Spanned> {
        return Single.fromCallable {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                Html.fromHtml(assetManager.getStringFromAsset(assetName),
                        Html.FROM_HTML_MODE_LEGACY)
            } else {
                @Suppress("DEPRECATION")
                Html.fromHtml(assetManager.getStringFromAsset(assetName))
            }
        }
    }
}