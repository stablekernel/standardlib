package com.rosshambrick.standardlib;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

public class SingleFragmentActivity extends ActionBarActivity {
    private static final String EXTRA_FRAGMENT_NAME = "EXTRA_FRAGMENT_NAME";
    private Toolbar toolbar;
    private boolean traceLog;

    public static <TFragment extends Fragment> Intent newIntent(Context context, Class<TFragment> fragmentName, Bundle fragmentArgs) {
        return SingleFragmentActivity.newIntent(context, fragmentName, fragmentArgs, SingleFragmentActivity.class);
    }

    public static <TFragment extends Fragment, TActivity extends SingleFragmentActivity> Intent newIntent(Context context, Class<TFragment> fragmentName, Bundle fragmentArgs, Class<TActivity> activityClass) {
        Intent intent = new Intent(context, activityClass);
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
