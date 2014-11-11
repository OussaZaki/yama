package Tables;

public class PostsTable {
	private int id;
	private int ownerId;
	private String body;
	private String postingTime;
	public PostsTable(int id, int ownerId, String body, String postingTime) {
		super();
		this.id = id;
		this.ownerId = ownerId;
		this.body = body;
		this.postingTime = postingTime;
	}
	public PostsTable() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(int owner_id) {
		this.ownerId = owner_id;
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
	public void setPostingTime(String posting_time) {
		this.postingTime = posting_time;
	}
}
