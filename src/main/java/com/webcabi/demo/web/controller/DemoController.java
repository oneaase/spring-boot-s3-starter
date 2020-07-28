package com.webcabi.demo.web.controller;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.webcabi.demo.domain.service.AmazonS3Service;
import com.webcabi.demo.web.model.UploadForm;

@Controller
public class DemoController {
	
	@Autowired
	private AmazonS3Service awsS3Service;
	
	@GetMapping("/")
	public String getUploadForm(Model model) {
		model.addAttribute("form", new UploadForm());
		
		return "upload";
	}
	
	@RequestMapping("/image/{filename}")
	@ResponseBody
	public ResponseEntity<byte[]> downloadImage(@PathVariable("filename") String filename) throws IOException {
		return awsS3Service.downloadImage(filename);
	}
	
	@RequestMapping("/download/{filename}")
	@ResponseBody
	public ResponseEntity<byte[]> downloadFile(@PathVariable("filename") String filename) throws IOException {
		return awsS3Service.downloadFile(filename);
	}
	
	@PostMapping("/upload")
	public String upload(UploadForm files) throws IOException {
		
		for (MultipartFile file : files.getFiles()) {
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentLength(file.getSize());
			
			InputStream input = file.getInputStream();
			awsS3Service.upload(file.getOriginalFilename(), input, objectMetadata);
		}
		
		return "redirect:/";
	}
}
