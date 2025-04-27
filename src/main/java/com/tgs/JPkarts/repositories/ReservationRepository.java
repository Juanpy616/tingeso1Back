package com.tgs.JPkarts.repositories;

import com.tgs.JPkarts.entities.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    public void deleteById(Long id);
}
