package Servlets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Tables.UserTable;

public abstract class ServletBase {
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	
	public ServletBase(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}
	
	public UserTable getCurrentUser(){
		return (UserTable)request.getSession().getAttribute("user_info");
	}
	public void setCurrentUser(UserTable user) {
		request.getSession().removeAttribute("user_info");
		request.getSession().setAttribute("user_info",user);
	}
}
