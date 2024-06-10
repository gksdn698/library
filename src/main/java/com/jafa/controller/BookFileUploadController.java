package com.jafa.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jafa.domain.BookAttachVO;

import net.coobird.thumbnailator.Thumbnailator;

@RequestMapping("/bookfiles")
@RestController
public class BookFileUploadController {

	@PreAuthorize("isAuthenticated()")
    @PostMapping("/upload")
    public ResponseEntity<List<BookAttachVO>> upload(@RequestParam("uploadFile") MultipartFile[] multipartFiles){
        List<BookAttachVO> list = new ArrayList<BookAttachVO>();
        
        // Book 파일 경로
        File uploadPath = new File("c:/storage/book/", getFolder());
        
        if(!uploadPath.exists()) {
            uploadPath.mkdirs();
        }
        
        for(MultipartFile multipartFile : multipartFiles) {
            BookAttachVO attachVO = new BookAttachVO();
            String fileName = multipartFile.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            
            File saveFile = new File(uploadPath, uuid + "_" + fileName);
            
            attachVO.setFileName(fileName);
            attachVO.setUuid(uuid);
            attachVO.setUploadPath(getFolder());
            
            try {
                if (checkImageType(saveFile)) { // 이미지 파일이면
                    attachVO.setFileType(true);
                    FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_" + uuid + "_" + fileName));
                    Thumbnailator.createThumbnail(multipartFile.getInputStream(), thumbnail, 200, 200);    
                }
                // 파일 저장
                multipartFile.transferTo(saveFile);
                list.add(attachVO);
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<List<BookAttachVO>>(list, HttpStatus.OK);
    }
	
	@GetMapping("/display")
	public ResponseEntity<byte[]> getFile(String fileName){
		File file = new File("c:/storage/book/"+fileName);
		HttpHeaders headers = new HttpHeaders();
		ResponseEntity<byte[]> result = null;
		try {
			headers.add("Content-Type", Files.probeContentType(file.toPath()));
			result = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(file),headers,HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@PostMapping("/deleteFile")
	public ResponseEntity<String> deleteFile(BookAttachVO vo){
		File file = new File("c:/storage/book/"+vo.getUploadPath()+"/"+vo.getUuid()+"_"+vo.getFileName());
		file.delete();
		if(vo.isFileType()) {
			file = new File("c:/storage/book/"+vo.getUploadPath()+"/s_"+vo.getUuid()+"_"+vo.getFileName());
			file.delete();
		}
		return new ResponseEntity<String>("success",HttpStatus.OK);
	}
	
	@GetMapping("/download")
	public ResponseEntity<Resource> downloadFile(String fileName){
		Resource resource = new FileSystemResource("c:/storage/book/"+fileName);
		
		HttpHeaders headers = new HttpHeaders();
		
		if(!resource.exists()) { // 파일이 없는 경우
			return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
		}
		String resourceName = resource.getFilename();
		String resourceOriginName = resourceName.substring(resourceName.indexOf("_")+1);
		String downloadName = null;
		
		try {
			downloadName = URLEncoder.encode(resourceOriginName,"utf-8");
			headers.add("Content-Disposition", "attaachment;fileName="+downloadName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Resource>(resource,headers,HttpStatus.OK);
	}
	
	private String getFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		return sdf.format(new Date());
	}
	
	private boolean checkImageType(File file) throws IOException {
		String contentType = Files.probeContentType(file.toPath());
		return contentType != null ? contentType.startsWith("image") : false;
	}
}
