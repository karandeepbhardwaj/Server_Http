package com.http;

public class Logger {

    private boolean debug = false;

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void print(String m){
        if (this.debug){
            System.out.println(m);
        }
    }
}
