package http;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import util.HttpRequestUtils;

@Slf4j
public class RequestParams {

	private Map<String, String> params = new HashMap<>();

	public void addQueryString(String queryString) {
		putParams(queryString);
	}

	private void putParams(String data) {
		log.debug("data : {}", data);
		if(data == null || data.isEmpty()) {
			return;
		}
		params.putAll(HttpRequestUtils.parseQueryString(data));
		log.debug("params : {}", params);
	}

	public void addBody(String body) {
		putParams(body);
	}

	public String getParameter(String name) {
		return params.get(name);
	}
}
