package com.memo.post.bo;

import java.util.Collections;
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
	
	// 페이징 필드
	private static final int POST_MAX_SIZE = 3; // 지금은 3으로 고정. 필요하다면 변경or 아예 클래스로 만들수도.

	// userId로 글목록 조회
	// input:userId / output:List<Post>
	public List<Post> getPostListByUserId(int userId, Integer prevId, Integer nextId) {
		// 게시글 번호 10 9 8 | 7 6 5 | 4 3 2 | 1
		
		// 예시)  만약 4 3 2 페이지에 있을 때.
		// 1) 다음: 2보다 작은 3개 DESC -> 그냥 알아서 가져와진다.
		// 2) 이전: 4보다 큰 3개 ASC(5 6 7) -> 이걸 다시 뒤집기 List reverse(7 6 5)
		// 3) 페이징 정보 없음: 최신순 3개 DESC -> 끝
		
		Integer standardId = null; // 기준이 되는 postId
		String direction = null; // 방향(이전, 다음, 정보없음)
		
		if (prevId != null) { // 1) 이전
			standardId = prevId; 
			direction = "prev";
			
			// 이전) 리턴 
			List<Post> postList = postMapper.selectPostListByUserId(userId, standardId, direction, POST_MAX_SIZE);
			
			// ASC 상태 뒤집기 - reverse List
			Collections.reverse(postList); // 뒤집고 저장까지 해줌
			
			return postList;
			
		} else if(nextId != null) { // 2) 다음
			standardId = nextId; 
			direction = "next";
		}
		// 3) 페이징 정보 없음 -> 둘 다 null
		
		// 리턴 - 3. 페이징정보 없음 + 1. 다음(얘도 그냥 이거 쓰면 되겠다)
		return postMapper.selectPostListByUserId(userId, standardId, direction, POST_MAX_SIZE);
	}
	
	
	// 마지막 페이지 확인 메소드(이전)
	public boolean isPrevLastPageByUserId(int userId, int prevId) {
		// prevId는 페이지가 비어있어도 0으로 들어옴.
		
		// userId, sort(방향)
		int postId = postMapper.selectPostIdByUserIdSort(userId, "DESC"); // 가장 최신 id값 select
		return postId == prevId; // 같으면 (마지막)-> true / 다르면 false
	}
	
	// 마지막 페이지 확인(다음)
	public boolean isNextLastPageByUserId(int userId, int nextId) {
		int postId = postMapper.selectPostIdByUserIdSort(userId, "ASC"); 
		return postId == nextId; // 같으면 true(마지막) / 다르면 false
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
