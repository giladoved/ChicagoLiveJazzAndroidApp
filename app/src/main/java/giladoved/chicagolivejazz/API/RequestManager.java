package giladoved.chicagolivejazz.API;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.List;

import giladoved.chicagolivejazz.Models.Show;

/**
 * Created by giladoved on 8/12/17.
 */

public class RequestManager {
    private static RequestManager instance;
    private RequestQueue requestQueue;
    private static Context context;
    public List<Show> shows;

    public RequestManager(Context context) {
        this.context = context;

        requestQueue = getRequestQueue();
    }

    public static synchronized RequestManager getInstance(Context context) {
        if (instance == null) {
            instance = new RequestManager(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }
}
