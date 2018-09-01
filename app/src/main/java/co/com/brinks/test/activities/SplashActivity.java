package co.com.brinks.test.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import co.com.brinks.test.R;

public class SplashActivity extends ParentActivity
{
    private static final long SPLASH_TIME_OUT = 3000;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mContext = this;

        new Handler().postDelayed(() ->
        {
            Intent i = new Intent(mContext,MainActivity.class);
            finish();
            startActivity(i);
        }, SPLASH_TIME_OUT);
    }
}
