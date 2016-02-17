package eecs398_lock;

import java.util.ArrayList;

/**
 * Created by JRG94 on 2/17/2016.
 * A class for handling a set of locks
 */
public class SmartLockManager {

    private ArrayList<SmartLock> locks;

    // An empty constructor for initializes the list of locks
    public SmartLockManager() {
        locks = new ArrayList<SmartLock>();
    }


}
