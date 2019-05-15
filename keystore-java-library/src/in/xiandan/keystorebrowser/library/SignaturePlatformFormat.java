package in.xiandan.keystorebrowser.library;

/**
 * author  dengyuhan
 * created 2019/5/15 14:48
 */
public class SignaturePlatformFormat {
    private String name;
    private String displayName;
    private String format;
    private String link;

    public SignaturePlatformFormat(String name, String displayName, String format, String link) {
        this.name = name;
        this.displayName = displayName;
        this.format = format;
        this.link = link;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public SignaturePlatformFormat() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
