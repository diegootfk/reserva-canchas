package com.reservacanchas.cl.reserva_service.repository;

import com.reservacanchas.cl.reserva_service.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByEstado(String estado);

    List<Reserva> findByIdUsuario(Long idUsuario);

    List<Reserva> findByIdCancha(Long idCancha);
}