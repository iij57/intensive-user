package com.sk.svdonation.repository;

import org.springframework.data.repository.CrudRepository;

import com.sk.svdonation.entity.SVCampaignDetailEntity;
import com.sk.svdonation.entity.SVCampaignEntity;

public interface SVCampaignDetailRepository extends CrudRepository<SVCampaignDetailEntity, Long> {
	
    Iterable<SVCampaignDetailEntity> findByCampaign(SVCampaignEntity campaign); 

}