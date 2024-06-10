package com.jafa.task;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jafa.domain.BoardAttachVO;
import com.jafa.repository.BoardAttachRepository;

@Component
public class FileCheckTask {
	
	@Autowired
	BoardAttachRepository boardAttachRepository;
	
	// 초/분/시/날/달/요일/(연도:선택)
//	@Scheduled(cron = "2 * * * * *")
	public void checkFile() {
		// 데이터베이스에 기록된 파일 정보 
		List<BoardAttachVO> fileList = boardAttachRepository.pastFiles();
		
		// 데이터베이스에 기록된 파일 정보 기반으로 Path객체 생성 
		List<Path> fileListPath = fileList.stream()
			.map(vo-> Paths.get("c:/storage/board",vo.getUploadPath(),vo.getUuid()+"_"+vo.getFileName()))
			.collect(Collectors.toList());
		
		// 썸네일 파일 경로 
		fileList.stream()
			.map(vo-> Paths.get("c:/storage/board",vo.getUploadPath(),"s_"+vo.getUuid()+"_"+vo.getFileName()))
			.forEach(e-> fileListPath.add(e));
		// 실제 업로드된 파일 
		File targetDir = Paths.get("c:/storage/board",getYesterdayFolder()).toFile();
		File[] delTargetList = targetDir.listFiles(file -> !fileListPath.contains(file.toPath()));
		Arrays.stream(delTargetList).forEach(f -> f.delete());
	}

	private String getYesterdayFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Calendar cal = Calendar.getInstance(); 
		cal.add(Calendar.DATE, -0);
		return sdf.format(cal.getTime());
	}
	
}
