
public class Summary {
public Integer total_count;
public String can_like;
public Boolean has_liked;
public Integer getTotal_count() {
	return total_count;
}
public void setTotal_count(Integer total_count) {
	this.total_count = total_count;
}
public String getCan_like() {
	return can_like;
}
public void setCan_like(String can_like) {
	this.can_like = can_like;
}
public Boolean getHas_liked() {
	return has_liked;
}
public void setHas_liked(Boolean has_liked) {
	this.has_liked = has_liked;
}
public Summary(Integer total_count, String can_like, Boolean has_liked) {
	super();
	this.total_count = total_count;
	this.can_like = can_like;
	this.has_liked = has_liked;
}
public Summary() {
	super();
	// TODO Auto-generated constructor stub
}

}
