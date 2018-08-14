import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PageAllPostData 
{
	private ArrayList<String>header;
	private ArrayList<PageAllPostModel>data;

	public ArrayList<PageAllPostModel> getData() {
		return data;
	}

	public void setData(ArrayList<PageAllPostModel> data) {
		this.data = data;
	}

	public PageAllPostData(ArrayList<PageAllPostModel> data) {
		super();
		this.data = data;
	}

	public PageAllPostData() {
		super();
	}

	public ArrayList<String> getHeader() {
		return header;
	}

	public void setHeader(ArrayList<String> header) {
		this.header = header;
	}

	@Override
	public String toString() {
		return "PageAllPostData [data=" + data + "]";
	}
	

}
