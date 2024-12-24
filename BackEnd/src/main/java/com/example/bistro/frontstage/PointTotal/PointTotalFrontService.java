package com.example.bistro.frontstage.PointTotal;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bistro.backstage.PointsTotal.PointsTotalBean;
import com.example.bistro.backstage.PointsTotal.PointsTotalRepository;
import com.example.bistro.backstage.members.Members;
import com.example.bistro.backstage.members.MembersRepository;

@Service
public class PointTotalFrontService {
    @Autowired
    private PointsTotalRepository pointsTotalRepository;
    
    @Autowired
    private MembersRepository memberRepository; // 需要注入會員的 Repository

    @Transactional
    public void updateMemberPoints(Integer memberId, Integer pointsToAdd) {
        // 先查詢會員是否存在
        Members member = memberRepository.findById(memberId)
            .orElseThrow(() -> new RuntimeException("Member not found"));

        // 查詢是否已有點數紀錄
        Optional<PointsTotalBean> existingPoints = pointsTotalRepository.findByMembersId(memberId);

        if (existingPoints.isPresent()) {
            // 更新現有點數
            PointsTotalBean pointsTotal = existingPoints.get();
            pointsTotal.setPointsTotal(pointsTotal.getPointsTotal() + pointsToAdd);
            pointsTotalRepository.save(pointsTotal);
        } else {
            // 建立新的點數紀錄
            PointsTotalBean newPointsTotal = new PointsTotalBean();
            newPointsTotal.setMembers(member);
            newPointsTotal.setPointsTotal(pointsToAdd);
            pointsTotalRepository.save(newPointsTotal);
        }
    }
    
    public List<PointsTotalBean> findAllMemberPoint(){
    	return pointsTotalRepository.findAll();
    }
}