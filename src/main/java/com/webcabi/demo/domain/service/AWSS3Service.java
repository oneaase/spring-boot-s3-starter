package com.webcabi.demo.domain.service;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.webcabi.demo.domain.AWSS3Properties;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AWSS3Service {
	
	@Autowired
	TransferManager transferManager;
	
	@Autowired
	AWSS3Properties properties;
	
	public void upload(String key, InputStream input, ObjectMetadata objectMetadata) {
		try {
			log.info("Uploading " + key + " to s3://" + properties.getBucket());
			Upload upload = transferManager.upload(properties.getBucket(), key, input, objectMetadata);
			upload.waitForUploadResult();
		} catch (AmazonServiceException ex) {
			ex.printStackTrace();
		} catch (AmazonClientException ex) {
			ex.printStackTrace();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}
}
