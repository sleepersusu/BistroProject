package com.example.bistro.backstage.reservations;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReservationsController {

	@Autowired
	private ReservationsService reservationsService;

//	@Autowired
//	private SeatsCountRepository SeatsCountRepo;

	@GetMapping("/Bistro/findAllReservations")
	public String findAllReservations(Model model) {
		List<Reservations> reservationsList = reservationsService.findAllReservations();

		model.addAttribute("allReservations", reservationsList);

		return "reservations/showAllReservationsView";

	}

	@PostMapping("/Bistro/deleteReservations")
	public String deleteReservations(@RequestParam Integer id) {

		reservationsService.deleteReservations(id);

		return "redirect:/Bistro/findAllReservations";

	}

	@PostMapping("/Bistro/updatePost")
	public String updateReservations(Integer id2, String customerName2, String customerGender2, String contactPhone2,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date reservationDate2, String startTime2,
			Integer numberPeople2, String notes2, String reservationStatus2) throws ParseException {

		reservationsService.updateReservations(id2, customerName2, customerGender2, contactPhone2, reservationDate2,
				startTime2, numberPeople2, notes2, reservationStatus2);

		return "redirect:/Bistro/findAllReservations";
	}

	@PostMapping("/Bistro/postReservations")
	public String postReservations(String customerName, String customerGender, String contactPhone,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date reservationDate,
			@DateTimeFormat(pattern = "HH:mm") String startTime, Integer numberPeople, String notes,
			String reservationStatus) throws ParseException {

		reservationsService.createReservations(customerName, customerGender, contactPhone, reservationDate, startTime,
				numberPeople, notes, reservationStatus);
		return "redirect:/Bistro/findAllReservations";
	}

}
