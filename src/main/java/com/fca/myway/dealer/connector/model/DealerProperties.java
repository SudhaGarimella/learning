package com.fca.myway.dealer.connector.model;

import lombok.Getter;

@Getter
public class DealerProperties {

	private String dealerConnectURL;

	public DealerProperties(String dealerConnectURL) {
		this.dealerConnectURL = dealerConnectURL;
	}

}
