package com.tgs.JPkarts.controllers;

import com.tgs.JPkarts.entities.ReservationEntity;
import com.tgs.JPkarts.entities.VoucherEntity;
import com.tgs.JPkarts.services.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tgs/rmkarts/voucher")
@CrossOrigin("*")
public class VoucherController {
    @Autowired
    VoucherService voucherService;

    @GetMapping("/")
    public ResponseEntity<List<VoucherEntity>> listVouchers() {
        List<VoucherEntity> vouchers = voucherService.getAllVouchers();
        return ResponseEntity.ok(vouchers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VoucherEntity> getVoucherById(@PathVariable long id) {
        VoucherEntity voucher = voucherService.getVoucherById(id);
        return ResponseEntity.ok(voucher);
    }

    @PostMapping("/")
    public ResponseEntity<VoucherEntity> createVoucher(@RequestBody VoucherEntity voucher){
        VoucherEntity voucherNew = voucherService.saveVoucher(voucher);
        return ResponseEntity.ok(voucherNew);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VoucherEntity> updateVoucher(@RequestBody VoucherEntity voucher) {
        VoucherEntity updatedVoucher = voucherService.updateVoucher(voucher);
        return ResponseEntity.ok(updatedVoucher);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteVoucher(@PathVariable long id) throws Exception {
        var isDeleted = voucherService.deleteVoucherById(id);
        return ResponseEntity.noContent().build();
    }


}
