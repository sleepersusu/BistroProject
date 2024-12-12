package com.example.bistro.frontstage.reservations;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.bistro.backstage.reservations.ReservationDTO;
import com.example.bistro.backstage.reservations.Reservations;
import com.example.bistro.backstage.reservations.ReservationsService;

@RestController
public class ReservationRestController {
	
	@Autowired
	private ReservationsService reservationsService; 
		
	@PostMapping("/api/Bistro/insert")
	public Reservations insert(@RequestBody ReservationDTO dto) {
		
		Reservations reservations = new Reservations();
		reservations.setCustomerName(dto.getCustomerName());
		reservations.setCustomerGender(dto.getCustomerName());
		reservations.setContactPhone(dto.getContactPhone());		
		reservations.setReservationDate(dto.getReservationDate());			
		reservations.setStartTime(dto.getStartTime());		
		reservations.setNumberPeople(dto.getNumberPeople());
		reservations.setNotes(dto.getNotes());
		if(dto.getReservationStatus() == null) {
			reservations.setReservationStatus("已確認");
		}			
		reservationsService.insert(reservations);
		return reservations;		//可以改成字串OK
	}
	
	@PostMapping("/api/Bistro/remaining")
	public List<String> remaining(@RequestBody ReservationDTO reservation) throws ParseException {
		
		int count1800=0;
		int count2000=0;
		int count2200=0;
				
		List<Reservations> list=reservationsService.findCount(reservation.getReservationDate(),"18:00");
		for (Reservations r : list) {
			count1800 += r.getNumberPeople();
		}		
		List<Reservations> list2=reservationsService.findCount(reservation.getReservationDate(),"20:00");
		for (Reservations r : list2) {
			count2000 += r.getNumberPeople();
		}		
		List<Reservations> list3=reservationsService.findCount(reservation.getReservationDate(),"22:00");
		for (Reservations r : list3) {
			count2200 += r.getNumberPeople();
		}
		
		Integer oneseat=reservationsService.findSeatTypeCount(1, "1人桌");
		Integer twoseat=reservationsService.findSeatTypeCount(1, "2人桌");
		Integer foutseat=reservationsService.findSeatTypeCount(1, "4人桌");		
		Integer maxCapacity = oneseat + twoseat*2 + foutseat*4;

		    List<String> availableTimeslots = new ArrayList<>();

		    if ((maxCapacity - count1800) >= reservation.getNumberPeople()) {
		        availableTimeslots.add("18:00");
		    }
		    if ((maxCapacity - count2000) >= reservation.getNumberPeople()) {
		        availableTimeslots.add("20:00");
		    }
		    if ((maxCapacity - count2200) >= reservation.getNumberPeople()) {
		        availableTimeslots.add("22:00");
		    }

		    if (!availableTimeslots.isEmpty()) {
		        return availableTimeslots;
		    } else {
		        // 如果没有可用的时段，则提示用户没有足够的座位
		        throw new RuntimeException("没有足够的座位可用，请选择其他时间段。");
		    }					
	}
	
	
	
	
}
