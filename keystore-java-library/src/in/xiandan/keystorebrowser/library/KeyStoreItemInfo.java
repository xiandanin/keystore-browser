package in.xiandan.keystorebrowser.library;

/**
 * author  dengyuhan
 * created 2019/5/15 11:27
 */
public class KeyStoreItemInfo {

    private String alias;
    private long created_time;
    private String created_time_str;
    private CertificateInfo cert;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public long getCreated_time() {
        return created_time;
    }

    public void setCreated_time(long created_time) {
        this.created_time = created_time;
    }

    public String getCreated_time_str() {
        return created_time_str;
    }

    public void setCreated_time_str(String created_time_str) {
        this.created_time_str = created_time_str;
    }

    public CertificateInfo getCert() {
        return cert;
    }

    public void setCert(CertificateInfo cert) {
        this.cert = cert;
    }
}
