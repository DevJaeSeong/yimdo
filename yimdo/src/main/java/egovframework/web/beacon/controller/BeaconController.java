package egovframework.web.beacon.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.web.beacon.service.BeaconService;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class BeaconController {

	private BeaconService beaconService;
	
	@Autowired
	public BeaconController(BeaconService beaconService) {
		
		this.beaconService = beaconService;
	}
	
	@PostMapping("/beacon")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> beacon(@RequestBody Map<String, String> reciveData, HttpServletResponse response) {
		log.debug("<= {}", reciveData);
		
		beaconService.acceptBeaconSignal(reciveData);
		
		Map<String, Object> message = new HashMap<String, Object>();
		message.put("resultStatus", 1);
		message.put("data", null);
		
		return new ResponseEntity<>(message, HttpStatus.OK);
	}
}
