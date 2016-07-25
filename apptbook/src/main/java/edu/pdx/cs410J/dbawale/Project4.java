package edu.pdx.cs410J.dbawale;

import edu.pdx.cs410J.web.HttpRequestHelper;

import javax.sound.midi.SysexMessage;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ConnectException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import static java.lang.System.exit;

/**
 * The main class that parses the command line and communicates with the
 * Appointment Book server using REST.
 */
public class Project4 {

    public static final String MISSING_ARGS = "Missing command line arguments";
    private static Boolean isSearch=false,isHost=false,isPort=false,isPrint=false,isReadme=false,isDescription=false;
    private static String host,port;
    private static int hostAt=-1,portAt=-1;
    private static String owner;
    private static String descrption;
    private static String begindate;
    private static String enddate;

    public static void main(String[] args){

        checkargumentlength(args);

        //Extract all options from current args
        for(int i=0;i<args.length;i++){
            if(args[i].charAt(0)=='-')
            {
                try {
                    checkCurrentOption(args[i],i);
                } catch (Exception e) {
                    System.err.println("Unknown option found");
                    exit(1);
                }
            }
        }

        //If readme is found then print description of project and exit
        if(isReadme){
            System.out.println("This program lets you create and search appointments on a remote server.\n" +
                    "You can search for appointments between given times using the -search option\n" +
                    "You can add appointments by specifying the owner, description, startTime and endTime\n" +
                    "Be sure to specify both the host and the port while running the program!");
            exit(0);
        }
        //If port is specified but host is not
        if(isPort&&!isHost)
        {
            System.err.println("Error to specify port without host");
            exit(1);
        }
        //If host is specified but port is not
        if(isHost&&!isPort)
        {
            System.err.println("Error to specify host without port");
            exit(1);
        }


        //Find all required parameters and assign them
        for(int i=0;i<args.length;i++){
            if(args[i].charAt(0)!='-')
            {
                if(i==hostAt)
                    continue;
                if(i==portAt)
                    continue;
                try {
                    processcurrentargs(args,i);
                    break;
                } catch (Exception e) {
                    System.err.println("Unknown argument found");
                    exit(1);
                }
            }
        }

        //If both host and port are specified
        if(isHost&&isPort)
        {
            host = args[hostAt];
            port = args[portAt];
        }

        if(!isSearch && isHost && isPort){
            try {
                int portInt = Integer.parseInt(port);
                AppointmentBookRestClient client = new AppointmentBookRestClient(host,portInt);
                HttpRequestHelper.Response response = client.postValuesToServer(owner,descrption,begindate,enddate);
                System.out.println(response.getContent());

            }
            catch (NumberFormatException e)
            {
                System.err.println("Port "+ port + " is not a valid port");
                exit(1);
            } catch (IOException e) {
                System.err.println("Cannot connect to server. System sent message: \n"+ e.getMessage());
            }
            catch (IllegalArgumentException e)
            {
                System.err.println("Could not connect to server " + host + " on " + port);
                System.err.println("System sent the message: " + e.getMessage());
            }
        }
        else if(isSearch&&isHost&&isPort){
            try {
                int portInt = Integer.parseInt(port);
                AppointmentBookRestClient client = new AppointmentBookRestClient(host,portInt);
                HttpRequestHelper.Response response = client.getSearchValueResponse(owner,begindate,enddate);
                System.out.println(response.getContent());
            }
            catch (NumberFormatException e)
            {
                System.err.println("Port "+ port + " is not a valid port");
                exit(1);
            } catch (IOException e) {
                System.err.println("Cannot connect to server. System sent message: \n"+ e.getMessage());
            }
            catch (IllegalArgumentException e)
            {
                System.err.println("Could not connect to server " + host + " on " + port);
                System.err.println("System sent the message: " + e.getMessage());
            }
        }

        if(isPrint&&!isSearch){
            System.out.println(descrption + " from " + begindate + " to " + enddate);
        }


    }

    private static void checkCurrentOption(String arg, int i) throws Exception{
        switch (arg)
        {
            case "-host":
                isHost=true;
                hostAt =i+1;
                break;
            case "-port":
                isPort=true;
                portAt=i+1;
                break;
            case "-search":
                isSearch=true;
                break;
            case "-print":
                isPrint=true;
                break;
            case "-README":
                isReadme=true;
                break;
            default:
                throw new Exception();

        }
    }

    /**
     * Processes a set of given arguments that are previously checked for validiy.
     * Checks whether the arguments at position index and index + 1 are not dates or times.
     * Checks whether the arguments at position index + 2 and index + 4 are dates.
     * Checks whether the arguments at position index + 3 and index + 5 are times.
     * Creates the appointment and prints description if processprint flag is set
     * @param args A set of arguments that have been checked for validity
     * @param index The starting index at which options are present in args[]
     * @throws Exception Throws an exception if the description is found to be empty.
     */
    private static void processcurrentargs(String[] args, int index) throws Exception {
        try {
            DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);
            //df.setLenient(true);
            if(!isSearch) {
                if (!checkdate(args[index]) && !checkdate(args[index + 1])) {
                    if (checkdate(args[index + 2]) && checkdate(args[index + 5])) {
                        if (checktime(args[index + 3]) && checktime(args[index + 6])) {
                            owner = args[index];
                            descrption = args[index + 1];
                            if (descrption == "") {
                                throw new Exception();
                            } else {
                                isDescription = true;
                            }
//                        begindate = args[index + 2];
//                        begintime = args[index + 3];
//                        enddate = args[index + 4];
//                        endtime = args[index + 5];
//                        begin = begindate + " " + begintime;
//                        end = enddate + " " + endtime;
                            begindate = args[index + 2] + " " + args[index + 3] + " " + args[index + 4];
                            enddate = args[index + 5] + " " + args[index + 6] + " " + args[index + 7];


                            try {
                                Date begindt = df.parse(begindate);
                                Date enddt = df.parse(enddate);
                            } catch (ParseException e) {
                                System.err.println("Error parsing datetime format");
                                exit(1);
                            }
                        } else {
                            System.err.println("Please check format of time and try again\n");
                            exit(1);
                        }
                    } else {
                        System.err.println("Please check format of date and try again\n");
                        exit(1);
                    }
                } else {
                    System.err.println("Either owner or description are of the format DATE or TIME. Please enter correct format.\n");
                    exit(1);
                }
            }
            else if(isSearch)
            {
                if (!checkdate(args[index])) {
                    if (checkdate(args[index + 1]) && checkdate(args[index + 4])) {
                        if (checktime(args[index + 2]) && checktime(args[index + 5])) {
                            owner = args[index];
                            begindate = args[index + 1] + " " + args[index + 2] + " " + args[index + 3];
                            enddate = args[index + 4] + " " + args[index + 5] + " " + args[index + 6];

                            try {
                                Date begindt = df.parse(begindate);
                                Date enddt = df.parse(enddate);
                            } catch (ParseException e) {
                                System.err.println("Error parsing datetime format");
                                exit(1);
                            }
                        } else {
                            System.err.println("Please check format of time and try again\n");
                            exit(1);
                        }
                    } else {
                        System.err.println("Please check format of date and try again\n");
                        exit(1);
                    }
                } else {
                    System.err.println("Either owner or description are of the format DATE or TIME. Please enter correct format.\n");
                    exit(1);
                }
            }
        }
        catch (NumberFormatException e)
        {
            System.err.println("Check command format and try again.");
            exit(1);
        }
    }

    /**
     * Checks if a given string is in the appropriate date format. Returns true if so, false if not.
     * @param tocheck The string to be checked
     * @return True if appropriate, false if not
     * @throws NumberFormatException Throws NumberFormatException if characters are present instead of numbers.
     */
    public static Boolean checkdate(String tocheck) throws NumberFormatException
    {
        String [] split;
        split = tocheck.split("/");
        if (split.length!=3)
            return false;
        if(split[0].length()<1||Integer.parseInt(split[0])>12||split[0].length()>2)
            return false;
        if(split[1].length()<1||Integer.parseInt(split[1])>31||split[0].length()>2)
            return false;
        if(split[2].length()<1||Integer.parseInt(split[2])<1900||split[2].length()>4||Integer.parseInt(split[2])>2016)
            return false;
        return true;
    }

    /**
     * Checks if a given string is in the appropriate time format. Returns true if so, false if not.
     * @param tocheck The string to be checked
     * @return True if appropriate, false if not
     * @throws NumberFormatException Throws NumberFormatException if characters are present instead of numbers.
     */
    public static Boolean checktime(String tocheck) throws NumberFormatException
    {
        String [] split;
        split = tocheck.split(":");
        if(split.length!=2)
            return false;
        if(split[0].length()==0||split[1].length()==0)
            return false;
        if(split[0].length()>2||split[1].length()>2)
            return false;
        if(Integer.parseInt(split[0])>23||Integer.parseInt(split[1])>59)
            return false;
        return true;
    }

    /**
     * Takes a set of arguments and checks their number for validation
     * @param args A set of arguments from commmand line
     * @return The number of arguments
     * @throws Exception Throws an exception if the number is less than 6 or more than 8
     */
    private static void checkargumentlength(String[] args)
    {
        Boolean problem=false;
        if(args.length<8&&args.length!=1)
        {
            //throw new Exception();
            problem=true;
        }
        else if(args.length>15)
        {
            problem=true;
        }
        if(problem)
        {
            System.err.println("Please check command line arguments and try again");
            exit(1);
        }
    }

//    public static void main(String... args) {
//        String hostName = null;
//        String portString = null;
//        String key = null;
//        String value = null;
//
//        for (String arg : args) {
//            if (hostName == null) {
//                hostName = arg;
//
//            } else if ( portString == null) {
//                portString = arg;
//
//            } else if (key == null) {
//                key = arg;
//
//            } else if (value == null) {
//                value = arg;
//
//            } else {
//                usage("Extraneous command line argument: " + arg);
//            }
//        }
//
//        if (hostName == null) {
//            usage( MISSING_ARGS );
//
//        } else if ( portString == null) {
//            usage( "Missing port" );
//        }
//
//        int port;
//        try {
//            port = Integer.parseInt( portString );
//
//        } catch (NumberFormatException ex) {
//            usage("Port \"" + portString + "\" must be an integer");
//            return;
//        }
//
//        AppointmentBookRestClient client = new AppointmentBookRestClient(hostName, port);
//
//        HttpRequestHelper.Response response;
//        try {
//            if (key == null) {
//                // Print all key/value pairs
//                response = client.getAllKeysAndValues();
//
//            } else if (value == null) {
//                // Print all values of key
//                response = client.getValues(key);
//
//            } else {
//                // Post the key/value pair
//                response = client.addKeyValuePair(key, value);
//            }
//
//            checkResponseCode( HttpURLConnection.HTTP_OK, response);
//
//        } catch ( IOException ex ) {
//            error("While contacting server: " + ex);
//            return;
//        }
//
//        System.out.println(response.getContent());
//
//        System.exit(0);
//    }

    /**
     * Makes sure that the give response has the expected HTTP status code
     * @param code The expected status code
     * @param response The response from the server
     */
    private static void checkResponseCode( int code, HttpRequestHelper.Response response )
    {
        if (response.getCode() != code) {
            error(String.format("Expected HTTP code %d, got code %d.\n\n%s", code,
                                response.getCode(), response.getContent()));
        }
    }

    private static void error( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);

        exit(1);
    }

    /**
     * Prints usage information for this program and exits
     * @param message An error message to print
     */
    private static void usage( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);
        err.println();
        err.println("usage: java Project4 host port [key] [value]");
        err.println("  host    Host of web server");
        err.println("  port    Port of web server");
        err.println("  key     Key to query");
        err.println("  value   Value to add to server");
        err.println();
        err.println("This simple program posts key/value pairs to the server");
        err.println("If no value is specified, then all values are printed");
        err.println("If no key is specified, all key/value pairs are printed");
        err.println();

        exit(1);
    }
}