package in.xiandan.keystorebrowser.library;

import java.util.Map;

/**
 * author  dengyuhan
 * created 2019/5/15 11:13
 */
public class ApkInfo {
    private String alias;
    private String packageName;
    private Integer versionCode;
    private String versionName;
    private String signatureType;
    private CertificateInfo cert;
    private Map<String, String> extras;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public CertificateInfo getCert() {
        return cert;
    }

    public void setCert(CertificateInfo cert) {
        this.cert = cert;
    }

    public Map<String, String> getExtras() {
        return extras;
    }

    public void setExtras(Map<String, String> extras) {
        this.extras = extras;
    }

    public String getSignatureType() {
        return signatureType;
    }

    public void setSignatureType(String signatureType) {
        this.signatureType = signatureType;
    }
}
