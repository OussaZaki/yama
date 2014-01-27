package Servlets;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Tables.ChoiceTable;
import Tables.SurveyTable;
import Tables.UserTable;
import drivers.SurveyDriver;

public class Surveys extends ServletBase {

	public Surveys(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	public void vote(Integer surveyId, Integer choiceId) throws IOException{
		response.setContentType("application/json");
		UserTable user = getCurrentUser();
		if(user == null){
			response.getOutputStream().println("{\"success\": false, \"error\": \"need_login\"}");
			return;
		}
		if(SurveyDriver.vote(user.getId(), surveyId, choiceId)){
			SurveyTable survey = SurveyDriver.getById(surveyId);
			String output = "{ \"success\": true, ";
			output = output + "\"question\": \""+survey.getQuestion().replace('"', ' ')+"\", ";
			output = output + "\"choices\": [";
			ChoiceTable[] choices = survey.getChoiceTable();
			int N = choices.length-1;
			for(int i=0; i<N; i++){
				output = output + "{";
					output = output + "\"choice\": \""+choices[i].getChoice().replace('"', ' ')+"\", ";
					output = output + "\"vote\": "+choices[i].getVotes()+", ";
					output = output + "\"percentage\": "+choices[i].getPercentageVotes()+" ";
				output = output + "}, ";
			}
			if(N>=0){
				output = output + "{";
					output = output + "\"choice\": \""+choices[N].getChoice().replace('"', ' ')+"\", ";
					output = output + "\"vote\": "+choices[N].getVotes()+", ";
					output = output + "\"percentage\": "+choices[N].getPercentageVotes()+" ";
				output = output + "}";
			}
			output = output + "]";
			output = output + "}";
			response.getOutputStream().println(output);
		}
		else
			response.getOutputStream().println("{\"success\": false, \"error\": \"unknown\"}");
	}

	public void addSurvey(Integer userId) throws IOException, ServletException {
		response.setContentType("application/json");
		UserTable user = getCurrentUser();
		if(user == null){
			response.getOutputStream().println("{\"success\": false, \"error\": \"need_login\"}");
			return;
		}
		request.setAttribute("user",user);
		request.setAttribute("type","mySurvey");
		String choices = request.getParameter("choices");
		String choiceList[]=choices.split(",");
		if (choiceList==null) {
			response.getOutputStream().println("{\"success\": false, \"error\": \"unknown\"}");
			return;
		}	
		String question=request.getParameter("question");
		if (question.equals("") || choiceList.length<2) {
			return;
		}	
		//get parameters from HTTP POST
		 //String[] choiceList = choices.toArray(new String[choices.size()]);
		 SurveyDriver.createSurvey(userId, question, choiceList);
		//if post body is not null then add the post and tag users or give an error message, which is not expected to occur.
		//request.getRequestDispatcher("/SurveyList.jsp").include(request, response);	
		response.getOutputStream().println("{\"success\": true}");
	}
	
	public void loadMySurveys(Integer userId) throws IOException, ServletException { 
		UserTable user = getCurrentUser();
		if(user == null){
			response.getOutputStream().println("<script>window.location.reload()</script>");
			return;
		}
		request.setAttribute("user",user);
		request.setAttribute("type","mySurvey");
		
		SurveyTable[] surveyList = SurveyDriver.getUserSurvey(userId);
		request.setAttribute("surveyList", surveyList);
		Set<Integer> joinedSurveys = new TreeSet<Integer>();
		for(int i : SurveyDriver.getUserJoinedSurveyIds(user.getId()))
			joinedSurveys.add(i);
		request.setAttribute("joinedSurveys", joinedSurveys);
		
		request.getRequestDispatcher("/SurveyList.jsp").include(request, response);
	}
	
	public void loadCompletedSurveys(Integer userId) throws IOException, ServletException {
		UserTable user = getCurrentUser();
		if(user == null){
			response.getOutputStream().println("<script>window.location.reload()</script>");
			return;
		}
		request.setAttribute("user",user);
		request.setAttribute("type","myCompletedSurvey");
		
		SurveyTable[] surveyList = SurveyDriver.getUserJoinedSurvey(userId);
		request.setAttribute("surveyList", surveyList);
		Set<Integer> joinedSurveys = new TreeSet<Integer>();
		for(int i : SurveyDriver.getUserJoinedSurveyIds(user.getId()))
			joinedSurveys.add(i);
		request.setAttribute("joinedSurveys", joinedSurveys);
		
		request.getRequestDispatcher("/SurveyList.jsp").include(request, response);
	}
	/**
	 * Gets posts and users[] paramaters from HTTP POST,
	 * Creates a post in database table and tags users given in users[]
	 * in created post.
	 * Response is either 'success' or and error message written in a single line.
	 * @throws IOException
	 */
	
}
