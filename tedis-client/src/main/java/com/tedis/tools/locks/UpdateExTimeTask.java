package com.tedis.tools.locks;

import com.tedis.client.connection.Connection;
import com.tedis.protocol.Result;
import com.tedis.util.LuaScriptReader;

public class UpdateExTimeTask implements Runnable {

    private boolean flag = true;
    private Connection<Result> conn;
    private static int INCR = 10;

    public UpdateExTimeTask(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void run() {
        while (flag) {
            int ttl = Integer.parseInt(conn.ttl(TedisLock.LOCK_KEY).sync().getResult());
            int newExTime = ttl + INCR;
            String script = LuaScriptReader.read("updateExTime.lua");
            conn.eval(script, 1, TedisLock.LOCK_KEY, String.valueOf(newExTime));
            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        flag = false;
    }
}
