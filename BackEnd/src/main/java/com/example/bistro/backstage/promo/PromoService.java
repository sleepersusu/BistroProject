package com.example.bistro.backstage.promo;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bistro.backstage.members.Members;
import com.example.bistro.backstage.members.MembersRepository;
import com.example.bistro.backstage.pointPrizes.PointPrizesBean;
import com.example.bistro.backstage.pointPrizes.PointPrizesRepository;
import com.example.bistro.frontstage.promoCode.PromoCodeBean;

@Service
public class PromoService {
    
    @Autowired
    private PromoRepository promoRepository;
    
    @Autowired
    private MembersRepository membersRepository;
    
    @Autowired
    private PointPrizesRepository pointPrizesRepository;

    public List<promoDTO> findAllPromo() {
        List<PromoCodeBean> promoList = promoRepository.findAll();
        return promoList.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    private promoDTO convertToDTO(PromoCodeBean promo) {
    	promoDTO dto = new promoDTO();
        dto.setId(promo.getID());
        dto.setPromoCode(promo.getPromoCode());
        dto.setMemberId(promo.getMembers().getId());
        dto.setMemberName(promo.getMembers().getMemberName());
        dto.setPointPrizesId(promo.getPointPrizes().getId());
        dto.setPointPrizesName(promo.getPointPrizes().getPointPrizesName());
        return dto;
    }

    public PromoCodeBean createPromo(String promoCode, Integer memberId, Integer pointPrizesId) {
        Members member = membersRepository.findById(memberId)
            .orElseThrow(() -> new RuntimeException("會員不存在"));
            
        PointPrizesBean prize = pointPrizesRepository.findById(pointPrizesId)
            .orElseThrow(() -> new RuntimeException("獎品不存在"));

        PromoCodeBean promoCodeBean = new PromoCodeBean();
        promoCodeBean.setPromoCode(promoCode);
        promoCodeBean.setMembers(member);
        promoCodeBean.setPointPrizes(prize);

        return promoRepository.save(promoCodeBean);
    }

    public void deletePromoById(Integer id) {
        promoRepository.deleteById(id);
    }
}