package webserver;

public enum HttpType {
	GET,
	POST;
	
	public boolean isPost() {
		return this == HttpType.POST;
	}
}
