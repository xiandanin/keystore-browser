package in.xiandan.keystorebrowser.library;

import android.content.res.AXmlResourceParser;

import com.android.apksig.ApkVerifier;
import com.android.apksig.apk.ApkFormatException;
import com.google.gson.Gson;
import com.meituan.android.walle.PayloadReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.spec.InvalidKeySpecException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * author  dengyuhan
 * created 2019/5/15 10:57
 */
public class ApkParser {
    private Gson gson;

    public ApkParser() {
        gson = new Gson();
    }

    public ApkInfo parser(File apkFile) throws CertificateEncodingException, NoSuchAlgorithmException, InvalidKeySpecException, IOException, XmlPullParserException, ApkFormatException {
        //基本信息
        ApkInfo apkInfo = createApkInfoByApk(apkFile);

        //证书信息
        Certificate certificate = getCertificateByEntryStream(apkFile);
        CertificateInfo certificateInfo = CertificateParser.getCertificateInfo(certificate);
        apkInfo.setCert(certificateInfo);

        //签名类型
        ApkVerifier.Result result = new ApkVerifier.Builder(apkFile).build().verify();
        if (result.isVerifiedUsingV1Scheme()) {
            apkInfo.setSignatureType("V1");
        }
        if (result.isVerifiedUsingV2Scheme()) {
            apkInfo.setSignatureType("V2");
        }
        return apkInfo;
    }

    private ApkInfo createApkInfoByApk(File apkFile) throws IOException, XmlPullParserException, ApkFormatException, NoSuchAlgorithmException {
        ApkInfo apkInfo = new ApkInfo();
        apkInfo.setAlias(apkFile.getName());
        Map<String, String> extras = new LinkedHashMap<>();
        //如果有walle信息
        Map<String, String> walle = gson.fromJson(PayloadReader.getString(apkFile, 0x71777777), Map.class);
        if (walle != null) {
            extras.putAll(walle);
        }

        ZipFile zipFile = new ZipFile(apkFile, ZipFile.OPEN_READ);
        ZipEntry entry = zipFile.getEntry("AndroidManifest.xml");
        AXmlResourceParser parser = new AXmlResourceParser();
        parser.open(zipFile.getInputStream(entry));

        int type;
        while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
            if (type == XmlPullParser.START_TAG) {
                for (int i = 0; i != parser.getAttributeCount(); ++i) {
                    String attributeName = parser.getAttributeName(i);
                    if (attributeName.equals("versionCode")) {
                        apkInfo.setVersionCode(parser.getAttributeValueData(i));
                    } else if (attributeName.equals("versionName")) {
                        apkInfo.setVersionName(parser.getAttributeValue(i));
                    } else if (attributeName.equals("package")) {
                        apkInfo.setPackageName(parser.getAttributeValue(i));
                    }
                    if (apkInfo.getPackageName() != null && apkInfo.getPackageName().length() > 0
                            && apkInfo.getVersionName() != null && apkInfo.getVersionName().length() > 0
                            && apkInfo.getVersionCode() != null && apkInfo.getVersionCode() > 0) {
                        break;
                    }
                }
                extras.putAll(resolveApkExtraItem(parser));
            }
        }
        parser.close();
        apkInfo.setExtras(extras);
        return apkInfo;
    }

    private Map<String, String> resolveApkExtraItem(AXmlResourceParser parser) {
        Map<String, String> extras = new LinkedHashMap<>();
        if (parser.getName().equals("meta-data")) {
            String metaName = parser.getAttributeValue(0);
            String metaValue = parser.getAttributeValue(1);
            String regex = ".*(UMENG_CHANNEL|TINKER_ID|BUGLY_CHANNEL|TINKER_PATCH_APPLICATION|tinker_version).*";
            boolean matches = Pattern.matches(regex, metaName);
            if (matches) {
                extras.put(metaName, metaValue);
            }
        }
        return extras;
    }

    private Certificate getCertificateByEntryStream(File apkFile) throws IOException {
        JarFile jar = new JarFile(apkFile);
        JarEntry manifest = jar.getJarEntry("AndroidManifest.xml");
        InputStream is = jar.getInputStream(manifest);

        byte[] buffer = new byte[8192];
        while (is.read(buffer, 0, buffer.length) != -1) {
        }
        is.close();
        return manifest.getCertificates()[0];
    }

}