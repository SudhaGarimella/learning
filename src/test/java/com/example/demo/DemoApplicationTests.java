package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;



@SpringBootTest
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}

}

@AutoConfigureMockMvc
@SpringBootTest(classes=BootstrapApp.class)

public class DealerControllerIntTest {

	@Autowired
	private MockMvc mvc;
	@Autowired
	private ObjectMapper objMapper;
	
	private static String authHeader = "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IklHVi1mWkRsWWVSTkpyal9VT3ZMRVhhdDUzSSJ9.eyJzdWIiOiJUMjExNUJNIiwiYXVkIjoibXl3YXlfb2lkY19iMmRfZGV2IiwianRpIjoieVBZZ1BvUzlHY3hLbGV3S1MxdnpvNCIsImlzcyI6Imh0dHBzOi8vZmVkZXJhdGlvbi1kZXYuY2hyeXNsZXIuY29tIiwiaWF0IjoxNjA0OTA2MzE5LCJleHAiOjE2MDQ5MDgxMTksImZyYW5jaGlzZXMiOiJKVFJEQyIsInBvc2l0aW9uX2NvZGUiOiI2MCIsImRlYWxlcl90eXBlIjoiSlRSREMiLCJnaXZlbl9uYW1lIjoiQmhhcmdhdmkiLCJkZWFsZXJfY29kZSI6IjA1MDAyIiwidWlkIjoiVDIxMTVCTSIsImdsb2JhbF91bml0IjoiRCIsIm5hbWUiOiJUMjExNUJNIiwiZGVhbGVyX25hbWUiOiJGUkFOSyBDIFZJREVPTiBJTkMiLCJicmFuZCI6IkpUUkRDIiwiZGVhbGVyX3N0YXRlIjoiUEEiLCJmYW1pbHlfbmFtZSI6Ik1hamV0aSIsInJlZ2lvbl9jb2RlIjoiMzUiLCJhY3IiOiJ1cm46b2FzaXM6bmFtZXM6dGM6U0FNTDoyLjA6YWM6Y2xhc3NlczpQYXNzd29yZFByb3RlY3RlZFRyYW5zcG9ydCJ9.LjoOMxgsEjPtY5LHMYI_g1Gfp4jfYiAaCobcz8dmj4yEQWAzcYBcn3Hd87s5smEhjz8hd-vz-1S4YjjOt_QAJd6UHaQTR6w6mKgKQY8OzBLcFvC4WlfTXzYuNGm_lem3LdxF6hIKw-CbTdb3-VR9Wx1NH6uPt-syD865x9-Xx9pYOK4QIxtmRezWijqPAwzB3BnN-I4326C5YcINc_vHNC9dQmCYzLFxeo2G0RF_6-L7jkIDFd5TSLAEpGIfkkn3TBHbWOm-cI2U31jIerz_zrCT60qmX8twDM7Rql1TYabl-OrWo0xMUyI88BkAJYwBwtg9tZ5WR1xDqGqnEiX45A";

	@Test
	public void noResults() throws Exception {

		MvcResult res = mvc.perform(get("/dealer/1234").contentType("application/json"))
				.andExpect(status().is(200)).andReturn();
		String resp = res.getResponse().getContentAsString();
		assertThat(resp.contains("No results available for entered Dealer Code. Please re-check"));
	}
	
	@Test
	public void allOk() throws Exception {

		MvcResult res = mvc.perform(get("/dealer/26195").contentType("application/json"))
				.andExpect(status().is(200)).andReturn();
		
		String resp = res.getResponse().getContentAsString();
		assertThat(resp.contains("\"numberOfResultsReturned\":\"1\""));
		
		DealerInfo info = objMapper.readValue(resp, DealerInfo.class);
		System.out.println("info: "+info);
		assertNotNull(info);
		assertNotNull(info.getDealer());
		
		
		for(Dealer dealer :info.getDealer() ) {
			assertNotNull(dealer.getDealerCode());
			
			for(DealerObjects dealerObj : dealer.getDealerObjects()) {
				assertNotNull(dealerObj.getDealerName());
				assertNotNull(dealerObj.getDealerCity());
				assertNotNull(dealerObj.getDealerState());
				assertNotNull(dealerObj.getDealerZipCode());
				assertNotNull(dealerObj.getPhoneNumber());
			
			assertNotNull(dealerObj.getDealerAddress1());}
		}
		
		MvcResult res2 = mvc.perform(get("/dealer/getauthpayload")
				.contentType("application/json")
				.header("authorization", authHeader))
				.andExpect(status().is(200)).andReturn();
		
		resp = res2.getResponse().getContentAsString();
		assertThat(resp.contains("dealer_code="));
	}
	
	
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
