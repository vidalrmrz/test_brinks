package co.com.brinks.test.network;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import co.com.brinks.test.BrinksTestApplication;
import co.com.brinks.test.BuildConfig;
import co.com.brinks.test.R;

import static com.android.volley.Request.Method.POST;

/**
 * Created by vidalramirez on 1/09/18.
 */

public class VolleyService
{
    private static final String HOST = "https://itunes.apple.com/";

    private static final String TAG = "VolleyService";

    private static final int TIME_OUT = 30 * 1000;

    private static final int MAX_RETRIES = 0;

    private static String TAG_JSON_OBJ = "json_obj_req";

    private static boolean SHOW_LOG = BuildConfig.DEBUG;

    private static VolleyService mInstance;

    private VolleyService()
    {
        super();
    }

    public static VolleyService getInstance()
    {
        if (mInstance == null)
        {
            mInstance = new VolleyService();
        }
        return mInstance;
    }

    private JsonObjectRequest sendJsonRequest(int method, String path, JSONObject jsonRequest, final VolleyResult<String> resultListener)
    {
        if (SHOW_LOG)
        {
            Log.i(this.getClass().getSimpleName(), String.format("Request %s : %s", method== POST ? "POST":"GET", path));
        }
        if (jsonRequest!=null)
        {
            Log.i(this.getClass().getSimpleName(), String.format("Parameters : %s", jsonRequest.toString()));
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method, path, jsonRequest, jsonObject ->
        {
            if (resultListener != null)
            {
                try
                {
                    resultListener.onSuccess(jsonObject.toString());
                }
                catch (Exception e)
                {
                    String message = e.getMessage();
                    if (SHOW_LOG)
                    {
                        Log.e(TAG, "Error: " + message, e);
                    }
                    tratarError(e, resultListener);
                }
            }
        }, volleyError -> tratarError(volleyError, resultListener))
        {
            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError)
            {
                if (volleyError.networkResponse != null && volleyError.networkResponse.data != null)
                {
                    String message;
                    if (SHOW_LOG)
                    {
                        message = new String(volleyError.networkResponse.data);
                    }
                    else
                    {
                        message = BrinksTestApplication.getInstance().getString(R.string.error_server);
                    }
                    return new VolleyError(message);
                }
                return volleyError;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT,
                MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        BrinksTestApplication.getInstance().addToRequestQueue(jsonObjectRequest, TAG_JSON_OBJ);
        return jsonObjectRequest;
    }

    public static <T> void tratarError(Exception e, VolleyResult<T> resultListener)
    {
        String message = "";
        if (e != null)
        {
            if (SHOW_LOG)
            {
                message = e.getMessage();
            }
            else
            {
                message = BrinksTestApplication.getInstance().getString(R.string.error_server);
            }
        }
        VolleyError error = new VolleyError(message);
        tratarError(error, resultListener);

    }

    public static <T> void tratarError(VolleyError error, VolleyResult<T> resultListener)
    {
        if (resultListener != null)
        {
            String message = error.getMessage();
            if (message == null || message.length() == 0
                    || error instanceof TimeoutError
                    || error instanceof NoConnectionError
                    || error instanceof NetworkError
                    )
            {
                message = BrinksTestApplication.getInstance().getString(R.string.error_internet_connection);
            }
            else  if (error instanceof ServerError)
            {
                message = BrinksTestApplication.getInstance().getString(R.string.error_server);
            }

            VolleyError volleyError = new VolleyError(message);
            resultListener.onError(volleyError, null);
        }
    }

    public void downloadImage(String urlPhoto, final ImageView imageView, int placeholder, final VolleyResult<Bitmap> listener)
    {
        if(placeholder!=0)
        {
            imageView.setImageResource(placeholder);
        }

        if (urlPhoto!=null && urlPhoto.length()>0)
        {
            Response.Listener<Bitmap> retornar = response ->
            {
                Bitmap bitmap = response;
                imageView.setImageBitmap(bitmap);
                if (listener != null)
                {
                    try
                    {
                        listener.onSuccess(bitmap);
                    }
                    catch (Exception e)
                    {
                        tratarError(e, listener);
                    }
                }
            };

            ImageRequest ir = new ImageRequest(urlPhoto, retornar, 0, 0, ImageView.ScaleType.CENTER_INSIDE, null, volleyError ->
            {
                if (volleyError != null && listener!=null)
                {
                    listener.onError(volleyError, null);
                }
            });

            ir.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT,
                    MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            BrinksTestApplication.getInstance().addToRequestQueue(ir, TAG_JSON_OBJ);

        }
    }

    /**************************** INICIO APIS ************************/

    /**
     * Obtiene las 20 aplicaciones pagas más populares
     */
    public JsonObjectRequest getTopPayApps(VolleyResult<String> resultListener)
    {
        String url = HOST + "us/rss/toppaidapplications/limit=20/json";
        JsonObjectRequest jsonRequest = sendJsonRequest(POST, url,  null, resultListener);
        return jsonRequest;
    }
    /*************************** END APIS **********************************/
}
