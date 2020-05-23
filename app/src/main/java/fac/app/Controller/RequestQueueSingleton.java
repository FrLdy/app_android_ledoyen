package fac.app.Controller;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Gstion de la file de requètes **/
public class RequestQueueSingleton {
    private static RequestQueueSingleton instance;
    private RequestQueue requestQueue;
    private static Context context;

    private RequestQueueSingleton(Context context){
        RequestQueueSingleton.context = context;
        this.requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            this.requestQueue  = Volley.newRequestQueue(RequestQueueSingleton.context.getApplicationContext());
        }
        return this.requestQueue;
    }

    public static synchronized RequestQueueSingleton getInstanceUnique(Context context) {
        if (RequestQueueSingleton.instance == null) {
            RequestQueueSingleton.instance = new RequestQueueSingleton(context);
        }
        return RequestQueueSingleton.instance;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void startRequestQueue(){
        this.requestQueue.start();
    }
}
