package Servlets;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Tables.EventTable;
import Tables.UserTable;
import drivers.EventDriver;
import drivers.UserDriver;


public class Events extends ServletBase{
	
	public Events(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}
	
	public void details(Integer eventId) throws ServletException, IOException{
		UserTable user = getCurrentUser();
		if(user == null){
			request.getRequestDispatcher("/User/login").forward(request, response);
			return;
		}
		request.setAttribute("user", user);
		
		EventTable event = EventDriver.getEvent(eventId);
		if(event == null)
			return;
		request.setAttribute("event", event);
		
		UserTable[] users = UserDriver.getUsersByEvent(event.getId());
		request.setAttribute("users", users);
		
		UserTable creator = UserDriver.getById(event.getUserId());
		request.setAttribute("creator", creator);
		
		request.setAttribute("attending", EventDriver.isAttending(user.getId(), event.getId()));
		
        request.getRequestDispatcher("/layout/header.jsp").include(request, response);
        request.getRequestDispatcher("/EventDetails.jsp").include(request, response);
        request.getRequestDispatcher("/layout/footer.jsp").include(request, response);
	}
	
	public void attend(Integer id) throws IOException{
		response.setContentType("application/json");
		UserTable user = getCurrentUser();
		if(user == null){
			response.getOutputStream().println("{\"success\": false, \"error\": \"need_login\"}");
			return;
		}
		
		if(EventDriver.getEvent(id) == null)
			response.getOutputStream().println("{\"success\": false, \"error\": \"unknown\"}");
		else if(EventDriver.insertUsersInEvent(user.getId(), id))
			response.getOutputStream().println("{\"success\": true}");
		else
			response.getOutputStream().println("{\"success\": false, \"error\": \"unknown\"}");
	}
	
	public void unAttend(Integer id) throws IOException{
		response.setContentType("application/json");
		UserTable user = getCurrentUser();
		if(user == null){
			response.getOutputStream().println("{\"success\": false, \"error\": \"need_login\"}");
			return;
		}
		
		if(EventDriver.getEvent(id) == null)
			response.getOutputStream().println("{\"success\": false, \"error\": \"unknown\"}");
		else if(EventDriver.deleteUsersInEvent(user.getId(), id))
			response.getOutputStream().println("{\"success\": true}");
		else
			response.getOutputStream().println("{\"success\": false, \"error\": \"unknown\"}");
	}
	
	public void myEvents(Integer userId)throws IOException, ServletException{
		if(getCurrentUser() == null){
			request.getRequestDispatcher("/User/login").forward(request, response);
			return;
		}
		
		if(userId == 0)
			userId = getCurrentUser().getId();

		request.setAttribute("type", "myEvents");

		EventTable[] events=EventDriver.getUserEvent(userId);
		Map<Integer, UserTable> users = new TreeMap<Integer, UserTable>();
		for(EventTable event : events){
			if(users.get(event.getUserId()) == null){
				users.put(event.getUserId(), UserDriver.getById(event.getUserId()));
			}
		}

		request.setAttribute("user",getCurrentUser());
		request.setAttribute("users", users);
        request.setAttribute("events", events);
        request.getRequestDispatcher("/EventList.jsp").include(request, response);
	}
	
	public void attendedEvents(Integer userId)throws IOException, ServletException{
		if(getCurrentUser() == null){
			request.getRequestDispatcher("/User/login").forward(request, response);
			return;
		}
		
		request.setAttribute("type", "");
        EventTable[] events=EventDriver.getUserJoinedEvent(userId);
		Map<Integer, UserTable> users = new TreeMap<Integer, UserTable>();
		for(EventTable event : events){
			if(users.get(event.getUserId()) == null){
				users.put(event.getUserId(), UserDriver.getById(event.getUserId()));
			}
		}

		request.setAttribute("user",getCurrentUser());
		request.setAttribute("users", users);
        request.setAttribute("events", events);
		request.getRequestDispatcher("/EventList.jsp").include(request, response);
		
	}
	public void addEvents()throws IOException, ServletException{
		response.setContentType("application/json");
		UserTable user = getCurrentUser();
		if(user == null){
			response.getOutputStream().println("{\"success\": false, \"error\": \"need_login\"}");
			return;
		}
		
		String time=request.getParameter("datetime");
		String description=request.getParameter("description");
		String place=request.getParameter("place");
		if(EventDriver.createEvent(user.getId(), description, place, time))
			response.getOutputStream().println("{\"success\": true}");
		else
			response.getOutputStream().println("{\"success\": false, \"error\": \"unknown\"}");
	}

}

