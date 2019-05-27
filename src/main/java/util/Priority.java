package util;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: lucaszyang
 * @Date: 2019/05/27/17:35
 * @Description:
 */
public enum  Priority {

    HIGH("High"),
    MIDDLE("Middle"),
    LOW("Low"),
    OPTIONAL("Nice To Have");

    private String value;

    Priority(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
