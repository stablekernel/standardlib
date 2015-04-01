package com.rosshambrick.standardlib;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import rx.Observable;
import rx.Subscriber;
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
                        .observeOn(AndroidSchedulers.mainThread())
                        .lift(new SafeFragmentOperator<T>(fragment));
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

    private static class SafeFragmentOperator<T> implements Observable.Operator<T, T> {
        private static final String TAG = "SafeFragmentOperator";
        private Fragment fragment;

        public SafeFragmentOperator(Fragment fragment) {
            this.fragment = fragment;
        }

        @Override
        public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {

            return new Subscriber<T>() {
                @Override
                public void onCompleted() {
                    if (fragment != null && fragment.isAdded() && !fragment.getActivity().isFinishing()) {
                        subscriber.onCompleted();
                    } else {
                        Log.d(TAG, "Did not call onCompleted().  Fragment is in an invalid state");
                        unsubscribe();
                        fragment = null;
                    }
                }

                @Override
                public void onError(Throwable e) {
                    if (fragment != null && fragment.isAdded() && !fragment.getActivity().isFinishing()) {
                        subscriber.onError(e);
                    } else {
                        unsubscribe();
                        fragment = null;
                    }
                }

                @Override
                public void onNext(T t) {
                    if (fragment != null && fragment.isAdded() && !fragment.getActivity().isFinishing()) {
                        subscriber.onNext(t);
                    } else {
                        unsubscribe();
                        fragment = null;
                    }
                }
            };
        }
    }
}
