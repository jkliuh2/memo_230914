package com.memo.post.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.memo.post.domain.Post;

@Mapper
public interface PostMapper {

	// post-list-view에 보내는 List
	// 3. 페이징 정보 없음 + 1. 다음
	// input:loginId + 추가정보들 / output:List<Post>
	public List<Post> selectPostListByUserId(
			@Param("userId") int userId,
			@Param("standardId") Integer standardId,
			@Param("direction") String direction,
			@Param("limit") int limit
			);
	
	// 마지막 id select
	public int selectPostIdByUserIdSort(
			@Param("userId") int userId, 
			@Param("sort") String sort);
	
	// insert
	// input:userId, subject, content, imagePath / output:int
	public int insertPost(
			@Param("userId") int userId, 
			@Param("subject") String subject, 
			@Param("content") String content,
			@Param("imagePath") String imagePath);
	
	// select
	// input: id(postId), userId / output: Post
	public Post selectPostByPostIdUserId(
			@Param("postId") int postId, 
			@Param("userId") int userId);
	
	// update
	// input: id(postId), subject, content, imagePath / output:X
	public void updatePostByPostId(
			@Param("postId") int postId,
			@Param("subject") String subject, 
			@Param("content") String content,
			@Param("imagePath") String imagePath);
	
	// delete
	// input: id(postId) / output:int(성공시 1 리턴)
	public int deletePostByPostId(int postId);
}
