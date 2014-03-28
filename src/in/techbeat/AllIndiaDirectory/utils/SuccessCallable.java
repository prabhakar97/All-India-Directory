package in.techbeat.AllIndiaDirectory.utils;

import in.techbeat.AllIndiaDirectory.model.NumberDetail;

import java.util.concurrent.Callable;

/**
 * Created by prabhakar on 27/3/14.
 */
public abstract class SuccessCallable implements Callable<Void> {
    private NumberDetail numberDetails;

    public void setNumberDetails(NumberDetail numberDetails) {
        this.numberDetails = numberDetails;
    }

    public NumberDetail getNumberDetails() {
        return numberDetails;
    }

}
