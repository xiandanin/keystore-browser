package in.xiandan.keystorebrowser.library;

/**
 * author  dengyuhan
 * created 2019/5/14 16:54
 */
public class CertificateInfo {
    private KeyStoreSubjectInfo subject;
    private KeyStoreSubjectInfo issuer;
    private String serialNumber;
    private long start_time;
    private String start_time_str;
    private long end_time;
    private String end_time_str;
    private String signature_algorithm;
    private KeyStoreSignatureInfo signature;
    private String publicKeyAlgorithm;
    private int version;

    public KeyStoreSubjectInfo getSubject() {
        return subject;
    }

    public void setSubject(KeyStoreSubjectInfo subject) {
        this.subject = subject;
    }

    public KeyStoreSubjectInfo getIssuer() {
        return issuer;
    }

    public void setIssuer(KeyStoreSubjectInfo issuer) {
        this.issuer = issuer;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public String getStart_time_str() {
        return start_time_str;
    }

    public void setStart_time_str(String start_time_str) {
        this.start_time_str = start_time_str;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public String getEnd_time_str() {
        return end_time_str;
    }

    public void setEnd_time_str(String end_time_str) {
        this.end_time_str = end_time_str;
    }

    public String getSignature_algorithm() {
        return signature_algorithm;
    }

    public void setSignature_algorithm(String signature_algorithm) {
        this.signature_algorithm = signature_algorithm;
    }

    public KeyStoreSignatureInfo getSignature() {
        return signature;
    }

    public void setSignature(KeyStoreSignatureInfo signature) {
        this.signature = signature;
    }

    public String getPublicKeyAlgorithm() {
        return publicKeyAlgorithm;
    }

    public void setPublicKeyAlgorithm(String publicKeyAlgorithm) {
        this.publicKeyAlgorithm = publicKeyAlgorithm;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
