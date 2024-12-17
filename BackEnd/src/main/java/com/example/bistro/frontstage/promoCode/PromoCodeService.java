package com.example.bistro.frontstage.promoCode;

import com.example.bistro.backstage.members.Members;
import com.example.bistro.backstage.members.MembersRepository;
import com.example.bistro.backstage.pointPrizes.PointPrizesBean;
import com.example.bistro.backstage.pointPrizes.PointPrizesRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromoCodeService {

    @Autowired
    private PromoCodeRepository promoCodeRepository;

    @Autowired
    private MembersRepository membersRepository;

    @Autowired
    private PointPrizesRepository pointPrizesRepository;

    public void createPromoCode(PromoCodeDTO promoData) {

        Members member = membersRepository.findById(promoData.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found"));

        PointPrizesBean pointPrize = pointPrizesRepository.findById(promoData.getPointPrizesId())
                .orElseThrow(() -> new RuntimeException("Point Prize not found"));

        PromoCodeBean promoCodeBean = new PromoCodeBean();
        promoCodeBean.setMembers(member);
        promoCodeBean.setPointPrizes(pointPrize);
        promoCodeBean.setPromoCode(promoData.getPromoCode());

        promoCodeRepository.save(promoCodeBean);
    }
    
    public List<PromoCodeBean> findMemberPromoCode(int memberId){
    	return promoCodeRepository.findMemberPromoCode(memberId);
    }
    
}
