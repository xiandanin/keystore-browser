package in.xiandan.keystorebrowser.library;

import java.util.List;

/**
 * author  dengyuhan
 * created 2019/5/14 17:20
 */
public class KeyStoreSignatureInfo {
    private String md5;
    private String sha1;
    private String sha256;

    private List<SignaturePlatformFormat> format;

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public List<SignaturePlatformFormat> getFormat() {
        return format;
    }

    public void setFormat(List<SignaturePlatformFormat> format) {
        this.format = format;
    }
}
