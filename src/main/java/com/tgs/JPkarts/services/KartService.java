package com.tgs.JPkarts.services;

import com.tgs.JPkarts.entities.KartEntity;
import com.tgs.JPkarts.repositories.KartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KartService {
    @Autowired
    KartRepository kartRepository;

    public List<KartEntity> getAllKarts() { return kartRepository.findAll();}
    public KartEntity saveKart(KartEntity kartEntity) { return kartRepository.save(kartEntity);}
    public KartEntity updateKart(KartEntity kart) { return kartRepository.save(kart);}
    public boolean deleteKartById(Long id) throws  Exception {
        try {
            kartRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
