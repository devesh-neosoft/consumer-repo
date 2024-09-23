package com.devesh.service;

import com.devesh.exception.CodeException;
import com.devesh.exception.ErrorCode;
import com.devesh.model.Customer;
import com.devesh.repositories.CustomerRepository;
import com.devesh.util.GenericResponse;
import com.devesh.util.KafkaConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.List;


@Service
@Slf4j
public class CustomerService {

	@Autowired
	public CustomerRepository studentRepository;

	@KafkaListener(topics = KafkaConstants.TOPIC, groupId = KafkaConstants.GROUP_ID)
	public Customer listener(Customer s) {
        log.info("***Msg received from Kafka Topic ::{}", s);
		studentRepository.save(s);
		return s;
	}

	public ResponseEntity<GenericResponse<List<Customer>>> getAllCustomer() {
		List<Customer> collect;
		try {
			List<Customer> students = studentRepository.findAll();
			if (!CollectionUtils.isEmpty(students)) {
				collect = students;
			} else {
				throw new CodeException("User Not found", ErrorCode.COMMON);
			}
			return ResponseEntity.ok(
					new GenericResponse<>("Users Fetched Successfully", true, HttpStatus.OK.value(), collect)
			);
		} catch (CodeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new GenericResponse<>(e.getMessage(), false, ErrorCode.COMMON.getCode(), null));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new GenericResponse<>("Internal Server Error", false, HttpStatus.INTERNAL_SERVER_ERROR.value(), null));
		}

	}
}
