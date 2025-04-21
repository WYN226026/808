package tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

//网络连接监听器。
public class NetworkChangeReceiver extends BroadcastReceiver {
    private NetworkChangeListener listener;

    public NetworkChangeReceiver(NetworkChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (listener != null) {
            if (NetworkUtils.isNetworkAvailable(context)) {
                listener.onNetworkConnected();
            }
        }
    }

    public interface NetworkChangeListener {
        void onNetworkConnected();
    }
}
