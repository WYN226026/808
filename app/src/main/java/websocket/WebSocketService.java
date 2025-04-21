package websocket;

import static com.example.freezecl4_zcx.StartActivity.cpuid;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import tools.PermissionsDB;

public class WebSocketService extends Service {
    private static final String TAG = "WebSocketService";
    //ws://8.209.105.157:8080/ws
    private static final String SERVER_URL = "ws://8.209.105.157:8080/ws";
    private WebSocket webSocket;
    private WebSocketListener socketListener;
    private String clientId; // 唯一标识符

    public static boolean web = false;
    private PermissionsDB permissionsDB ;
    @Override
    public void onCreate() {
        super.onCreate();
        permissionsDB = PermissionsDB.getInstance(this);
        //开启读写
        permissionsDB.openReadDB();
        permissionsDB.openWriteDB();
        socketListener = new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                WebSocketService.this.webSocket = webSocket;
                Log.d(TAG, "WebSocket连接已打开");
                setClientId(cpuid);
                sendMessage("机器端:"+cpuid);
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                Log.d(TAG, "接收到服务器消息：" + text + web);
                // 处理接收到的消息
                // 发送广播，将消息传递给Activity
                if(Objects.equals(text, "Successfully_connected")){
                    web = true;
                }
                if(Objects.equals(text, "Connection_disconnected")){
                    web = false;
                }
                if (Objects.equals(text, "Forced_shutdown")) {
                    long i = permissionsDB.updateFlag("flag1",0);
                }
                if(Objects.equals(text, "Forced_on")){
                    long  i =permissionsDB.updateFlag("flag1",1);
                }
                if(text.startsWith("setCount_")){
                    long  i = permissionsDB.updateFlag("timing",Integer.valueOf(text.substring("setCount_".length()))-1);
                    permissionsDB.updateFlag("timingSecond",60);
                }
                Intent intent = new Intent("websocket_message_received");
                intent.putExtra("message", text);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            }
            private void forceStopApp() {
                android.os.Process.killProcess(android.os.Process.myPid());  // 结束当前进程
                System.exit(1);  // 退出应用
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                Log.d(TAG, "WebSocket连接正在关闭：" + reason);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                Log.d(TAG, "WebSocket连接已关闭：" + reason);
                web = false;
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                Log.e(TAG, "WebSocket连接失败：" + t.getMessage());
            }
        };

        connectWebSocket();
    }

    private void connectWebSocket() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(SERVER_URL)
                .build();
        webSocket = client.newWebSocket(request, socketListener);
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void sendMessage(String message ) {
        if (webSocket != null&&message !=null) {
            webSocket.send(message); // 发送消息时附带唯一标识符
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (webSocket != null) {
            webSocket.close(1000, "Service destroyed");
        }
    }
    public void close(){
        webSocket.close(1000,"service destroyed");
        webSocket = null;
    }
    public void afreshOpen(){
        close();
        socketListener = new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                WebSocketService.this.webSocket = webSocket;
                Log.d(TAG, "WebSocket连接已打开");
                setClientId(cpuid);
                sendMessage("机器端:"+cpuid);
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                Log.d(TAG, "接收到服务器消息：" + text + web);
                // 处理接收到的消息
                // 发送广播，将消息传递给Activity
                if(Objects.equals(text, "Successfully_connected")){
                    web = true;
                }
                if(Objects.equals(text, "Connection_disconnected")){
                    web = false;
                }
                if (Objects.equals(text, "Forced_shutdown")) {
                    long i = permissionsDB.updateFlag("flag1",0);
                    System.out.println("修改flag1 1 标志位 "+i);
                }
                if(Objects.equals(text, "Forced_on")){
                    long  i =permissionsDB.updateFlag("flag1",1);
                    System.out.println("修改flag1 0 标志位 "+i );
                }
                if(text.startsWith("setCount_")){
                    long  i = permissionsDB.updateFlag("timing",Integer.valueOf(text.substring("setCount_".length())));
                    permissionsDB.updateFlag("timingSecond",60);
                    System.out.println("修改次数 ： "+ i);
                }
                Intent intent = new Intent("websocket_message_received");
                intent.putExtra("message", text);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            }
            private void forceStopApp() {
                android.os.Process.killProcess(android.os.Process.myPid());  // 结束当前进程
                System.exit(1);  // 退出应用
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                Log.d(TAG, "WebSocket连接正在关闭：" + reason);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                Log.d(TAG, "WebSocket连接已关闭：" + reason);
                web = false;
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                Log.e(TAG, "WebSocket连接失败：" + t.getMessage());
            }
        };

        connectWebSocket();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder();
    }

    public class LocalBinder extends Binder {
        public WebSocketService getService() {
            return WebSocketService.this;
        }
    }
}
