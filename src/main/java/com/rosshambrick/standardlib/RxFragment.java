package com.rosshambrick.standardlib;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.app.AppObservable;
import rx.android.lifecycle.LifecycleEvent;
import rx.android.lifecycle.LifecycleObservable;
import rx.functions.Action0;
import rx.subjects.BehaviorSubject;

public abstract class RxFragment extends DialogFragment {

    private BlockingProgressFragment blockingProgressFragment;
    private Toolbar toolbar;

    private final BehaviorSubject<LifecycleEvent> lifecycleSubject = BehaviorSubject.create();

    private Observable<LifecycleEvent> lifecycle() {
        return lifecycleSubject.asObservable();
    }

    abstract protected boolean isDebug();

    @Override
    public void onAttach(android.app.Activity activity) {
        super.onAttach(activity);
        lifecycleSubject.onNext(LifecycleEvent.ATTACH);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        lifecycleSubject.onNext(LifecycleEvent.CREATE);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
        lifecycleSubject.onNext(LifecycleEvent.CREATE_VIEW);
    }

    @Override
    public void onStart() {
        super.onStart();
        lifecycleSubject.onNext(LifecycleEvent.START);
    }

    @Override
    public void onResume() {
        super.onResume();
        lifecycleSubject.onNext(LifecycleEvent.RESUME);
    }

    @Override
    public void onPause() {
        lifecycleSubject.onNext(LifecycleEvent.PAUSE);
        super.onPause();
    }

    @Override
    public void onStop() {
        lifecycleSubject.onNext(LifecycleEvent.STOP);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        lifecycleSubject.onNext(LifecycleEvent.DESTROY_VIEW);
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        lifecycleSubject.onNext(LifecycleEvent.DETACH);
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lifecycleSubject.onNext(LifecycleEvent.DESTROY);
    }

    protected void showBlockingProgress() {
        showBlockingProgress(null);
    }

    protected void showBlockingProgress(Subscription subscription) {
        blockingProgressFragment = BlockingProgressFragment.newInstance();
        blockingProgressFragment.show(getFragmentManager(), BlockingProgressFragment.TAG);
        blockingProgressFragment.setOnCancelListener(subscription);
    }

    public void handleError(Throwable e) {
        handleError(null, e);
    }

    protected void handleError(String message, Throwable e) {
        ErrorHandler.handleError(getActivity(), message, e);
    }

    protected void dismissBlockingProgress() {
        if (blockingProgressFragment != null) {
            blockingProgressFragment.dismiss();
            blockingProgressFragment = null;
        }
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    protected <T> Subscription blockingSubscribe(Observable<T> observable, Observer<T> observer) {
        Subscription subscription = observe(observable)
                .finallyDo(new Action0() {
                    @Override
                    public void call() {
                        RxFragment.this.dismissBlockingProgress();
                    }
                })
                .subscribe(observer);
        showBlockingProgress(subscription);
        return subscription;
    }

    protected <T> Subscription subscribe(Observable<T> observable, Observer<T> observer) {
        Observable<T> boundObservable = observe(observable);
        return boundObservable.subscribe(observer);
    }

    protected <T> Observable<T> observe(Observable<T> observable) {
        Observable<T> boundObservable = AppObservable.bindFragment(this, observable);
        return LifecycleObservable.bindFragmentLifecycle(lifecycle(), boundObservable);
    }

    protected void toast(int messageId) {
        Toast.makeText(getActivity(), messageId, Toast.LENGTH_SHORT).show();
    }

    protected void toast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    protected int getColor(int colorRes) {
        return getResources().getColor(colorRes);
    }

    protected int getDimensionPixelOffset(int dpResource) {
        return getResources().getDimensionPixelOffset(dpResource);
    }

    protected void debugToast(int messageResId) {
        if (isDebug()) {
            Toast.makeText(getActivity(), messageResId, Toast.LENGTH_LONG).show();
        }
    }

    public void onCompleted() {
        //nothing by default
    }

    public void onError(Throwable e) {
        handleError(e);
    }

}
