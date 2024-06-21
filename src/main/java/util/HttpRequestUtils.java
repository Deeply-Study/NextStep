package util;

import static util.Constants.EQUAL_SYMBOL;
import static util.Constants.HTTP_HEADER_FIELD_SEPARATOR;
import static util.Constants.PARAM_SEPARATOR;
import static util.Constants.QUERY_STRING_PREFIX;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import model.User;

public class HttpRequestUtils {
    public HttpRequestUtils() {}

    /**
     * @param queryString은
     *            URL에서 ? 이후에 전달되는 field1=value1&field2=value2 형식임
     * @return
     */
    public static Map<String, String> parseQueryString(String queryString) {
        return parseValues(queryString, "&");
    }

    /**
     * @param 쿠키
     *            값은 name1=value1; name2=value2 형식임
     * @return
     */
    public static Map<String, String> parseCookies(String cookies) {
        return parseValues(cookies, ";");
    }

    private static Map<String, String> parseValues(String values, String separator) {
        if (Strings.isNullOrEmpty(values)) {
            return Maps.newHashMap();
        }

        String[] tokens = values.split(separator);
        return Arrays.stream(tokens).map(t -> getKeyValue(t, "=")).filter(p -> p != null)
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
    }

    static Pair getKeyValue(String keyValue, String regex) {
        if (Strings.isNullOrEmpty(keyValue)) {
            return null;
        }

        String[] tokens = keyValue.split(regex);
        if (tokens.length != 2) {
            return null;
        }

        return new Pair(tokens[0], tokens[1]);
    }

    public static Pair parseHeader(String header) {
        return getKeyValue(header, ": ");
    }

    public static class Pair {
        String key;
        String value;

        Pair(String key, String value) {
            this.key = key.trim();
            this.value = value.trim();
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((key == null) ? 0 : key.hashCode());
            result = prime * result + ((value == null) ? 0 : value.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Pair other = (Pair) obj;
            if (key == null) {
                if (other.key != null)
                    return false;
            } else if (!key.equals(other.key))
                return false;
            if (value == null) {
                if (other.value != null)
                    return false;
            } else if (!value.equals(other.value))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "Pair [key=" + key + ", value=" + value + "]";
        }
    }

    // 이하 직접 구현한 함수입니다.

    /**
     * 'key=value' 형식의 요청 파라미터에서 key, value 분리
     * @param requestParams
     *
     * @return key, value가 분리된 요청 파라미터
     */
    public Map<String, String> parseRequestParams(String requestParams) {
        String[] pairs = requestParams.split(PARAM_SEPARATOR);
        Map<String, String> params = new HashMap<>();

        for (int i = 0; i < pairs.length; i++) {
            int equalIdx = pairs[i].indexOf(EQUAL_SYMBOL);
            params.put(pairs[i].substring(0, equalIdx), pairs[i].substring(equalIdx+1));
        }

        return params;
    }

    /**
     * StartLine 추출
     * @param br
     * @return Start Line
     * @throws IOException
     */
    public String getStartLine(BufferedReader br) throws IOException {
        return br.readLine();
    }

    /**
     * StartLine에서 request method 추출
     * @param startLine
     * @return request method
     */
    public String getRequestMethod(String startLine) {
        return startLine.split(" ")[0];
    }

    /**
     * StartLine에서 Request target 추출
     * @param startLine
     * @return Request Target
     */
    public String getRequestTarget(String startLine) {
        return startLine.split(" ")[1];
    }

    /**
     * Header 추출
     * @param br
     * @return key, value가 분리된 http header
     * @throws IOException
     */
    public Map<String, String> getHeader(BufferedReader br) throws IOException {
        Map<String, String> header = new HashMap<>();
        String line = "";

        while(!(line = br.readLine()).isEmpty()) {
            String[] splitedHeader = line.split(HTTP_HEADER_FIELD_SEPARATOR);
            header.put(splitedHeader[0], splitedHeader[1]);
        }

        return header;
    }

    /**
     * GET요청의 Request Tartget에서 Query String 추출
     * @param requestUrl
     * @return Query String
     */
    private String getQueryStringInHttpHeader(String requestUrl) {
        int queryStringStartIdx = requestUrl.indexOf(QUERY_STRING_PREFIX) + 1;
        StringBuilder sb = new StringBuilder();

        if (queryStringStartIdx != 0) {
            sb.append(requestUrl.substring(queryStringStartIdx));
        }

        return sb.toString();
    }
}
