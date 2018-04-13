package org.tempuri;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Servlet implementation class WeatherReport
 */
@WebServlet("/WeatherReport")
public class WeatherReport extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WeatherReport() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String referrer = request.getHeader("referer");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String state = request.getParameter("nameOfState");
		
		String city = request.getParameter("city_name");
		String[] typeWeather = request.getParameterValues("typeOfWeatherReport");
		String resp = null;
		IServiceProxy obj = new IServiceProxy();
		if(typeWeather != null && typeWeather.length == 2 ){
			resp = obj.getWeather_tenDays(city, state, true);
			out.println("<!DOCTYPE html>");
			out.println("<html>");
			out.println("<body>");
			out.println("<center>");
			try {
				JSONObject jObj = (JSONObject) new JSONTokener(resp).nextValue();
				JSONObject temp = jObj.getJSONObject("forecast").getJSONObject("simpleforecast");
				JSONArray records = temp.getJSONArray("forecastday");
				out.println("<table border=\"1\" ><tr><th>Day</th>");
				out.println("<th>Image</th>");
				out.println("<th>High</th>");
				out.println("<th>Low</th>");
				out.println("<th>Conditions</th></tr>");
				for (int i = 0; i < records.length(); ++i) {
					JSONObject tempFDay = records.getJSONObject(i);
					JSONObject tempLoop = tempFDay.getJSONObject("date");
					String s = tempLoop.getString("pretty");
					out.println("<tr><td align=\"center\">" + s + "</td>");
					s = tempFDay.getString("icon_url");
					out.println("<td align=\"center\"><img src=\"" + s + "\"/></td>");
					tempLoop = tempFDay.getJSONObject("high");
					s = tempLoop.getString("fahrenheit");
					out.println("<td align=\"center\">" + s + " F (");
				    s = tempLoop.getString("celsius");
				    out.println(s+" C)</td>");
				    tempLoop = tempFDay.getJSONObject("low");
					s = tempLoop.getString("fahrenheit");
					out.println("<td align=\"center\">" + s + " F (");
				    s = tempLoop.getString("celsius");
				    out.println(s+" C)</td>");
				    s = tempFDay.getString("conditions");
				    out.println("<td align=\"center\">"+s+"</td></tr>");
				}
				out.println("</table>");
				out.println("<br><br><a href=\""+referrer+"\">Go Back!</a>");
				out.println("</center></body></html>");
			}
			catch(NullPointerException e) {
				e.printStackTrace();
				out.println("Provide enter city name!");
				out.println("<br><br><a href=\""+referrer+"\">Go Back!</a>");
				out.println("</center></body></html>");
			}
			catch(Exception e) {
				e.printStackTrace();
				out.println("Provide valid city name!");
				out.println("<br><br><a href=\""+referrer+"\">Go Back!</a>");
				out.println("</center></body></html>");
			}
		}
		else if(typeWeather != null && typeWeather.length == 1){
			if(typeWeather[0].equals("hourW")){
				resp = obj.getWeather_hourly(city, state, true);
				//html table init
				out.println("<!DOCTYPE html>");
				out.println("<html>");
				out.println("<body>");
				out.println("<center>");
				try {
					JSONObject jObj = (JSONObject) new JSONTokener(resp).nextValue();
					JSONArray records = jObj.getJSONArray("hourly_forecast");
					out.println("<table border=\"1\" ><tr><th>Time</th>");
					out.println("<th>Image</th>");
					out.println("<th>Temperature</th>");
					out.println("<th>Feels Like</th>");
					out.println("<th>Relative Humidity</th>");
					out.println("<th>Prediction</th></tr>");
					for (int i = 0; i < records.length(); ++i) {
					    JSONObject temp = records.getJSONObject(i);
					    JSONObject tempJsonObj = temp.getJSONObject("FCTTIME");
					    String s = tempJsonObj.getString("pretty");
					    out.println("<tr><td align=\"center\">" + s + "</td>");
					    s = temp.getString("icon_url");
					    out.println("<td align=\"center\"><img src=\"" + s + "\"/></td>");
					    tempJsonObj = temp.getJSONObject("temp");
					    s = tempJsonObj.getString("english");
					    out.println("<td align=\"center\">" + s + "F (");
					    s = tempJsonObj.getString("metric");
					    out.println(s+"C)</td>");
					    tempJsonObj = temp.getJSONObject("feelslike");
					    s = tempJsonObj.getString("english");
					    out.println("<td align=\"center\">" + s + " F (");
					    s = tempJsonObj.getString("metric");
					    out.println(s+" C)</td>");
					    s = temp.getString("humidity");
					    out.println("<td align=\"center\">"+s+"%</td>");
					    s = temp.getString("wx");
					    out.println("<td align=\"center\">"+s+"</td></tr>");
					}
					out.println("</table>");
					out.println("<br><br><a href=\""+referrer+"\">Go Back!</a>");
					out.println("</center></body></html>");
				}
				catch(NullPointerException e) {
					e.printStackTrace();
					out.println("Provide enter city name!");
					out.println("<br><br><a href=\""+referrer+"\">Go Back!</a>");
					out.println("</center></body></html>");
				}
				catch(Exception e) {
					e.printStackTrace();
					out.println("Provide valid city name!");
					out.println("<br><br><a href=\""+referrer+"\">Go Back!</a>");
					out.println("</center></body></html>");
				}
			}
			else {
				resp = obj.getWeather_tenDays(city, state, true);
				out.println("<!DOCTYPE html>");
				out.println("<html>");
				out.println("<body>");
				out.println("<center>");
				try {
					JSONObject jObj = (JSONObject) new JSONTokener(resp).nextValue();
					JSONObject temp = jObj.getJSONObject("forecast").getJSONObject("simpleforecast");
					JSONArray records = temp.getJSONArray("forecastday");
					out.println("<table border=\"1\" ><tr><th>Day</th>");
					out.println("<th>Image</th>");
					out.println("<th>High</th>");
					out.println("<th>Low</th>");
					out.println("<th>Conditions</th></tr>");
					for (int i = 0; i < records.length(); ++i) {
						JSONObject tempFDay = records.getJSONObject(i);
						JSONObject tempLoop = tempFDay.getJSONObject("date");
						String s = tempLoop.getString("pretty");
						out.println("<tr><td align=\"center\">" + s + "</td>");
						s = tempFDay.getString("icon_url");
						out.println("<td align=\"center\"><img src=\"" + s + "\"/></td>");
						tempLoop = tempFDay.getJSONObject("high");
						s = tempLoop.getString("fahrenheit");
						out.println("<td align=\"center\">" + s + " F (");
					    s = tempLoop.getString("celsius");
					    out.println(s+" C)</td>");
					    tempLoop = tempFDay.getJSONObject("low");
						s = tempLoop.getString("fahrenheit");
						out.println("<td align=\"center\">" + s + " F (");
					    s = tempLoop.getString("celsius");
					    out.println(s+" C)</td>");
					    s = tempFDay.getString("conditions");
					    out.println("<td align=\"center\">"+s+"</td></tr>");
					}
					out.println("</table>");
					out.println("<br><br><a href=\""+referrer+"\">Go Back!</a>");
					out.println("</center></body></html>");
				}
				catch(NullPointerException e) {
					e.printStackTrace();
					out.println("Provide enter city name!");
					out.println("<br><br><a href=\""+referrer+"\">Go Back!</a>");
					out.println("</center></body></html>");
				}
				catch(Exception e) {
					e.printStackTrace();
					out.println("Provide valid city name!");
					out.println("<br><br><a href=\""+referrer+"\">Go Back!</a>");
					out.println("</center></body></html>");
				}
			}
		}
		else{
			resp = obj.getWeather(city, state);
			System.out.println(resp);
			out.println("<!DOCTYPE html>");
			out.println("<html>");
			out.println("<body>");
			out.println("<center>");
			try {
				JSONObject jObj = (JSONObject) new JSONTokener(resp).nextValue();
				JSONObject currentObs = jObj.getJSONObject("current_observation");
				
				out.println("<table border=\"1\" ><tr><th>Weather</th>");
				out.println("<th>Temperature</th>");
				out.println("<th>Feels Like</th>");
				out.println("<th>Relative Humidity</th>");
				out.println("<th>Wind</th>");
				out.println("<th>Wind Direction</th></tr>");
				String temp = currentObs.getString("icon_url");
				
				out.println("<tr><td align=\"center\"><img src=\"" + temp + "\"/></td>");
				temp = currentObs.getString("temperature_string");
				out.println("<td align=\"center\">" + temp + "</td>");
				temp = currentObs.getString("feelslike_string");
				out.println("<td align=\"center\">" + temp + "</td>");
				temp = currentObs.getString("relative_humidity");
				out.println("<td align=\"center\">" + temp + "</td>");
				temp = currentObs.getString("wind_string");
				out.println("<td align=\"center\">" + temp + "</td>");
				temp = currentObs.getString("wind_dir");
				out.println("<td align=\"center\">" + temp + "</td></tr></table>");
				out.println("<br><br><a href=\""+referrer+"\">Go Back!</a>");
				out.println("</center></body></html>");
				
			} catch(NullPointerException e) {
				e.printStackTrace();
				out.println("Provide enter city name!");
				out.println("<br><br><a href=\""+referrer+"\">Go Back!</a>");
				out.println("</center></body></html>");
			}
			catch(Exception e) {
				e.printStackTrace();
				out.println("Provide valid city name!");
				out.println("<br><br><a href=\""+referrer+"\">Go Back!</a>");
				out.println("</center></body></html>");
			}
		}
	}

}
