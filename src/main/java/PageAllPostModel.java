import com.capitalone.socialApiFb.model.Comments;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/*
{
      "id": "102134663215085_1711628102265725",
      "message": "Wondering what’s next for the global economy? Our investment team offers insights and actions to help investors navigate these challenging times. http://bit.ly/2uB4e7p",
      "created_time": "2018-07-19T21:50:06+0000",
      "updated_time": "2018-07-19T21:50:06+0000",
      "name": "Read our 2018 Midyear Outlook",
      "type": "link",
      "privacy": {
        "allow": "",
        "deny": "",
        "description": "Public",
        "friends": "",
        "value": "EVERYONE"
      },
      "link": "https://bit.ly/2uB4e7p",
      "permalink_url": "https://www.facebook.com/102134663215085/posts/1711628102265725/",
      "promotable_id": "102134663215085_1711628102265725",
      "shares": {
        "count": 9
      },
      "likes": {
        "data": [],
        "summary": {
          "total_count": 16,
          "can_like": false,
          "has_liked": false
        }
      },
      "comments": {
        "data": [],
        "summary": {
          "order": "ranked",
          "total_count": 0,
          "can_comment": false
        }
      }
    }*/
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageAllPostModel 
{
	private String id;
	private String message;
	private String created_time;
	private String updated_time;
	private String name;
	private String type;
	private Privacy privacy;
	private Shares shares;
	private Likes likes;
	private Comments comments;
	private String link;
	private String permalink_url;
	private String promotable_id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCreated_time() {
		return created_time;
	}
	public void setCreated_time(String created_time) {
		this.created_time = created_time;
	}
	public String getUpdated_time() {
		return updated_time;
	}
	public void setUpdated_time(String updated_time) {
		this.updated_time = updated_time;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Privacy getPrivacy() {
		return privacy;
	}
	public void setPrivacy(Privacy privacy) {
		this.privacy = privacy;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getPermalink_url() {
		return permalink_url;
	}
	public void setPermalink_url(String permalink_url) {
		this.permalink_url = permalink_url;
	}
	public String getPromotable_id() {
		return promotable_id;
	}
	public void setPromotable_id(String promotable_id) {
		this.promotable_id = promotable_id;
	}
	public PageAllPostModel(String id, String message, String created_time,
			String updated_time, String name, String type, Privacy privacy,
			String link, String permalink_url, String promotable_id) {
		super();
		this.id = id;
		this.message = message;
		this.created_time = created_time;
		this.updated_time = updated_time;
		this.name = name;
		this.type = type;
		this.privacy = privacy;
		this.link = link;
		this.permalink_url = permalink_url;
		this.promotable_id = promotable_id;
	}
	public PageAllPostModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "PageAllPostModel [id=" + id + ", message=" + message
				+ ", created_time=" + created_time + ", updated_time="
				+ updated_time + ", name=" + name + ", type=" + type
				+ ", privacy=" + privacy + ", link=" + link
				+ ", permalink_url=" + permalink_url + ", promotable_id="
				+ promotable_id + "]";
	}
	
}
