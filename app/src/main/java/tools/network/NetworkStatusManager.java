package tools.network;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Handler;
import android.os.Looper;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import tools.SetDataBase;

public class NetworkStatusManager {
    private static final int CHECK_INTERVAL_MS = 1000; // 1秒
    private ConnectivityManager connectivityManager;
    private Context context;
    private Handler handler;
    private Runnable statusCheckRunnable;
    private int wifi_error ;
    SetDataBase setDB;
    public NetworkStatusManager(Context context) {
        this.context = context;
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        handler = new Handler(Looper.getMainLooper());
        getDB();
    }

    private void getDB() {
        setDB = new SetDataBase(context);
    }

    public void startNetworkStatusChecking() {
        statusCheckRunnable = new Runnable() {
            @Override
            public void run() {
                checkWifiStatus();
                handler.postDelayed(this, CHECK_INTERVAL_MS); // 每1秒执行一次
            }
        };
        handler.post(statusCheckRunnable); // 启动定时任务
    }

    public void stopNetworkStatusChecking() {
        if (statusCheckRunnable != null) {
            handler.removeCallbacks(statusCheckRunnable); // 停止定时任务
        }
    }

    private void checkWifiStatus() {
        // 获取当前连接的网络
        Network activeNetwork = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            activeNetwork = connectivityManager.getActiveNetwork();
        }
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
        wifi_error = setDB.getSingleColumnDataById(1,"flag31");
        if(wifi_error == 1 ){
            if (networkCapabilities != null && networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                sendNetworkStatusBroadcast("wifiAvailable");
            } else {
                sendNetworkStatusBroadcast("wifiUnavailable");
            }
        }

    }

    private void sendNetworkStatusBroadcast(String status) {
        Intent intent = new Intent("network_message_received");
        intent.putExtra("message", status);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}

