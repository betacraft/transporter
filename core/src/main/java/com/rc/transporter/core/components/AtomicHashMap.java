package com.rc.transporter.core.components;

import com.sun.corba.se.impl.orbutil.concurrent.Mutex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Hashmap with extra features like
 * 1. extra mutex for atomic operations
 * 2. with some lookup retries
 * Author: akshay
 * Date  : 1/8/14
 * Time  : 4:34 PM
 */
public class AtomicHashMap<K, V> {
    /**
     * Logger
     */
    private Logger logger = LoggerFactory.getLogger(AtomicHashMap.class);
    /**
     * Connection catalog
     */
    protected ConcurrentHashMap<K, V> map;
    /**
     * Catalog locks for each entry
     */
    protected ConcurrentHashMap<K, Mutex> mutexMap;
    /**
     * Max retries
     */
    protected int maxHashmapChecks = 3;
    /**
     * Delay between two checks (ms)
     */
    protected int delay = 200;

    /**
     * Constructor
     *
     * @param maxRetries
     * @param delay
     */
    public AtomicHashMap (final int maxRetries, final int delay) {
        this.maxHashmapChecks = maxRetries;
        this.delay = delay;
    }

    public V get (final K key) throws InterruptedException {
        int retries = 0;
        Mutex mutex = null;
        while (retries < this.maxHashmapChecks) {
            mutex = this.mutexMap.get(key);
            if (mutex == null) {
                Thread.sleep(delay);
            } else {
                break;
            }
            ++retries;
        }
        if (mutex == null) {
            logger.error("No entry for key {}", key);
            return null;
        }
        mutex.acquire();
        V value = null;
        retries = 0;
        while (retries < this.maxHashmapChecks) {
            value = this.map.get(key);
            if (value == null) {
                Thread.sleep(this.delay);
                ++retries;
            } else {
                break;
            }
        }
        mutex.release();
        return value;
    }
}
