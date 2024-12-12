package com.example.bistro.backstage.reservations;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "SeatsCount")
public class SeatsCount {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer seatsTimeId;
	private String seatType;
	private Integer seatCount;

	public Integer getSeatsTimeId() {
		return seatsTimeId;
	}
	public void setSeatsTimeId(Integer seatsTimeId) {
		this.seatsTimeId = seatsTimeId;
	}
	public String getSeatType() {
		return seatType;
	}
	public void setSeatType(String seatType) {
		this.seatType = seatType;
	}
	public Integer getSeatCount() {
		return seatCount;
	}
	public void setSeatCount(Integer seatCount) {
		this.seatCount = seatCount;
	}
	
}
