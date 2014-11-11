package Tables;

import java.sql.Timestamp;

public class ForumTopicTable {
	private int id;
	private int forumId;
	private String title;
	private int repliesCount;
	private int viewsCount;
	private Timestamp creationTime;
	private int userId;
	private int lastPostId;
	
	public ForumTopicTable(int id, int forumId, String title, int repliesCount,
			int viewsCount, Timestamp creationTime, int userId, int lastPostId) {
		this.id = id;
		this.forumId = forumId;
		this.title = title;
		this.repliesCount = repliesCount;
		this.viewsCount = viewsCount;
		this.creationTime = creationTime;
		this.userId = userId;
		this.lastPostId = lastPostId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getForumId() {
		return forumId;
	}

	public void setForumId(int forumId) {
		this.forumId = forumId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getRepliesCount() {
		return repliesCount;
	}

	public void setRepliesCount(int repliesCount) {
		this.repliesCount = repliesCount;
	}

	public int getViewsCount() {
		return viewsCount;
	}

	public void setViewsCount(int viewsCount) {
		this.viewsCount = viewsCount;
	}

	public Timestamp getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Timestamp creationTime) {
		this.creationTime = creationTime;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getLastPostId() {
		return lastPostId;
	}

	public void setLastPostId(int lastPostId) {
		this.lastPostId = lastPostId;
	}
}
