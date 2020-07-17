package com.webcabi.demo.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;

@Configuration
public class AWSS3Config {
	
	@Bean(destroyMethod = "shutdownNow")
	public TransferManager transferManager() {
		return TransferManagerBuilder.standard().build();
	}
}
