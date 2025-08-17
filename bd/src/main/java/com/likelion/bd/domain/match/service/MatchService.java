package com.likelion.bd.domain.match.service;

import com.likelion.bd.domain.match.web.dto.RecommandInfluencerRes;

import java.util.List;

public interface MatchService {
    RecommandInfluencerRes top5ForBusinessMan(Long businessManId);
}
