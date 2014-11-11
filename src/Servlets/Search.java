package Servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Tables.UserTable;

public class Search extends ServletBase {
	public Search(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}
	
	public void index() throws IOException, ServletException{
		UserTable user = getCurrentUser();
		if(user == null){
			request.getRequestDispatcher("/User/login").forward(request, response);
			return;
		}
		
		request.setAttribute("user", user);
		request.getRequestDispatcher("/layout/header.jsp").include(request, response);
		request.getRequestDispatcher("/Search.jsp").include(request, response);
		request.getRequestDispatcher("/layout/footer.jsp").include(request, response);
	}
}
