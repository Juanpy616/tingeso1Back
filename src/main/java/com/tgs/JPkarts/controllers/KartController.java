package com.tgs.JPkarts.controllers;

import com.tgs.JPkarts.entities.KartEntity;
import com.tgs.JPkarts.repositories.KartRepository;
import com.tgs.JPkarts.services.KartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tgs/rmkarts/kart")
public class KartController {
    @Autowired
    KartService kartService;
    @Autowired
    KartRepository kartRepository;

    @PostMapping("/")
    public ResponseEntity<KartEntity> createKart(@RequestBody KartEntity kart){
        KartEntity kartNew = kartService.saveKart(kart);
        return ResponseEntity.ok(kartNew);
    }

    @GetMapping("/")
    public ResponseEntity<List<KartEntity>> listKarts(){
        List<KartEntity> karts = kartService.getAllKarts();
        return ResponseEntity.ok(karts);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteKart(@PathVariable long id) throws Exception {
        var isDeleted = kartService.deleteKartById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<KartEntity> updateKart(@RequestBody KartEntity kart) {
        KartEntity kartUpdated = kartService.updateKart(kart);
        return ResponseEntity.ok(kartUpdated);
    }

}
