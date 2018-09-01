package co.com.brinks.test.activities;

import android.os.Bundle;

import butterknife.ButterKnife;
import co.com.brinks.test.R;

/**
 * Created by vidalramirez on 1/09/18.
 */

public class MainActivity extends ParentActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }
}
