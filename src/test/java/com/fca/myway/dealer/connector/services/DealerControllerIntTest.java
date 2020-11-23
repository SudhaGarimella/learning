package com.fca.myway.dealer.connector.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fca.myway.dealer.connector.bootstrap.BootstrapApp;
import com.fca.myway.dealer.connector.exceptions.HandlingDealerAPIException;
import com.fca.myway.dealer.connector.exceptions.HandlingDealerApplicationException;
import com.fca.myway.dealer.connector.model.Dealer;
import com.fca.myway.dealer.connector.model.DealerInfo;
import com.fca.myway.dealer.connector.model.DealerObjects;

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
	
	@Test
	public void blankDealerCode() throws Exception {

		MvcResult res = mvc.perform(get("/dealer/").contentType("application/json"))
				.andExpect(status().is4xxClientError()).andReturn();
		String resp = res.getResponse().getContentAsString();
		assertThat(resp.contains("\"numberOfResultsReturned\":\"0\""));
	}
	
	@Test
	public void exceptionsTest() throws Exception {
		FrameworkGlobalExceptionHandler fgeh = new FrameworkGlobalExceptionHandler();
		WebClientResponseException ex = Mockito.mock(WebClientResponseException.class);
		Mockito.when(ex.getResponseBodyAsString()).thenReturn("Bad request");
		Mockito.when(ex.getStatusCode()).thenReturn(HttpStatus.BAD_GATEWAY);
		assertNotNull(fgeh.handleWebClientResponseException(ex));
		
		HandlingDealerAPIException ex2 = Mockito.mock(HandlingDealerAPIException.class);
		Mockito.when(ex2.getMessage()).thenReturn("Bad request");
		Mockito.when(ex2.getStatusCode()).thenReturn(HttpStatus.BAD_GATEWAY);
		assertNotNull(fgeh.handleAPIException(ex2));
		
		HandlingDealerApplicationException ex3 = Mockito.mock(HandlingDealerApplicationException.class);
		Mockito.when(ex3.getMessage()).thenReturn("Bad request");
		assertNotNull(fgeh.handleApplicationException(ex3));
		
		MvcResult res = mvc.perform(get("/dealer/getauthpayload")
				.contentType("application/json")
				.header("authorization", "random"))
				.andExpect(status().is(400)).andReturn();
		
		String resp = res.getResponse().getContentAsString();
		assertThat(resp.length()==0);
		
	}
}