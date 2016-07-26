package edu.pdx.cs410J.dbawale;

import com.google.common.annotations.VisibleForTesting;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This servlet provides a REST API for working with an
 * <code>AppointmentBook</code>.
 */
public class AppointmentBookServlet extends HttpServlet
{
    private AppointmentBook book = new AppointmentBook();

    /**
     * Handles an HTTP GET request from a client by checking the parameters provided
     * and either returning a pretty printed description of the appointments
     * or providing a search functionality.
     */
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        response.setContentType( "text/plain" );


        String owner = getParameter("owner",request);
        String beginTime = getParameter("beginTime",request);
        String endTime = getParameter("endTime",request);
        if(owner==null)
        {
            missingRequiredParameter(response,owner);
        }
        if(beginTime==null || endTime==null)
        {
            //Just return all appointments formatted by PrettyPrinter
            if(book.getOwnerName()==null)
            {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Appointment book on server not created");
                return;
            }
            else if(!book.getOwnerName().equals(owner))
            {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Appointment book for " + owner + " not available on server");
                return;
            }
            PrintWriter pw = response.getWriter();
            PrettyPrinter prettyPrinter = new PrettyPrinter();
            String str = prettyPrinter.getprettystring(book);
            pw.write(str);
            response.setStatus(HttpServletResponse.SC_OK);

        }
        else
        {
            //Return all appointments between startTime and endTime
            if(book.getOwnerName()==null)
            {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Appointment book on server not created");
                return;
            }
            else if(!book.getOwnerName().equals(owner))
            {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Appointment book for " + owner + " not available on server");
                return;
            }
            ArrayList<Appointment> appts = (ArrayList)book.getAppointments();
            DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);
            ArrayList<Appointment> toreturn = new ArrayList<Appointment>();
            try {
                Date start = df.parse(beginTime);
                Date end = df.parse(endTime);
                if(start.compareTo(end)>0)
                {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST,"End time cannot occur before start time");
                    return;
                }
                for(Appointment appt : appts)
                {
                    if(appt.beginTime.compareTo(start)>=0 && appt.beginTime.compareTo(end)<=0)
                    {
                        toreturn.add(appt);
                    }
                }
                PrintWriter pw = response.getWriter();
                PrettyPrinter prettyPrinter = new PrettyPrinter();
                String prettydesc = prettyPrinter.getprettystringforspecifiedapptlist(toreturn);
                pw.println(prettydesc);
                pw.flush();
            } catch (ParseException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Error in date/time format");
                return;
            }
            response.setStatus(HttpServletResponse.SC_OK);

        }
    }

    /**
     * Handles an HTTP Post request for adding an appointment to an appointment book.
     * If the current owner is null, that means an owner has never been set, and a new appointment book
     * is created.
     * If the current owner is found on the server, then the appointment is added to the owner's appointment book
     * If an owner is found on the server, but the passed owner name doesn't match it, then an error is thrown.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        response.setContentType( "text/plain" );
        String owner = getParameter("owner",request);
        String description = getParameter("description",request);
        String beginTime = getParameter("beginTime",request);
        String endTime = getParameter("endTime",request);
        checkAppointmentParameters(response, owner, description, beginTime, endTime);
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);
        try {
            Date startDate = dateFormat.parse(beginTime);
            Date endDate = dateFormat.parse(endTime);
            if(book.getOwnerName()==null)
            {
                book = new AppointmentBook(owner);

            }
            //else if(book.getOwnerName()!=owner)
            else if(!book.getOwnerName().equals(owner))
            {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Owner name not found on server");
                return;
            }
            book.addAppointment(new Appointment(description, startDate,endDate));
            PrintWriter pw = response.getWriter();
            pw.println("Added appointment!");
            response.setStatus( HttpServletResponse.SC_OK);
        } catch (ParseException e) {
            String message = "Incorrect Date/Time Format";
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, message);
            return;
        }
    }

    /**
     * Checks if the specified parameters are null, and sets error code accordingly
     * @param response The HttpServletResponse
     * @param owner The owner passed in the put request
     * @param description The description passed in the put request
     * @param beginTime The begin time passed in the put request
     * @param endTime The end time passed in the put request
     * @throws IOException
     */
    private void checkAppointmentParameters(HttpServletResponse response, String owner, String description, String beginTime, String endTime) throws IOException {
        if(owner==null)
        {
            missingRequiredParameter(response,owner);
        }
        if(description==null)
        {
            missingRequiredParameter(response,description);
        }
        if(beginTime==null)
        {
            missingRequiredParameter(response,beginTime);
        }
        if(endTime==null)
        {
            missingRequiredParameter(response,endTime);
        }
    }



    /**
     * Writes an error message about a missing parameter to the HTTP response.
     *
     * The text of the error message is created by {@link Messages#missingRequiredParameter(String)}
     */
    private void missingRequiredParameter( HttpServletResponse response, String parameterName )
        throws IOException
    {
        String message = Messages.missingRequiredParameter(parameterName);
        response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
        return;
    }


    /**
     * Returns the value of the HTTP request parameter with the given name.
     *
     * @return <code>null</code> if the value of the parameter is
     *         <code>null</code> or is the empty string
     */
    private String getParameter(String name, HttpServletRequest request) {
      String value = request.getParameter(name);
      if (value == null || "".equals(value)) {
        return null;

      } else {
        return value;
      }
    }
}
