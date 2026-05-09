package com.reservacanchas.cl.pago_service.repository;

import com.reservacanchas.cl.pago_service.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagoRepository extends JpaRepository<Pago, Long> {
}