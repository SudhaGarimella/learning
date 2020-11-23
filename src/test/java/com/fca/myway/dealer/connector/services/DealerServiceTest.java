package com.fca.myway.dealer.connector.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import com.fca.myway.dealer.connector.exceptions.HandlingDealerApplicationException;
import com.fca.myway.dealer.connector.model.DealerInfo;
import com.fca.myway.dealer.connector.service.DealerService;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
public class DealerServiceTest {

	private WebClient webClient = mock(WebClient.class);

	private WebClient.RequestBodyUriSpec requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
	private WebClient.RequestBodySpec requestBodySpec = mock(WebClient.RequestBodySpec.class);
	private WebClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class); // GET
	private WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
	private WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

	@InjectMocks
	private DealerService service;
	
	

	@Test
	void getDealerDetailsNoResultsTest() throws Exception {
		DealerInfo dealerInfo = new DealerInfo();

		System.out.println("webClient: " + webClient);
		String dealerCode = "26195";
		when(webClient.get()).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.uri("?dealerCode={dealerCode}", dealerCode)).thenReturn(requestHeadersSpec);
		when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
		when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
		when(responseSpec.bodyToMono(DealerInfo.class)).thenReturn(Mono.just(dealerInfo));

		assertThrows(HandlingDealerApplicationException.class, () -> service.getDealerDetails("26195"));

	}

	@Test
	void getDealerDetails0ResultsTest() throws Exception {
		DealerInfo dealerInfo = new DealerInfo();
		dealerInfo.setNumberOfResultsReturned("0");
		
		System.out.println("webClient: " + webClient);
		String dealerCode = "26195";
		when(webClient.get()).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.uri("?dealerCode={dealerCode}", dealerCode)).thenReturn(requestHeadersSpec);
		when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
		when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
		when(responseSpec.bodyToMono(DealerInfo.class)).thenReturn(Mono.just(dealerInfo));

		assertThrows(HandlingDealerApplicationException.class, () -> service.getDealerDetails("26195"));

	}

	@Test
	void getDealerDetailsSomeResultsTest() throws Exception {
		DealerInfo dealerInfo = new DealerInfo();
		dealerInfo.setNumberOfResultsReturned("1");

		System.out.println("webClient: " + webClient);
		String dealerCode = "26195";
		when(webClient.get()).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.uri("?dealerCode={dealerCode}", dealerCode)).thenReturn(requestHeadersSpec);
		when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
		when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
		when(responseSpec.bodyToMono(DealerInfo.class)).thenReturn(Mono.just(dealerInfo));

		assertNotNull( service.getDealerDetails("26195"));

	}
}
