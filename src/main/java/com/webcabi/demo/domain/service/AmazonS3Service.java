package com.webcabi.demo.domain.service;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.util.IOUtils;
import com.webcabi.demo.domain.AmazonS3Properties;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AmazonS3Service {
	
	@Autowired
	TransferManager transferManager;
	
	@Autowired
	AmazonS3Properties properties;
	
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
	
	public ResponseEntity<byte[]> downloadFile(String filename) throws IOException {
		AmazonS3 client = transferManager.getAmazonS3Client();
		S3Object object = client.getObject(properties.getBucket(), filename);
		
		byte[] bytes = IOUtils.toByteArray(object.getObjectContent());
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		httpHeaders.setContentLength(bytes.length);
		httpHeaders.setContentDispositionFormData("attachment", filename);
		
		return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
	}
	
	public ResponseEntity<byte[]> downloadImage(String filename) throws IOException {
		AmazonS3 client = transferManager.getAmazonS3Client();
		S3Object object = client.getObject(properties.getBucket(), filename);
		
		byte[] bytes = IOUtils.toByteArray(object.getObjectContent());
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.IMAGE_PNG);
		httpHeaders.setContentLength(bytes.length);
		
		return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
	}
}
