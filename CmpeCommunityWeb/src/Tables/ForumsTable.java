package Tables;

public class ForumsTable {
	private int id;
	private int parentId;
	private String name;
	private String description;
	private int topicsCount;
	private int postsCount;
	private int lastPostId;
	
	public ForumsTable(int id, int parentId, String name, String description, int topicsCount, int postsCount, int lastPostId){
		this.id = id;
		this.parentId = parentId;
		this.name = name;
		this.description = description;
		this.topicsCount = topicsCount;
		this.postsCount = postsCount;
		this.lastPostId = lastPostId;
	}
	
	public int getLastPostId() {
		return lastPostId;
	}

	public int getPostsCount() {
		return postsCount;
	}
	
	public int getTopicsCount() {
		return topicsCount;
	}
	
	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	public int getParentId() {
		return parentId;
	}

	public int getId() {
		return id;
	}
}
