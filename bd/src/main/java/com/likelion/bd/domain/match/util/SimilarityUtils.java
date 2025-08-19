package com.likelion.bd.domain.match.util;


import java.util.*;
import java.util.stream.Collectors;

/*
 * 매칭 점수 계산에 공통으로 쓰는 유틸들 모음.
 * - 문자열 정규화(동의어/표기 통일)
 * - 자카드 계수(Jaccard)
 * - 베이지안 보정 평균(Bayesian average)
 * - 로그 스케일 정규화(Log-scale)
 *
 * 모든 메서드는 "0~1 범위"의 안전한 값을 반환하도록 방어 로직을 포함
 */
public class SimilarityUtils {
    private SimilarityUtils() {
    }

    /*
     * 문자열 정규화:
     * - 트림 → 소문자 → 공백 제거
     * - 프로젝트 시드 기준 일부 동의어/영문 표기를 한국어 표준 라벨로 통합
     *   예) instagram/insta/인스타 → "인스타그램", reels → "릴스", shorts → "쇼츠"
     */

    public static String normalize(String s) {
        if (s == null) return "";
        String t = s.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", "");
        return t
                .replace("instagram", "인스타그램").replace("insta", "인스타그램").replace("인스타", "인스타그램")
                .replace("youtube", "유튜브").replace("yt", "유튜브").replace("유튭", "유튜브")
                .replace("reels", "릴스").replace("shorts", "쇼츠")
                .replace("브이로그", "vlog");
    }

    /* 컬렉션 → 정규화된 Set (null/빈 문자열 필터링) */
    public static Set<String> toNormalizedSet(Collection<String> raw) {
        if (raw == null) return Collections.emptySet();
        return raw.stream()
                .filter(Objects::nonNull)
                .map(SimilarityUtils::normalize)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }

    /*
     * 자카드 계수: J(A,B) = |A ∩ B| / |A ∪ B|
     * - 이산 태그(업종/토픽 등)의 "정확 매칭"에 적합
     * - A, B 둘 다 비어있으면 1.0(정보 없음으로 동일 취급), 한쪽만 비면 0.0
     */
    public static double jaccard(Set<String> a, Set<String> b) {
        if (a == null) a = Collections.emptySet();
        if (b == null) b = Collections.emptySet();
        if (a.isEmpty() && b.isEmpty()) return 1.0;
        if (a.isEmpty() || b.isEmpty()) return 0.0;

        Set<String> inter = new HashSet<>(a);
        inter.retainAll(b); //여기서 동일 문자열만 교집합으로 남는다.

        Set<String> union = new HashSet<>(a);
        union.addAll(b); //집합이므로 동일 문자열은 1개로 취급
        return (double) inter.size() / (double) union.size();
    }

    /*
     * 베이지안 보정 평균 (R, C ∈ [0,1]):
     * Adjusted = (v/(v+m))*R + (m/(v+m))*C
     * - v: 실제 리뷰 수
     * - m: 의사 리뷰 수(스무딩 강도)
     * - R: 해당 대상을 0~1로 정규화한 평균 평판
     * - C: 전역 사전 평균(0~1)
     */
    public static double bayesianAverage(double avg01, long reviewCount, double priorMean, double m) {
        double v = Math.max(0, reviewCount);
        return (v / (v + m)) * avg01 + (m / (v + m)) * priorMean;
    }

    /*
     * 도달력 로그 스케일 정규화:
     *  log10(f+1) / log10(Fref+1)  → 0~1
     * - f: 대상 팔로워 수
     * - Fref: 기준 팔로워(보통 95퍼센타일). heavy-tail 완화.
     * - Fref ≤ 0 보호, 분모 0 보호.
     */
    public static double logScale(long follower, long ref) {
        long F = Math.max(1, ref);
        double num = Math.log10(follower + 1.0);
        double den = Math.log10(F + 1.0);
        if (den == 0.0) return 0.0;
        double v = num / den;
        // 안전 범위 클램프
        if (v < 0) return 0.0;
        if (v > 1) return 1.0;
        return v;
    }
}
