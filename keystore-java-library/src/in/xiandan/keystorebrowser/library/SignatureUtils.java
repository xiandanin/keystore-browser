package in.xiandan.keystorebrowser.library;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * author  dengyuhan
 * created 2019/5/15 11:19
 */
public class SignatureUtils {
    public static final String MD5 = "md5";
    public static final String SHA_1 = "SHA-1";
    public static final String SHA_256 = "SHA-256";

    public static String getMessageDigest(byte[] input, String digestType) throws NoSuchAlgorithmException {
        return SignatureUtils.formatDigestBytes(digest(input, digestType));
    }


    public static byte[] digest(byte[] input, String digestType) throws NoSuchAlgorithmException {
        return MessageDigest.getInstance(digestType).digest(input);
    }

    private static String formatDigestBytes(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv.toUpperCase());
            stringBuilder.append(":");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }
}
