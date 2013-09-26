package com.rc.transporter.core;

/**
 * Contract for decoder of incoming data
 * Author: akshay
 * Date  : 9/25/13
 * Time  : 12:03 AM
 */
public interface ITransportDecoder<I> {

    /**
     * Method to decode incoming data
     *
     * @param incomingData data to be decoded
     * @return @Object decoded data
     * @throws Exception if anything goes wrong
     */
    public Object decode(I incomingData) throws Exception;

}
