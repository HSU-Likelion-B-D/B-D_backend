package com.likelion.bd.domain.match.util;


import java.util.ArrayList;
import java.util.List;

/*
 * 간단한 통계 유틸:
 * - 퍼센타일(선형 보간)
 * - 중앙값
 * - 클램프(범위 제한)
 *
 * 팔로워/리뷰는 Long 범위로 충분하므로 long 버전만 제공.
 */
public class StatsUtils {
    private StatsUtils(){}

    /*
     * p∈[0,1] 퍼센타일 (선형 보간, 오름차순 기준)
     * - 리스트가 비어있으면 0 반환(방어)
     * - p<=0 → 최솟값, p>=1 → 최댓값
     * - 예: p=0.95 → 상위 5% 경계값
     */

    public static long percentile(List<Long> values, double p) {
        if (values == null || values.isEmpty()) return 0L;
        List<Long> sorted = new ArrayList<>(values);
        sorted.sort(Long::compareTo);
        int n = sorted.size();
        if (p <= 0) return sorted.get(0);
        if (p >= 1) return sorted.get(n-1);

        double idx = p * (n - 1);
        int lo = (int) Math.floor(idx);
        int hi = (int) Math.ceil(idx);
        if (lo == hi) return sorted.get(lo);

        double frac = idx - lo;
        // 선형 보간 후 반올림
        return Math.round(sorted.get(lo) + frac * (sorted.get(hi) - sorted.get(lo)));
    }

    /* 중앙값 = percentile(values, 0.5) */
    public static long median(List<Long> values) {
        return percentile(values, 0.5);
    }

    /* 실수 범위 고정 */
    public static double clamp(double v, double lo, double hi) {
        return Math.max(lo, Math.min(hi, v));
    }
}
