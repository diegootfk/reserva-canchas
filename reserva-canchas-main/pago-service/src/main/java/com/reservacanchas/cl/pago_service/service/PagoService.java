package com.reservacanchas.cl.pago_service.service;

import com.reservacanchas.cl.pago_service.dto.PagoDTO;
import com.reservacanchas.cl.pago_service.exception.ResourceNotFoundException;
import com.reservacanchas.cl.pago_service.model.Pago;
import com.reservacanchas.cl.pago_service.repository.PagoRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class PagoService {

    private static final Logger logger = LoggerFactory.getLogger(PagoService.class);

    private final PagoRepository pagoRepository;
    private final RestTemplate restTemplate;

    public PagoService(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
        this.restTemplate = new RestTemplate();
    }

    public Pago guardar(PagoDTO pagoDTO) {

        logger.info("Iniciando registro de pago para reserva {}", pagoDTO.getIdReserva());

        Boolean reservaExiste = restTemplate.getForObject(
                "http://localhost:7093/reservas/" + pagoDTO.getIdReserva() + "/exists",
                Boolean.class
        );

        if (reservaExiste == null || !reservaExiste) {
            logger.warn("No se pudo registrar pago. Reserva {} no existe", pagoDTO.getIdReserva());
            throw new ResourceNotFoundException("La reserva no existe");
        }

        Pago pago = new Pago();
        pago.setIdReserva(pagoDTO.getIdReserva());
        pago.setMonto(pagoDTO.getMonto());
        pago.setMetodoPago(pagoDTO.getMetodoPago());
        pago.setEstadoPago("PAGADO");

        Pago pagoGuardado = pagoRepository.save(pago);

        logger.info("Pago creado correctamente con ID {}", pagoGuardado.getId());

        return pagoGuardado;
    }

    public List<Pago> listar() {
        logger.info("Listando todos los pagos");
        return pagoRepository.findAll();
    }

    public Pago buscarPorId(Long id) {
        logger.info("Buscando pago con ID {}", id);

        return pagoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Pago con ID {} no encontrado", id);
                    return new ResourceNotFoundException("Pago no encontrado");
                });
    }

    public Pago actualizar(Long id, PagoDTO pagoDTO) {

        logger.info("Actualizando pago con ID {}", id);

        Pago pago = buscarPorId(id);

        pago.setIdReserva(pagoDTO.getIdReserva());
        pago.setMonto(pagoDTO.getMonto());
        pago.setMetodoPago(pagoDTO.getMetodoPago());

        Pago pagoActualizado = pagoRepository.save(pago);

        logger.info("Pago con ID {} actualizado correctamente", id);

        return pagoActualizado;
    }

    public void eliminar(Long id) {
        logger.info("Eliminando pago con ID {}", id);

        Pago pago = buscarPorId(id);
        pagoRepository.delete(pago);

        logger.info("Pago con ID {} eliminado correctamente", id);
    }

    public boolean existePorId(Long id) {
        logger.info("Verificando existencia de pago con ID {}", id);
        return pagoRepository.existsById(id);
    }

    public List<Pago> buscarPorMetodoPago(String metodoPago) {
        logger.info("Buscando pagos por método {}", metodoPago);
        return pagoRepository.findByMetodoPago(metodoPago);
    }

    public List<Pago> buscarPorEstadoPago(String estadoPago) {
        logger.info("Buscando pagos por estado {}", estadoPago);
        return pagoRepository.findByEstadoPago(estadoPago);
    }

    public List<Pago> buscarPorReserva(Long idReserva) {
        logger.info("Buscando pagos asociados a reserva {}", idReserva);
        return pagoRepository.findByIdReserva(idReserva);
    }
}