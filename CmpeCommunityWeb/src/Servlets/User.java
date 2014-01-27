package Servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Tables.UserTable;
import UtilityPack.HashString;

import drivers.*;

public class User extends ServletBase {

	public User(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}
	
	public void recommended() throws ServletException, IOException{
		UserTable user = getCurrentUser();
		if(user == null)
			return;
		UserTable[] users = UserDriver.getRecommendedByUserId(user.getId());
		request.setAttribute("users", users);
		request.getRequestDispatcher("/recommended.jsp").forward(request, response);
	}

	public void login() throws ServletException, IOException{
		request.getRequestDispatcher("/login.jsp").forward(request, response);
	}
	
	public void loginAction() throws ServletException, IOException{
		String email = request.getParameter("email_login");
		String password = request.getParameter("password_login");
		if(email==null || password==null )
			request.getRequestDispatcher("/User/login").forward(request, response);
		if(UserDriver.isCredentialsValid(email, password)){
			//TODO record login to session
			UserTable user = drivers.UserDriver.getByEmail(email);
			 HttpSession session = request.getSession();
			 session.setAttribute("user_info",user);
			response.sendRedirect("/CmpeCommunityWeb/Profile/index");
			//request.getRequestDispatcher("/Profile/index").forward(request, response);
		}
		else{
			request.setAttribute("loginFailed", 1);
			request.getRequestDispatcher("/User/loginAction").forward(request, response);
		}
	}
	
	public void registerAction() throws Exception{
		String email = request.getParameter("email_signup");
		String name = request.getParameter("first-name")+" "+request.getParameter("last-name");
		String password = request.getParameter("password_signup");
		String tags =  request.getParameter("hidden-tags");
		if (email==null) {
			return;
		}
		if(password.equals(request.getParameter("re-password"))){
			//TODO passwords does not match
		}
		//TODO real values for dates
		String birthDate = "1970-01-01";
		String registerDate = "1970-01-01";
		Tables.UserTable user = null;
		Tables.TagsTable[] tagsTable = null;
	
		try {
			user = new Tables.UserTable(email, name, HashString.encrypt(password), birthDate, registerDate);
			String[] tags_arr = tags.split(",");
			tagsTable = new Tables.TagsTable[tags_arr.length];
			for (int i = 0;i<tags_arr.length;i++){
				tagsTable[i] = new Tables.TagsTable(tags_arr[i]);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		if(UserDriver.createUser(user) && TagsDriver.createTags(tagsTable,user)){
			request.setAttribute("user_info",user);
			request.getRequestDispatcher("/Profile/index").forward(request, response);
		} else{
			request.getRequestDispatcher("/User/loginAction").forward(request, response);
			System.err.println("Error during user creation");
			//TODO error
		}
	}
	
	public void logout() throws ServletException, IOException{
		//TODO logout action with session
		request.getSession().removeAttribute("user_info");
		request.getRequestDispatcher("/login.jsp").forward(request, response);
	}
}
