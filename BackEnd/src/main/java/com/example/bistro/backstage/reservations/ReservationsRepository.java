package com.example.bistro.backstage.reservations;



import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationsRepository extends  JpaRepository<Reservations, Integer> {

	

}
