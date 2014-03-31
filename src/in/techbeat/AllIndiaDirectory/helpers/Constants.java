package in.techbeat.AllIndiaDirectory.helpers;

/**
 * Created by prabhakar on 31/3/14.
 */
public enum Constants {
    NOT_FOUND("Not found"),
    TEMPORARY_ERROR("Temporary Error");

    String message;

    Constants(final String message) {
        this.message = message;
    }

    public String getText() {
        return message;
    }
}
