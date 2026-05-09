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

    public Cancha guardar(CanchaDTO canchaDTO){

        Cancha cancha = new Cancha();

        cancha.setNombre(canchaDTO.getNombre());
        cancha.setTipoCancha(canchaDTO.getTipoCancha());
        cancha.setPrecioHora(canchaDTO.getPrecioHora());
        cancha.setCapacidad(canchaDTO.getCapacidad());
        cancha.setEstado(canchaDTO.getEstado());

        return canchaRepository.save(cancha);
    }

    public List<Cancha> listar(){
        return canchaRepository.findAll();
    }

    public boolean existePorId(Long id){
        return canchaRepository.existsById(id);
    }
}