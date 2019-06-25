package com.sk.svdonation.repository;

import java.util.List;

import com.sk.svdonation.entity.SVBeneficiaryEntity;
import com.sk.svdonation.entity.SVCampaignEntity;
import com.sk.svdonation.entity.SVTokenUsageEntity;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface SVTokenUsageRepository extends CrudRepository<SVTokenUsageEntity, Long> {
    @Query("SELECT e FROM SVTokenUsageEntity e JOIN FETCH e.beneficiary b JOIN FETCH b.beneficiaryMember WHERE e.beneficiary = :beneficiary")
    Iterable<SVTokenUsageEntity> findAllByBeneficiary(@Param("beneficiary") SVBeneficiaryEntity beneficiary);

    @Query("SELECT e FROM SVTokenUsageEntity e WHERE e.beneficiary.campaign = :campaign")
    List<SVTokenUsageEntity> findAllByCampaign(@Param("campaign") SVCampaignEntity campaign);
}