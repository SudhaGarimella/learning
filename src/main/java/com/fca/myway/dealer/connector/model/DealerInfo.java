package com.fca.myway.dealer.connector.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString

public class DealerInfo {

	String numberOfResultsReturned;

	private List<Dealer> dealer = new ArrayList<>();



}
