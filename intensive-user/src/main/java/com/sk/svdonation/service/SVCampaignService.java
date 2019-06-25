package com.sk.svdonation.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import com.sk.svdonation.dto.SVBeneficiaryResponseDTO;
import com.sk.svdonation.dto.SVCampaignPostDTO;
import com.sk.svdonation.dto.SVCampaignResponseDTO;
import com.sk.svdonation.dto.SVDonationResponseDTO;
import com.sk.svdonation.dto.SVMemberDTO;
import com.sk.svdonation.dto.SVMyCampaignResponseDTO;
import com.sk.svdonation.entity.SVBeneficiaryEntity;
import com.sk.svdonation.entity.SVCampaignDetailEntity;
import com.sk.svdonation.entity.SVCampaignEntity;
import com.sk.svdonation.entity.SVCampaignStatus;
import com.sk.svdonation.entity.SVDonationEntity;
import com.sk.svdonation.entity.SVMemberBaseEntity;
import com.sk.svdonation.entity.SVMemberFoundationEntity;
import com.sk.svdonation.entity.SVMemberGeneralEntity;
import com.sk.svdonation.repository.SVBeneficiaryRepository;
import com.sk.svdonation.repository.SVCampaignDetailRepository;
import com.sk.svdonation.repository.SVCampaignRepository;
import com.sk.svdonation.repository.SVDonationRepository;
import com.sk.svdonation.repository.SVTokenUsageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SVCampaignService {
    private SVCampaignRepository campaignRepository;
    private SVMemberService memberService;
    private SVWeb3JService web3jService;

    @Value("${svdonation.kaleido.address.svc}")
    private String svcAddress;
    @Value("${svdonation.kaleido.address.svp}")
    private String svpAddress;
    @Value("${svdonation.foundations.foundation01}")
    private String foundtaion01;
    @Value("${svdonation.foundations.foundation02}")
    private String foundtaion02;
    @Value("${svdonation.foundations.foundation03}")
    private String foundtaion03;
    @Value("${svdonation.foundations.foundation04}")
    private String foundtaion04;
    @Value("${svdonation.foundations.foundation05}")
    private String foundtaion05;
    
    @Autowired
    private SVDonationRepository donationRepository;
    
    @Autowired
    private SVBeneficiaryRepository beneficiaryRepository;
    
    @Autowired
    private SVCampaignDetailRepository campaignDetailRepository;

    @Autowired
    private SVTokenUsageRepository tokenUsageRepository;

    public SVCampaignService(SVCampaignRepository campaignRepository, SVMemberService memberService, SVWeb3JService web3jService) {
        this.campaignRepository = campaignRepository;
        this.memberService = memberService;
        this.web3jService = web3jService;
    }

    public List<SVMyCampaignResponseDTO> getCampaignsByDonator(String memberId) {
        log.info("CALL SVCampaignService.getCampaignsByDonator {memberId: " + memberId + "}");
        final List<SVMyCampaignResponseDTO> returnValue = new ArrayList<>();
        List<SVDonationEntity> donations = donationRepository.findAllByDonator(SVMemberGeneralEntity.create(memberId));
        long[] campaignIdList = donations.stream().mapToLong(donation -> donation.getCampaign().getCampaignId()).toArray();

        campaignRepository.findAllByDonator(campaignIdList).forEach(campaign -> {
            SVCampaignResponseDTO campaignDTO = convert(campaign, false);
            returnValue.add(new SVMyCampaignResponseDTO(
                                                        campaignDTO.getCampaignId(),
                                                        campaignDTO.getCampaignAddress(),
                                                        campaignDTO.getCampaignTitle(),
                                                        campaignDTO.getCampaignImageUrl(),
                                                        campaignDTO.getCampaignStartAt(),
                                                        campaignDTO.getCampaignFinishAt(),
                                                        campaignDTO.getTargetTokenAmount(),
                                                        campaignDTO.getUsageTokenAmount(),
                                                        campaignDTO.getSumOfSVC(),
                                                        donations.stream().filter(donation -> donation.getCampaign().getCampaignId() == campaign.getCampaignId()).mapToLong(item -> item.getAmountOfSVC()).sum(),
                                                        campaignDTO.getSumOfSVP(),
                                                        campaignDTO.getStatus(),
                                                        campaignDTO.getFoundation()
                                                        ));
        });
        
        return returnValue;
    }
    
    /**
     * 캠페인 목록 조회
     * 
     * @return list of {@link SVCampaignResponseDTO}
     */
    @Transactional(readOnly = true)
    public List<SVCampaignResponseDTO> getCampaigns() {
        final List<SVCampaignResponseDTO> returnValue = new ArrayList<>();
        
        Iterable<Object[]> campaignsNative = campaignRepository.findCampaignsByNative();
        
        for(Object[] campaignNative : campaignsNative) {
        	
            Set<SVDonationResponseDTO> donations = new HashSet<>();
            Set<SVBeneficiaryResponseDTO> beneficiaries = new HashSet<>();
            Map<String,Object> details = new HashMap<>();
            
            SVMemberDTO member = new SVMemberDTO(campaignNative[8].toString(), campaignNative[9].toString(), campaignNative[10].toString(), campaignNative[11].toString(), campaignNative[12].toString());
            returnValue.add(new SVCampaignResponseDTO(
            									campaignNative[0].toString(), 
            									campaignNative[1].toString(),
            									campaignNative[2].toString(), 
            									campaignNative[15].toString(), 
            									campaignNative[3].toString(), 
            									campaignNative[4].toString(), 
            									campaignNative[5].toString(), 
            									Long.parseLong(campaignNative[6].toString()),
            									Long.parseLong(campaignNative[16].toString())+Long.parseLong(campaignNative[17].toString()),
            									Long.parseLong(campaignNative[13].toString()), 
            									Long.parseLong(campaignNative[14].toString()),
            									campaignNative[7].toString(),
                                                member,
                                                donations,
                                                beneficiaries,
                                                details
                                            ));
        	
        	
        	
        }
        
//        Iterable<SVCampaignEntity> campaignEntity = campaignRepository.findAll();
//
//        for(SVCampaignEntity campaign : campaignEntity) {
//            returnValue.add(convert(campaign, false));
//        }

//        campaignRepository.findAll().forEach(campaign -> {
//            returnValue.add(convert(campaign, false));
//        });
        return returnValue;
    }
    
    @Transactional(readOnly = true)
    public List<SVCampaignResponseDTO> getCampaignsNoOrder() {
        final List<SVCampaignResponseDTO> returnValue = new ArrayList<>();
        
        Iterable<Object[]> campaignsNative = campaignRepository.findCampaignsNoOrderByNative();
        
        for(Object[] campaignNative : campaignsNative) {
        	
            Set<SVDonationResponseDTO> donations = new HashSet<>();
            Set<SVBeneficiaryResponseDTO> beneficiaries = new HashSet<>();
            Map<String,Object> details = new HashMap<>();
            
            SVMemberDTO member = new SVMemberDTO(campaignNative[8].toString(), campaignNative[9].toString(), campaignNative[10].toString(), campaignNative[11].toString(), campaignNative[12].toString());
            returnValue.add(new SVCampaignResponseDTO(
            									campaignNative[0].toString(), 
            									campaignNative[1].toString(),
            									campaignNative[2].toString(), 
            									campaignNative[15].toString(), 
            									campaignNative[3].toString(), 
            									campaignNative[4].toString(), 
            									campaignNative[5].toString(), 
            									Long.parseLong(campaignNative[6].toString()),
            									Long.parseLong(campaignNative[16].toString())+Long.parseLong(campaignNative[17].toString()),
            									Long.parseLong(campaignNative[13].toString()), 
            									Long.parseLong(campaignNative[14].toString()),
            									campaignNative[7].toString(),
                                                member,
                                                donations,
                                                beneficiaries,
                                                details
                                            ));
        	
        	
        	
        }
        
        return returnValue;
    }

    /**
     * 캠페인 상세조회
     * 
     * @param campaignId
     * @return {@link SVCampaignResponseDTO}
     * @throws NoSuchElementException 인자값에 해당하는 캠페인 데이터가 없는 경우 발생
     */
    public SVCampaignResponseDTO getCampaign(long campaignId) {
    	
        //SVCampaignEntity campaign = getCampaignEntity(campaignId);
        
        //단일이라 Iterable를 빼고 싶은데 안빠지네 편히 하기 위해 List로 받아서 0번만 사용함...
        List<Object[]> campaignData = campaignRepository.findCampaignsByNativeByCampaign(campaignId);
        
        Object[] campaignNative = campaignData.get(0);
        
        Set<SVDonationResponseDTO> donations = new HashSet<>();
        
        SVCampaignEntity campaignEntity = new SVCampaignEntity(campaignId);
        
        for(SVDonationEntity donation : donationRepository.findAllByCampaign(campaignEntity)) {
            SVMemberGeneralEntity donator = (SVMemberGeneralEntity) donation.getDonator();
            SVMemberDTO member = new SVMemberDTO(donator.getMemberId(), donator.getMemeberName(), donator.getMemberType().toString(), donator.getWalletAddress(), donator.getPrivateKey());
            donations.add(new SVDonationResponseDTO(String.valueOf(donation.getDonationId()), donation.getAmountOfSVC(), campaignId, member));
        };
        
        
        Set<SVBeneficiaryResponseDTO> beneficiaries = new HashSet<>();
        
        for(SVBeneficiaryEntity beneficiary : beneficiaryRepository.findAllByCampaign(campaignEntity)) {
            SVMemberGeneralEntity beneficiaryMember = (SVMemberGeneralEntity) beneficiary.getBeneficiaryMember();

            SVMemberDTO member = new SVMemberDTO(beneficiaryMember.getMemberId(), beneficiaryMember.getMemeberName(), beneficiaryMember.getMemberType().toString(), beneficiaryMember.getWalletAddress(), beneficiaryMember.getPrivateKey());
            beneficiaries.add(new SVBeneficiaryResponseDTO(String.valueOf(beneficiary.getBeneficiaryId()), beneficiary.getBeneficiaryToken(), campaignId, member));
        };
        
        
        HashMap<String,Object> details = new HashMap<>();
        List<HashMap<String, Object>> operatinglList = new ArrayList<HashMap<String,Object>>();
        List<HashMap<String, Object>> donatinglList = new ArrayList<HashMap<String,Object>>();
        
        for(SVCampaignDetailEntity detail : campaignDetailRepository.findByCampaign(campaignEntity)) {
            //details.add(new SVCampaignDetailResponseDTO(detail.getDetailId(), campaignId, detail.getDetailInfo(), detail.getDetailSVC(), detail.getDetailType().toString()));
        	HashMap<String,Object> campaignDetails = new HashMap<>();
        	
        	if("OPERATING".equals(detail.getDetailType().toString())) {
        		
        		campaignDetails.put(detail.getDetailInfo().toString(), detail.getDetailSVC());
        		operatinglList.add(campaignDetails);
        		
        	}else if("DONATING".equals(detail.getDetailType().toString())) {
        		
        		campaignDetails.put(detail.getDetailInfo().toString(), detail.getDetailSVC());
        		donatinglList.add(campaignDetails);
        		
        	}
        }
        
        details.put("operating", operatinglList);
        details.put("donating", donatinglList);
        
        
        
        SVMemberDTO member = new SVMemberDTO(campaignNative[8].toString(), campaignNative[9].toString(), campaignNative[10].toString(), campaignNative[11].toString(), campaignNative[12].toString());
        String detail = "";
        if(member.getMemberId().equals("Foundation01")) detail = this.foundtaion01;
        if(member.getMemberId().equals("Foundation02")) detail = this.foundtaion02;
        if(member.getMemberId().equals("Foundation03")) detail = this.foundtaion03;
        if(member.getMemberId().equals("Foundation04")) detail = this.foundtaion04;
        if(member.getMemberId().equals("Foundation05")) detail = this.foundtaion05;
        SVMemberDTO memberDetail = new SVMemberDTO(member.getMemberId(), member.getName(), detail, member.getMemberType(), member.getWalletAddress(), member.getPrivateKey());
        return new SVCampaignResponseDTO(
        									campaignNative[0].toString(), 
        									campaignNative[1].toString(),
        									campaignNative[2].toString(), 
        									campaignNative[15].toString(), 
        									campaignNative[3].toString(), 
        									campaignNative[4].toString(), 
        									campaignNative[5].toString(), 
        									Long.parseLong(campaignNative[6].toString()),
        									Long.parseLong(campaignNative[16].toString())+Long.parseLong(campaignNative[17].toString()),
        									Long.parseLong(campaignNative[13].toString()), 
        									Long.parseLong(campaignNative[14].toString()),
        									campaignNative[7].toString(),
                                            memberDetail,
                                            donations,
                                            beneficiaries,
                                            details
                                        );
        
        
        
        
        //return convert(campaign, true);
    }
    
    public SVCampaignResponseDTO getCampaignNodetails(long campaignId) {
    	
        //SVCampaignEntity campaign = getCampaignEntity(campaignId);
        
        //단일이라 Iterable를 빼고 싶은데 안빠지네 편히 하기 위해 List로 받아서 0번만 사용함...
        List<Object[]> campaignData = campaignRepository.findCampaignsByNativeByCampaign(campaignId);
        
        Object[] campaignNative = campaignData.get(0);
        
        Set<SVDonationResponseDTO> donations = new HashSet<>();
        
        SVCampaignEntity campaignEntity = new SVCampaignEntity(campaignId);
        
		/*
		 * for(SVDonationEntity donation :
		 * donationRepository.findAllByCampaign(campaignEntity)) { SVMemberGeneralEntity
		 * donator = (SVMemberGeneralEntity) donation.getDonator(); SVMemberDTO member =
		 * new SVMemberDTO(donator.getMemberId(), donator.getMemeberName(),
		 * donator.getMemberType().toString(), donator.getWalletAddress(),
		 * donator.getPrivateKey()); donations.add(new
		 * SVDonationResponseDTO(String.valueOf(donation.getDonationId()),
		 * donation.getAmountOfSVC(), campaignId, member)); };
		 */
        
        
        Set<SVBeneficiaryResponseDTO> beneficiaries = new HashSet<>();
        
		/*
		 * for(SVBeneficiaryEntity beneficiary :
		 * beneficiaryRepository.findAllByCampaign(campaignEntity)) {
		 * SVMemberGeneralEntity beneficiaryMember = (SVMemberGeneralEntity)
		 * beneficiary.getBeneficiaryMember();
		 * 
		 * SVMemberDTO member = new SVMemberDTO(beneficiaryMember.getMemberId(),
		 * beneficiaryMember.getMemeberName(),
		 * beneficiaryMember.getMemberType().toString(),
		 * beneficiaryMember.getWalletAddress(), beneficiaryMember.getPrivateKey());
		 * beneficiaries.add(new
		 * SVBeneficiaryResponseDTO(String.valueOf(beneficiary.getBeneficiaryId()),
		 * beneficiary.getBeneficiaryToken(), campaignId, member)); };
		 */
        
        
        HashMap<String,Object> details = new HashMap<>();
        List<HashMap<String, Object>> operatinglList = new ArrayList<HashMap<String,Object>>();
        List<HashMap<String, Object>> donatinglList = new ArrayList<HashMap<String,Object>>();
        
        for(SVCampaignDetailEntity detail : campaignDetailRepository.findByCampaign(campaignEntity)) {
            //details.add(new SVCampaignDetailResponseDTO(detail.getDetailId(), campaignId, detail.getDetailInfo(), detail.getDetailSVC(), detail.getDetailType().toString()));
        	HashMap<String,Object> campaignDetails = new HashMap<>();
        	
        	if("OPERATING".equals(detail.getDetailType().toString())) {
        		
        		campaignDetails.put(detail.getDetailInfo().toString(), detail.getDetailSVC());
        		operatinglList.add(campaignDetails);
        		
        	}else if("DONATING".equals(detail.getDetailType().toString())) {
        		
        		campaignDetails.put(detail.getDetailInfo().toString(), detail.getDetailSVC());
        		donatinglList.add(campaignDetails);
        		
        	}
        }
        
        details.put("operating", operatinglList);
        details.put("donating", donatinglList);
        
        
        
        SVMemberDTO member = new SVMemberDTO(campaignNative[8].toString(), campaignNative[9].toString(), campaignNative[10].toString(), campaignNative[11].toString(), campaignNative[12].toString());
        String detail = "";
        if(member.getMemberId().equals("Foundation01")) detail = this.foundtaion01;
        if(member.getMemberId().equals("Foundation02")) detail = this.foundtaion02;
        if(member.getMemberId().equals("Foundation03")) detail = this.foundtaion03;
        if(member.getMemberId().equals("Foundation04")) detail = this.foundtaion04;
        if(member.getMemberId().equals("Foundation05")) detail = this.foundtaion05;
        SVMemberDTO memberDetail = new SVMemberDTO(member.getMemberId(), member.getName(), detail, member.getMemberType(), member.getWalletAddress(), member.getPrivateKey());
        return new SVCampaignResponseDTO(
        									campaignNative[0].toString(), 
        									campaignNative[1].toString(),
        									campaignNative[2].toString(), 
        									campaignNative[15].toString(), 
        									campaignNative[3].toString(), 
        									campaignNative[4].toString(), 
        									campaignNative[5].toString(), 
        									Long.parseLong(campaignNative[6].toString()),
        									Long.parseLong(campaignNative[16].toString())+Long.parseLong(campaignNative[17].toString()),
        									Long.parseLong(campaignNative[13].toString()), 
        									Long.parseLong(campaignNative[14].toString()),
        									campaignNative[7].toString(),
                                            memberDetail,
                                            donations,
                                            beneficiaries,
                                            details
                                        );
        
        
        
        
        //return convert(campaign, true);
    }

    /**
     * 캠페인 상세조회
     * 
     * @param campaignId
     * @return {@link SVCampaignEntity}
     * @throws NoSuchElementException 인자값에 해당하는 캠페인 데이터가 없는 경우 발생
     */
    public SVCampaignEntity getCampaignEntity(long campaignId) {
        return campaignRepository.findById(campaignId).get();
    }

    @Transactional
    // TODO 블록체인 로직 성공 후 DB 업데이트 오류 시 롤백 처리
    public long createCampaign(SVCampaignPostDTO campaign, String foundationId) {
        log.info("CALL SVCampaignService.createCampaign {campaign: " + campaign + ", foundationId: " + foundationId + "}");
        SVCampaignEntity entity = convert(campaign, foundationId);
        campaignRepository.save(entity);

        try {
            SVMemberBaseEntity memberEntity = memberService.getSVMemberEntity(foundationId);
            long nonce = web3jService.getNonce(memberEntity.getWalletAddress());
            long finishAt = entity.getCampaignFinishAt().atZone(ZoneId.systemDefault()).toEpochSecond();

            // TODO constructor(address _token, uint256 _goalAmount, uint256 _releaseTime) public
            String encodedConstructor = FunctionEncoder.encodeConstructor(
                                                                            Arrays.asList(
                                                                                new Address(svcAddress), 
                                                                                new Uint256(campaign.getTargetTokenAmount()), 
                                                                                new Uint256(finishAt)
                                                                            )
                                                                        );

            String signedTx = web3jService.createContractSignedTransaction(nonce, campaign.getContractData(), encodedConstructor, memberEntity.getPrivateKey());

            EthGetTransactionReceipt receipt = web3jService.sendRawTransactionAndReturnReceipt(signedTx);
            // TODO Exception 정의 필요
            if(receipt == null) throw new RuntimeException("Deploy smart contract failed!!");

            String contractAddress = receipt.getResult().getContractAddress();
            log.info("Deployed smart contract [" + contractAddress + "]");
            // TODO 블록체인과 동기화에 대한 취약점..DB 오류 나면??
            entity.updateCampaignAddress(contractAddress);
        } catch (Exception e) {
            log.error("Deploy smart contract failed!!: ", e);
            // TODO Exception 정의 필요
            throw new RuntimeException(e);
        }

        return entity.getCampaignId();
    }

    private SVCampaignResponseDTO convert(SVCampaignEntity campaign, boolean isDetail) {
        Set<SVDonationResponseDTO> donations = new HashSet<>();
        Set<SVBeneficiaryResponseDTO> beneficiaries = new HashSet<>();
        //Set<SVCampaignDetailResponseDTO> details = new HashSet<>();
        Map<String,Object> details = new HashMap<>();

        long sumOfSVC = campaign.getDonations().stream().mapToLong(item -> item.getAmountOfSVC()).sum();
        
        //투표된 SVP의 값을 가져와 더한다.
        //List<SVVotingEntity> votingList = svVotingService.getVotingByCampaignId(campaign.getCampaignId());
        //long sumOfSVP = votingList.stream().mapToLong(item -> item.getSvpAmount()).sum();
        
        long sumOfSVP = campaign.getVoting().stream().mapToLong(item -> item.getSvpAmount()).sum();
        // for(int i=0;i<votingList.size(); i++) { 
        //     sumOfSVP += votingList.get(i).getSvpAmount(); 
        // }

        if(isDetail) {
            for(SVDonationEntity donation : campaign.getDonations()) {
                SVMemberGeneralEntity donator = (SVMemberGeneralEntity) donation.getDonator();
                SVMemberDTO member = new SVMemberDTO(donator.getMemberId(), donator.getMemeberName(), donator.getMemberType().toString(), donator.getWalletAddress(), donator.getPrivateKey());
                donations.add(new SVDonationResponseDTO(String.valueOf(donation.getDonationId()), donation.getAmountOfSVC(), campaign.getCampaignId(), member));
            };

            for(SVBeneficiaryEntity beneficiary : campaign.getBeneficiaries()) {
                SVMemberGeneralEntity beneficiaryMember = (SVMemberGeneralEntity) beneficiary.getBeneficiaryMember();

                SVMemberDTO member = new SVMemberDTO(beneficiaryMember.getMemberId(), beneficiaryMember.getMemeberName(), beneficiaryMember.getMemberType().toString(), beneficiaryMember.getWalletAddress(), beneficiaryMember.getPrivateKey());
                beneficiaries.add(new SVBeneficiaryResponseDTO(String.valueOf(beneficiary.getBeneficiaryId()), beneficiary.getBeneficiaryToken(), campaign.getCampaignId(), member));
            };

			/*
			 * for(SVCampaignDetailEntity detail : campaign.getDetails()) { details.add(new
			 * SVCampaignDetailResponseDTO(detail.getDetailId(), campaign.getCampaignId(),
			 * detail.getDetailInfo(), detail.getDetailSVC(),
			 * detail.getDetailType().toString())); }
			 */
        }
        
        //DB값들을 합치고 있었네? 그렇다면...voting에서 svp만 가져와서 합쳐 주지 ㅇ_ㅇ
		/*
		 * long accountBalanceSvc =
		 * web3jService.getSVCBalance(campaign.getCampaignAddress());
		 * 
		 * long accountBalanceSvp =
		 * web3jService.getSVPBalance(campaign.getCampaignAddress());
		 * 
		 * logger.info("svcBallance="+accountBalanceSvc);
		 * logger.info("svpBallance="+accountBalanceSvp);
		 */

        SVMemberFoundationEntity foundation = (SVMemberFoundationEntity) campaign.getFoundation();
        SVMemberDTO member = new SVMemberDTO(foundation.getMemberId(), foundation.getFoundationName(), foundation.getMemberType().toString(), foundation.getWalletAddress(), foundation.getPrivateKey());
        return new SVCampaignResponseDTO(
                                            String.valueOf(campaign.getCampaignId()), 
                                            campaign.getCampaignAddress(),
                                            campaign.getCampaignTitle(), 
                                            campaign.getCampaignContent(), 
                                            campaign.getCampaignImageUrl(), 
                                            campaign.getCampaignStartAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), 
                                            campaign.getCampaignFinishAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), 
                                            campaign.getTargetTokenAmount(),
                                            tokenUsageRepository.findAllByCampaign(campaign).stream().mapToLong(tokenUsage -> tokenUsage.getPayment()).sum(),
                                            sumOfSVC, 
                                            sumOfSVP, 
                                            campaign.getStatus().toString(),
                                            member,
                                            donations,
                                            beneficiaries,
                                            details
                                        );
    }

    private SVCampaignEntity convert(SVCampaignPostDTO campaign, String foundationId) {
        LocalDateTime startAt = LocalDateTime.parse(campaign.getCampaignStartAt(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime finishAt = LocalDateTime.parse(campaign.getCampaignFinishAt(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return SVCampaignEntity.create("", campaign.getCampaignTitle(), 
                                    campaign.getCampaignContent(),
                                    campaign.getCampaignImageUrl(), 
                                    startAt, 
                                    finishAt, 
                                    campaign.getTargetTokenAmount(),
                                    SVCampaignStatus.ING,
                                    SVMemberFoundationEntity.create(foundationId));
    }
}