package com.reservacanchas.cl.pago_service.service;

import com.reservacanchas.cl.pago_service.dto.PagoDTO;
import com.reservacanchas.cl.pago_service.exception.ResourceNotFoundException;
import com.reservacanchas.cl.pago_service.model.Pago;
import com.reservacanchas.cl.pago_service.repository.PagoRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class PagoService {

    private static final Logger logger =
            LoggerFactory.getLogger(PagoService.class);

    private final PagoRepository pagoRepository;
    private final WebClient.Builder webClientBuilder;

    public PagoService(
            PagoRepository pagoRepository,
            WebClient.Builder webClientBuilder) {

        this.pagoRepository = pagoRepository;
        this.webClientBuilder = webClientBuilder;
    }

    public Pago guardar(PagoDTO pagoDTO) {

        logger.info("Iniciando registro de pago para reserva {}",
                pagoDTO.getIdReserva());

        Boolean reservaExiste = webClientBuilder.build()
                .get()
                .uri("http://localhost:7093/reservas/"
                        + pagoDTO.getIdReserva()
                        + "/exists")
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if (reservaExiste == null || !reservaExiste) {

            logger.warn(
                    "No se pudo registrar pago. Reserva {} no existe",
                    pagoDTO.getIdReserva()
            );

            throw new ResourceNotFoundException(
                    "La reserva no existe"
            );
        }

        Pago pago = new Pago();

        pago.setIdReserva(pagoDTO.getIdReserva());
        pago.setMonto(pagoDTO.getMonto());
        pago.setMetodoPago(pagoDTO.getMetodoPago());
        pago.setEstadoPago("PAGADO");

        Pago pagoGuardado = pagoRepository.save(pago);

        logger.info(
                "Pago creado correctamente con ID {}",
                pagoGuardado.getId()
        );

        return pagoGuardado;
    }

    public List<Pago> listar() {

        logger.info("Listando todos los pagos");

        List<Pago> pagos = pagoRepository.findAll();

        logger.info("Se encontraron {} pagos", pagos.size());

        return pagos;
    }

    public Pago buscarPorId(Long id) {

        logger.info("Buscando pago con ID {}",
                id);

        return pagoRepository.findById(id)
                .orElseThrow(() -> {

                    logger.error(
                            "Pago no encontrado con ID {}",
                            id
                    );

                    return new ResourceNotFoundException(
                            "Pago no encontrado"
                    );
                });
    }

    public Pago actualizar(Long id, PagoDTO pagoDTO) {

        logger.info("Actualizando pago con ID {}",
                id);

        Pago pago = buscarPorId(id);

        pago.setIdReserva(pagoDTO.getIdReserva());
        pago.setMonto(pagoDTO.getMonto());
        pago.setMetodoPago(pagoDTO.getMetodoPago());

        Pago pagoActualizado = pagoRepository.save(pago);

        logger.info(
                "Pago actualizado correctamente con ID {}",
                id
        );

        return pagoActualizado;
    }

    public void eliminar(Long id) {

        logger.info("Eliminando pago con ID {}",
                id);

        Pago pago = buscarPorId(id);

        pagoRepository.delete(pago);

        logger.info(
                "Pago eliminado correctamente con ID {}",
                id
        );
    }

    public boolean existePorId(Long id) {

        logger.info(
                "Verificando existencia de pago con ID {}",
                id
        );

        boolean existe = pagoRepository.existsById(id);

        logger.info(
                "Resultado existencia pago {}: {}",
                id,
                existe
        );

        return existe;
    }

    public List<Pago> buscarPorMetodoPago(String metodoPago) {

        logger.info(
                "Buscando pagos por método {}",
                metodoPago
        );

        return pagoRepository.findByMetodoPago(metodoPago);
    }

    public List<Pago> buscarPorEstadoPago(String estadoPago) {

        logger.info(
                "Buscando pagos por estado {}",
                estadoPago
        );

        return pagoRepository.findByEstadoPago(estadoPago);
    }

    public List<Pago> buscarPorReserva(Long idReserva) {

        logger.info(
                "Buscando pagos asociados a reserva {}",
                idReserva
        );

        return pagoRepository.findByIdReserva(idReserva);
    }

    public double calcularIva(double montoNeto) {

        logger.info(
                "Calculando 19% de IVA para el monto neto: {}",
                montoNeto
        );

        return montoNeto * 0.19;
    }

    public double aplicarDescuento(
            double montoOriginal,
            double porcentajeDescuento) {

        logger.info(
                "Aplicando un descuento del {}% al monto inicial: {}",
                porcentajeDescuento,
                montoOriginal
        );

        double descuento =
                montoOriginal * (porcentajeDescuento / 100.0);

        return montoOriginal - descuento;
    }

    public double calcularTotalConIva(double montoNeto) {

        logger.info(
                "Calculando total a pagar sumando IVA para el monto neto: {}",
                montoNeto
        );

        return montoNeto + calcularIva(montoNeto);
    }
}