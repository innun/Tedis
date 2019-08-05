package com.tedis.client;

import com.tedis.api.Connection;
import com.tedis.client.common.TedisFuture;
import com.tedis.client.pool.TedisPool;
import com.tedis.protocol.Result;
import com.tedis.util.LuaScriptReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScriptExecutor {
    private static Logger log = LoggerFactory.getLogger(ScriptExecutor.class);
    private static TedisPool pool;
    private static Connection<Result> conn;

    static {
        pool = TedisPool.pool();
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
