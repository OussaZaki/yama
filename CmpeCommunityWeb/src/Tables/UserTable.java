package Tables;

public class UserTable {
	private String email;
	private int id;
	private String passwordHash;
	private String name;
	private String birthDate;
	private String registerDate;
	public UserTable(String email, String name, String passwordHash,String birthDate,
			String registerDate) {
		super();
		this.email = email;
		this.name = name;
		this.passwordHash=passwordHash;
		this.birthDate = birthDate;
		this.registerDate = registerDate;
	}
	public UserTable(int id, String email, String name, String passwordHash, String birthDate, String registerDate){
		super();
		this.id = id;
		this.email = email;
		this.name = name;
		this.passwordHash=passwordHash;
		this.birthDate = birthDate;
		this.registerDate = registerDate;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPasswordHash() {
		return passwordHash;
	}
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	public String getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}
}
