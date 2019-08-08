package com.tedis.client;

import com.tedis.client.connection.Connection;
import com.tedis.client.common.TedisFuture;
import com.tedis.client.pool.ConnPool;
import com.tedis.protocol.Result;
import com.tedis.util.LuaScriptReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScriptExecutor {
    private static Logger log = LoggerFactory.getLogger(ScriptExecutor.class);
    private static ConnPool pool;
    private static Connection<Result> conn;

    static {
        pool = ConnPool.pool();
        conn = pool.connection();
    }

    public static TedisFuture<Result> execute(String scriptFileName, int keyNum, String... args) {
        String script = LuaScriptReader.read(scriptFileName);
        if (script == null) {
            log.error("lua script read fail");
        }
        return conn.eval(script, keyNum, args);
    }
}
