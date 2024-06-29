package http;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("요청 라인 테스트")
class RequestLineTest {

	@Test
	@DisplayName("GET 메소드 생성 테스트")
	public void create_method_get() {
		RequestLine line = new RequestLine("GET /index.html HTTP/1.1");
		assertEquals(HttpMethod.GET, line.getMethod());
		assertEquals("/index.html", line.getPath());
	}

	@Test
	@DisplayName("POST 메소드 생성 테스트")
	public void create_method_post() {
		RequestLine line = new RequestLine("POST /index.html HTTP/1.1");
		assertEquals(HttpMethod.POST, line.getMethod());
	}

	@Test
	@DisplayName("파라미터 경로 테스트")
	public void create_path_and_params() {
		RequestLine line = new RequestLine("GET /user/create?userId=javajigi&password=pass HTTP/1.1");
		assertEquals(HttpMethod.GET, line.getMethod());
		assertEquals("/user/create", line.getPath());
		assertEquals("userId=javajigi&password=pass", line.getQueryString());
	}
}
