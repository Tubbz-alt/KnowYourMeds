package com.tompee.utilities.knowyourmeds.feature.detail

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.jakewharton.rxbinding2.view.RxView
import com.tompee.utilities.knowyourmeds.KnowYourMedsApplication
import com.tompee.utilities.knowyourmeds.R
import com.tompee.utilities.knowyourmeds.base.BaseActivity
import com.tompee.utilities.knowyourmeds.core.asset.AssetManager
import com.tompee.utilities.knowyourmeds.di.component.DaggerDetailComponent
import com.tompee.utilities.knowyourmeds.di.component.DetailComponent
import com.tompee.utilities.knowyourmeds.di.module.DetailModule
import com.tompee.utilities.knowyourmeds.di.module.DetailPresenterModule
import com.tompee.utilities.knowyourmeds.feature.detail.menu.MenuDialog
import com.tompee.utilities.knowyourmeds.feature.detail.menu.MenuDialogListener
import com.tompee.utilities.knowyourmeds.feature.detail.progress.ProgressDialog
import com.tompee.utilities.knowyourmeds.model.Type
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_med_detail.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class DetailActivity : BaseActivity(), DetailView, MenuDialogListener {

    @Inject
    lateinit var assetManager: AssetManager

    @Inject
    lateinit var detailPresenter: DetailPresenter

    lateinit var detailComponent: DetailComponent
    private lateinit var progressDialog: ProgressDialog
    private val fragmentIndexSubject = PublishSubject.create<Type>()

    companion object {
        operator fun get(activity: Activity): DetailActivity {
            return activity as DetailActivity
        }
    }

    //region DetailActivity

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        background.setImageDrawable(assetManager.getDrawableFromAsset("search_bg.jpg"))
        setToolbar(toolbar, true)
        progressDialog = ProgressDialog(this, getString(R.string.fetch_details))

        RxView.clicks(menu).subscribe {
            val dialog = MenuDialog(this, detailComponent, this)
            dialog.show()
        }
        detailPresenter.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        detailPresenter.detachView()
    }
    //endregion

    //region BaseActivity
    override fun setupComponent() {
        detailComponent = DaggerDetailComponent.builder()
                .appComponent(KnowYourMedsApplication[this].component)
                .detailPresenterModule(DetailPresenterModule())
                .detailModule(DetailModule())
                .build()
        detailComponent.inject(this)
    }

    override fun layoutId(): Int = R.layout.activity_med_detail
    //endregion

    //region MenuDialogListener

    override fun onMenuClicked(type: Type) {
        fragmentIndexSubject.onNext(type)
    }
    //endregion

    //region DetailView
    override fun setTitle(title: String) {
        toolbar_text.text = title
    }

    override fun fragmentType(): Observable<Type> = fragmentIndexSubject

    override fun showProcessingDialog() {
        progressDialog.show()
    }

    override fun hideProcessingDialog() {
        progressDialog.hide()
    }

    override fun setActiveFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
        supportFragmentManager.executePendingTransactions()
    }

    //endregion
}