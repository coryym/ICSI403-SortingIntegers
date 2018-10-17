package csi403;


// Import required java libraries
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
import org.json.*;


// Extend HttpServlet class
public class SortingRationalNumbers extends HttpServlet {
	

  // Standard servlet method 
  public void init() throws ServletException
  {
      // Do any required initialization here - likely none
  }

  // Standard servlet method - we will handle a POST operation
  public void doPost(HttpServletRequest request,
                    HttpServletResponse response)
            throws ServletException, IOException
  {
	  try {
      doService(request, response);
	  }catch(Exception EX) {
	      // Set response content type to be JSON
	      //response.setContentType("application/json");
	      // Send back the response JSON message
	      PrintWriter out = response.getWriter();
	      out.println("{ \"message\" : \"Malformed JSON\" } "); 
	  }
  }

  // Standard servlet method - we will not respond to GET
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
            throws ServletException, IOException
  {
      // Set response content type and return an error message
      response.setContentType("application/json");
      PrintWriter out = response.getWriter();
      out.println("{ 'message' : 'Use POST!'}");
  }


  // Our main worker method
  // Parses messages e.g. {"inList" : [5, 32, 3, 12]}
  // Returns the list reversed.   
  private void doService(HttpServletRequest request,
                    HttpServletResponse response)
            throws ServletException, IOException, JSONException
  {
      // Get received JSON data from HTTP request
      BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
      String jsonStr = "";
      if(br != null){
          jsonStr = br.readLine();
      }
      
      // Create JsonReader object
      //StringReader strReader = new StringReader(jsonStr);
      //JSONReader reader = Json.createReader(strReader);

      // Get the singular JSON object (name:value pair) in this message.    
      JSONObject obj = new JSONObject(jsonStr);
      // From the object get the array named "inList"
      JSONArray inArray = obj.getJSONArray("inList");

      // Reverse the data in the list
      JSONArray outArrayBuilder = new JSONArray();
      /*for (int i = inArray.size() - 1; i >= 0; i--) {
          outArrayBuilder.add(inArray.getInt(i)); 
      }*/
      
      boolean flag = false;
      
      double array[] = new double[inArray.length()];
      
      for (int i = 0; i < inArray.length(); i++) {
    	  array[i] = (inArray).getDouble(i);
      }
      
      for (int i=0; i < inArray.length(); i++) {
    	  if(array[i] % 1 != 0) {
    		  flag = true;
    	  }
      }

      if(!flag) {
    	  long startTime=System.nanoTime();
    	  double temp = 0;
    	  for (int i=0; i < array.length; i++) {
    		  for(int j = 1; j < array.length; j++) {
    			  if(array[j-1] > array[j]) {
    				  temp = array[j-1];
    				  array[j-1] = array[j];
    				  array[j] = temp;
    			  }
    		  }
    	  }
    	  long endTime = System.nanoTime();
          
          for (int i=0; i < array.length; i++) {
        	  outArrayBuilder.put(array[i]);
          }
          
          long finalTime = endTime - startTime;
          
          // Set response content type to be JSON
          // response.setContentType("application/json");
          // Send back the response JSON message
          PrintWriter out = response.getWriter();
          out.println("{ \"outList\" : " + outArrayBuilder.toString() + "}");
          out.println("\"Algorithm\": \"BubbleSort\"");
          out.println("\"timeMS\": " + finalTime);
          
      }else {
    	  PrintWriter out = response.getWriter();
	      out.println("{ \"message\" : \"Malformed JSON\" } "); 
    	  
      }
  }

    
  // Standard Servlet method
  public void destroy()
  {
      // Do any required tear-down here, likely nothing.
  }
}

