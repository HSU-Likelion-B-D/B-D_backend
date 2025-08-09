package com.likelion.bd.global.init;

import com.likelion.bd.domain.influencer.entity.ContentStyle;
import com.likelion.bd.domain.influencer.entity.ContentTopic;
import com.likelion.bd.domain.influencer.entity.Platform;
import com.likelion.bd.domain.influencer.entity.PreferTopic;
import com.likelion.bd.domain.influencer.repository.ContentStyleRepository;
import com.likelion.bd.domain.influencer.repository.ContentTopicRepository;
import com.likelion.bd.domain.influencer.repository.PlatformRepository;
import com.likelion.bd.domain.influencer.repository.PreferTopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final PlatformRepository platformRepository;
    private final ContentTopicRepository contentTopicRepository;
    private final ContentStyleRepository contentStyleRepository;
    private final PreferTopicRepository preferTopicRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        // 활동 플랫폼 데이터 초기화
        if (platformRepository.count() == 0) {
            List<String> platformNames = Arrays.asList(
                    "인스타그램", "유튜브", "블로그", "틱톡", "릴스", "쇼츠", "페이스북", "기타"
            );
            platformNames.forEach(name -> {
                Platform platform = Platform.builder().name(name).build();
                platformRepository.save(platform);
            });
        }

        // 콘텐츠 분야 데이터 초기화
        if (contentTopicRepository.count() == 0) {
            List<String> topicNames = Arrays.asList(
                    "음식/카페", "뷰티", "패션", "헬스/피트니스", "키즈", "교육/정보",
                    "VLOG", "게임/IT", "반려동물", "영화/드라마", "음악/댄스", "기타"
            );
            topicNames.forEach(name -> {
                ContentTopic topic = ContentTopic.builder().name(name).build();
                contentTopicRepository.save(topic);
            });
        }

        // 콘텐츠 스타일 데이터 초기화
        if (contentStyleRepository.count() == 0) {
            List<String> styleNames = Arrays.asList(
                    "정보전달형", "유머/유쾌 중심", "감정 중심형", "전문가형",
                    "예술적/감각적", "도전/이벤트형", "트렌디한", "일상 공유형", "기타"
            );
            styleNames.forEach(name -> {
                ContentStyle style = ContentStyle.builder().name(name).build();
                contentStyleRepository.save(style);
            });
        }

        // 선호 분야 데이터 초기화
        if (preferTopicRepository.count() == 0) {
            List<String> industryNames = Arrays.asList(
                    "음식/음료", "쇼핑/소매", "반려동물", "뷰티/서비스",
                    "운동/건강", "문화/체험", "콘텐츠", "기타"
            );
            industryNames.forEach(name -> {
                PreferTopic topic = PreferTopic.builder().name(name).build();
                preferTopicRepository.save(topic);
            });
        }
    }
}
