package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;

@SpringBootTest
class DemoApplicationTests {

	@Test
	void contextLoads() {
		//对原始密码加密
		//每次加密的密码都不一样
		String hashpw = BCrypt.hashpw("123456", BCrypt.gensalt());
		System.out.println(hashpw);
		//校验原始密码和BCrypt密码是否一致
		boolean checkpw = BCrypt.checkpw("123", hashpw);
		System.out.println(checkpw);
	}

}
