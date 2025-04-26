package com.tgs.JPkarts.repositories;


import com.tgs.JPkarts.entities.VoucherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<VoucherEntity, Long> {
    List<VoucherEntity> findByClientName(String clientName);
    List<VoucherEntity> findByReservationId(Long id);
}
