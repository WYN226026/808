package tools;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import android_serialport_api.Device;
import android_serialport_api.SerialPortManager;

public class SerialPortUtil extends AppCompatActivity {
    private static final String TAG = "cease";
    public static SerialPortManager serialPortManager;
    public String selectPort = "/dev/ttyS2"; //s1
    public int selectSpeed = 57600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    // TODO 打开串口
    public void serialPort(){
        if(serialPortManager == null){
            Device device = new Device();
            device.path = selectPort;
            device.speed = selectSpeed;
            serialPortManager = new SerialPortManager(device);
            new ReadThread().start();
        }
    }
    // TODO 发送数据(String类型)
    public void serial_send(String send) {
        byte[] bytes = hexString2Bytes(send);
        if(bytes != null) {
            serialPortManager.sendPacket(bytes);
        }
        Log.e("", "发送了数据: " + send);

    }
    private byte[] hexString2Bytes(String src) {
        byte[] ret = new byte[src.length() / 2];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < tmp.length / 2; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }
    private byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0}));
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1}));
        return (byte) (_b0 ^ _b1);
    }
    // TODO 接收数据（String类型）
    private class ReadThread extends Thread{
        @Override
        public void run() {

            serialPortManager.setOnDataReceiveListener(new SerialPortManager.OnDataReceiveListener() {
                byte[] buffer = new byte[64];
                @Override
                public void onDataReceive(byte[] recvBytes, int i) {
                    if (recvBytes != null && recvBytes.length > 0) {
                        if(onDataReceiveListener == null){
                            Log.e(TAG, "onDataReceiveListener is null1" );
                        }else {
                            buffer = recvBytes;
                            onDataReceiveListener.onDataReceive(buffer);
                            //Log.e(TAG, "onDataReceive: data is normal");
                        }
                    }else {
                        Log.e("error", "onDataReceive is null2 " );
                    }
                }
            });
        }
    }


    public OnDataReceiveListener onDataReceiveListener = null;
    public interface OnDataReceiveListener {
        void onDataReceive(byte[] buffer);
    }
    public void setOnDataReceiveListener(OnDataReceiveListener dataReceiveListener) {
        onDataReceiveListener = dataReceiveListener;
    }
}
