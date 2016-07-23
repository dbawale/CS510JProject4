package edu.pdx.cs410J.dbawale;

import org.junit.Ignore;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

/**
 * A unit test for the {@link AppointmentBookServlet}.  It uses mockito to
 * provide mock http requests and responses.
 */
public class AppointmentBookServletTest {

  @Test
  @Ignore
  public void initiallyServletContainsNoKeyValueMappings() throws ServletException, IOException {
    AppointmentBookServlet servlet = new AppointmentBookServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);

    when(response.getWriter()).thenReturn(pw);

    servlet.doGet(request, response);

    int expectedMappings = 0;
    verify(pw).println(Messages.getMappingCount(expectedMappings));
    verify(response).setStatus(HttpServletResponse.SC_OK);
  }

  @Test
  @Ignore
  public void addOneMapping() throws ServletException, IOException {
    AppointmentBookServlet servlet = new AppointmentBookServlet();

    String testKey = "TEST KEY";
    String testValue = "TEST VALUE";

    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter("key")).thenReturn(testKey);
    when(request.getParameter("value")).thenReturn(testValue);

    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);

    when(response.getWriter()).thenReturn(pw);

    servlet.doPost(request, response);
    verify(pw).println(Messages.mappedKeyValue(testKey, testValue));
    verify(response).setStatus(HttpServletResponse.SC_OK);

    assertThat(servlet.getValueForKey(testKey), equalTo(testValue));
  }

  @Test
  @Ignore
  public void testGetWithJustOwner() throws IOException, ServletException {
    AppointmentBookServlet servlet = new AppointmentBookServlet();
    String owner = "deven";
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter("owner")).thenReturn(owner);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);
    when(response.getWriter()).thenReturn(pw);
    servlet.doGet(request,response);
    verify(pw).println("Got Just owner: " + owner);
    verify(response).setStatus(HttpServletResponse.SC_OK);
  }

  @Test
  @Ignore
  public void testGetWithOwnerAndTime() throws IOException, ServletException {
    AppointmentBookServlet servlet = new AppointmentBookServlet();
    String owner = "deven";
    String beginTime = "8:10";
    String endTime = "9:10";
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter("owner")).thenReturn(owner);
    when(request.getParameter("beginTime")).thenReturn(beginTime);
    when(request.getParameter("endTime")).thenReturn(endTime);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);
    when(response.getWriter()).thenReturn(pw);
    servlet.doGet(request,response);
    verify(pw).println("Got owner, beginTime and endTime: " + owner + beginTime + endTime);
    verify(response).setStatus(HttpServletResponse.SC_OK);
  }

  @Test
  public void testPostWithAllParameters() throws IOException, ServletException {
    AppointmentBookServlet servlet = new AppointmentBookServlet();
    String owner = "deven";
    String beginTime = "7/22/2016 9:12 PM";
    String endTime = "7/22/2016 10:12 PM";
    String description = "lunch";
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter("owner")).thenReturn(owner);
    when(request.getParameter("beginTime")).thenReturn(beginTime);
    when(request.getParameter("endTime")).thenReturn(endTime);
    when(request.getParameter("description")).thenReturn(description);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);
    when(response.getWriter()).thenReturn(pw);
    servlet.doPost(request,response);
    verify(response).setStatus(HttpServletResponse.SC_OK);
    verify(pw).println("Added appointment!");
  }
}
