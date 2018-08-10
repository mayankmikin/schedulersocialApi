import java.util.ArrayList;


public class Likes {
	public ArrayList<Object>data;
	public Summary summary;
	
	public Summary getSummary() {
		return summary;
	}
	public void setSummary(Summary summary) {
		this.summary = summary;
	}

	public Likes() {
		super();
	}
	public ArrayList<Object> getData() {
		return data;
	}
	public void setData(ArrayList<Object> data) {
		this.data = data;
	}
	public Likes(ArrayList<Object> data, Summary summary) {
		super();
		this.data = data;
		this.summary = summary;
	}
	
	

}
