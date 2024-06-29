package http;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("요청 파라미터 테스트")
class RequestParamsTest {

	@Test
	@DisplayName("가입 테스트")
	public void add() throws Exception {
		RequestParams params = new RequestParams();
		params.addQueryString("id=1");
		params.addBody("userId=javajigi&password=password");
		assertEquals("1", params.getParameter("id"));
		assertEquals("javajigi", params.getParameter("userId"));
		assertEquals("password", params.getParameter("password"));
	}
}
