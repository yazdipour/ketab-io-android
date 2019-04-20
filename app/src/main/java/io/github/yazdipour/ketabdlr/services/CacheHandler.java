package io.github.yazdipour.ketabdlr.services;

import android.content.Context;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

public class CacheHandler {
    private final static String TAG = "SnappyDB";
    private static DB snappyDB;
    private static CacheHandler cacheHandler;

    private static void OpenDb(final Context context) throws SnappydbException {
        if ((snappyDB == null || !snappyDB.isOpen()) && context != null)
            snappyDB = DBFactory.open(context, "log");
    }

//    snappyDB.get("atomic integer", AtomicInteger .class);
// https://github.com/nhachicha/SnappyDB

    public static CacheHandler getHandler(final Context context) {
        try {
            OpenDb(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cacheHandler == null ? cacheHandler = new CacheHandler() : cacheHandler;
    }

    ///>>>>>>>>>>>>>>>> PUSH <<<<<<<<<<<<<<<<<<<//
    // Push/Save Logs to Database
    private void push(String[] arr) throws SnappydbException {
        if (arr.length > 0) snappyDB.put("", arr);
    }

    ///>>>>>>>>>>>>>>>> PULL <<<<<<<<<<<<<<<<<<<//
    public String[] pullArray(String key, int uid) throws SnappydbException {
        if (!snappyDB.exists(key)) return null;
        return snappyDB.getObjectArray(key, String.class);
//        return snappyDB.getArray(key, AppLog.class);
    }

    private String pull(String key, int uid) throws SnappydbException {
        if (!snappyDB.exists(key)) return null;
        return snappyDB.get(key, String.class);
    }
    ///>>>>>>>>>>>>>>>> DEL <<<<<<<<<<<<<<<<<<<//

    public void remove(String type, int uid) throws SnappydbException {
//        snappyDB.del(uid);
    }
    ///>>>>>>>>>>>>>>>> DONE <<<<<<<<<<<<<<<<<<<//

    public void wipeDb() {
        try {
            snappyDB.destroy();
            snappyDB.close();
            snappyDB = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
