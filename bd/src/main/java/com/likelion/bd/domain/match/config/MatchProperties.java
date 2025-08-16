package com.likelion.bd.domain.match.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/*
 * 매칭 점수 계산에 쓰이는 모든 상수/가중치.
 * - application.yml이 없어도 "아래 기본값"으로 동작
 * - yml이 있으면 match.* 값으로 override 된다.
 */

@Getter
@Setter
@ConfigurationProperties(prefix = "match")
public class MatchProperties {

    // 최종 가중치 (합 1.0 권장)
    //마지막에 계산할때 이 값들을 곱할 것.
    //뭐가 더 비중이 있다고 판단되는지에 따라 합 1.0에서 바꾸면 된다.
    private double wCategory      = 0.45;
    private double wMoodStyle     = 0.15;
    private double wPromoPlatform = 0.15;
    private double wReputation    = 0.15;
    private double wReach         = 0.10;

    // 전역 사전평균 C 스무딩: C = (N/(N+mC))*globalAvg01 + (mC/(N+mC))*C0
    //리뷰가 mC개쯤 쌓이기 전에는 c0(기준 평균)을 반영하라는 의미이다.
    //mC가 클수록 초기에 c0의 영향이 강해질 것이다 -> 리뷰의 개수가 mC에 도달하는게 시간이 오래 걸리겠죠?
    //mC가 작으면 c0의 영향이 약해진다. (무조건 작다고 좋은 것은 아니다. 20~50에서 조절해 나가는 것이 좋다.)
    //c0가 너무 낙관적이거나(0.9) 비관적이면(0.5) 안좋은 계정이 과대평가 되거나 좋은 계정이 눌릴 수 있다.
    private double c0 = 0.75;     // 리뷰 적을 때 기준 평균(=3.75/5)
    private double mC = 30.0;     // 스무딩 강도(의사 리뷰 수)
    private double cClampMin = 0.60; // 안전 클램프
    private double cClampMax = 0.95;

    // 개별 m (후보군 리뷰 중앙값 기반, 아래 범위로 클램프)
    //비정상적으로 높거나 낮은 전역 평균을 막는 장치
    private double mFallback = 6.0;
    private double mClampMin = 3.0;
    private double mClampMax = 12.0;

    // 도달력 로그 정규화 기준 백분위
    private double reachPercentile = 0.95;
}
