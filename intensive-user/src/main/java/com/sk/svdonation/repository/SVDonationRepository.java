package com.sk.svdonation.repository;

import java.util.List;

import com.sk.svdonation.entity.SVCampaignEntity;
import com.sk.svdonation.entity.SVDonationEntity;
import com.sk.svdonation.entity.SVMemberGeneralEntity;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface SVDonationRepository extends CrudRepository<SVDonationEntity, Long> {
    @Query("SELECT distinct e FROM SVDonationEntity e JOIN FETCH e.campaign JOIN FETCH e.donator WHERE e.donator = :donator")
    List<SVDonationEntity> findAllByDonator(@Param("donator") SVMemberGeneralEntity donator);

    @Query("SELECT distinct e FROM SVDonationEntity e JOIN FETCH e.donator WHERE e.campaign = :campaign")
    Iterable<SVDonationEntity> findAllByCampaign(@Param("campaign") SVCampaignEntity campaign);   
}