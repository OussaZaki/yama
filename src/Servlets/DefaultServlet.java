package Servlets;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DBPack.DBStatement;

/**
 * Servlet implementation class defaultServlet
 */
@WebServlet("/default")
public class DefaultServlet extends HttpServlet {

	//TODO fix indexes removing YamaWeb from url
	//[0] => "",
	//[1] => "YamaWeb",
	//[2] => "className",
	//[3] => "methodName"
	private static final int CLASS_NAME_INDEX = 2;
	private static final int METHOD_NAME_INDEX = 3;
	private static final int PARAMETER_START = 4;
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DefaultServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    private boolean doesMethodMatch(Method method, String[] parts){
		Class<?>[] parameterTypes = method.getParameterTypes();
		if(parameterTypes.length != parts.length-PARAMETER_START)
			return false;
		int i=0;
		for (Class<?> p : parameterTypes) {
			String name = p.getName();
			try{
				if(name.equalsIgnoreCase("java.lang.Integer"))
					Integer.parseInt(parts[PARAMETER_START+i]);
				else if(name.equalsIgnoreCase("java.lang.String"))
					continue;
				else if(name.equalsIgnoreCase("java.lang.Double"))
					Double.parseDouble(parts[PARAMETER_START+i]);
				else
					return false;
			} catch(NumberFormatException e){
				return false;
			}
			i++;
		}
		return true;
    }
    
    private Object[] convertArgs(Method method, String[] args){
    	Object[] objects = new Object[args.length];
		Class<?>[] parameterTypes = method.getParameterTypes();
		int i=0;
		for (Class<?> p : parameterTypes) {
			String name = p.getName();
			try{
				if(name.equalsIgnoreCase("java.lang.Integer"))
					objects[i] = Integer.parseInt(args[i]);
				else if(name.equalsIgnoreCase("java.lang.String"))
					objects[i] = args[i];
				else if(name.equalsIgnoreCase("java.lang.Double"))
					objects[i] = Double.parseDouble(args[i]);
			} catch(NumberFormatException e){
				System.err.println("If this message is shown, there is something wrong in doesMethodMatch method " +
						"in defaultServlet class!");
				objects[i] = null;
			}
			i++;
		}
		return objects;
    }
    
    private void redirect(HttpServletRequest request, HttpServletResponse response){
		String[] parts = request.getRequestURI().split("/");
		if(parts.length<METHOD_NAME_INDEX+1){
			parts = new String[METHOD_NAME_INDEX+1];
			int i=0;
			for (String string : request.getRequestURI().split("/"))
				parts[i++] = string;
			parts[METHOD_NAME_INDEX] = "index";
		}
		Object object = null;
		Method method = null;
		try {
			Class<?> dispatchClass = Class.forName("Servlets."+parts[CLASS_NAME_INDEX]);
			Method[] methods = dispatchClass.getDeclaredMethods();
			for (Method m : methods) {
				if(m.getName().equalsIgnoreCase(parts[METHOD_NAME_INDEX]) && doesMethodMatch(m, parts)){
					method = m;
					break;
				}
			}
			if(method==null)
				throw new NoSuchMethodException();
			object = dispatchClass.getConstructor(HttpServletRequest.class, HttpServletResponse.class).newInstance(request, response);
			String[] args = new String[parts.length-PARAMETER_START];
			for(int i=PARAMETER_START; i<parts.length; i++)
				args[i-PARAMETER_START] = parts[i];
			method.invoke(object, convertArgs(method, args));
		} catch (ClassNotFoundException e) {
			System.out.println("Hi");
			System.err.println(e.toString());
			return;
		} catch (InstantiationException e) {
			System.err.println(e.toString());
			return;
		} catch (NoSuchMethodException e) {
			System.err.println(e.toString());
			return;
		} catch (IllegalAccessException e) {
			// TODO show 404 error
			//e.printStackTrace();
			System.err.println(e.toString());
			return;
		} catch(InvocationTargetException e){
			//Catch possible exceptions thrown from invoked method.
			//TODO show server error page
			System.err.print(e.toString()+" in "+method.getClass().getName()+"."+method.getName()+" : ");
			System.err.println(e.getTargetException().toString());
			return;
		}
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		redirect(request, response);
		//DBStatement.closeConnection();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		redirect(request, response);
		//DBStatement.closeConnection();
	}
        
}
