package com.spring.wolves.packs.controller;

import com.spring.wolves.packs.exception.ResourceNotFoundException;
import com.spring.wolves.packs.model.Packs;
import com.spring.wolves.packs.repository.PackRepository;
import com.spring.wolves.packs.repository.WolfRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping
public class PackController {

    @Autowired
    private PackRepository packRepository;

    @Autowired
    private WolfRepository wolfRepository;


    @GetMapping("/packs")
    public List<Packs> getPacks() {
        return packRepository.findAll();
    }

    @GetMapping("/packs/{id}")
    public ResponseEntity<Packs> getPacksById(
            @PathVariable(value = "id") Long packsId) throws ResourceNotFoundException {
        Packs user = packRepository.findById(packsId)
                .orElseThrow(() -> new ResourceNotFoundException("Pack not found :: " + packsId));
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/packs")
    public Packs createPacks(@Valid @RequestBody Packs packs) {
        return packRepository.save(packs);
    }

    @PutMapping("/packs/{id}")
    public ResponseEntity <Packs> updatePacks(
            @PathVariable(value = "id") Long packsId,
            @Valid @RequestBody Packs userDetails) throws ResourceNotFoundException {
        Packs packs = packRepository.findById(packsId)
                .orElseThrow(() -> new ResourceNotFoundException("Pack not found :: " + packsId));
        wolfRepository.deleteAll(packs.getWolfList());
        packs.setPackName(userDetails.getPackName());
        packs.setWolfList(userDetails.getWolfList());
        final Packs updatedUser = packRepository.save(packs);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/packs/{id}")
    public ResponseEntity<HttpStatus> deletePacksById(
            @PathVariable(value = "id") Long packsId) throws ResourceNotFoundException {
        Packs packs = packRepository.findById(packsId)
                .orElseThrow(() -> new ResourceNotFoundException("Pack not found :: " + packsId));
        try {
            packRepository.delete(packs);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/packs/{packsId}/wolves/{wolvesId}")
    public ResponseEntity<HttpStatus> deleteWolvesInPackById(
            @PathVariable(value = "packsId") Long packsId,@PathVariable(value = "wolvesId") Long wolvesId) throws ResourceNotFoundException {
        Packs packs = packRepository.findById(packsId)
                .orElseThrow(() -> new ResourceNotFoundException("Packs not found :: " + packsId));
        try {
            packs.getWolfList().remove(wolfRepository.findById(wolvesId)
                    .orElseThrow(() -> new ResourceNotFoundException("Wolves not found :: " + wolvesId)));
            packRepository.save(packs);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @DeleteMapping("/packs/{packsId}/wolves")
    public Map<String,Boolean> deleteAllWolvesFromPack(
            @PathVariable(value = "packsId") Long packsId) throws ResourceNotFoundException {
        Packs packs = packRepository.findById(packsId)
                .orElseThrow(() -> new ResourceNotFoundException("Packs not found :: " + packsId));
        packs.getWolfList().removeAll(packs.getWolfList());
        packRepository.save(packs);
        Map <String, Boolean > response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

}
