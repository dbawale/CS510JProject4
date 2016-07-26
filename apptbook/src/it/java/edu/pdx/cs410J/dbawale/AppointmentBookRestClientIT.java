package edu.pdx.cs410J.dbawale;

import edu.pdx.cs410J.web.HttpRequestHelper.Response;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

/**
 * Integration test that tests the REST calls made by {@link AppointmentBookRestClient}
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppointmentBookRestClientIT {
  private static final String HOSTNAME = "localhost";
  private static final String PORT = System.getProperty("http.port", "8080");

  private AppointmentBookRestClient newAppointmentBookRestClient() {
    int port = Integer.parseInt(PORT);
    return new AppointmentBookRestClient(HOSTNAME, port);
  }

//  @Test
//  @Ignore
//  public void test0RemoveAllMappings() throws IOException {
//    AppointmentBookRestClient client = newAppointmentBookRestClient();
//    Response response = client.removeAllMappings();
//    assertThat(response.getContent(), response.getCode(), equalTo(200));
//  }
//
//  @Test
//  @Ignore
//  public void test1EmptyServerContainsNoMappings() throws IOException {
//    AppointmentBookRestClient client = newAppointmentBookRestClient();
//    Response response = client.getAllKeysAndValues();
//    String content = response.getContent();
//    assertThat(content, response.getCode(), equalTo(200));
//    assertThat(content, containsString(Messages.getMappingCount(0)));
//  }
//
//  @Test
//  @Ignore
//  public void test2AddOneKeyValuePair() throws IOException {
//    AppointmentBookRestClient client = newAppointmentBookRestClient();
//    String testKey = "TEST KEY";
//    String testValue = "TEST VALUE";
//    Response response = client.addKeyValuePair(testKey, testValue);
//    String content = response.getContent();
//    assertThat(content, response.getCode(), equalTo(200));
//    assertThat(content, containsString(Messages.mappedKeyValue(testKey, testValue)));
//  }
//
//  @Test
//  @Ignore
//  public void missingRequiredParameterReturnsPreconditionFailed() throws IOException {
//    AppointmentBookRestClient client = newAppointmentBookRestClient();
//    Response response = client.postToMyURL();
//    assertThat(response.getContent(), containsString(Messages.missingRequiredParameter("key")));
//    assertThat(response.getCode(), equalTo(HttpURLConnection.HTTP_PRECON_FAILED));
//  }

  @Test
  public void testPutReturnsCorrectResponseCode() throws IOException {
    AppointmentBookRestClient client = newAppointmentBookRestClient();
    try {
      Response response = client.postValuesToServer("deven", "lunch", "7/24/2016 4:15 pm", "7/24/2016 5:15 pm");
      assertThat(response.getCode(), equalTo(HttpURLConnection.HTTP_OK));
      assertThat(response.getContent(), containsString("Added appointment!"));
    }
    catch (ConnectException e)
    {
      System.err.println("Could not connect to server " + HOSTNAME + " on " + PORT+ ". Are you sure you're running the server?");
      System.err.println("System sent the message: " + e.getMessage());
    }
    catch (IllegalArgumentException e)
    {
      System.err.println("Could not connect to server " + HOSTNAME + " on " + PORT+ ". Are you sure you're running the server?");
      System.err.println("System sent the message: " + e.getMessage());
    }
  }

  @Test
  public void testPutWithIncorrectDateTimeFormatReturnsAppropriateError() throws IOException{
    AppointmentBookRestClient client = newAppointmentBookRestClient();
    Response response = client.postValuesToServer("deven","lunch","7/24f/2016 4:15 pm","7/24/2016 5:15 pm");
    assertThat(response.getCode(),equalTo(HttpURLConnection.HTTP_BAD_REQUEST));
  }

  @Test
  public void testPutWithNullReturnsRequiredParameterError() throws IOException{
    AppointmentBookRestClient client = newAppointmentBookRestClient();
    Response response = client.postValuesToServer("deven","","7/24f/2016 4:15 pm","7/24/2016 5:15 pm");
    assertThat(response.getCode(),equalTo(HttpURLConnection.HTTP_PRECON_FAILED));
    System.err.println(response.getContent());
  }

  @Test
  public void testGetAndPutTogether() throws IOException{
    AppointmentBookRestClient client = newAppointmentBookRestClient();
    Response response = client.postValuesToServer("deven","lunch","7/24/2016 4:15 pm","7/24/2016 5:15 pm");
    assertThat(response.getCode(),equalTo(HttpURLConnection.HTTP_OK));
    assertThat(response.getContent(),containsString("Added appointment!"));
    response = client.getSearchValueResponse("deven","7/24/2016 4:15 pm","7/24/2016 4:15 pm");
    assertThat(response.getCode(),equalTo(HttpURLConnection.HTTP_OK));
    System.out.println(response.getContent());
  }

  @Test
  public void testAppointmentWithAnotherOwner() throws IOException{
    AppointmentBookRestClient client = newAppointmentBookRestClient();
    Response response = client.postValuesToServer("deven","lunch","7/24/2016 4:15 pm","7/24/2016 5:15 pm");
    assertThat(response.getCode(),equalTo(HttpURLConnection.HTTP_OK));
    assertThat(response.getContent(),containsString("Added appointment!"));
    response = client.getSearchValueResponse("deven bawale","7/24/2016 4:15 pm","7/24/2016 4:15 pm");
    assertThat(response.getCode(),equalTo(HttpURLConnection.HTTP_BAD_REQUEST));
    assertThat(response.getContent(),equalTo("Appointment book for deven bawale not available on server"));
  }

}
