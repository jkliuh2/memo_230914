package com.memo.post.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.memo.post.domain.Post;

@Mapper
public interface PostMapper {

	// input:X / output:List<Map>
	public List<Map<String, Object>> selectPostList();
	
	// input:loginId / output:List<Post>
	public List<Post> selectPostListByUserId(int userId);
	
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
}
