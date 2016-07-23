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
        FileOutputStream ostream = new FileOutputStream(filename);
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);
        ArrayList<Appointment> currentAppointments = (ArrayList)abstractAppointmentBook.getAppointments();
        ostream.write("This appoinment book belongs to: ".getBytes());
        ostream.write(abstractAppointmentBook.getOwnerName().getBytes());
        ostream.write("\n".getBytes());
        String str = "There are " + currentAppointments.size() + " appointments in this appointment book.\n\n";
        ostream.write(str.getBytes());
        str = "The appointments are:\n\n";
        ostream.write(str.getBytes());
        int i=1;
        for(Appointment appt : currentAppointments)
        {
            str = i + ": " + appt.getDescription() + "\n";
            ostream.write(str.getBytes());
            str = "   Starts at: " + df.format(appt.beginTime) + "\t\t" + "Ends at: " + df.format(appt.endTime) + "\n";
            ostream.write(str.getBytes());
            long diff = appt.endTime.getTime() - appt.beginTime.getTime();
            str = "   The duration of this appointment is " + TimeUnit.MILLISECONDS.toMinutes(diff) + " minutes. \n\n";
            ostream.write(str.getBytes());
            i++;
        }
    }


    /**
     * Pretty prints an appointment book to standard out
     * @param book The book to be printed
     */
    public void printtostdout(AbstractAppointmentBook book)
    {
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);
        ArrayList<Appointment> currentAppointments = (ArrayList)book.getAppointments();
        System.out.print("This appoinment book belongs to: ");
        System.out.print(book.getOwnerName());
        System.out.print("\n");
        String str = "There are " + currentAppointments.size() + " appointments in this appointment book.\n\n";
        System.out.print(str);
        str = "The appointments are:\n\n";
        System.out.print(str);
        int i=1;
        for(Appointment appt : currentAppointments)
        {
            str = i + ": " + appt.getDescription() + "\n";
            System.out.print(str);
            str = "   Starts at: " + df.format(appt.beginTime) + "\t\t" + "Ends at: " + df.format(appt.endTime) + "\n";
            System.out.print(str);
            long diff = appt.endTime.getTime() - appt.beginTime.getTime();
            str = "   The duration of this appointment is " + TimeUnit.MILLISECONDS.toMinutes(diff) + " minutes. \n\n";
            System.out.print(str);
            i++;
        }
    }
}
