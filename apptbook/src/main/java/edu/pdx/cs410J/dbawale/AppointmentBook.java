package edu.pdx.cs410J.dbawale;

import edu.pdx.cs410J.AbstractAppointment;
import edu.pdx.cs410J.AbstractAppointmentBook;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The concrete implementation for the {@link AbstractAppointmentBook} class.
 * owner is the owner of the appointment book
 * appointmentList is the list of appointments
 */
public class AppointmentBook extends AbstractAppointmentBook {
    String owner;
    ArrayList<Appointment> appointmentList;

    /**
     * Default constructor for AppointmentBook
     * Assigns owner as null and creates empty appointment list
     */
    public AppointmentBook(){
        this.owner=null;
        this.appointmentList=new ArrayList<Appointment>();
    }

    /**
     * Constructor for AppointmentBook
     * @param owner The name of the owner
     */
    public AppointmentBook(String owner) {
        this.owner=owner;
        appointmentList = new ArrayList<Appointment>();
    }

    /**
     * Returns the name of the owner of the current appointment book
     * @return The name of the owner
     */
    @Override
    public String getOwnerName() {
        return this.owner;
    }

    /**
     * Returns the list of appointments in the appointment book
     * @return The list of appointments
     */
    @Override
    public Collection getAppointments() {
        return this.appointmentList;
    }

    /**
     * Adds an appointment to the appointment book
     * @param abstractAppointment The abstract implementation of Appointment
     */
    @Override
    public void addAppointment(AbstractAppointment abstractAppointment) {

        this.appointmentList.add((Appointment) abstractAppointment);
        this.sort();
    }

    /**
     * Sorts the appointments in the appointment book, chronologically by begin time.
     * If two appointments begin at the same time, they are sorted chronologically by end time.
     * Otherwise, they are sorted lexicographically by description.
     */
    public void sort()
    {
        for(int i =0;i<appointmentList.size()-1;i++)
        {
            for(int j=0;j<appointmentList.size()-1-i;j++)
            {
                if(appointmentList.get(j).compareTo(appointmentList.get(j+1))>=0)
                {
                    Appointment temp = appointmentList.get(j);
                    appointmentList.set(j,appointmentList.get(j+1));
                    appointmentList.set(j+1,temp);
                }
            }
        }
    }
}
