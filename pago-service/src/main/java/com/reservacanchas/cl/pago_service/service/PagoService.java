package com.reservacanchas.cl.pago_service.service;

import com.reservacanchas.cl.pago_service.dto.PagoDTO;
import com.reservacanchas.cl.pago_service.exception.RecursoNoEncontradoException;
import com.reservacanchas.cl.pago_service.model.Pago;
import com.reservacanchas.cl.pago_service.repository.PagoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final RestTemplate restTemplate;

    public PagoService(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
        this.restTemplate = new RestTemplate();
    }

    public Pago guardar(PagoDTO pagoDTO) {

        Boolean reservaExiste = restTemplate.getForObject(
                "http://localhost:9093/reservas/" + pagoDTO.getIdReserva() + "/exists",
                Boolean.class
        );

        if (reservaExiste == null || !reservaExiste) {
            throw new RecursoNoEncontradoException("La reserva no existe");
        }

        Pago pago = new Pago();
        pago.setIdReserva(pagoDTO.getIdReserva());
        pago.setMonto(pagoDTO.getMonto());
        pago.setMetodoPago(pagoDTO.getMetodoPago());
        pago.setEstadoPago("PAGADO");

        return pagoRepository.save(pago);
    }

    public List<Pago> listar() {
        return pagoRepository.findAll();
    }

    public Pago buscarPorId(Long id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Pago no encontrado"));
    }

    public Pago actualizar(Long id, PagoDTO pagoDTO) {

        Pago pago = buscarPorId(id);

        pago.setIdReserva(pagoDTO.getIdReserva());
        pago.setMonto(pagoDTO.getMonto());
        pago.setMetodoPago(pagoDTO.getMetodoPago());

        return pagoRepository.save(pago);
    }

    public void eliminar(Long id) {
        Pago pago = buscarPorId(id);
        pagoRepository.delete(pago);
    }

    public boolean existePorId(Long id) {
        return pagoRepository.existsById(id);
    }
}