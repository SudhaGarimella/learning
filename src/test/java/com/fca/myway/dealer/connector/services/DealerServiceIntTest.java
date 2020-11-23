package com.fca.myway.dealer.connector.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fca.myway.dealer.connector.exceptions.HandlingDealerApplicationException;
import com.fca.myway.dealer.connector.model.DealerInfo;
import com.fca.myway.dealer.connector.model.DealerProperties;
import com.fca.myway.dealer.connector.service.DealerService;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

@RunWith(SpringRunner.class)
public class DealerServiceIntTest {
	public static MockWebServer mockBackEnd;
	private ObjectMapper MAPPER = new ObjectMapper();

	@Autowired
	private DealerService appointmentService;

	@Autowired
	DealerProperties dealerProps;

	@BeforeAll
	static void setUp() throws IOException {

		mockBackEnd = new MockWebServer();
		mockBackEnd.start();
	}

	@AfterAll
	static void tearDown() throws IOException {
		mockBackEnd.shutdown();
	}

	@BeforeEach
	void initialize() {
		appointmentService = new DealerService();
		dealerProps = new DealerProperties("http://localhost:"+mockBackEnd.getPort());
		
		ReflectionTestUtils.setField(appointmentService, "dealerProps", dealerProps);
		

		appointmentService.init();
	}

	@Test
	void getDealerPositiveTest() throws Exception {
		DealerInfo info = new DealerInfo();
		info.setNumberOfResultsReturned("1");

		mockBackEnd.enqueue(new MockResponse().setBody(MAPPER.writeValueAsString(info)).addHeader("Content-Type",
				"application/json"));
		DealerInfo status = appointmentService.getDealerDetails("26915");

		RecordedRequest recordedRequest = mockBackEnd.takeRequest();

		assertEquals("GET", recordedRequest.getMethod());
		assertEquals("/?dealerCode=26915", recordedRequest.getPath());
		assertThat(status).isNotNull();
	}
	
	@Test
	void getDealerPositiveError() throws Exception {
		DealerInfo info = new DealerInfo();
		info.setNumberOfResultsReturned("1");

		mockBackEnd.enqueue(new MockResponse().setBody(MAPPER.writeValueAsString(info)).addHeader("Content-Type",
				"application/json"));
		

		//RecordedRequest recordedRequest = mockBackEnd.takeRequest();

		
		assertThrows(HandlingDealerApplicationException.class, () ->  appointmentService.getDealerDetails(""));
	
	}
}
