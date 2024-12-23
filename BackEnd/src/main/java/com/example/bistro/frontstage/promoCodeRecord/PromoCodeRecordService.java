package com.example.bistro.frontstage.promoCodeRecord;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bistro.backstage.members.Members;
import com.example.bistro.backstage.members.MembersRepository;


@Service
public class PromoCodeRecordService {
	
	@Autowired
	private PromoCodeRecordRepository promoCodeRecordRepository;
	
	@Autowired
	private MembersRepository membersRepository;
	
//    @Transactional // 加上事務管理
//    public void createPromoCodeRecord(PromoCodeRecordDTO promoData) {
//        Members member = membersRepository.findById(promoData.getMemberId())
//            .orElseThrow(() -> new RuntimeException("找不到會員"));
//            
//        // 處理多筆優惠碼
//        for (String promoCode : promoData.getPromoCodes()) {
//            PromoCodeRecordBean promoCodeRecordBean = new PromoCodeRecordBean();
//            promoCodeRecordBean.setMembers(member);
//            promoCodeRecordBean.setPromoCode(promoCode);
//            promoCodeRecordBean.setPointPrizesName(pointPrizesName);
//            promoCodeRecordRepository.save(promoCodeRecordBean);
//            
//            // 同時刪除已使用的優惠碼
//            promoCodeRecordRepository.deleteMemberPromoCode(promoCode);
//        }
//    }
	@Transactional
	public void createPromoCodeRecord(PromoCodeRecordDTO promoData) {
	    Members member = membersRepository.findById(promoData.getMemberId())
	            .orElseThrow(() -> new RuntimeException("找不到會員"));

	    // 取得 promoCodes 和 pointPrizesName 的列表
	    List<String> promoCodes = promoData.getPromoCodes();
	    List<String> pointPrizesNames = promoData.getPointPrizesName();

	    // 確保兩個列表的長度相同
	    if (promoCodes.size() != pointPrizesNames.size()) {
	        throw new RuntimeException("優惠碼和獎品名稱數量不匹配");
	    }

	    // 同時遍歷兩個列表
	    for (int i = 0; i < promoCodes.size(); i++) {
	        PromoCodeRecordBean promoCodeRecordBean = new PromoCodeRecordBean();
	        promoCodeRecordBean.setMembers(member);
	        promoCodeRecordBean.setPromoCode(promoCodes.get(i));
	        promoCodeRecordBean.setPointPrizesName(pointPrizesNames.get(i));
	        promoCodeRecordRepository.save(promoCodeRecordBean);

	        // 同時刪除已使用的優惠碼
	        promoCodeRecordRepository.deleteMemberPromoCode(promoCodes.get(i));
	    }
	}
	
	public void deletePromoCodeRecord(String promoCode) {
	    int rowsAffected = promoCodeRecordRepository.deleteMemberPromoCode(promoCode);
	    if (rowsAffected == 0) {
	        throw new RuntimeException("未找到優惠碼，刪除失敗");
	    }
	}
	
    public List<PromoCodeRecordBean> findAllPromoCodeRecord() {
        return promoCodeRecordRepository.findAll();
    }
}
