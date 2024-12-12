package com.example.bistro.backstage.reservations;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SeatsCountRepository extends JpaRepository<SeatsCount, Integer>{

	@Query("select seatCount from SeatsCount where seatsTimeId= :id and seatType= :type")
	Integer findBySeatsTimeIdAndSeatType(@Param("id")int id,@Param("type")String type);
}
