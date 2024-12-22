package com.example.bistro.frontstage.reservations;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.bistro.backstage.reservations.Reservations;
import com.example.bistro.backstage.reservations.ReservationsRepository;
import com.example.bistro.backstage.reservations.SeatsCountRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class ReservationsFrontstageService {

	@Autowired
	private ReservationsRepository ReservationsRepo;

	@Autowired
	private SeatsCountRepository SeatsCountRepo;

	// 這些可以放在配置文件中，使用 @Value 或其他配置方式來注入
	

	// 發送訂位確認簡訊的動態方法
//	public void sendReservationConfirmation(String contactPhone, Date reservationDate, String starTime,
//			Integer numberPeople) {
//
//    		 String formattedPhone= "";   				 
////    		 String formattedPhone = "+886" + contactPhone.substring(1);; 接使用者的電話號碼
//
//		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//		
//		LocalDate Date = LocalDate.now();   //要把格式改成
//		
//		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//	        String formattedDate = Date.format(formatter);
//
//		String messageContent = String.format("您的訂位成功！\n" + "訂位日期：%s\n" + "訂位時間：%s\n" + "人數：%d\n" + "感謝您的預約，期待您的光臨！",
//				formattedDate, starTime, numberPeople);
//
//		Message message = Message.creator(new PhoneNumber(formattedPhone), // 接收簡訊的號碼
//				new PhoneNumber(FROM_PHONE_NUMBER), // 你的 Twilio 虛擬號碼
//				messageContent) // 簡訊的內容
//				.create();
//
//		System.out.println("Message sent! SID: " + message.getSid());
//
//	}

	public Reservations insert(ReservationDTO dto) {

		if (dto.getCustomerName() == null || dto.getCustomerName().trim().isEmpty()) {
			throw new IllegalArgumentException("姓名不能為空白");
		}
		if (dto.getContactPhone() == null || !dto.getContactPhone().matches("\\d{10}")) {
			throw new IllegalArgumentException("電話號碼必須是10個數字");
		}
		if (dto.getStartTime() == null) {
			throw new IllegalArgumentException("請你選擇訂位時段");
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
		} else if (numberPeople == 2) {
			occupiedTables[1] += 1;
		} else {
			occupiedTables[0] += 1;
		}
	}

	// 用于判断是否可以在某个时段预定
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
			if (availableFourSeat >= 2) {
				return true;
			}
		} else if (numberPeople > 4) {
			// 5-6人 目前暫定一個4人桌加一個2人桌
			if (availableFourSeat >= 1 && availableTwoSeat >= 1) {
				return true;
			}
			// 也可以分配4人桌
			if (availableFourSeat >= 2) {
				return true;
			}
		} else if (numberPeople > 2) {
			if (availableFourSeat >= 1) {
				return true;
			}
			// 或者使用兩個2人桌
			if (availableTwoSeat >= 2) {
				return true;
			}
		} else if (numberPeople == 2) {
			if (availableTwoSeat >= 1 || availableFourSeat >= 1) {
				return true;
			}
			if (availableOneSeat >= 2) {
				return true;
			}
		} else {
			if (availableOneSeat >= 1 || availableTwoSeat >= 1 || availableFourSeat >= 1) {
				return true;
			}
		}

		return false;
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
			return new Integer[] { 0, 0, 0 }; // 發生錯誤時返回空佔用數據
		}
	}

}
