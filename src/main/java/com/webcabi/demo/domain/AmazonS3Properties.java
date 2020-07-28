package com.webcabi.demo.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "cloud.aws.s3")
@Data
public class AmazonS3Properties {
	
	private String bucket;
}
