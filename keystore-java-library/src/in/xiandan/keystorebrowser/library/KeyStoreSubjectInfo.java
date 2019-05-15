package in.xiandan.keystorebrowser.library;

/**
 * author  dengyuhan
 * created 2019/5/14 16:54
 */
public class KeyStoreSubjectInfo {
    private String commonName;
    private String organizationName;
    private String organizationUnit;
    private String localityName;
    private String stateName;
    private String country;

    private String str;


    public KeyStoreSubjectInfo(String str) {
        this.str = str;
        String[] split = str.split(", ");
        if (split.length >= 6) {
            this.commonName = split[0].replace("CN=", "");
            this.organizationName = split[1].replace("OU=", "");
            this.organizationUnit = split[2].replace("O=", "");
            this.localityName = split[3].replace("L=", "");
            this.stateName = split[4].replace("ST=", "");
            this.country = split[5].replace("C=", "");
        }
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationUnit() {
        return organizationUnit;
    }

    public void setOrganizationUnit(String organizationUnit) {
        this.organizationUnit = organizationUnit;
    }

    public String getLocalityName() {
        return localityName;
    }

    public void setLocalityName(String localityName) {
        this.localityName = localityName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return str;
    }
}
