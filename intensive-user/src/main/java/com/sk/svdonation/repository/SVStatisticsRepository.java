package com.sk.svdonation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.sk.svdonation.entity.SVCampaignEntity;

public interface SVStatisticsRepository extends CrudRepository<SVCampaignEntity, Long> {
    
    @Query(value = "SELECT CAMPAIGN FROM SV_DONATION WHERE DONATOR = :id GROUP BY CAMPAIGN", nativeQuery=true)
    List<Object[]> findDonateCampaignsByMember(@Param("id") String id);
    
    @Query(value = "SELECT IFNULL(SUM(AMOUNT_OF_SVC),0) as SVC FROM SV_DONATION WHERE DONATOR = :id", nativeQuery=true)
    List<Object[]> findDonateSVCByMember(@Param("id") String id);
    
    @Query(value = "SELECT IFNULL(SUM(SVP_AMOUNT),0) as SVP FROM SV_VOTING WHERE MEMBER_ID =:id", nativeQuery=true)
    List<Object[]> findVotingSVPByMember(@Param("id") String id);
}