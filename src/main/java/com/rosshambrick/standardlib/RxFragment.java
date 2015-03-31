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
import rx.functions.Action0;

@SuppressWarnings("UnusedDeclaration")
public abstract class RxFragment extends DialogFragment {

    private BlockingProgressFragment blockingProgressFragment;
    private Toolbar toolbar;
    private static boolean LOGGING;

// Disabling because this does not work with RxEspresso
//    private final BehaviorSubject<LifecycleEvent> lifecycleSubject = BehaviorSubject.create();

//    private Observable<LifecycleEvent> lifecycle() {
//        return lifecycleSubject.asObservable();
//    }

    abstract protected boolean isDebug();

    public static void enableTraceLog(boolean enable) {
        LOGGING = enable;
    }

    @Override
    public void onAttach(android.app.Activity activity) {
        if (LOGGING) {
            Log.d("TRACE", "--> RxFragment.onAttach()");
        }
        super.onAttach(activity);
//        lifecycleSubject.onNext(LifecycleEvent.ATTACH);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (LOGGING) {
            Log.d("TRACE", "--> RxFragment.onCreate()");
        }
        super.onCreate(savedInstanceState);
//        lifecycleSubject.onNext(LifecycleEvent.CREATE);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (LOGGING) {
            Log.d("TRACE", "--> RxFragment.onActivityCreated()");
        }
        super.onActivityCreated(savedInstanceState);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (LOGGING) {
            Log.d("TRACE", "--> RxFragment.onViewCreated()");
        }
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
//        lifecycleSubject.onNext(LifecycleEvent.CREATE_VIEW);
    }

    @Override
    public void onStart() {
        if (LOGGING) {
            Log.d("TRACE", "--> RxFragment.onStart()");
        }
        super.onStart();
//        lifecycleSubject.onNext(LifecycleEvent.START);
    }

    @Override
    public void onResume() {
        if (LOGGING) {
            Log.d("TRACE", "--> RxFragment.onResume()");
        }
        super.onResume();
//        lifecycleSubject.onNext(LifecycleEvent.RESUME);
    }

    @Override
    public void onPause() {
        if (LOGGING) {
            Log.d("TRACE", "--> RxFragment.onPause()");
        }
//        lifecycleSubject.onNext(LifecycleEvent.PAUSE);
        super.onPause();
    }

    @Override
    public void onStop() {
        if (LOGGING) {
            Log.d("TRACE", "--> RxFragment.onStop()");
        }
//        lifecycleSubject.onNext(LifecycleEvent.STOP);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        if (LOGGING) {
            Log.d("TRACE", "--> RxFragment.onDestroyView()");
        }
//        lifecycleSubject.onNext(LifecycleEvent.DESTROY_VIEW);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        if (LOGGING) {
            Log.d("TRACE", "--> RxFragment.onDestroy()");
        }
//        lifecycleSubject.onNext(LifecycleEvent.DETACH);
        super.onDetach();
    }

    @Override
    public void onDetach() {
        if (LOGGING) {
            Log.d("TRACE", "--> RxFragment.onDetach()");
        }
//        lifecycleSubject.onNext(LifecycleEvent.DESTROY);
        super.onDestroy();
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
        Log.e(getClass().getSimpleName(), e.getLocalizedMessage(), e);
        if (isDebug() || message != null) {
            Toast.makeText(getActivity(), message == null ? e.getLocalizedMessage() : message, Toast.LENGTH_LONG).show();
        }
    }

    protected void dismissBlockingProgress() {
        if (blockingProgressFragment != null) {
            blockingProgressFragment.dismiss();
            blockingProgressFragment = null;
        }
    }

    public Toolbar getToolbar() {
        if (toolbar == null) {
            throw new RuntimeException("Toolbar has not been set.  Make sure not to call getToolbar() until onViewCreated() at the earliest.");
        }
        return toolbar;
    }

    protected <T> Subscription blockingSubscribe(Observable<T> observable, final Observer<T> observer) {
        Subscription subscription = bind(observable)
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        dismissBlockingProgress();
                    }
                })
                .subscribe(observer);
        showBlockingProgress(subscription);
        return subscription;
    }

    protected <T> Subscription subscribe(Observable<T> observable, Observer<T> observer) {
        Observable<T> boundObservable = bind(observable);
        return boundObservable.subscribe(observer);
    }

    protected <T> Observable<T> bind(Observable<T> observable) {
        return AppObservable.bindFragment(this, observable);
//        return LifecycleObservable.bindFragmentLifecycle(lifecycle(), boundObservable);
    }

    protected void toast(int messageId) {
        ToastUtil.show(getActivity(), messageId);
    }

    protected void toast(String message) {
        ToastUtil.show(getActivity(), message);
    }

    protected int getColor(int colorRes) {
        return getResources().getColor(colorRes);
    }

    protected int getDimensionPixelOffset(int dpResource) {
        return getResources().getDimensionPixelOffset(dpResource);
    }

    protected void debugToast(int messageResId) {
        if (isDebug()) {
            Toast.makeText(getActivity(), messageResId, Toast.LENGTH_SHORT).show();
        }
    }

    public void onCompleted() {
        //nothing by default
    }

    public void onError(Throwable e) {
        handleError(e);
    }

}
