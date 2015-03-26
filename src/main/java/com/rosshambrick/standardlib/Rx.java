package com.rosshambrick.standardlib;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.annotations.Experimental;
import rx.schedulers.Schedulers;

/**
 * WIP
 *
 * A composable version of RxJava helpers to avoid the need to subclass from a
 * base RxFragment etc.
 */
@Experimental
public final class Rx {
    public static <T> Observable.Transformer<T, T> bind(final Activity activity) {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
                //TODO: add protection against invalid Activity state
            }
        };
    }

    /***
     * Use this to make an Observable run in a background threadpool and
     * handle the result on the UI thread.  This will complete the Observable
     * when the Fragment is being destroyed
     *
     * @param fragment
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<T, T> bind(final Fragment fragment) {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
                //TODO: add protection against invalid Fragment state
            }
        };
    }

    public static void init(Application application) {
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }
}
