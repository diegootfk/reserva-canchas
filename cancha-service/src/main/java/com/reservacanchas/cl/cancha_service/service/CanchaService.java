package com.reservacanchas.cl.cancha_service.service;

import com.reservacanchas.cl.cancha_service.dto.CanchaDTO;
import com.reservacanchas.cl.cancha_service.model.Cancha;
import com.reservacanchas.cl.cancha_service.repository.CanchaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CanchaService {

    private final CanchaRepository canchaRepository;

    public CanchaService(CanchaRepository canchaRepository) {
        this.canchaRepository = canchaRepository;
    }

    public Cancha guardar(CanchaDTO canchaDTO) {
        Cancha cancha = new Cancha();

        cancha.setNombre(canchaDTO.getNombre());
        cancha.setTipoCancha(canchaDTO.getTipoCancha());
        cancha.setPrecioHora(canchaDTO.getPrecioHora());
        cancha.setCapacidad(canchaDTO.getCapacidad());
        cancha.setEstado(canchaDTO.getEstado());

        return canchaRepository.save(cancha);
    }

    public List<Cancha> listar() {
        return canchaRepository.findAll();
    }

    public Cancha buscarPorId(Long id) {
        return canchaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cancha no encontrada"));
    }

    public Cancha actualizar(Long id, CanchaDTO canchaDTO) {
        Cancha cancha = buscarPorId(id);

        cancha.setNombre(canchaDTO.getNombre());
        cancha.setTipoCancha(canchaDTO.getTipoCancha());
        cancha.setPrecioHora(canchaDTO.getPrecioHora());
        cancha.setCapacidad(canchaDTO.getCapacidad());
        cancha.setEstado(canchaDTO.getEstado());

        return canchaRepository.save(cancha);
    }

    public void eliminar(Long id) {
        Cancha cancha = buscarPorId(id);
        canchaRepository.delete(cancha);
    }

    public boolean existePorId(Long id) {
        return canchaRepository.existsById(id);
    }
}