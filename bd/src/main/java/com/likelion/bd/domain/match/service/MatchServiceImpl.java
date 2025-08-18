package com.likelion.bd.domain.match.service;

import com.likelion.bd.domain.match.web.dto.RecommandInfluencerRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    //    private final BusinessManMatchRepository businessManRepo;
//    private final InfluencerMatchRepository influencerRepo;
//    private final MatchProperties props;
    @Override
    public RecommandInfluencerRes top5ForBusinessMan(Long businessManId) {
        return null;
    }
}
