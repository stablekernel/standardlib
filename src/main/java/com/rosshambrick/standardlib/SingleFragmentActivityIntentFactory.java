package com.rosshambrick.standardlib;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class SingleFragmentActivityIntentFactory {

    public Class<? extends SingleFragmentActivity> activityClass = SingleFragmentActivity.class;

    /**
     * This allows you to create an Intent that will start a new SingleFragmentActivity that
     * automatically instantiates the Fragment class specified and adds it to the fragment container
     *
     * @param context
     * @param fragmentClass
     * @return Intent
     */
    public Intent newIntent(Context context, Class<? extends Fragment> fragmentClass) {
        return newIntent(context, fragmentClass, null);
    }

    /**
     * This allows you to create an Intent that will start a new SingleFragmentActivity that
     * automatically instantiates the Fragment class specified and adds it to the fragment container.
     * <p>
     * This method is useful but somewhat misleading since your Fragment instance is only used to
     * get the Class and Fragment Arguments to pass along to the SingleFragmentActivity
     *
     * @param context
     * @param fragment
     * @return
     */
    @Deprecated
    public Intent newIntent(Context context, Fragment fragment) {
        return newIntent(context, fragment.getClass(), fragment.getArguments());
    }

    /**
     * This allows you to create an Intent that will start a new SingleFragmentActivity that
     * automatically instantiates the Fragment class specified, adds it to the fragment container,
     * and passes along the Fragment Arguments.
     *
     * @param context
     * @param fragmentName
     * @param fragmentArgs
     * @param <TFragment>
     * @return
     */
    public <TFragment extends Fragment> Intent newIntent(Context context, Class<TFragment> fragmentName, Bundle fragmentArgs) {
        Intent intent = new Intent(context, activityClass);
        Bundle extras;
        if (fragmentArgs != null) {
            extras = new Bundle(fragmentArgs);
        } else {
            extras = new Bundle();
        }
        extras.putString(SingleFragmentActivity.EXTRA_FRAGMENT_NAME, fragmentName.getCanonicalName());
        intent.putExtras(extras);
        return intent;
    }

}
