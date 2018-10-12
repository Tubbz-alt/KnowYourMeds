package com.tompee.utilities.knowyourmeds.base

import com.tompee.utilities.knowyourmeds.model.SchedulerPool
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BasePresenter<V : BaseView, I : BaseInteractor>(protected val interactor: I,
                                                               protected val scheduler: SchedulerPool) {
    private val compositeDisposable = CompositeDisposable()
    protected lateinit var view: V

    fun attachView(mvpView: V) {
        view = mvpView
        onAttachView()
    }

    fun detachView() {
        compositeDisposable.clear()
        onDetachView()
    }

    protected fun addSubscription(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    abstract fun onDetachView()

    abstract fun onAttachView()
}