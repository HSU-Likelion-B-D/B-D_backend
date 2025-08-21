package com.likelion.bd.domain.match.service;

import com.likelion.bd.domain.match.web.dto.RecommendBusinessManRes;
import com.likelion.bd.domain.match.web.dto.RecommendInfluencerRes;

import java.util.List;

public interface MatchService {
    List<RecommendInfluencerRes> top5ForBusinessMan(Long userId);
    List<RecommendBusinessManRes> top5ForInfluencer(Long userId);
}
