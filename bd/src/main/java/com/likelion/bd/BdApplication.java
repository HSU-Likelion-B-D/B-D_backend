package com.likelion.bd;

import com.likelion.bd.domain.match.config.MatchProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties(MatchProperties.class) // ← 이 한 줄로 빈 등록 + 바인딩 활성화
public class BdApplication {

	public static void main(String[] args) {
		SpringApplication.run(BdApplication.class, args);
	}

}
