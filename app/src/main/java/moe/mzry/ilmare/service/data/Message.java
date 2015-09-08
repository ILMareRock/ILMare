package moe.mzry.ilmare.service.data;

/**
 * Placeholder class for the user message.
 */
public class Message {

    private String content;
    private LocationSpec locationSpec;

    public Message() {}

    public Message(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocationSpec getLocationSpec() {
        return locationSpec;
    }

    public void setLocationSpec(LocationSpec locationSpec) {
        this.locationSpec = locationSpec;
    }
}
