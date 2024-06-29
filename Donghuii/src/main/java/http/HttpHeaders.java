package http;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpHeaders {

	private static final String CONTENT_LENGTH = "Content-Length";
	private Map<String, String> headers = new HashMap<>();

	void add(String header) {
		log.debug("header : {}", header);
		String[] splitedHeader = header.split(":");
		headers.put(splitedHeader[0], splitedHeader[1].trim());
	}

	String getHeader(String name) {
		return headers.get(name);
	}

	int getIntHeader(String name) {
		String header = getHeader(name);
		return header == null ? 0 : Integer.parseInt(header);
	}

	int getContentLength(){
		return getIntHeader(CONTENT_LENGTH);
	}
}
