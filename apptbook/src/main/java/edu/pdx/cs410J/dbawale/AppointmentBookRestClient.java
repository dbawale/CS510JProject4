package edu.pdx.cs410J.dbawale;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;
import java.net.ConnectException;

/**
 * A helper class for accessing the rest client
 */
public class AppointmentBookRestClient extends HttpRequestHelper
{
    private static final String WEB_APP = "apptbook";
    private static final String SERVLET = "appointments";

    private final String url;


    /**
     * Creates a client to the appointment book REST service running on the given host and port
     * @param hostName The name of the host
     * @param port The port
     */
    public AppointmentBookRestClient( String hostName, int port )
    {
        this.url = String.format( "http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET );
    }

    /**
     * Searches appointment book on server for values with owner, beginTime and endTime
     * @param owner The owner of the appointment book to be searched
     * @param beginTime The begin time
     * @param endTime The end time
     * @return The response from the server
     * @throws IOException
     */
    public Response getSearchValueResponse(String owner, String beginTime, String endTime) throws IOException {
            return get(this.url,"owner",owner,"beginTime",beginTime,"endTime",endTime);
    }

    /**
     * Creates a new appointment by posting values to the server.
     * @param owner The owner of the appointment book
     * @param description The description of the appointment to be added
     * @param beginTime The start time of the appointment to be added
     * @param endTime The end time of the appointment to be added
     * @return The response from the server
     * @throws IOException
     */
    public Response postValuesToServer(String owner, String description, String beginTime, String endTime) throws IOException {
        return post(this.url,"owner",owner,"description",description,"beginTime",beginTime,"endTime",endTime);
    }
}
