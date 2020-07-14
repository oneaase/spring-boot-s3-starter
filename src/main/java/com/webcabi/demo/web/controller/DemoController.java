package com.webcabi.demo.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class DemoController {
	
	@GetMapping("/")
	public String getUploadForm(Model model) {
		
		return "upload";
	}
	
	@PostMapping("/upload")
	public String upload(MultipartFile file) throws IOException {
		
		String storeDir = "/private/tmp/upload";
		Path storeDirPath = Paths.get(storeDir);
		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		
		InputStream input = file.getInputStream();
		Files.copy(input, storeDirPath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
		
		return "redirect:/";
	}
}
