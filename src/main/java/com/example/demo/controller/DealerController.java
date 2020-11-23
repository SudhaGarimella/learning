package com.example.demo.controller;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fca.myway.dealer.connector.constants.ApplicationConstants;
import com.fca.myway.dealer.connector.exceptions.HandlingDealerAPIException;
import com.fca.myway.dealer.connector.exceptions.HandlingDealerApplicationException;
import com.fca.myway.dealer.connector.model.DealerInfo;
import com.fca.myway.dealer.connector.service.DealerService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(ApplicationConstants.BASE_REQUEST_PATH)
@Slf4j
public class DealerController {

	@Autowired
	DealerService service;

	@GetMapping("/{id}")
	@ApiOperation(value = "Returns the Dealer information based on the dealer code passed", notes = "Dealer Code needs to be passed as an input for which the dealer details such as address are returned", response = DealerInfo.class)
	public @ResponseBody DealerInfo getDealerDetails(@PathVariable("id") String dealerCode) {
		log.info("getDealerDetails() dealerCode =  " + dealerCode);

		DealerInfo info = null;
		try {
			info = service.getDealerDetails(dealerCode);
		} catch (HandlingDealerAPIException e) {
			log.error("EXCEPTION: getDealerDetails(): " + e.getMessage());
		} catch (HandlingDealerApplicationException e) {
			log.error("EXCEPTION: getDealerDetails(): " + e.getMessage());
		}
		log.info("getDealerDetails() Output- DealerInfo" + info);
		return info;

	}

	

}
