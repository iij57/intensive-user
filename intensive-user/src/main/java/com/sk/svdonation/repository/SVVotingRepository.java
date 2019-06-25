package com.sk.svdonation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.sk.svdonation.entity.SVCampaignEntity;
import com.sk.svdonation.entity.SVVotingEntity;

public interface SVVotingRepository extends CrudRepository<SVVotingEntity, Long> {
	@Query("SELECT distinct e FROM SVVotingEntity e JOIN FETCH e.campaign WHERE e.campaign = :campaign")
	List<SVVotingEntity> findByCampaign(SVCampaignEntity campaign);

	@Query("SELECT distinct e FROM SVVotingEntity e JOIN FETCH e.campaign WHERE e.memberId = :memberId")
	List<SVVotingEntity> findAllByMemberId(String memberId);
	
	List<SVVotingEntity> findAll();
}