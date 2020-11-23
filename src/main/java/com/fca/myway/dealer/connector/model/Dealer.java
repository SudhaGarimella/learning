package com.fca.myway.dealer.connector.model;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString

public class Dealer {

	String dealerCode;
	List<DealerObjects> dealerObjects;
	
}
