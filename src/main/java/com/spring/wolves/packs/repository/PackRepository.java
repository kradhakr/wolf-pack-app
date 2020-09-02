package com.spring.wolves.packs.repository;

import com.spring.wolves.packs.model.Packs;
import com.spring.wolves.packs.model.Wolf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PackRepository extends JpaRepository<Packs, Long>{
}
