package Tables;

import java.sql.Timestamp;


public class ForumPostTable {
	private int id;
	private String content;
	private Timestamp postTime;
	private int userId;
	private int topicId;
	
	public ForumPostTable(int id, String content, Timestamp postTime,
			int userId, int topicId) {
		this.id = id;
		this.content = content;
		this.postTime = postTime;
		this.userId = userId;
		this.topicId = topicId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Timestamp getPostTime() {
		return postTime;
	}

	public void setPostTime(Timestamp postTime) {
		this.postTime = postTime;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getTopicId() {
		return topicId;
	}

	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}
}
