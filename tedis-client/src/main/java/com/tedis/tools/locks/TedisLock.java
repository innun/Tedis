package com.tedis.tools.locks;

import com.tedis.client.connection.Connection;
import com.tedis.api.Lock;
import com.tedis.client.exception.IllegalLockOperation;
import com.tedis.protocol.Result;
import com.tedis.util.LuaScriptReader;
import com.tedis.util.UniqueCodeGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TedisLock implements Lock {

    private static Logger log = LoggerFactory.getLogger(TedisLock.class);

    static final String LOCK_KEY;
    private static String expireTime;

    private Connection<Result> conn;
    private static final String UNIQUE_CODE;
    private UpdateExTimeTask task;
    private Thread updateThread;

    static {
        LOCK_KEY = "TEDIS_LOCK_KEY";
        UNIQUE_CODE = UniqueCodeGenerator.uniqueCode();
    }

    /**
     * default lock expire time: 3600 seconds
     */
    public TedisLock(Connection<Result> conn) {
        this(conn,3600);
    }

    public TedisLock(Connection<Result> conn, int expireTime) {
        this.conn = conn;
        task = new UpdateExTimeTask(conn);
        updateThread = new Thread(task);
        TedisLock.expireTime = String.valueOf(expireTime);
    }

    @Override
    public void lock() {
        while (true) {
            Result res = conn.set(LOCK_KEY, UNIQUE_CODE, "NX", "EX", expireTime).sync();
            if (!res.getResult().equals("nil")) {
                log.info(Thread.currentThread().getName() + " acquires lock success");
//                updateThread.start();
                break;
            }
            log.info(Thread.currentThread().getName() + " acquires lock fail");
        }
    }

    @Override
    public void unlock() {
        if (conn == null) {
            throw new IllegalLockOperation("current thread does not maintain a lock");
        }
//        String value = conn.get(LOCK_KEY);
//        if (value.substring(1, value.length() - 1).equals(UNIQUE_CODE)) {
//            conn.del(LOCK_KEY);
//        }
//        task.stop();
        unlockWithLua();
    }

    private void unlockWithLua() {
        String script = LuaScriptReader.read("unlock.lua");
        if (script == null) {
            log.error("lua script read fail");
        }
        conn.eval(script, 1, LOCK_KEY, UNIQUE_CODE);
    }
}
