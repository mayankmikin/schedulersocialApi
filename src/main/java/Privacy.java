
public class Privacy {
	
	private String allow;
	private String deny;
	private String description;
	private String friends;
	private String value;
	public String getAllow() {
		return allow;
	}
	public void setAllow(String allow) {
		this.allow = allow;
	}
	public String getDeny() {
		return deny;
	}
	public void setDeny(String deny) {
		this.deny = deny;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFriends() {
		return friends;
	}
	public void setFriends(String friends) {
		this.friends = friends;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Privacy(String allow, String deny, String description,
			String friends, String value) {
		super();
		this.allow = allow;
		this.deny = deny;
		this.description = description;
		this.friends = friends;
		this.value = value;
	}
	public Privacy() {
		super();
		// TODO Auto-generated constructor stub
	}
	

}
