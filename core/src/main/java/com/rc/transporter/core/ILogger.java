package com.rc.transporter.core;

/**
 * Logging interface
 * Author: akshay
 * Date  : 11/14/13
 * Time  : 4:09 PM
 */
public interface ILogger {
    public ILogger appendLog (final String log, final Throwable... exceptions);
}
