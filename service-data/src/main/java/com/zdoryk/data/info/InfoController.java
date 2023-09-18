package com.zdoryk.data.info;

import com.zdoryk.data.dto.ContactDTO;
import com.zdoryk.data.dto.PartnerDTO;
import com.zdoryk.data.dto.PartnersPagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/info")
public class InfoController {

    private final InfoService infoService;

    @PostMapping("partner/add")
    public ResponseEntity<?> savePartner(
            @Valid @RequestBody PartnerDTO partnerDTO
    ){
        infoService.savePartner(partnerDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("partner/delete")
    public ResponseEntity<?> deletePartner(
            @RequestParam("id") Long id
    ){
        infoService.deletePartner(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("partner/update")
    public ResponseEntity<String> updatePartner(
            @RequestBody PartnerDTO partnerDTO
    ){
        infoService.updatePartner(partnerDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("partner/get-all")
    public ResponseEntity<PartnersPagination> getAllPartners(
            @RequestParam(
                    value = "page",
                    defaultValue = "1"
            ) Integer page,
            @RequestParam(
                    value = "size",
                    defaultValue = "5"
            ) Integer size
    ){
        return ResponseEntity.ok(infoService.getAllPartners(page,size));
    }

    @GetMapping ("partner/get")
    ResponseEntity<PartnerDTO> getPartnerById(
            @RequestParam("id") Long id
    ){
        return ResponseEntity.ok(infoService.getPartnerById(id));
    }

    @PatchMapping("contact/update")
    public ResponseEntity<?> updateContact(
            @Valid @RequestBody ContactDTO contactDTO
    ){
        infoService.updateContact(contactDTO);
        return ResponseEntity.ok().build();
    }

}
