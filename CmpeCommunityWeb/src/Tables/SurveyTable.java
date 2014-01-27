package Tables;

public class SurveyTable {
	private int id;
	private int userId;
	private String question;
	private ChoiceTable[] choiceTable;
	public SurveyTable(int id, int userId, String question,
			ChoiceTable[] choiceTable) {
		super();
		this.id = id;
		this.userId = userId;
		this.question = question;
		this.choiceTable = choiceTable;
	}
	public SurveyTable() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public ChoiceTable[] getChoiceTable() {
		return choiceTable;
	}
	public void setChoiceTable(ChoiceTable[] choiceTable) {
		this.choiceTable = choiceTable;
	}
}
