package com.script.academia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.script.academia.entities.MensagemSuporte;

public interface MensagemSuporteRepository extends JpaRepository<MensagemSuporte, Long> {
	
}
