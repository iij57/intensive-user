package com.sk.svdonation.service;

import java.util.NoSuchElementException;

import com.sk.svdonation.dto.SVMemberDTO;
import com.sk.svdonation.entity.SVMemberBaseEntity;
import com.sk.svdonation.entity.SVMemberFoundationEntity;
import com.sk.svdonation.entity.SVMemberGeneralEntity;
import com.sk.svdonation.repository.SVMemberRepository;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SVMemberService {
    private SVMemberRepository memberRepository;

    /**
     * 
     * @param id
     * @return
     * @throws NoSuchElementException id에 해당하는 사용자가 없다면 발생
     */
    public SVMemberDTO getSVMember(String id) {
        SVMemberBaseEntity entity = getSVMemberEntity(id);
        String name = (entity instanceof SVMemberGeneralEntity) ? ((SVMemberGeneralEntity) entity).getMemeberName() : ((SVMemberFoundationEntity) entity).getFoundationName();
        return new SVMemberDTO(entity.getMemberId(), name, entity.getMemberType().toString(), entity.getWalletAddress(), entity.getPrivateKey());
        
    }

    /**
     * 
     * @param id
     * @return
     * @throws NoSuchElementException id에 해당하는 사용자가 없다면 발생
     */
    SVMemberBaseEntity getSVMemberEntity(String id) {
        return memberRepository.findById(id).get();
    }
}