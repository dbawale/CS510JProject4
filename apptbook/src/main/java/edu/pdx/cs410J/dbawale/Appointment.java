package edu.pdx.cs410J.dbawale;

import edu.pdx.cs410J.AbstractAppointment;

import java.text.DateFormat;
import java.util.Date;

import static java.lang.System.exit;

/**
 * Concrete implementation of the {@link AbstractAppointment} class
 * description is the description of the appointment, such as "lunch"
 * begintTime is the time at which the appointment starts
 * endTime is the time at which the appointment ends
 */
public class Appointment extends AbstractAppointment implements Comparable{
  String description;
  //String beginTime;
  //String endTime;
  Date beginTime;
  Date endTime;

  /**
   * Constructor for the Appointment class
   * @param description The description of the appointment
   * @param beginTime The time at which the appointment begins
   * @param endTime The time at which the appointment ends
     */
  /*public Appointment(String description, String beginTime, String endTime) {
    this.description=description;
    this.beginTime=beginTime;
    this.endTime=endTime;
  }*/
  public Appointment(String description, Date beginTime, Date endTime){
    this.description=description;
    this.beginTime=beginTime;
    this.endTime=endTime;
  }

  /**
   * Returns the beginTime of the appointment
   * @return The time at which the appointment begins
     */
  @Override
//  public String getBeginTimeString() {
//    return this.beginTime;
//  }
  public String getBeginTimeString(){return DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT).format(beginTime);}

  /**
   * Returns the endTime of the appointment
   * @return The time at which the appointment ends
     */
  @Override
//  public String getEndTimeString() {
//    return this.endTime;
//  }
  public String getEndTimeString(){
    return DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT).format(endTime);
  }

  /**
   * Returns the description of the appointment
   * @return The description of the appointment
     */
  @Override
  public String getDescription() {
    return this.description;
  }

  /**
   * Compares this object with the specified object for order.  Returns a
   * negative integer, zero, or a positive integer as this object is less
   * than, equal to, or greater than the specified object.
   * <p>
   * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
   * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
   * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
   * <tt>y.compareTo(x)</tt> throws an exception.)
   * <p>
   * <p>The implementor must also ensure that the relation is transitive:
   * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
   * <tt>x.compareTo(z)&gt;0</tt>.
   * <p>
   * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
   * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
   * all <tt>z</tt>.
   * <p>
   * <p>It is strongly recommended, but <i>not</i> strictly required that
   * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
   * class that implements the <tt>Comparable</tt> interface and violates
   * this condition should clearly indicate this fact.  The recommended
   * language is "Note: this class has a natural ordering that is
   * inconsistent with equals."
   * <p>
   * <p>In the foregoing description, the notation
   * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
   * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
   * <tt>0</tt>, or <tt>1</tt> according to whether the value of
   * <i>expression</i> is negative, zero or positive.
   *
   * @param o the object to be compared.
   * @return a negative integer, zero, or a positive integer as this object
   * is less than, equal to, or greater than the specified object.
   * @throws NullPointerException if the specified object is null
   * @throws ClassCastException   if the specified object's type prevents it
   *                              from being compared to this object.
   */
  @Override
  public int compareTo(Object o) {
    if(o==null)
    {
      throw new NullPointerException("Null pointer exception");
    }
    try {
      Appointment appt = (Appointment) o;
      if(!this.beginTime.equals(appt.beginTime))
      {
        //compare begin time and return appropriate value
        int cmp = this.beginTime.compareTo(appt.beginTime);
        return cmp;
      }
      else if(!this.endTime.equals(appt.endTime))
      {
        //compare end time and return appropriate value
        int cmpendtime = this.endTime.compareTo(appt.endTime);
        return cmpendtime;
      }
      else
      {
        //compare description and return appropriate value
        int cmpdescription = this.description.compareTo(appt.description);
        return cmpdescription;
      }
    }
    catch (ClassCastException e) {
      System.err.println("Error casting " + o.getClass().toString() + " to Appointment");
      exit(1);
    }
    return(-2);
  }
}
