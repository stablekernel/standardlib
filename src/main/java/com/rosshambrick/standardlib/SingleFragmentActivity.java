package com.rosshambrick.standardlib;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class SingleFragmentActivity extends ActionBarActivity {
    private static final String EXTRA_FRAGMENT_NAME = "EXTRA_FRAGMENT_NAME";
    private Toolbar toolbar;
    private boolean traceLog;

    /**
     * This allows you to create an Intent that will start a new SingleFragmentActivity that
     * automatically instantiates the Fragment class specified and adds it to the fragment container
     *
     * @param context
     * @param fragmentClass
     * @return Intent
     */
    public static Intent newIntent(Context context, Class<? extends Fragment> fragmentClass) {
        return newIntent(context, fragmentClass, null);
    }

    /**
     * This allows you to create an Intent that will start a new SingleFragmentActivity that
     * automatically instantiates the Fragment class specified and adds it to the fragment container.
     *
     * This method is useful but somewhat misleading since your Fragment instance is only used to
     * get the Class and Fragment Arguments to pass along to the SingleFragmentActivity
     *
     * @param context
     * @param fragment
     * @return
     */
    @Deprecated
    public static Intent newIntent(Context context, Fragment fragment) {
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
    public static <TFragment extends Fragment> Intent newIntent(Context context, Class<TFragment> fragmentName, Bundle fragmentArgs) {
        Intent intent = new Intent(context, SingleFragmentActivity.class);
        Bundle extras;
        if (fragmentArgs != null) {
            extras = new Bundle(fragmentArgs);
        } else {
            extras = new Bundle();
        }
        extras.putString(EXTRA_FRAGMENT_NAME, fragmentName.getCanonicalName());
        intent.putExtras(extras);
        return intent;
    }

    protected void enableTraceLog(boolean enable) {
        traceLog = enable;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (traceLog) Log.d("TRACE", "--> SingleFragmentActivity.onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, getFragment())
                    .commit();
        }
        if (traceLog) Log.d("TRACE", "<-- SingleFragmentActivity.onCreate()");
    }

    protected Fragment getFragment() {
        String fragmentName = getIntent().getStringExtra(EXTRA_FRAGMENT_NAME);
        if (fragmentName == null) {
            throw new RuntimeException("You must either provide a fragment name or override getFragment() in a subclass to provide a fragment instance");
        }

        return Fragment.instantiate(this, fragmentName, getIntent().getExtras());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    protected void handleError(Throwable e) {
        handleError(null, e);
    }

    protected void handleError(String message, Throwable e) {
        ErrorHandler.handleError(this, message, e);
    }

    protected Toolbar getToolbar() {
        return toolbar;
    }
}
