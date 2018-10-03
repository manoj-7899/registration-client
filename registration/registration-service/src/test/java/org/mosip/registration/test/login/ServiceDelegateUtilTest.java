package org.mosip.registration.test.login;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mosip.kernel.core.spi.logging.MosipLogger;
import org.mosip.registration.constants.RegConstants;
import org.mosip.registration.dto.OtpGeneratorRequestDto;
import org.mosip.registration.dto.ResponseDTO;
import org.mosip.registration.exception.RegBaseCheckedException;
import org.mosip.registration.util.restclient.RestClientUtil;
import org.mosip.registration.util.restclient.ServiceDelegateUtil;
import org.springframework.core.env.Environment;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;

public class ServiceDelegateUtilTest {
	@Mock
	RestClientUtil restClientUtil;
	
	@Mock
	MosipLogger logger;

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@InjectMocks
	ServiceDelegateUtil delegateUtil;

	@Mock
	Environment environment;

	@Test
	public void getURITest() {
		ReflectionTestUtils.setField(delegateUtil, "LOGGER", logger);
		doNothing().when(logger).debug(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString());
		
		Map<String, String> requestParamMap = new HashMap<String, String>();
		requestParamMap.put(RegConstants.USERNAME_KEY, "yashReddy");
		requestParamMap.put(RegConstants.OTP_GENERATED, "099887");
		Assert.assertEquals(delegateUtil.getUri(requestParamMap, "http://localhost:8080/otpmanager/otps").toString(),
				"http://localhost:8080/otpmanager/otps?otp=099887&key=yashReddy");
	}

	@Test
	public void getRequestTest() throws RegBaseCheckedException {
		ReflectionTestUtils.setField(delegateUtil, "LOGGER", logger);
		doNothing().when(logger).debug(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString());
		
		ResponseDTO response = new ResponseDTO();
		when(environment.getProperty("otp_validator.service.httpmethod")).thenReturn("GET");
		when(environment.getProperty("otp_validator.service.url")).thenReturn("http://localhost:8080/otpmanager/otps");
		when(environment.getProperty("otp_validator.service.responseType")).thenReturn("org.mosip.registration.dto.OtpValidatorResponseDto");
		when(environment.getProperty("otp_validator.service.headers")).thenReturn("Content-Type:APPLICATION/JSON");
		when(environment.getProperty("otp_validator.service.authrequired")).thenReturn("false");
		when(environment.getProperty("otp_validator.service.authheader")).thenReturn("Authorization:BASIC");
		
		when(restClientUtil.invoke(Mockito.any())).thenReturn(response);
		Map<String, String> requestParamMap = new HashMap<String, String>();
		requestParamMap.put(RegConstants.USERNAME_KEY, "yashReddy");
		requestParamMap.put(RegConstants.OTP_GENERATED, "099886");
		assertNotNull(delegateUtil.get("otp_validator", requestParamMap));
	}

	@Test
	public void postRequestTest() throws URISyntaxException, HttpClientErrorException, RegBaseCheckedException {
		

		ReflectionTestUtils.setField(delegateUtil, "LOGGER", logger);
		doNothing().when(logger).debug(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString());
		
		ResponseDTO response = new ResponseDTO();
		when(environment.getProperty("otp_generator.service.httpmethod")).thenReturn("POST");
		when(environment.getProperty("otp_generator.service.url")).thenReturn("http://localhost:8080/otpmanager/otps");
		when(environment.getProperty("otp_generator.service.requestType")).thenReturn("org.mosip.registration.dto.OtpGeneratorResponseDto");
		when(environment.getProperty("otp_generator.service.headers")).thenReturn("Content-Type:APPLICATION/JSON");
		when(environment.getProperty("otp_generator.service.authrequired")).thenReturn("false");
		when(environment.getProperty("otp_generator.service.authheader")).thenReturn("Authorization:BASIC");
		
		when(restClientUtil.invoke(Mockito.any())).thenReturn(response);
		OtpGeneratorRequestDto generatorRequestDto=new OtpGeneratorRequestDto();
		generatorRequestDto.setKey("yashReddy");
		assertNotNull(delegateUtil.post("otp_generator", generatorRequestDto));		
	}
}
