package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.model.Payment;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.service.PayametService;

public class PaymentServicesImpl implements PayametService{

	 @Autowired
	    private PaymentRepository paymentRepository;

	    @Override
	    public Payment savePayment(Payment payment) {
	        return paymentRepository.save(payment);
	    }

}
