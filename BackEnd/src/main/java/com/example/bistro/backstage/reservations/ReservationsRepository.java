package com.example.bistro.backstage.reservations;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationsRepository extends  JpaRepository<Reservations, Integer> {

	@Query("from Reservations where reservationDate= :d and startTime= :t")
	List<Reservations>findByReservationDateAndStartTime(@Param("d") Date reservationDate,@Param("t")String startTime);

}
