package com.sk.svdonation.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.sk.svdonation.entity.SVGoodbuyEntity;

public interface SVGoodbuyRepository extends CrudRepository<SVGoodbuyEntity, String> {
	
	List<SVGoodbuyEntity> findByMemberIdOrderByCreateAtDesc(@Param("memberId") String memberId);
	
	Iterable<SVGoodbuyEntity> findAll();
	
	SVGoodbuyEntity findByBuyId(long buyId);
	
}