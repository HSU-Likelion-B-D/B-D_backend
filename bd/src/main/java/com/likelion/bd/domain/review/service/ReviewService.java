package com.likelion.bd.domain.review.service;

import com.likelion.bd.domain.review.web.dto.ReviewCreateReq;
import com.likelion.bd.domain.review.web.dto.ReviewKeywordRes;
import com.likelion.bd.global.jwt.UserPrincipal;

public interface ReviewService {

    void createReview(ReviewCreateReq reviewCreateReq, UserPrincipal userPrincipal);

    ReviewKeywordRes extractKeywordsForUser(UserPrincipal userPrincipal);
}
