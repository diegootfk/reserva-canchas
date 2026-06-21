package com.reservacanchas.cl.pago_service.service;

import com.reservacanchas.cl.pago_service.dto.PagoDTO;
import com.reservacanchas.cl.pago_service.exception.ResourceNotFoundException;
import com.reservacanchas.cl.pago_service.model.Pago;
import com.reservacanchas.cl.pago_service.repository.PagoRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class PagoService {

    private static final Logger logger = LoggerFactory.getLogger(PagoService.class);

    private static final String RESERVA_SERVICE_URL = "http://localhost:7093/reservas/";

    private final PagoRepository pagoRepository;
    private final RestTemplate restTemplate;

    public PagoService(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
        this.restTemplate = new RestTemplate();
    }

    public Pago guardar(PagoDTO pagoDTO) {

        logger.info("Iniciando registro de pago para reserva ID: {}", pagoDTO.getIdReserva());

        validarReservaExiste(pagoDTO.getIdReserva());

        Pago pago = new Pago();

        pago.setIdReserva(pagoDTO.getIdReserva());
        pago.setMonto(pagoDTO.getMonto());
        pago.setMetodoPago(pagoDTO.getMetodoPago());
        pago.setEstadoPago("PAGADO");

        Pago pagoGuardado = pagoRepository.save(pago);

        logger.info("Pago registrado correctamente con ID: {}", pagoGuardado.getId());

        return pagoGuardado;
    }

    public List<Pago> listar() {

        logger.info("Iniciando búsqueda de todos los pagos");

        List<Pago> pagos = pagoRepository.findAll();

        logger.info("Búsqueda finalizada. Total de pagos encontrados: {}", pagos.size());

        return pagos;
    }

    public Pago buscarPorId(Long id) {

        logger.info("Iniciando búsqueda de pago con ID: {}", id);

        return pagoRepository.findById(id)
                .map(pago -> {
                    logger.info("Pago encontrado correctamente con ID: {}", id);
                    return pago;
                })
                .orElseThrow(() -> {
                    logger.warn("Pago no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Pago no encontrado");
                });
    }

    public Pago actualizar(Long id, PagoDTO pagoDTO) {

        logger.info("Iniciando actualización de pago con ID: {}", id);

        Pago pago = buscarPorId(id);

        validarReservaExiste(pagoDTO.getIdReserva());

        pago.setIdReserva(pagoDTO.getIdReserva());
        pago.setMonto(pagoDTO.getMonto());
        pago.setMetodoPago(pagoDTO.getMetodoPago());

        Pago pagoActualizado = pagoRepository.save(pago);

        logger.info("Pago actualizado correctamente con ID: {}", id);

        return pagoActualizado;
    }

    public void eliminar(Long id) {

        logger.warn("Iniciando eliminación de pago con ID: {}", id);

        Pago pago = buscarPorId(id);

        pagoRepository.delete(pago);

        logger.info("Pago eliminado correctamente con ID: {}", id);
    }

    public boolean existePorId(Long id) {

        logger.info("Verificando existencia de pago con ID: {}", id);

        boolean existe = pagoRepository.existsById(id);

        logger.info("Resultado de existencia para pago con ID {}: {}", id, existe);

        return existe;
    }

    public List<Pago> buscarPorMetodoPago(String metodoPago) {

        logger.info("Buscando pagos con método de pago: {}", metodoPago);

        List<Pago> pagos = pagoRepository.findByMetodoPago(metodoPago);

        logger.info("Se encontraron {} pagos con método de pago: {}", pagos.size(), metodoPago);

        return pagos;
    }

    public List<Pago> buscarPorEstadoPago(String estadoPago) {

        logger.info("Buscando pagos con estado: {}", estadoPago);

        List<Pago> pagos = pagoRepository.findByEstadoPago(estadoPago);

        logger.info("Se encontraron {} pagos con estado: {}", pagos.size(), estadoPago);

        return pagos;
    }

    public List<Pago> buscarPorReserva(Long idReserva) {

        logger.info("Buscando pagos asociados a reserva ID: {}", idReserva);

        List<Pago> pagos = pagoRepository.findByIdReserva(idReserva);

        logger.info("Se encontraron {} pagos asociados a reserva ID: {}", pagos.size(), idReserva);

        return pagos;
    }

    private void validarReservaExiste(Long idReserva) {

        logger.info("Validando existencia de reserva con ID: {}", idReserva);

        try {
            Boolean reservaExiste = restTemplate.getForObject(
                    RESERVA_SERVICE_URL + idReserva + "/exists",
                    Boolean.class
            );

            if (reservaExiste == null || !reservaExiste) {
                logger.warn("Validación fallida: reserva con ID {} no existe", idReserva);
                throw new ResourceNotFoundException("La reserva no existe");
            }

            logger.info("Reserva validada correctamente con ID: {}", idReserva);

        } catch (RestClientException ex) {
            logger.error("Error remoto al validar reserva con ID: {}", idReserva, ex);
            throw new ResourceNotFoundException("No se pudo validar la existencia de la reserva");
        }
    }
}