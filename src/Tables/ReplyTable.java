package Tables;

public class ReplyTable {
	private int id;
	private int postId;
	private int ownerId;
	private String body;
	private String postingTime;
	public ReplyTable(int id, int postId, int ownerId, String body, String postingTime){
		this.id = id;
		this.postId = postId;
		this.ownerId = ownerId;
		this.body = body;
		this.postingTime = postingTime;
	}
	
	public int getId() {
		return id;
	}
	public int getPostId() {
		return postId;
	}
	public void setPostId(int postId) {
		this.postId = postId;
	}
	public int getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getPostingTime() {
		return postingTime;
	}
	public void setPostingTime(String postingTime) {
		this.postingTime = postingTime;
	}
}
