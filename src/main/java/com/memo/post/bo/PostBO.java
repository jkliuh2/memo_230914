package com.memo.post.bo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.memo.common.FileManagerService;
import com.memo.post.domain.Post;
import com.memo.post.mapper.PostMapper;

@Service
public class PostBO {
	
	@Autowired
	private PostMapper postMapper;
	
	@Autowired
	private FileManagerService fileManagerService;

	// userId로 글목록 조회
	// input:userId / output:List<Post>
	public List<Post> getPostListByUserId(int userId) {
		return postMapper.selectPostListByUserId(userId);
	}
	
	// insert
	// input:params(5개) / output:int
	public int addPost(int userId, String userLoginid, 
			String subject, String content, MultipartFile file) {
		
		String imagePath = null;
		
		// file이 null이 아닐 때 => 파일을 경로값으로 변경
		if (file != null) {
			imagePath = fileManagerService.saveFile(userLoginid, file);
		}
		
		return postMapper.insertPost(userId, subject, content, imagePath);
	}
}
