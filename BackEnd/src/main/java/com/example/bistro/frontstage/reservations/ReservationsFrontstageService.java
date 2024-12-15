package com.example.bistro.frontstage.reservations;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

	public List<Reservations> findCount(Date reservationDate, String startTime) throws ParseException {
		
		return ReservationsRepo.findByReservationDateAndStartTime(reservationDate, startTime);
	}

	public int findSeatTypeCount(int id, String type) {
		return SeatsCountRepo.findBySeatsTimeIdAndSeatType(id, type);
	}

	public int getReservedCount(Date reservationDate, String time) throws ParseException {
		List<Reservations> reservationsList = findCount(reservationDate, time);
		int count = 0;
		for (Reservations reservation : reservationsList) {
			count += reservation.getNumberPeople(); // 累加每筆預約的人數
		}
		return count;
	}

	public List<String> getAvailableTimeSlots(Date reservationDate, int numberPeople) throws ParseException {
		int count1800 = getReservedCount(reservationDate, "18:00");
		int count2000 = getReservedCount(reservationDate, "20:00");
		int count2200 = getReservedCount(reservationDate, "22:00");

		int oneSeat = findSeatTypeCount(1, "1人桌");
		int twoSeat = findSeatTypeCount(1, "2人桌");
		int fourSeat = findSeatTypeCount(1, "4人桌");
		int maxCapacity = oneSeat + twoSeat * 2 + fourSeat * 4;

		List<String> availableTimeslots = new ArrayList<>();

		if ((maxCapacity - count1800) >= numberPeople) {
			availableTimeslots.add("18:00");
		}
		if ((maxCapacity - count2000) >= numberPeople) {
			availableTimeslots.add("20:00");
		}
		if ((maxCapacity - count2200) >= numberPeople) {
			availableTimeslots.add("22:00");
		}

		return availableTimeslots;
	}
}
