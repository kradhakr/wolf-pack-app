package com.spring.wolves.packs.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import com.spring.wolves.packs.exception.ResourceNotFoundException;
import com.spring.wolves.packs.model.Wolf;
import com.spring.wolves.packs.repository.PackRepository;
import com.spring.wolves.packs.repository.WolfRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping
public class WolfController {
	@Autowired
	private WolfRepository wolfRepository;


	@GetMapping("/wolves")
	public List<Wolf> getAllWolves() {
		return wolfRepository.findAll();
	}

	@GetMapping("/wolves/{id}")
	public ResponseEntity<Wolf> getWolvesById(@PathVariable(value = "id") Long wolfId)
			throws ResourceNotFoundException {
		Wolf wolf = wolfRepository.findById(wolfId)
				.orElseThrow(() -> new ResourceNotFoundException("Wolf not found for this id :: " + wolfId));
		return ResponseEntity.ok().body(wolf);
	}

	@PostMapping("/wolves")
	public Wolf createWolves(@Valid @RequestBody Wolf wolf) {
		return wolfRepository.save(wolf);
	}

	@PutMapping("/wolves/{id}")
	public ResponseEntity<Wolf> updateWolves(@PathVariable(value = "id") Long wolfId,
											 @Valid @RequestBody Wolf wolfDetails) throws ResourceNotFoundException {
		Wolf wolf = wolfRepository.findById(wolfId)
				.orElseThrow(() -> new ResourceNotFoundException("Wolf not found for this id :: " + wolfId));
		wolf.setWolfName(wolfDetails.getWolfName());
		wolf.setGender(wolfDetails.getGender());
		wolf.setBirthDate(wolfDetails.getBirthDate());
		wolf.setLocation(wolfDetails.getLocation());
		final Wolf updatedWolf = wolfRepository.save(wolf);
		return ResponseEntity.ok(updatedWolf);
	}

	@DeleteMapping("/wolves/{id}")
	public Map<String, Boolean> deleteWolves(@PathVariable(value = "id") Long wolfId)
			throws ResourceNotFoundException {
		    Wolf wolf = wolfRepository.findById(wolfId)
				.orElseThrow(() -> new ResourceNotFoundException("Wolf not found for this id :: " + wolfId));

			wolfRepository.delete(wolf);
			Map<String, Boolean> response = new HashMap<>();
			response.put("deleted", Boolean.TRUE);
		    return response;
	}


}
