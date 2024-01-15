package com.memo.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.memo.user.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
	// <엔티티, pk의 타입>
	
	// 내가 만든 메소드인 경우 여기서도 만들어 줘야 함(기본제공 아닌 경우)
	
	// null or UserEntity(단건)
	public UserEntity findByLoginId(String loginId);
	
	// null or UserEntity(단건)
	public UserEntity findByLoginIdAndPassword(String loginId, String password);
}
