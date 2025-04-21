package update;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UpdateChecker {
    public static final String VERSION_URL = "https://qihang.ltd/st_h808v/s808_w.txt";
    public static final String APK_DOWNLOAD_URL = "http://qihang.ltd/dz/zcx/freezeCl4_zcx.apk";
    public interface UpdateCheckListener {
        void onCheckComplete(boolean isUpdateAvailable);
    }

    public static void isUpdateAvailable(String currentVersion, UpdateCheckListener listener) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(VERSION_URL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 在UI线程上更新UI
                new Handler(Looper.getMainLooper()).post(() -> listener.onCheckComplete(false));
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String latestVersion = response.body().string();
                    // 在UI线程上更新UI
                    new Handler(Looper.getMainLooper()).post(() -> listener.onCheckComplete(compareVersions(currentVersion, latestVersion) < 0));
                } else {
                    // 在UI线程上更新UI
                    new Handler(Looper.getMainLooper()).post(() -> listener.onCheckComplete(false));
                }
            }
        });
    }
    /**
     *  首先将传入的两个版本号字符串按照.分割成数组
     *  接着逐个比较这些数组的元素。如果任一版本号的部分缺失（例如，一个版本号有3部分，另一个只有2部分），则假设缺失的部分为0。
     *  如果发现任何不同，则根据哪个版本号更大返回相应的值（-1表示version1更小，1表示version1更大）。如果所有比较的部分都相等，则返回0，表示两个版本号相同。
     * **/
    private static int compareVersions(String version1, String version2) {
        String[] version1Parts = version1.split("\\.");
        String[] version2Parts = version2.split("\\.");
        int length = Math.max(version1Parts.length, version2Parts.length);
        for (int i = 0; i < length; i++) {
            int v1 = i < version1Parts.length ? Integer.parseInt(version1Parts[i]) : 0;
            int v2 = i < version2Parts.length ? Integer.parseInt(version2Parts[i]) : 0;

            if (v1 < v2) {
                return -1;
            } else if (v1 > v2) {
                return 1;
            }
        }
        return 0;
    }
}

