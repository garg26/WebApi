package com.neostencil.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.neostencil.requests.LockRequest;
import com.neostencil.responses.LockResponse;
import com.neostencil.services.LockService;

/**
 * 
 * @author shilpa
 *
 */
@Controller
@RequestMapping(value = "/api/v1")
public class LockController {

	@Autowired
	LockService lockService;

	@RequestMapping(value = "/acquire-lock", method = RequestMethod.POST)
	@ResponseBody
	LockResponse acquireLock(@Valid @RequestBody LockRequest request) {
		LockResponse response = lockService.acquireLock(request);
		return response;
	}

	@RequestMapping(value = "/release-lock", method = RequestMethod.DELETE)
	@ResponseBody
	LockResponse releaseLock(@Valid @RequestBody LockRequest request) {
		LockResponse response = lockService.releaseLock(request);
		return response;
	}

}
