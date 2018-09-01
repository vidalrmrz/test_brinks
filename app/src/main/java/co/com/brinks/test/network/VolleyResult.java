package co.com.brinks.test.network;

import android.widget.Toast;

import com.android.volley.VolleyError;

import co.com.brinks.test.BrinksTestApplication;

/**
 * Created by vidalramirez on 1/09/18.
 */

public abstract class VolleyResult<T>
{
    public abstract  void onSuccess(T response) throws Exception;

    public void onError(VolleyError error, T response)
    {
        Toast.makeText(BrinksTestApplication.getInstance(), response != null ? response.toString() : error.getMessage(), Toast.LENGTH_LONG).show();
    }
}
