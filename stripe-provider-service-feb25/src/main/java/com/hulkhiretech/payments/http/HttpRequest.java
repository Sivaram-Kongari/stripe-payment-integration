package com.hulkhiretech.payments.http;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HttpRequest {
	
	private HttpMethod method;
	private String url;
	private HttpHeaders headers;
	private Object requestBody;

}
