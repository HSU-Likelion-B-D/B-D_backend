package com.likelion.bd.domain.match.util;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.ToDoubleBiFunction;

/*
 * 도메인 교차 유사도(무드↔스타일, 프로모션↔플랫폼)와 Soft Jaccard 제공.
 *
 * Soft Jaccard (대칭형):
 *   (Σ_{a∈A} max_{b∈B} sim(a,b) + Σ_{b∈B} max_{a∈A} sim(a,b)) / (|A| + |B|)
 * - 자카드의 "의미 유사" 확장. sim∈[0,1]이면 결과도 [0,1].
 * - 시간복잡도 O(|A||B|). (A,B가 보통 작으므로 실무에서 문제 없음)
 */
public class DomainSimilarity {
    private DomainSimilarity() {
    }

    public static String norm(String s) {
        return SimilarityUtils.normalize(s);
    }

    /* Soft Jaccard (대칭형) */
    public static double softJaccard(Set<String> A, Set<String> B,
                                     ToDoubleBiFunction<String, String> simFn) {
        if (A == null) A = Collections.emptySet();
        if (B == null) B = Collections.emptySet();
        if (A.isEmpty() && B.isEmpty()) return 1.0;
        if (A.isEmpty() || B.isEmpty()) return 0.0;

        double sumA = 0.0;
        for (String a : A) {
            double best = 0.0;
            for (String b : B) best = Math.max(best, simFn.applyAsDouble(a, b));
            sumA += best;
        }
        double sumB = 0.0;
        for (String b : B) {
            double best = 0.0;
            for (String a : A) best = Math.max(best, simFn.applyAsDouble(a, b));
            sumB += best;
        }
        return (sumA + sumB) / (A.size() + B.size());
    }

    // ========= 교차 유사도 맵 =========
    // 키/값 모두 정규화하여 공백이나 영/한 표기 흔들림에 안전하게 매칭.
    private static final Map<String, Map<String, Double>> MOOD_STYLE = new HashMap<>();
    private static final Map<String, Map<String, Double>> PROMO_PLATFORM = new HashMap<>();

    //역방향 맵 추가
    private static final Map<String, Map<String, Double>> STYLE_MOOD = new HashMap<>();
    private static final Map<String, Map<String, Double>> PLATFORM_PROMO = new HashMap<>();
    static {
        // 무드 → 스타일 가중치
        put(MOOD_STYLE, "감성적인", mapOf("예술적/감각적", 0.90, "감정 중심형", 0.80, "일상 공유형", 0.60));
        put(MOOD_STYLE, "빈티지", mapOf("예술적/감각적", 0.70, "일상 공유형", 0.60, "정보전달형", 0.40));
        put(MOOD_STYLE, "러블리", mapOf("감정 중심형", 0.75, "일상 공유형", 0.70, "유머/유쾌 중심", 0.50));
        put(MOOD_STYLE, "힙한", mapOf("트렌디한", 0.90, "유머/유쾌 중심", 0.60, "도전/이벤트형", 0.60));
        put(MOOD_STYLE, "직장인", mapOf("정보전달형", 0.80, "전문가형", 0.80, "일상 공유형", 0.50));
        put(MOOD_STYLE, "자연친화적", mapOf("예술적/감각적", 0.70, "정보전달형", 0.60, "일상 공유형", 0.50));
        put(MOOD_STYLE, "조용한", mapOf("정보전달형", 0.70, "전문가형", 0.60, "일상 공유형", 0.50));
        put(MOOD_STYLE, "활기찬", mapOf("도전/이벤트형", 0.85, "유머/유쾌 중심", 0.70, "트렌디한", 0.60));
        put(MOOD_STYLE, "10대많음", mapOf("트렌디한", 0.85, "도전/이벤트형", 0.70, "유머/유쾌 중심", 0.60));
        put(MOOD_STYLE, "20대많음", mapOf("트렌디한", 0.80, "유머/유쾌 중심", 0.65, "도전/이벤트형", 0.60));
        put(MOOD_STYLE, "가족단위", mapOf("일상 공유형", 0.80, "정보전달형", 0.70, "감정 중심형", 0.50));
        put(MOOD_STYLE, "친구모임많음", mapOf("유머/유쾌 중심", 0.75, "일상 공유형", 0.65, "도전/이벤트형", 0.60));

        // 홍보 → 플랫폼 가중치
        put(PROMO_PLATFORM, "인스타그램", mapOf("인스타그램", 1.00, "릴스", 0.90, "페이스북", 0.40));
        put(PROMO_PLATFORM, "유튜브", mapOf("유튜브", 1.00, "쇼츠", 0.90));
        put(PROMO_PLATFORM, "블로그", mapOf("블로그", 1.00));
        put(PROMO_PLATFORM, "숏폼", mapOf("릴스", 0.95, "쇼츠", 0.95, "틱톡", 0.95, "인스타그램", 0.60, "유튜브", 0.60));
        put(PROMO_PLATFORM, "vlog", mapOf("유튜브", 0.80, "인스타그램", 0.60, "틱톡", 0.50));
        put(PROMO_PLATFORM, "기타", mapOf("기타", 0.60));

        reverseMap(MOOD_STYLE, STYLE_MOOD);
        reverseMap(PROMO_PLATFORM, PLATFORM_PROMO);
    }

    private static Map<String, Double> mapOf(Object... kv) {
        Map<String, Double> m = new HashMap<>();
        for (int i = 0; i < kv.length; i += 2) {
            String key = norm((String) kv[i]);
            Double val = (Double) kv[i + 1];
            m.put(key, val);
        }
        return m;
    }

    private static void put(Map<String, Map<String, Double>> root, String k, Map<String, Double> v) {
        root.put(norm(k), v);
    }

    private static void reverseMap(Map<String, Map<String, Double>> source, Map<String, Map<String,Double>> target){
        for(Map.Entry<String, Map<String, Double>> entry : source.entrySet()){
            String sourceKey = entry.getKey();
            for(Map.Entry<String,Double> innerEntry : entry.getValue().entrySet()){
                String targetKey = innerEntry.getKey();
                Double value = innerEntry.getValue();
                target.computeIfAbsent(targetKey, k-> new HashMap<>()).put(sourceKey,value);
            }
        }
    }

    /* 무드↔스타일 유사도(sim∈[0,1]). 맵에 없으면 동일어=1.0, 그 외=0.0 */
    public static double moodStyleSim(String mood, String style) {
        String m = norm(mood), s = norm(style); // 정규화(띄어쓰기/대소문자/영↔한 표기 통일)
        Map<String, Double> row = MOOD_STYLE.get(m); // 바깥 키(무드)에 대한 가중치 행
        if (row == null) return m.equals(s) ? 1.0 : 0.0; //무드가 맵에 없으면 동일어면 1.0 아니면 0.0
        return row.getOrDefault(s, m.equals(s) ? 1.0 : 0.0); //무드 행은 있지만 스타일 키가 없을 때도 동일어면 1.0, 아니면 0.0
    }

    /* 프로모션↔플랫폼 유사도(sim∈[0,1]). 맵에 없으면 동일어=1.0, 그 외=0.0 */
    public static double promoPlatformSim(String promo, String platform) {
        String p = norm(promo), f = norm(platform);
        Map<String, Double> row = PROMO_PLATFORM.get(p);
        if (row == null) return p.equals(f) ? 1.0 : 0.0;
        return row.getOrDefault(f, p.equals(f) ? 1.0 : 0.0);
    }
}
