package jeffemanuel.org.duckduck;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;
import jeffemanuel.org.modules.ActivityModule;

/**
 * Created by J Emanuel on 10/30/14.
 */
public class MainApplication extends Application {

    public static final String TAG = MainApplication.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private ObjectGraph objectGraph;

    private static MainApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
       Object[] modules = getModules().toArray();
        objectGraph = ObjectGraph.create(modules);
       // objectGraph.inject(this);
        mInstance = this;
    }

    public static synchronized MainApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
    protected List<Object> getModules() {
        return Arrays.<Object>asList(
                new ActivityModule(this)

        );
    }

    //public ObjectGraph getObjectGraph() {
      //  return this.objectGraph;
    //}

    public void inject(Object object) {
        objectGraph.inject(object);
    }

    public void addtoGraph(Object object) {
        objectGraph.plus(object);
    }
}