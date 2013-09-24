package com.rc.transporter.core;

/**
 * Transport client
 * Author: akshay
 * Date  : 9/23/13
 * Time  : 3:55 AM
 */
public interface ITransportClient<M> {
    /**
     * Method to initialize connection with @TransportServer or other server
     *
     * @param host             hostname
     * @param port             port
     * @param transportSession @TransportSession to listen to the events associated wtih session
     */
    public void connect(final String host, final int port, TransportSession<M> transportSession) throws Exception;

    /**
     * Method to close client
     */
    public void close();

}
