package com.teb.kilimanjaro.httpnetwork;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 访问网络时的json对象
 */
public class JsonEntry implements Serializable {
    private HashMap args;
    private HashMap session;

    public HashMap getSession () {
        return session;
    }

    public void setSession (HashMap session) {
        this.session = session;
    }

    public HashMap getArgs () {
        return args;
    }

    public void setArgs (HashMap args) {
        this.args = args;
    }

    @Override
    public String toString () {
        return "JsonEntry{" +
                "args=" + args +
                ", session=" + session +
                '}';
    }
}