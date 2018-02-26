package com.bma.studycollabs;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by Gopi on 10/31/2015.
 */
public class AppData extends  MultiDexApplication {

    public static String Shard_city_data = "CITY_DATA";
    public static String Shard_user_Id = "USER_ID";
    public static String Shard_user_Count = "USER_COUNT";
    public static SharedPreferences sharedpreferences;
    public static String Shared_pref = "BOOKS_CHECKOUT";

    private static AppData sInstance;
    public static String KEY = "12345";

    public static final String TAG = AppData.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
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

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }


    public static AppData getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        //TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "CaviarDreams.ttf");
        super.onCreate();

        registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(Activity activity,
                                          Bundle savedInstanceState) {
                // new activity created; force its orientation to portrait
                activity.setRequestedOrientation(
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }

        });


        sInstance = this;
    }

    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static void saveToPreferences(Context context, String preferenceName, boolean preferenceValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context, String preferenceName, String defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getString(preferenceName, defaultValue);
    }

    public static boolean readFromPreferences(Context context, String preferenceName, boolean defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getBoolean(preferenceName, defaultValue);
    }


    public static List<String> SplitStringVar(List<String> result, String val) {
        result = new ArrayList<String>();
        for (String id : val
                .split(",")) {
            result.add(id.trim());

        }
        return result;
    }

    public static String JointStringVar(List<String> list) {
        return TextUtils.join("~", list);
    }

    public static List<String> SplitVartilde(List<String> result, String val) {
        result = new ArrayList<String>();
        for (String id : val
                .split("~")) {
            result.add(id.trim());

        }
        return result;
    }

    public static String ConstructUrl(String url, HashMap<String, String> params) {
        try {
            StringBuilder stringBuilder = new StringBuilder(url);
            Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
            int i = 1;
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                if (i == 1) {
                    stringBuilder.append("?" + entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "UTF-8"));
                } else {
                    stringBuilder.append("&" + entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), "UTF-8"));
                }
                iterator.remove(); //avoids a ConcurrentModificationException
                i++;
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static void save_city_data(Context context, String city) {
//        clear_data(context);
        sharedpreferences = context.getSharedPreferences(Shared_pref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Shard_city_data, city);
        editor.apply();
    }

    public static void clear_data(Context context) {
        sharedpreferences = context.getSharedPreferences(Shared_pref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear().apply();
    }

    public static String getShard_city_data(Context ctx) {
        sharedpreferences = ctx.getSharedPreferences(Shared_pref, Context.MODE_PRIVATE);
        return sharedpreferences.getString(Shard_city_data, null);
    }


    public static int getShard_User_Id(Context ctx) {
        sharedpreferences = ctx.getSharedPreferences(Shared_pref, Context.MODE_PRIVATE);
        return sharedpreferences.getInt(Shard_user_Id, 0);
    }


    public static void save_user_Id(Context context, int id) {
        sharedpreferences = context.getSharedPreferences(Shared_pref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(Shard_user_Id, id);
        editor.apply();
    }

    public static void save_user_count(Context context, int id) {
        sharedpreferences = context.getSharedPreferences(Shared_pref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(Shard_user_Count, id);
        editor.apply();
    }

    public static int getShard_User_Count(Context ctx) {
        sharedpreferences = ctx.getSharedPreferences(Shared_pref, Context.MODE_PRIVATE);
        return sharedpreferences.getInt(Shard_user_Count, 0);
    }


    public static Spanned replaceHTML(String htmlText) {
        htmlText = htmlText.replaceAll("\r\n", "<br />");
        return Html.fromHtml(htmlText);
    }

}
