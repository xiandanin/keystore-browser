package in.xiandan.keystorebrowser.library;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import sun.security.x509.X509CertImpl;

/**
 * author  dengyuhan
 * created 2019/5/15 11:23
 */
public class CertificateParser {
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static CertificateInfo getCertificateInfo(Certificate cert) throws CertificateEncodingException, NoSuchAlgorithmException, InvalidKeySpecException {
        if (cert instanceof X509CertImpl) {
            X509CertImpl x509 = (X509CertImpl) cert;
            CertificateInfo info = new CertificateInfo();
            info.setSubject(new KeyStoreSubjectInfo(x509.getSubjectDN().getName()));
            info.setIssuer(new KeyStoreSubjectInfo(x509.getIssuerDN().getName()));
            info.setSerialNumber(new BigInteger(1, x509.getSerialNumber().toByteArray()).toString(16).toLowerCase());
            info.setStart_time(x509.getNotBefore().getTime());
            info.setStart_time_str(format.format(new Date(info.getStart_time())));
            info.setEnd_time(x509.getNotAfter().getTime());
            info.setEnd_time_str(format.format(new Date(info.getEnd_time())));

            //签名信息
            KeyStoreSignatureInfo signature = new KeyStoreSignatureInfo();
            byte[] encoded = cert.getEncoded();
            signature.setMd5(SignatureUtils.getMessageDigest(encoded, SignatureUtils.MD5));
            signature.setSha1(SignatureUtils.getMessageDigest(encoded, SignatureUtils.SHA_1));
            signature.setSha256(SignatureUtils.getMessageDigest(encoded, SignatureUtils.SHA_256));
            signature.setFormat(formatPlatform(signature.getMd5(), signature.getSha1(), encoded));
            info.setSignature(signature);
            info.setSignature_algorithm(x509.getSigAlgName());
            info.setVersion(x509.getVersion());
            info.setPublicKeyAlgorithm(String.format("%d 位 RSA 密钥", getPublicKeySize(cert.getPublicKey())));
            return info;
        }
        return null;
    }

    private static int getPublicKeySize(PublicKey publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String algorithm = publicKey.getAlgorithm();
        KeyFactory keyFact = KeyFactory.getInstance(algorithm);
        RSAPublicKeySpec keySpec = keyFact.getKeySpec(publicKey, RSAPublicKeySpec.class);
        BigInteger modulus = keySpec.getModulus();
        return modulus.toString(2).length();
    }

    private static List<SignaturePlatformFormat> formatPlatform(String md5, String sha1, byte[] encoded) throws NoSuchAlgorithmException {
        List<SignaturePlatformFormat> format = new ArrayList<>();
        String wechat = md5.toLowerCase().replace(":", "");

        //微信的格式
        format.add(new SignaturePlatformFormat("wechat", "微信开放平台", wechat, "https://open.weixin.qq.com"));
        format.add(new SignaturePlatformFormat("qq", "QQ互联", wechat, "https://connect.qq.com"));
        format.add(new SignaturePlatformFormat("weibo", "微博开放平台", wechat, "https://open.weibo.com"));

        format.add(new SignaturePlatformFormat("baidu_map", "百度地图开放平台", sha1, "http://lbsyun.baidu.com"));

        //String formatFacebook = "keytool -exportcert -alias %s -keystore %s | -storepass %s openssl sha1 -binary | openssl base64";
        //String facebook = String.format(formatFacebook, alias == null ? "[alias]" : alias, keystorePath == null ? "[keystore-file-path]" : keystorePath, storepass == null ? "[keystore-password]" : storepass);

        String facebook = Base64.getEncoder().encodeToString(SignatureUtils.digest(encoded, SignatureUtils.SHA_1));
        format.add(new SignaturePlatformFormat("facebook", "FaceBook开放平台", facebook, "https://developers.facebook.com"));
        return format;
    }

}
