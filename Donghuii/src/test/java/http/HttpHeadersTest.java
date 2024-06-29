package http;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
@DisplayName("HttpHeaders 테스트")
public class HttpHeadersTest {

	@Test
	@DisplayName("헤더에 keep-alive가 담기는지 테스트 해보자")
	public void add() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Connection: keep-alive");
		assertEquals("keep-alive", headers.getHeader("Connection"));
	}
}
