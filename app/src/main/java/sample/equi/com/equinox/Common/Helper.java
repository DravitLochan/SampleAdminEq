package sample.equi.com.equinox.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Helper {
    private static final Helper ourInstance = new Helper();

    public static Helper getInstance() {
        return ourInstance;
    }

    private Helper() {
    }

    public boolean networkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
