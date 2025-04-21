package tools;

public class CRC16Modbus {

    public static String calcCrc16Modbus(String data) {
        byte[] bytes = hexString2Bytes(data);
        int crc = 0xFFFF;
        for (int i = 0; i < bytes.length; i++) {
            crc ^= (int) bytes[i] & 0xFF;
            for (int j = 0; j < 8; j++) {
                if ((crc & 1) == 1) {
                    crc = (crc >> 1) ^ 0xA001;
                } else {
                    crc = crc >> 1;
                }
            }
        }
        return String.format("%04X", crc);
    }
    public static byte[] hexString2Bytes(String src) {
        byte[] ret = new byte[src.length() / 2];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < tmp.length / 2; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }
    public static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0}));
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1}));
        return (byte) (_b0 ^ _b1);
    }
}
