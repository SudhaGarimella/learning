package com.example.demo.controller;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import com.fca.myway.dealer.connector.constants.ApplicationConstants;
import com.fca.myway.dealer.connector.constants.FrameworkConstants;
import com.fca.myway.dealer.connector.exceptions.HandlingDealerAPIException;
import com.fca.myway.dealer.connector.exceptions.HandlingDealerApplicationException;
import com.fca.myway.dealer.connector.model.DealerInfo;
import com.fca.myway.dealer.connector.model.DealerProperties;
import com.fca.myway.dealer.connector.util.FrameworkUtil;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

@Service
@Slf4j
@Getter
public class DealerService {
	WebClient webClient;
	@Autowired
	DealerProperties dealerProps;

	@PostConstruct
	public void init() {
		log.info("Init()- Start");
		TcpClient tcpClient = TcpClient.create().option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
				.doOnConnected(connection -> {
					connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
					connection.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
				});

		webClient = WebClient.builder().baseUrl(dealerProps.getDealerConnectURL())
				.clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient))).filter(logRequest())
				.filter(logResponse()).build();
		log.info("Init()- End");
	}

	public DealerInfo getDealerDetails(String dealerCode)
			throws HandlingDealerAPIException, HandlingDealerApplicationException {
		log.info("getDealerDetails() dealerCode =  " + dealerCode);
		if ("".equals(dealerCode)) {
			throw new HandlingDealerApplicationException(ApplicationConstants.EXCEPTION_BLANK_DEALER_CODE);
		}

		log.info("getDealerDetails() - client" + webClient);
		if (webClient == null) {
			throw new HandlingDealerApplicationException(
					FrameworkConstants.EXCEPTION_WEB_CLIENT_NULL + "dealerCode: " + dealerCode);

		}
		DealerInfo info = webClient.get().uri("?dealerCode={dealerCode}", dealerCode).retrieve()
				.onStatus(HttpStatus::isError,
						clientResponse -> Mono.error(new HandlingDealerAPIException(FrameworkUtil.getAppNameWithLog(
								FrameworkConstants.EXCEPTION_ACCESSING_EXT_URL + " for dealerCode: " + dealerCode),
								clientResponse.statusCode())))
				.onStatus(HttpStatus::is5xxServerError,
						clientResponse -> Mono
								.error(new HandlingDealerAPIException(
										FrameworkUtil.getAppNameWithLog(
												FrameworkConstants.EXCEPTION_ACCESSING_EXT_URL_FIVE_XX
														+ " for dealerCode: " + dealerCode),
										clientResponse.statusCode())))
				.onStatus(HttpStatus::is4xxClientError,
						clientResponse -> Mono
								.error(new HandlingDealerAPIException(
										FrameworkUtil.getAppNameWithLog(
												FrameworkConstants.EXCEPTION_ACCESSING_EXT_URL_FOUR_XX
														+ " for dealerCode: " + dealerCode),
										clientResponse.statusCode())))
				.bodyToMono(DealerInfo.class).block();
		if (info == null || info.getNumberOfResultsReturned() == null
				|| "0".equals(info.getNumberOfResultsReturned())) {
			throw new HandlingDealerApplicationException(FrameworkUtil
					.getAppNameWithLog(ApplicationConstants.EXCEPTION_NO_RESULTS + " for dealerCode: " + dealerCode));

		}

		log.info("getDealerDetails() Output- DealerInfo" + info);
		return info;

	}

	private ExchangeFilterFunction logRequest() {
		return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
			log.info("Request " + clientRequest.method() + " URI : " + clientRequest.url());
			return Mono.just(clientRequest);
		});
	}

	private ExchangeFilterFunction logResponse() {
		return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
			log.info("Response Status Code " + clientResponse.statusCode());
			return Mono.just(clientResponse);
		});
	}

}
