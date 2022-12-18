package in.softment.trueplayerdevelopment.Model;

import java.util.Date;

public class UpcomingVideoCountModel {

    public boolean isEnabled = true;
    public Date time = new Date();


    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
