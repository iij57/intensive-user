package com.sk.svdonation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.sk.svdonation.entity.SVBeneficiaryEntity;
import com.sk.svdonation.entity.SVCampaignEntity;
import com.sk.svdonation.entity.SVMemberGeneralEntity;

public interface SVBeneficiaryRepository extends CrudRepository<SVBeneficiaryEntity, Long> {
    @Query("SELECT e FROM SVBeneficiaryEntity e JOIN FETCH e.beneficiaryMember WHERE e.campaign = :campaign")
    Iterable<SVBeneficiaryEntity> findAllByCampaign(@Param("campaign") SVCampaignEntity campaign);
    
    @Query("SELECT e FROM SVBeneficiaryEntity e JOIN FETCH e.beneficiaryMember WHERE e.beneficiaryMember = :beneficiaryMember")
    List<SVBeneficiaryEntity> findAllByBeneficiaryMember(SVMemberGeneralEntity beneficiaryMember);
    
    @Query(value="select TOKEN_PAYMENT_CATEGORY, SUM(TOKEN_PAYMENT) FROM SV_TOKEN_USAGE GROUP BY TOKEN_PAYMENT_CATEGORY", nativeQuery=true)
    Iterable<Object[]> findAllByCategory();
    
    @Query(value="SELECT svt.TOKEN_PAYMENT, date_format(svt.TOKEN_USAGE_AT,'%Y-%m-%d %H:%i:%S'), svt.TOKEN_PAYMENT_CATEGORY, svb.BENEFICIARY_CAMPAIGN, svb.BENEFICIARY_TOKEN, svm.MEMBER_NAME, svb.BENEFICIARY_MEMBER  FROM SV_TOKEN_USAGE svt LEFT OUTER JOIN SV_BENEFICIARY svb ON svt.TOKEN_BENEFICIARY = svb.BENEFICIARY_ID LEFT OUTER JOIN SV_MEMBER_BASE svm ON svb.BENEFICIARY_MEMBER = svm.MEMBER_ID WHERE svb.BENEFICIARY_CAMPAIGN = :id ORDER BY svt.TOKEN_USAGE_AT DESC", nativeQuery=true)
    Iterable<Object[]> findAllByTokenUsageByCampaign(@Param("id") Long id);
    
}