package com.reservacanchas.cl.pago_service.repository;

import com.reservacanchas.cl.pago_service.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    List<Pago> findByMetodoPago(String metodoPago);

    List<Pago> findByEstadoPago(String estadoPago);

    List<Pago> findByIdReserva(Long idReserva);
}