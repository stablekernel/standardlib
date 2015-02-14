package com.rosshambrick.standardlib;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.econet.EcoNetApplication;
import com.econet.R;
import com.econet.app.ErrorHandler;

public class SingleFragmentToolbarActivity extends ActionBarActivity {

    private static final String EXTRA_FRAGMENT_NAME = "EXTRA_FRAGMENT_NAME";
    private Toolbar toolbar;

    public static <TFragment extends Fragment> Intent newIntent(Context context, Class<TFragment> fragmentName, Bundle fragmentArgs) {
        return SingleFragmentToolbarActivity.newIntent(context, fragmentName, fragmentArgs, SingleFragmentToolbarActivity.class);
    }

    public static <TFragment extends Fragment, TActivity extends SingleFragmentToolbarActivity> Intent newIntent(Context context, Class<TFragment> fragmentName, Bundle fragmentArgs, Class<TActivity> activityClass) {
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EcoNetApplication.inject(this);
        setContentView(R.layout.activity_single_fragment);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.single_fragment_container, getFragment())
                    .commit();
        }
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
