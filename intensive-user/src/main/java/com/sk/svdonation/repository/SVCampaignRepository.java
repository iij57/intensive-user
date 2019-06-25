package com.sk.svdonation.repository;

import java.util.List;
import java.util.Optional;

import com.sk.svdonation.entity.SVCampaignEntity;
import com.sk.svdonation.entity.SVMemberGeneralEntity;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface SVCampaignRepository extends CrudRepository<SVCampaignEntity, Long> {
    @Override
    @Query("SELECT distinct e FROM SVCampaignEntity e LEFT JOIN FETCH e.donations d JOIN FETCH e.foundation")
    Iterable<SVCampaignEntity> findAll();

    @Query("SELECT distinct e.campaign.campaignId FROM SVDonationEntity e WHERE e.donator = :donator")
    List<Long> findCampaignIdsByDonator(@Param("donator") SVMemberGeneralEntity donator);

    @Query("SELECT distinct e FROM SVCampaignEntity e LEFT JOIN FETCH e.donations d JOIN FETCH e.foundation WHERE e.campaignId IN :campaignIds ORDER BY d.createAt DESC ")
    Iterable<SVCampaignEntity> findAllByDonator(@Param("campaignIds") long[] campaignIds);

    @Override
    @Query("SELECT e FROM SVCampaignEntity e LEFT JOIN FETCH e.donations d LEFT JOIN FETCH e.beneficiaries b JOIN FETCH e.foundation JOIN FETCH e.details LEFT JOIN FETCH d.donator LEFT JOIN FETCH b.beneficiaryMember WHERE e.id = :id")
    Optional<SVCampaignEntity> findById(@Param("id") Long id);
    
    @Query(value = "SELECT svc.CAMPAIGN_ID AS campaignId, svc.CAMPAIGN_ADDRESS AS campaignAddress, svc.CAMPAIGN_TITLE AS campaignTitle, svc.CAMPAIGN_IMG_URL AS campaignImageUrl, date_format(svc.CAMPAIGN_START_AT,'%Y-%m-%d %H:%i:%S') AS campaignStartAt, date_format(svc.CAMPAIGN_FINISH_AT,'%Y-%m-%d %H:%i:%S') AS campaignFinishAt, svc.TARGET_TOKEN_AMOUNT AS targetTokenAmount, svc.CAMPAIGN_STATUS AS satus, svm.MEMBER_ID AS memberId, svm.FOUNDATION_NAME AS 'name', svm.MEMBER_TYPE AS memberType, svm.MEMBER_WALLET_ADDRESS AS walletAddress, svm.MEMBER_PRIVATE_KEY AS privateKey, IFNULL((SELECT SUM(svd.AMOUNT_OF_SVC) FROM SV_DONATION svd WHERE svd.CAMPAIGN = svc.CAMPAIGN_ID),0) AS sumOfSVC, IFNULL((SELECT SUM(svv.SVP_AMOUNT) FROM SV_VOTING svv WHERE svv.CAMPAIGN = svc.CAMPAIGN_ID), 0) AS sumOfSVP, svc.CAMPAIGN_CONTENT AS campaignContent, IFNULL((SELECT SUM(SVC_AMOUNT) FROM SV_GOODBUY WHERE MEMBER_ID IN (SELECT BENEFICIARY_MEMBER FROM SV_BENEFICIARY WHERE BENEFICIARY_CAMPAIGN = svc.CAMPAIGN_ID)),0) AS usageGoodbuy, IFNULL((SELECT SUM(TOKEN_PAYMENT) FROM SV_TOKEN_USAGE WHERE TOKEN_BENEFICIARY IN (SELECT BENEFICIARY_ID FROM SV_BENEFICIARY WHERE BENEFICIARY_CAMPAIGN = svc.CAMPAIGN_ID)),0) AS usageToken  FROM SV_CAMPAIGN svc LEFT OUTER JOIN SV_MEMBER_BASE svm ON svc.CAMPAIGN_FOUNDATION = svm.MEMBER_ID LEFT OUTER JOIN (SELECT CAMPAIGN, max(CREATE_AT) AS DONATE_DATE from SV_DONATION group by CAMPAIGN) dn ON svc.CAMPAIGN_ID = dn.CAMPAIGN ORDER BY DONATE_DATE DESC", nativeQuery=true)
    Iterable<Object[]> findCampaignsByNative();
    
    @Query(value = "SELECT svc.CAMPAIGN_ID AS campaignId, svc.CAMPAIGN_ADDRESS AS campaignAddress, svc.CAMPAIGN_TITLE AS campaignTitle, svc.CAMPAIGN_IMG_URL AS campaignImageUrl, date_format(svc.CAMPAIGN_START_AT,'%Y-%m-%d %H:%i:%S') AS campaignStartAt, date_format(svc.CAMPAIGN_FINISH_AT,'%Y-%m-%d %H:%i:%S') AS campaignFinishAt, svc.TARGET_TOKEN_AMOUNT AS targetTokenAmount, svc.CAMPAIGN_STATUS AS satus, svm.MEMBER_ID AS memberId, svm.FOUNDATION_NAME AS 'name', svm.MEMBER_TYPE AS memberType, svm.MEMBER_WALLET_ADDRESS AS walletAddress, svm.MEMBER_PRIVATE_KEY AS privateKey, IFNULL((SELECT SUM(svd.AMOUNT_OF_SVC) FROM SV_DONATION svd WHERE svd.CAMPAIGN = svc.CAMPAIGN_ID),0) AS sumOfSVC, IFNULL((SELECT SUM(svv.SVP_AMOUNT) FROM SV_VOTING svv WHERE svv.CAMPAIGN = svc.CAMPAIGN_ID), 0) AS sumOfSVP, svc.CAMPAIGN_CONTENT AS campaignContent, IFNULL((SELECT SUM(SVC_AMOUNT) FROM SV_GOODBUY WHERE MEMBER_ID IN (SELECT BENEFICIARY_MEMBER FROM SV_BENEFICIARY WHERE BENEFICIARY_CAMPAIGN = svc.CAMPAIGN_ID)),0) AS usageGoodbuy, IFNULL((SELECT SUM(TOKEN_PAYMENT) FROM SV_TOKEN_USAGE WHERE TOKEN_BENEFICIARY IN (SELECT BENEFICIARY_ID FROM SV_BENEFICIARY WHERE BENEFICIARY_CAMPAIGN = svc.CAMPAIGN_ID)),0) AS usageToken  FROM SV_CAMPAIGN svc LEFT OUTER JOIN SV_MEMBER_BASE svm ON svc.CAMPAIGN_FOUNDATION = svm.MEMBER_ID", nativeQuery=true)
    Iterable<Object[]> findCampaignsNoOrderByNative();
    
    @Query(value = "SELECT svc.CAMPAIGN_ID AS campaignId, svc.CAMPAIGN_ADDRESS AS campaignAddress, svc.CAMPAIGN_TITLE AS campaignTitle, svc.CAMPAIGN_IMG_URL AS campaignImageUrl, date_format(svc.CAMPAIGN_START_AT,'%Y-%m-%d %H:%i:%S') AS campaignStartAt, date_format(svc.CAMPAIGN_FINISH_AT,'%Y-%m-%d %H:%i:%S') AS campaignFinishAt, svc.TARGET_TOKEN_AMOUNT AS targetTokenAmount, svc.CAMPAIGN_STATUS AS satus, svm.MEMBER_ID AS memberId, svm.FOUNDATION_NAME AS 'name', svm.MEMBER_TYPE AS memberType, svm.MEMBER_WALLET_ADDRESS AS walletAddress, svm.MEMBER_PRIVATE_KEY AS privateKey, IFNULL((SELECT SUM(svd.AMOUNT_OF_SVC) FROM SV_DONATION svd WHERE svd.CAMPAIGN = svc.CAMPAIGN_ID),0) AS sumOfSVC, IFNULL((SELECT SUM(svv.SVP_AMOUNT) FROM SV_VOTING svv WHERE svv.CAMPAIGN = svc.CAMPAIGN_ID), 0) AS sumOfSVP, svc.CAMPAIGN_CONTENT AS campaignContent, IFNULL((SELECT SUM(SVC_AMOUNT) FROM SV_GOODBUY WHERE MEMBER_ID IN (SELECT BENEFICIARY_MEMBER FROM SV_BENEFICIARY WHERE BENEFICIARY_CAMPAIGN = svc.CAMPAIGN_ID)),0) AS usageGoodbuy, IFNULL((SELECT SUM(TOKEN_PAYMENT) FROM SV_TOKEN_USAGE WHERE TOKEN_BENEFICIARY IN (SELECT BENEFICIARY_ID FROM SV_BENEFICIARY WHERE BENEFICIARY_CAMPAIGN = svc.CAMPAIGN_ID)),0) AS usageToken  FROM SV_CAMPAIGN svc LEFT OUTER JOIN SV_MEMBER_BASE svm ON svc.CAMPAIGN_FOUNDATION = svm.MEMBER_ID WHERE svc.CAMPAIGN_ID = :id", nativeQuery=true)
    List<Object[]> findCampaignsByNativeByCampaign(@Param("id") Long id);
    
}