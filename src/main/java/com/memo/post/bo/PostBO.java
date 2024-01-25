package com.memo.post.bo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.memo.common.FileManagerService;
import com.memo.post.domain.Post;
import com.memo.post.mapper.PostMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j // Logger를 만든 것과 같은 효과
@Service
public class PostBO {
//	private Logger logger = LoggerFactory.getLogger(PostBO.class);
//	private Logger logger = LoggerFactory.getLogger(this.getClass);
	
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
	public int addPost(int userId, String userLoginId, 
			String subject, String content, MultipartFile file) {
		
		String imagePath = null;
		
		// file이 null이 아닐 때 => 파일을 경로값으로 변경
		if (file != null) {
			imagePath = fileManagerService.saveFile(userLoginId, file);
		}
		
		return postMapper.insertPost(userId, subject, content, imagePath);
	}
	
	// 글 상세 select
	// input: id(postId), userId / output:Post
	public Post getPostByPostIdUserId(int postId, int userId) {
		return postMapper.selectPostByPostIdUserId(postId, userId);
	}
	
	// 글 update
	// input: 파라미터들 / output:X
	public void updatePostById(
			int userId, String userLoginId, 
			int postId, String subject, String content, MultipartFile file) {
		
		// 주의★ 이미지 Case 3가지(null-null / 있음-null / null(혹은 있음)-있음
		// 기존글 가져오기(1. 이미지파일 case를 위해, 2. 업데이트 대상이 존재하는지 확인)
		Post post = postMapper.selectPostByPostIdUserId(postId, userId);
		if (post == null) {
			log.info("[글 수정] post is null. postId:{}, userId:{}", postId, userId);
			return;
		}
		
		// 파일이 있다면
		// 1. 새 이미지를 업로드
		// 2. 1번 단계가 성공하면 기존 이미지 제거(기존 이미지가 있다면)
		String imagePath = null;
		if (file != null) {
			// 업로드(서버에 그림파일을)
			imagePath = fileManagerService.saveFile(userLoginId, file);
			
			// 업로드 성공 && 기존 이미지가 있으면 -> 기존 이미지를 제거
			if (imagePath != null && post.getImagePath() != null) {
				fileManagerService.deleteFile(post.getImagePath());
			}
		}
		
		// imagePath = 채워져있거나 / NULL  => 일단 Mapper로 보낸 다음 xml에서 if로 처리
		// db 업데이트
		postMapper.updatePostByPostId(postId, subject, content, imagePath);
	}
	
	
	// 글 삭제
	// input:postId, userId / output:x
	public void deletePostByPostIdUserId(int userId, int postId) {
		// 삭제할 글이 존재하는가 확인
		Post post = postMapper.selectPostByPostIdUserId(postId, userId);
		
		// 글이 없으면
		if (post == null) { 
			log.info("[글 삭제] post is null. postId:{}, userId:{}", postId, userId);
			return;
		}	
		
		// 글이 존재하면
		// 1. DB에서 삭제 -> 삭제 성공하면 int값 튀어나옴
		int deleteRowCount = postMapper.deletePostByPostId(postId);
		
		// 2. DB에서 삭제가 성공하면 && 이미지가 있다면(사실 어차피 fileMane~~에서 null이면 알아서 처리하긴 함) 
		// -> 이미지를 삭제
		if (deleteRowCount > 0 && post.getImagePath() != null) {
			fileManagerService.deleteFile(post.getImagePath());
		}
	}
}
