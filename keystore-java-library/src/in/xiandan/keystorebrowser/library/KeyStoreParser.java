package in.xiandan.keystorebrowser.library;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

/**
 * author  dengyuhan
 * created 2019/5/14 17:27
 */
public class KeyStoreParser {
    private SimpleDateFormat format;

    public KeyStoreParser() {
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public KeyStoreInfo parser(File input, String storepass) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, InvalidKeySpecException {
        KeyStoreInfo keyStoreInfo = new KeyStoreInfo();
        List<KeyStoreItemInfo> keyStoreAliases = new ArrayList<>();

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(new FileInputStream(input), storepass.toCharArray());

        //所有的alias
        Enumeration<String> aliases = keyStore.aliases();
        while (aliases.hasMoreElements()) {
            KeyStoreItemInfo info = new KeyStoreItemInfo();

            String alias = aliases.nextElement();
            info.setAlias(alias);

            //创建时间
            info.setCreated_time(keyStore.getCreationDate(alias).getTime());
            info.setCreated_time_str(format.format(new Date(info.getCreated_time())));

            //证书信息
            Certificate cert = keyStore.getCertificate(alias);
            CertificateInfo certificateInfo = CertificateParser.getCertificateInfo(cert);
            info.setCert(certificateInfo);

            keyStoreAliases.add(info);
        }
        keyStoreInfo.setAliases(keyStoreAliases);
        return keyStoreInfo;
    }


}
