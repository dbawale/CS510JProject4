package edu.pdx.cs410J.dbawale;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookDumper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Pretty printer class for Appointment Book project.
 * Creates a nicely-formatted textual presentation of an appointment book
 * that includes the duration of each appointment in minutes.
 */
public class PrettyPrinter implements AppointmentBookDumper {
    private String filename;

    /**
     * Default constructor for PrettyPrinter.
     * Assigns an empty filename.
     */
    PrettyPrinter()
    {
        this.filename="";
    }

    /**
     * Constructor for pretty printer with filename
     * @param filename The name of the file to be printed to
     */
    PrettyPrinter(String filename){
        this.filename=filename;
    }

    /**
     * Pretty prints the appointment book to the file specified.
     *
     * @param abstractAppointmentBook The appointment book to be pretty printed
     * @throws IOException If file not found on disk
     */
    @Override
    public void dump(AbstractAppointmentBook abstractAppointmentBook) throws IOException {

    }


    /**
     * Returns a string of pretty printed appointment books
     * @param book The book to be printed
     */
    public String getprettystring(AbstractAppointmentBook book)
    {
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);
        ArrayList<Appointment> currentAppointments = (ArrayList)book.getAppointments();
        StringBuilder sb = new StringBuilder();
        sb.append("This appoinment book belongs to: ");
        sb.append(book.getOwnerName());
        sb.append("\n");
        String str = "There are " + currentAppointments.size() + " appointments in this appointment book.\n\n";
        sb.append(str);
        str = "The appointments are:\n\n";
        sb.append(str);
        int i=1;
        PrintCurrentAppointments(df, currentAppointments, sb, i);
        return sb.toString();
    }

    /**
     * Returns a pretty printed list from the given appointment list
     * @param appts The list of appointments to be pretty printed
     * @return The pretty printed String.
     */
    public String getprettystringforspecifiedapptlist(ArrayList<Appointment> appts)
    {
        if(appts.size()==0)
        {
            return "No appointments are available for the specified duration.";
        }
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);
        ArrayList<Appointment> currentAppointments = appts;
        StringBuilder sb = new StringBuilder();
        String str = "There are " + currentAppointments.size() + " appointments in this time period.\n\n";
        sb.append(str);
        str = "The appointments are:\n\n";
        sb.append(str);
        int i=1;
        PrintCurrentAppointments(df, currentAppointments, sb, i);
        return sb.toString();
    }

    /**
     * Adds the currentAppointments to a StringBuilder
     * @param df A DateFormat for formatting dates
     * @param currentAppointments The list of appointments to be parsed and added
     * @param sb The StringBuilder to be appended to
     * @param i To iterate through current appointment number
     */
    private void PrintCurrentAppointments(DateFormat df, ArrayList<Appointment> currentAppointments, StringBuilder sb, int i) {
        String str;
        for(Appointment appt : currentAppointments)
        {
            str = i + ": " + appt.getDescription() + "\n";
            sb.append(str);
            str = "   Starts at: " + df.format(appt.beginTime) + "\t\t" + "Ends at: " + df.format(appt.endTime) + "\n";
            sb.append(str);
            long diff = appt.endTime.getTime() - appt.beginTime.getTime();
            str = "   The duration of this appointment is " + TimeUnit.MILLISECONDS.toMinutes(diff) + " minutes. \n\n";
            sb.append(str);
            i++;
        }
    }
}
