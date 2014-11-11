package Tables;

public class ChoiceTable {
	private int id;
	private int surveyId;
	private String choice;
	private int votes;
	private double percentageVotes;
	public ChoiceTable(int id, int surveyId, String choice, int votes,double percentageVotes) {
		super();
		this.id = id;
		this.surveyId = surveyId;
		this.choice = choice;
		this.votes = votes;
		this.percentageVotes=percentageVotes;
	}
	public ChoiceTable() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSurveyId() {
		return surveyId;
	}
	public void setSurveyId(int surveyId) {
		this.surveyId = surveyId;
	}
	public String getChoice() {
		return choice;
	}
	public void setChoice(String choice) {
		this.choice = choice;
	}
	public int getVotes() {
		return votes;
	}
	public void setVotes(int votes) {
		this.votes = votes;
	}
	public double getPercentageVotes() {
		return percentageVotes;
	}
	public void setPercentageVotes(double percentageVotes) {
		this.percentageVotes=percentageVotes;
	}
}
