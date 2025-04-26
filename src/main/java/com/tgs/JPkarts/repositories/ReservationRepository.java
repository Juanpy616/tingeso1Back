package com.tgs.JPkarts.repositories;

import com.tgs.JPkarts.entities.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    public List<ReservationEntity> findByClientName(String clientName);

    List<ReservationEntity> findByDateBetween(Date dateAfter, Date dateBefore);
    public void deleteById(Long id);


}
