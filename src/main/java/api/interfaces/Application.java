package api.interfaces;

public interface Application {

    void open();
    void forceStop();
    void clearData();
    String packageID();
    String activityID();
}
