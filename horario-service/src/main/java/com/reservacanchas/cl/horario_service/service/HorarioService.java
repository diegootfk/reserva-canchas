package com.reservacanchas.cl.horario_service.service;

import com.reservacanchas.cl.horario_service.model.Horario;
import com.reservacanchas.cl.horario_service.repository.HorarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class HorarioService {

    private final HorarioRepository horarioRepository;
    private final RestTemplate restTemplate;

    public HorarioService(HorarioRepository horarioRepository) {
        this.horarioRepository = horarioRepository;
        this.restTemplate = new RestTemplate();
    }

    public Horario guardar(Horario horario) {

        Boolean canchaExiste = restTemplate.getForObject(
                "http://localhost:9092/canchas/" + horario.getIdCancha() + "/exists",
                Boolean.class
        );

        if (canchaExiste == null || !canchaExiste) {
            throw new RuntimeException("La cancha no existe");
        }

        return horarioRepository.save(horario);
    }

    public List<Horario> listar() {
        return horarioRepository.findAll();
    }

    public boolean existePorId(Long id) {
        return horarioRepository.existsById(id);
    }
}