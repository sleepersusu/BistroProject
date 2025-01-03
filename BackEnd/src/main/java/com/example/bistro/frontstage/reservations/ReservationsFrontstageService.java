package com.example.bistro.frontstage.reservations;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.bistro.backstage.reservations.Reservations;
import com.example.bistro.backstage.reservations.ReservationsRepository;
import com.example.bistro.backstage.reservations.SeatsCountRepository;

@Service
public class ReservationsFrontstageService {

	@Autowired
	private ReservationsRepository ReservationsRepo;

	@Autowired
	private SeatsCountRepository SeatsCountRepo;

	public Reservations insert(ReservationDTO dto) {

		Map<String, String> errors = new HashMap<>();
		
		if (dto.getCustomerName() == null || dto.getCustomerName().trim().isEmpty()) {
			 errors.put("customerName", "姓名不能為空白");
		} else if (dto.getCustomerName().length() > 15) {
			  errors.put("customerName", "姓名不能超過15個字元");
		}
		if (dto.getCustomerGender() == null) {
			 errors.put("customerGender", "請你選擇性別");
		}
		if (dto.getNumberPeople() == 0) {
			  errors.put("numberpeople", "請你選擇人數");		
		}
		if (dto.getContactPhone() == null || !dto.getContactPhone().matches("^09\\d{8}$")) {
			errors.put("contactPhone", "電話號碼必須是09開頭，且包含10個數字");
		}
		if (dto.getReservationDate() == null) {
			 errors.put("reservationDate", "請你選擇訂位日期");
		}
		if (dto.getStartTime() == null) {
			  errors.put("startTime", "選擇的時段無效，請選擇可用的時段");		
		}
		
		 if (!errors.isEmpty()) {
		        throw new IllegalArgumentException(errors.toString());
		    }
		 
		Reservations reservations = new Reservations();
		reservations.setCustomerName(dto.getCustomerName());
		reservations.setCustomerGender(dto.getCustomerGender());
		reservations.setContactPhone(dto.getContactPhone());
		reservations.setReservationDate(dto.getReservationDate());
		reservations.setStartTime(dto.getStartTime());
		reservations.setNumberPeople(dto.getNumberPeople());
		reservations.setNotes(dto.getNotes());
		if (dto.getReservationStatus() == null) {
			reservations.setReservationStatus("已確認");
		}
		return ReservationsRepo.save(reservations);
	}

	// 找到訂位日期時間每一筆 可以用get取出每一筆人數 調用JPA
	public List<Reservations> findCount(Date reservationDate, String startTime) throws ParseException {

		return ReservationsRepo.findByReservationDateAndStartTime(reservationDate, startTime);
	}

	// 調用JPA取得各類桌子總數
	public int findSeatTypeCount(int id, String type) {
		return SeatsCountRepo.findBySeatsTimeIdAndSeatType(id, type);
	}

	// 計算占用的桌類  
	private Integer[] OccupyTable(Date reservationDate, String startTime) throws ParseException {

		List<Reservations> reservationList = findCount(reservationDate, startTime);

		Integer[] occupiedTables = { 0, 0, 0 };  // 初始化為 0

		for (Reservations reservation : reservationList) {
			int numberPeople = reservation.getNumberPeople();
			allocate(occupiedTables, numberPeople); // 再調用方法 根據人數分配桌型
		}
		return occupiedTables;
	}

	// 分配桌型計算該給哪種桌類
	private void allocate(Integer[] occupiedTables, int numberPeople) {
		// occupiedTables: [0]: 1人桌, [1]: 2人桌, [2]: 4人桌

		// 初始化
		if (occupiedTables[0] == null)
			occupiedTables[0] = 0;
		if (occupiedTables[1] == null)
			occupiedTables[1] = 0;
		if (occupiedTables[2] == null)
			occupiedTables[2] = 0;

		if (numberPeople <= 0)
			return;

		if (numberPeople > 6) {
			occupiedTables[2] += 2;
		} else if (numberPeople > 4) {
			// 5-6人的情況  暫定一個4人桌和一個2人桌
			occupiedTables[2] += 1;
			occupiedTables[1] += 1;
		} else if (numberPeople > 2) {
			occupiedTables[2] += 1;
		}else if (numberPeople == 2) {
		    if (occupiedTables[1] > 0) {
		        occupiedTables[1] += 1;
		    } else {
		        // 如果沒有2人桌，使用兩個1人桌
		        occupiedTables[0] += 2; // 兩人使用兩個1人桌。
		    }
		}else {
		    if (occupiedTables[0] > 0) {
		        occupiedTables[0] += 1;
		    } else {
		        // 如果沒有1人桌，使用2人桌。
		        occupiedTables[1] += 1; // 一人使用2人桌。
		    }
		}
	}

	// 用於判斷是否可以在某個时段訂位
	private boolean canReserve(int fourSeat, int twoSeat, int oneSeat, int numberPeople) {

		if (numberPeople <= 0 || numberPeople > 8) {
			return false;
		}

		// 複製可用座位數，避免修改原始值
		int availableFourSeat = fourSeat;
		int availableTwoSeat = twoSeat;
		int availableOneSeat = oneSeat;

		// 針對不同人數進行檢查
		  if (numberPeople > 6) {
		        // 需要兩張4人桌
		        if (availableFourSeat >= 2) {
		            return true;
		        }
		    } else if (numberPeople > 4) {
		        // 需要一張4人桌和一張2人桌
		        if (availableFourSeat >= 1 && availableTwoSeat >= 1) {
		            return true;
		        }
		    } else if (numberPeople > 2) {
		        // 3-4人的情況，使用一張4人桌
		        if (availableFourSeat >= 1) {
		            return true;
		        }
		    } else if (numberPeople == 2) {
		        // 優先檢查2人桌
		        if (availableTwoSeat >= 1) {
		            return true;
		        }
		        // 如果沒有2人桌，檢查兩張1人桌
		        if (availableOneSeat >= 2) {
		            return true;
		        }
		    } else {
		        // 1人的情況，優先使用1人桌
		        if (availableOneSeat >= 1) {
		            return true;
		        }
		        // 如果沒有1人桌，檢查2人桌是否可用
		        if (availableTwoSeat >= 1) {
		            return true;
		        }
		    }

		    return false; // 無法分配座位
	}

	// 取得日期人數 返回可以 選擇的時段
	public List<String> getAvailableTimeSlots(Date reservationDate, int numberPeople) {
		if (numberPeople <= 0 || numberPeople > 8) {
			return new ArrayList<>(); // 人數超出範圍，返回空列表
		}

		int oneSeat = findSeatTypeCount(1, "1人桌");
		int twoSeat = findSeatTypeCount(1, "2人桌");
		int fourSeat = findSeatTypeCount(1, "4人桌");

		String[] timeSlots = { "18:00", "20:00", "22:00" };
		List<String> availableTimeslots = new ArrayList<>();

		for (String time : timeSlots) {
			try {
				if (isTimeSlotAvailable(reservationDate, time, oneSeat, twoSeat, fourSeat, numberPeople)) {
					availableTimeslots.add(time);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return availableTimeslots;
	}
	//用來判斷 調用兩個方法 可以找出能訂位的時段
	private boolean isTimeSlotAvailable(Date reservationDate, String time, int oneSeat, int twoSeat, int fourSeat,
			int numberPeople) throws ParseException {
		Integer[] occupied = safeOccupyTable(reservationDate, time);
		int availableOneSeat = getAvailableSeats(oneSeat, occupied[0]);
		int availableTwoSeat = getAvailableSeats(twoSeat, occupied[1]);
		int availableFourSeat = getAvailableSeats(fourSeat, occupied[2]);
		return canReserve(availableFourSeat, availableTwoSeat, availableOneSeat, numberPeople);
	}
	// 這裡計算總座位減去占用座位
	private int getAvailableSeats(int totalSeats, Integer occupiedSeats) {
		return Math.max(0, totalSeats - (occupiedSeats != null ? occupiedSeats : 0));
	}

	private Integer[] safeOccupyTable(Date reservationDate, String time) {
		try {
			return OccupyTable(reservationDate, time);
		} catch (ParseException e) {
			e.printStackTrace();
			return new Integer[] { 0, 0, 0 }; 
		}
	}

}
