package tools;

// TODO 工具类 字符类型转换
public class DataTypeConversion {
    public static String bytesToHexString(byte[] src, int size) {
        StringBuilder ret = new StringBuilder();
        if (src == null || size <= 0) {
            return null;
        }
        for (int i = 0; i < size; i++) {
            String hex = Integer.toHexString(src[i] & 0xFF);
            if (hex.length() < 2) {
                hex = "0" + hex;
            }
            //hex += " ";
            ret.append(hex);
        }
        return ret.toString().toUpperCase();
    }

    public static String intToHex2(int a){
        StringBuilder d = new StringBuilder();
        String b;
        b = Integer.toString(a,16);
        if(b.length() %2 == 0){
            d.append(b);
        }else {
            d.append("0").append(b);
        }
        return d.toString();
    }

    public static String intToHex4(int i) {
        String hex = Integer.toHexString(i);
        while (hex.length() < 4) {
            hex = "0" + hex;
        }
        return hex.toUpperCase();
    }

}
