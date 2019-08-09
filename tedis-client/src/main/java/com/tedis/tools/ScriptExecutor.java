package com.tedis.tools;

import com.tedis.common.TedisFuture;
import com.tedis.connection.TraditionalConn;
import com.tedis.pool.ConnPool;
import com.tedis.protocol.Result;
import com.tedis.util.LuaScriptReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScriptExecutor {
    private static Logger log = LoggerFactory.getLogger(ScriptExecutor.class);
    private static ConnPool pool;
    private static TraditionalConn conn;

    static {
        pool = ConnPool.pool();
        conn = pool.traditionalConn();
    }

    public static TedisFuture<Result> execute(String scriptFileName, int keyNum, String... args) {
        String script = LuaScriptReader.read(scriptFileName);
        if (script == null) {
            log.error("lua script read fail");
        }
        return conn.eval(script, keyNum, args);
    }
}
