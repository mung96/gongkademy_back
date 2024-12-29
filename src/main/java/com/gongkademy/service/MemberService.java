package com.gongkademy.service;

import com.gongkademy.service.dto.UpdateProfileRequest;

public interface MemberService {
    public String updateProfile(Long memberId, UpdateProfileRequest request);
}
