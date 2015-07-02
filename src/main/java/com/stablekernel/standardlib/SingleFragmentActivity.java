package com.stablekernel.standardlib;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class SingleFragmentActivity extends ActionBarActivity {
    public static final String EXTRA_FRAGMENT_NAME = "EXTRA_FRAGMENT_NAME";
    private Toolbar toolbar;
    private boolean traceLog;

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
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if(null != currentFragment && currentFragment.onOptionsItemSelected(item)){
                return true;
            } else {
                finish();
                return true;
            }
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
