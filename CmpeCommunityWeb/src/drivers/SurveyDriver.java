package drivers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import DBPack.DBStatement;
import Tables.ChoiceTable;
import Tables.SurveyTable;

import com.mysql.jdbc.Statement;

public class SurveyDriver {
	public static SurveyTable getById(int id){
		try {
			String query="SELECT * FROM `surveys` WHERE `id`=?";
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
			ps.setInt(1, id);
			ResultSet result = ps.executeQuery();
			if(!result.next())
				return null;
			return new SurveyTable(result.getInt("id"), result.getInt("user_id"), result.getString("question"), getChoices(result.getInt("id")));
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return null;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public static SurveyTable[] getUserSurvey(int userId) {
		try {
			String query="SELECT * FROM `surveys` WHERE `user_id`=? order by creation_time";
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
			ps.setInt(1, userId);
			ResultSet result = ps.executeQuery();
			return convertSurveyToArray(result);
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return new SurveyTable[0];
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new SurveyTable[0];
		}
	}
	public static SurveyTable[] getUserJoinedSurvey(int userId) {
		try{
			String query="SELECT `surveys`.* FROM `surveys` INNER JOIN `users_in_survey` ON `users_in_survey`.`survey_id`=`surveys`.`id` WHERE `users_in_survey`.`user_id`= ? ORDER BY creation_time" ;
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
			ps.setInt(1, userId);
			ResultSet set = ps.executeQuery();
			return convertSurveyToArray(set);
		}  catch(SQLException e) {
			System.out.println(e.getMessage());
			return new SurveyTable[0];
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new SurveyTable[0];
		}
	}
	public static int[] getUserJoinedSurveyIds(int userId){
		try{
			String query="SELECT `surveys`.`id` FROM `surveys` INNER JOIN `users_in_survey` ON `users_in_survey`.`survey_id`=`surveys`.`id` WHERE `users_in_survey`.`user_id`= ? ORDER BY creation_time" ;
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
			ps.setInt(1, userId);
			ResultSet set = ps.executeQuery();
			return convertIntToArray(set);
		}  catch(SQLException e) {
			System.out.println(e.getMessage());
			return new int[0];
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new int[0];
		}
	}
	public static boolean createSurvey(int userId,String question,String[] choices) {
		try{
			String query="INSERT INTO surveys (question,user_id,creation_time) VALUES (?,?,NOW())" ;	
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, question);
			ps.setInt(2, userId);
			ps.executeUpdate();
			ResultSet result = ps.getGeneratedKeys();
			if(!result.next())
				return false;
			int surveyId = result.getInt(1);
			/*if(!SurveyDriver.insertUsersInSurvey(userId,surveyId)){
				//TODO delete previously created post
				return false;
			}*/
			if(!SurveyDriver.insertChoices(surveyId,choices)){
				//TODO delete previously created post
				return false;
			}
			return true;
		}  catch(SQLException e) {
			return false;
		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * @deprecated Use vote method instead, this method does not use users_in_survey table thus
	 * it is harmful to database referential consistency. NEVER USE THIS METHOD
	 * @param choiceId
	 * @return
	 */
	public static boolean voteChoice(int choiceId) {
		try{
			String query="UPDATE choices set votes=votes+1 where id=?" ;	
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, choiceId);
			ps.executeUpdate();
			return true;
		}  catch(SQLException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean vote(int userId, int surveyId, int choiceId){
		try{
			String q1 = "INSERT INTO `users_in_survey` (`survey_id`, `user_id`) VALUES (?, ?)";
			PreparedStatement ps1 = (PreparedStatement) DBStatement.getMainConnection().prepareStatement(q1, Statement.RETURN_GENERATED_KEYS);
			ps1.setInt(1, surveyId);
			ps1.setInt(2, userId);
			ps1.executeUpdate();
			
			String q2 = "UPDATE choices SET votes=votes+1 WHERE id=?" ;
			PreparedStatement ps2=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(q2, Statement.RETURN_GENERATED_KEYS);
			ps2.setInt(1, choiceId);
			ps2.executeUpdate();
			return true;
		}  catch(SQLException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static SurveyTable getSurvey(int surveyId) {
		try {
			String query="SELECT * FROM `surveys` WHERE `id`=?";
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
			ps.setInt(1, surveyId);
			ResultSet result = ps.executeQuery();
			return new SurveyTable(result.getInt("id"),result.getInt("user_id"),result.getString("question"),getChoices(surveyId));
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return null;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	public static ChoiceTable[] getChoices(int surveyId) {
		try {
			String query="SELECT * FROM `choices` WHERE `survey_id`=?";
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
			ps.setInt(1, surveyId);
			ResultSet result = ps.executeQuery();
			return convertToArray(result);
		} catch(SQLException e) {
			System.out.println(e.getMessage());
			return new ChoiceTable[0];
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new ChoiceTable[0];
		}
	}
	private static ChoiceTable[] convertToArray(ResultSet result) throws SQLException{
		int N = 0;
		double totalVotes=0;
		while(result.next()) { 
			N++;
			totalVotes+=result.getInt("votes");
		}
		result.beforeFirst();
		ChoiceTable[] choices = new ChoiceTable[N];
		int i=0;
		while(result.next())
			choices[i++] = new ChoiceTable(result.getInt("id"), result.getInt("survey_id"), result.getString("choice"), result.getInt("votes"),(totalVotes==0?0:result.getInt("votes")/totalVotes)*100);
		return choices;
	}
	private static int[] convertIntToArray(ResultSet result) throws SQLException{
		int N = 0;
		while(result.next()) N++;
		result.beforeFirst();
		int[] surveys = new int[N];
		int i=0;
		while(result.next())
			surveys[i++] = result.getInt("id");
		return surveys;
	}
	private static SurveyTable[] convertSurveyToArray(ResultSet result) throws SQLException{
		int N = 0;
		while(result.next()) N++;
		result.beforeFirst();
		SurveyTable[] surveys = new SurveyTable[N];
		int i=0;
		while(result.next())
			surveys[i++] = new SurveyTable(result.getInt("id"),result.getInt("user_id"),result.getString("question"),getChoices(result.getInt("id")));
		return surveys;
	}
	public static boolean insertUsersInSurvey(int userId,int surveyId) {
		try{
			String query="INSERT INTO users_in_survey (survey_id,user_id) VALUES (?,?)" ;	
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, surveyId);
			ps.setInt(2, userId);
			ps.executeUpdate();
			return true;
		}  catch(SQLException e) {
			return false;
		} catch (Exception e) {
			return false;
		}

	}
	private static boolean insertChoices(int surveyId, String[] choices) {
		try{
			for (int i=0;i<choices.length;i++) {

				if (!addChoice(surveyId,choices[i]))
					return false;

			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	private static boolean addChoice(int surveyId, String choice) {
		try{
			String query="INSERT INTO choices (survey_id,choice) VALUES (?,?)" ;	
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, surveyId);
			ps.setString(2, choice);
			ps.executeUpdate();
			return true;
		}  catch(SQLException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	public static ArrayList<Double> getVotes(int surveyId) {
		ArrayList<Double> list=new ArrayList<Double>();
		double totalVotes=0;
		try {
			String query="SELECT * FROM `surveys` WHERE `id`=?";
			PreparedStatement ps=(PreparedStatement) DBStatement.getMainConnection().prepareStatement(query);
			ps.setInt(1, surveyId);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				list.add(result.getInt("votes")/1.0);
				totalVotes+=result.getInt("votes");			
			}
			for (int i=0;i<list.size();i++) {
				list.set(i,list.get(i)*100/totalVotes);
			}
			return list;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}


	}

}
