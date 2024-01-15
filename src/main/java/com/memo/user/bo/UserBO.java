package com.memo.user.bo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.memo.user.entity.UserEntity;
import com.memo.user.repository.UserRepository;

@Service
public class UserBO {
	
	@Autowired
	private UserRepository userRepository;

	// loginId로 중복확인
	// input:loginId / output: UserEntity(있거나 or null) JPA로 아예 한 행을 가져올 것임.
	public UserEntity getUserEntityByLoginId(String loginId) {
		return userRepository.findByLoginId(loginId);
	}
	
	// 회원가입
	// input: 파라미터 4개 / output: Integer id(pk) 
	// (Entity가 아닌 key로 리턴)(실패할 수도 있으니 int가 아닌 Integer 
	public Integer addUser(String loginId, String password, String name, String email) {
		UserEntity userEntity = userRepository.save( // save()메소드는 기본제공. insert된 값을 다시 리턴해줌
					UserEntity.builder()
						.loginId(loginId)
						.password(password)
						.name(name)
						.email(email)
						.build()
				); // builder로 만들자마자 save()로 넘기는 코드.
		
		return userEntity == null ? null : userEntity.getId(); 
	}
	
	// 로그인
	// input: loginId, password / output:UserEntity
	public UserEntity getUserEntityByLoginIdPassword(String loginId, String password) {
		return userRepository.findByLoginIdAndPassword(loginId, password);
	}
}
