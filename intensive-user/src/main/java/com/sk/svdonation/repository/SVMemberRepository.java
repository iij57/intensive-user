package com.sk.svdonation.repository;

import com.sk.svdonation.entity.SVMemberBaseEntity;

import org.springframework.data.repository.CrudRepository;

public interface SVMemberRepository extends CrudRepository<SVMemberBaseEntity, String> {

}