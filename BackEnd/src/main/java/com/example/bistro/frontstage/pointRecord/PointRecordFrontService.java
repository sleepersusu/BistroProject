package com.example.bistro.frontstage.pointRecord;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.bistro.backstage.PointsRecords.PointsRecordsBean;
import com.example.bistro.backstage.PointsRecords.PointsRecordsRepository;
import com.example.bistro.backstage.members.Members;
import com.example.bistro.backstage.members.MembersRepository;
import com.example.bistro.backstage.members.MembersService;
import com.example.bistro.backstage.pointPrizes.PointPrizesBean;
import com.example.bistro.backstage.pointPrizes.PointPrizesRepository;

@Service
public class PointRecordFrontService {
	@Autowired
	private PointsRecordsRepository pointsRecordsRepository;

	@Autowired
	private MembersRepository membersRepository;

	@Autowired
	private PointPrizesRepository pointPrizesRepository;
	
    @Autowired
    private MembersService membersService;

	public List<Object[]> findAllPointsRecords() {
		return pointsRecordsRepository.findMembersAllPointRecord();
	}

	public PointsRecordsBean createPointsRecords(PointsRecordsBean PointsRecords) {
		return pointsRecordsRepository.save(PointsRecords);
	}

	public void deletPointsRecords(Integer id) {
		pointsRecordsRepository.deleteById(id);
	}

	public Map<String, Boolean> createPointsRecords(Integer memberId, Integer pointPrizesId, Date recordsDate) {
		Optional<Members> op = membersRepository.findById(memberId);
		Optional<PointPrizesBean> op2 = pointPrizesRepository.findById(pointPrizesId);

		if (op.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到會員資料");
		}
		if (op2.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到獎品資料");
		}

		PointsRecordsBean pointsRecordsBean = new PointsRecordsBean();
		pointsRecordsBean.setMembers(op.get());
		pointsRecordsBean.setPointPrizes(op2.get());
		pointsRecordsBean.setRecordsDate(recordsDate);

		pointsRecordsRepository.save(pointsRecordsBean);

		Map<String, Boolean> map = new HashMap<String, Boolean>();
		map.put("兌換狀態", true);
		return map;

	}

//	public PointsRecordsBean minusMemberPoint(int pointPrizesId, int memberId) {
//    	PointPrizesBean pointPrizesBean = pointPrizesRepository.findById(pointPrizesId)
//    			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到該獎品"));
//    	  			
//    	int deletePoints = pointPrizesBean.getPointPrizesPoints();
//    	
//    	PointsRecordsBean pointsRecordsBean = pointsRecordsRepository.minusMemberPoint(deletePoints, memberId);
//		return pointsRecordsBean;
//    }

	public void minusMemberPoint(int pointPrizesId, int memberId) {
	    try {
	        PointPrizesBean pointPrizesBean = pointPrizesRepository.findById(pointPrizesId)
	                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到該獎品"));

	        int deletePoints = pointPrizesBean.getPointPrizesPoints();
	        Members membersDate = membersService.findMembersById(memberId);
	        Integer PointDate = membersDate.getMemberPoint();
	        membersDate.setMemberPoint(PointDate-deletePoints);
	        membersService.updateMember(membersDate);

	        pointsRecordsRepository.minusMemberPoint(deletePoints, memberId);
	    } catch (Exception e) {
	        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "操作失败", e);
	    }
	}

	
	
}
