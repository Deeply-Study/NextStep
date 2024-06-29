package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import lombok.extern.slf4j.Slf4j;
import util.IOUtils;

@Slf4j
public class HttpRequest {
	private RequestLine requestLine;
	private HttpHeaders headers;
	private RequestParams requestParams = new RequestParams();

	public HttpRequest(InputStream is) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
			requestLine = new RequestLine(createRequestLine(br));
			requestParams.addQueryString(requestLine.getQueryString());
			headers = processHeaders(br);
			requestParams.addBody(IOUtils.readData(br, headers.getContentLength()));
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private String createRequestLine(BufferedReader br) throws IOException {
		String line = br.readLine();
		if(line == null) {
			throw new IllegalStateException();
		}
		return line;
	}

	private HttpHeaders processHeaders(BufferedReader br) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		String line;
		while(!(line = br.readLine()).equals("")) {
			headers.add(line);
		}
		return headers;
	}

	public HttpMethod getMethod() {
		return requestLine.getMethod();
	}

	public String getPath() {
		return requestLine.getPath();
	}

	public String getHeader(String name) {
		return headers.getHeader(name);
	}

	public String getparameter(String name) {
		return requestParams.getParameter(name);
	}

}
