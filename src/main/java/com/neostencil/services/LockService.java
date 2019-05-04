package com.neostencil.services;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neostencil.model.entities.Lock;
import com.neostencil.model.entities.LockId;
import com.neostencil.model.repositories.LockRepository;
import com.neostencil.requests.LockRequest;
import com.neostencil.responses.LockResponse;
import com.neostencil.security.JwtTokenUtil;

/**
 * 
 * @author shilpa
 *
 */
@Service
public class LockService {

	@Autowired
	LockRepository lockRepository;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	/**
	 * For acquiring a new lock at an entity
	 * 
	 * @param request
	 * @return
	 */
	public LockResponse acquireLock(LockRequest request) {
		LockResponse response = new LockResponse();

		if (request != null) {
			LockId id = new LockId(request.getEntityId(), request.getEntityType());

			Optional<Lock> lock = lockRepository.findById(id);
			if (lock.isPresent() && !lock.get().getAcquiredBy().equals(jwtTokenUtil.getLoggedInUserName())) {
				response.setSuccess(false);
				response.setAcquiredAt(lock.get().getAcquiredAt());
				response.setAcquiredBy(lock.get().getAcquiredBy());
			} else {
				response.setSuccess(true);

				Timestamp acquiredAt = Timestamp.valueOf(LocalDateTime.now());
				String acquiredBy = jwtTokenUtil.getLoggedInUserName();
				Lock newLock = new Lock(id, acquiredAt, acquiredBy);

				lockRepository.saveAndFlush(newLock);
			}
		}
		return response;
	}

	/**
	 * For releasing an already acquired lock
	 * 
	 * @param request
	 * @return
	 */
	public LockResponse releaseLock(LockRequest request) {
		LockResponse response = new LockResponse();

		if (request != null) {
			LockId id = new LockId(request.getEntityId(), request.getEntityType());

			Optional<Lock> lock = lockRepository.findById(id);
			if (lock.isPresent()) {
				lockRepository.delete(lock.get());
				response.setSuccess(true);
			} else {
				response.setSuccess(false);
			}
		}
		return response;

	}
}
