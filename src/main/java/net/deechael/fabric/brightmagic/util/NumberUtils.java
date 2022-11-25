package net.deechael.fabric.brightmagic.util;

public class NumberUtils {

    public static byte[] intToBytes(int a){
        byte[] ans = new byte[4];
        for (int i = 0; i < 4; i++)
            ans[i] = (byte) (a >> (i * 8));
        return ans;
    }

    public static int bytesToInt(byte[] a){
        int ans = 0;
        for (int i = 0; i < 4; i++) {
            ans <<= 8;
            ans |= (a[3 - i] & 0xff);
        }
        return ans;
    }

}
