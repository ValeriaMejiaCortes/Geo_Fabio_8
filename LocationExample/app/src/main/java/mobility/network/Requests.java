package mobility.network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import mobility.volley.CustomVolleyRequestQueue;


public class Requests {

    private Context context;
    private RequestQueue queue;

    public Requests(Context context) {
        this.setContext(context);
        queue = CustomVolleyRequestQueue.getInstance(context)
                .getRequestQueue();
    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public JsonObjectRequest post(String url,
                                  final JSONObject params,
                                  final Map<String, String> headers,
                                  Response.Listener<JSONObject> response,
                                  Response.ErrorListener error) {
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, params, response, error) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000 * 20,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
        return postRequest;
    }

    public StringRequest post(String url,
                              final Map<String, String> params,
                              final Map<String, String> headers,
                              Response.Listener<String> response,
                              Response.ErrorListener error) {
        StringRequest postRequest = new StringRequest(
                Request.Method.POST,
                url,
                response,
                error
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000 * 20,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
        return postRequest;
    }

    public StringRequest get(String url,
                             final Map<String, String> headers,
                             Response.Listener<String> response,
                             Response.ErrorListener error) {
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                response,
                error
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000 * 20,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
        return postRequest;
    }

    public JsonArrayRequest getJson(String url,
                             final Map<String, String> headers,
                             Response.Listener<JSONArray> response,
                             Response.ErrorListener error) {
        JsonArrayRequest getRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response,
                error
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };
        getRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000 * 20,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(getRequest);
        return getRequest;
    }

    public Map<String, String> getDefaultHeaders() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        return headers;
    }


    /**
     * Método utilizado para obtener el status code de una petición HTTP
     *
     * @param volleyError, Error generado al realizar el request
     * @return, Status Code de la petición
     */
    public int getStatusCode(VolleyError volleyError) {
        NetworkResponse respuesta = volleyError.networkResponse;
        return respuesta.statusCode;
    }


    /**
     * Método utilizado para obtener el mensaje de error generado por una petición HTTP
     *
     * @param volleyError, Error volley
     * @return, Error codificado
     */
    public VolleyError getMessageError(VolleyError volleyError) {
        if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
            VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
            volleyError = error;
        }
        return volleyError;
    }


    public String getRequestError(VolleyError volleyError) throws JSONException {
        String mensaje = getMessageError(volleyError).getMessage();
        JSONObject json = new JSONObject(mensaje);
        return json.getString("error");
    }
}
