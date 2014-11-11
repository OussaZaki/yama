package Tables;

public class TagsTable {

	
	private String  tag;
	private Integer id;
	private Boolean isPermanent = true;
	
	public TagsTable(String tags){
		tag = tags;
	}
	public TagsTable(String tags,Integer idTags){
		tag = tags;
		id = idTags;
	}
	public TagsTable(String tags,Boolean isPermanentTags){
		tag = tags;
		isPermanent = isPermanentTags;
	}
	public TagsTable(String tags,Integer idTags,Boolean isPermanentTags){
		tag = tags;
		id = idTags;
		isPermanent = isPermanentTags;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Boolean getIsPermanent() {
		return isPermanent;
	}
	public void setIsPermanent(Boolean isPermanentTags) {
		this.isPermanent = isPermanentTags;
	}
	
	
}
