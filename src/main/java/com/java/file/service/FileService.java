package com.java.file.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.java.file.dao.FileDaoInterface;
import com.java.util.HttpUtil;

@Service
public class FileService implements FileServiceInterface {
	
	@Autowired
	FileDaoInterface FDI;
	
	@Override
	public HashMap<String, Object> fileUpload(MultipartFile[] files, String dir, HttpServletRequest req) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		
		HashMap<String, Object> param = HttpUtil.getParamMap(req); // 유저넘버 게시판넘버를 받아온다.
		
		for(int i = 0; i < files.length; i++) {
			HashMap<String, Object> fileMap = new HashMap<String, Object>();
			String fileNm = files[i].getOriginalFilename();
			
			try {
				byte[] bytes = files[i].getBytes();
				String path = "/var/www/html/resources/" + dir + "/";
//				String path = req.getSession().getServletContext().getRealPath("/") + "resources/" + dir + "/";
				String dns = "http://gudi.iptime.org:10090/";
				
				File dirF = new File(path);
				
				if(!dirF.exists()) {
					dirF.mkdirs();
				}
				
				File f = new File(path + fileNm);
				OutputStream out = new FileOutputStream(f);
				out.write(bytes);
				out.close();
				
				fileMap.put("fileName", fileNm);
				fileMap.put("filePath", path);
				fileMap.put("fileURL", dns + "resources/" + dir + "/" + fileNm);
				
				fileMap.put("boardNo", param.get("boardNo"));
				fileMap.put("userNo", param.get("userNo"));
				
				/*************************************************************************/
				// DB 연결부분
				FDI.insert(fileMap); // fileMap이 Dao의 param 부분에 들어가야 하므로, fileMap.put 부분을 미리 써 받은 파라미터 값들을 보내준다.
				 
				  
				/*************************************************************************/
				
				list.add(fileMap);				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		map.put("upload", list);
		
		return map;
	}
	
}
