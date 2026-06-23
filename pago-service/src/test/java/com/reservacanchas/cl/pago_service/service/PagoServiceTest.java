package com.reservacanchas.cl.pago_service.service;

import com.reservacanchas.cl.pago_service.dto.PagoDTO;
import com.reservacanchas.cl.pago_service.exception.ResourceNotFoundException;
import com.reservacanchas.cl.pago_service.model.Pago;
import com.reservacanchas.cl.pago_service.repository.PagoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @InjectMocks
    private PagoService pagoService;

    private Pago pago;
    private PagoDTO pagoDTO;

    @BeforeEach
    void setUp() {

        pago = new Pago(
                1L,
                1L,
                25000.0,
                "TRANSFERENCIA",
                "PAGADO"
        );

        pagoDTO = new PagoDTO(
                1L,
                25000.0,
                "TRANSFERENCIA"
        );
    }

    @Test
    void listarDebeRetornarPagos() {

        when(pagoRepository.findAll())
                .thenReturn(List.of(pago));

        List<Pago> resultado = pagoService.listar();

        assertEquals(1, resultado.size());

        verify(pagoRepository).findAll();
    }

    @Test
    void buscarPorIdDebeRetornarPago() {

        when(pagoRepository.findById(1L))
                .thenReturn(Optional.of(pago));

        Pago resultado = pagoService.buscarPorId(1L);

        assertEquals(1L, resultado.getId());
    }

    @Test
    void buscarPorIdDebeLanzarExcepcion() {

        when(pagoRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> pagoService.buscarPorId(99L)
        );
    }

    @Test
    void actualizarDebeActualizarPago() {

        when(pagoRepository.findById(1L))
                .thenReturn(Optional.of(pago));

        when(pagoRepository.save(any(Pago.class)))
                .thenReturn(pago);

        Pago resultado =
                pagoService.actualizar(1L, pagoDTO);

        assertNotNull(resultado);

        verify(pagoRepository).save(any(Pago.class));
    }

    @Test
    void actualizarDebeLanzarExcepcionSiNoExiste() {

        when(pagoRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> pagoService.actualizar(99L, pagoDTO)
        );
    }

    @Test
    void eliminarDebeEliminarPago() {

        when(pagoRepository.findById(1L))
                .thenReturn(Optional.of(pago));

        pagoService.eliminar(1L);

        verify(pagoRepository).delete(pago);
    }

    @Test
    void existePorIdDebeRetornarTrue() {

        when(pagoRepository.existsById(1L))
                .thenReturn(true);

        assertTrue(pagoService.existePorId(1L));
    }

    @Test
    void existePorIdDebeRetornarFalse() {

        when(pagoRepository.existsById(99L))
                .thenReturn(false);

        assertFalse(pagoService.existePorId(99L));
    }

    @Test
    void buscarPorMetodoPagoDebeRetornarLista() {

        when(pagoRepository.findByMetodoPago("TRANSFERENCIA"))
                .thenReturn(List.of(pago));

        assertEquals(
                1,
                pagoService.buscarPorMetodoPago("TRANSFERENCIA").size()
        );
    }

    @Test
    void buscarPorEstadoPagoDebeRetornarLista() {

        when(pagoRepository.findByEstadoPago("PAGADO"))
                .thenReturn(List.of(pago));

        assertEquals(
                1,
                pagoService.buscarPorEstadoPago("PAGADO").size()
        );
    }

    @Test
    void buscarPorReservaDebeRetornarLista() {

        when(pagoRepository.findByIdReserva(1L))
                .thenReturn(List.of(pago));

        assertEquals(
                1,
                pagoService.buscarPorReserva(1L).size()
        );
    }

    // -------------------------------------------------------------------
    // PRUEBAS DE REGLAS DE NEGOCIO (Requisito Evaluación 3)
    // -------------------------------------------------------------------

    @Test
    @DisplayName("Regla de Negocio: Debe calcular correctamente el 19% de IVA sobre el monto neto")
    void calcularIvaDebeRetornarMontoCorrecto() {
        // Given
        double montoNeto = 20000.0;
        double ivaEsperado = 3800.0; // 20000 * 0.19

        // When
        double ivaCalculado = pagoService.calcularIva(montoNeto);

        // Then
        assertEquals(ivaEsperado, ivaCalculado, "El cálculo del IVA debe corresponder exactamente al 19% del monto neto");
    }

    @Test
    @DisplayName("Regla de Negocio: Debe aplicar un descuento al monto total")
    void aplicarDescuentoDebeRetornarMontoConRebaja() {
        // Given
        double montoInicial = 25000.0;
        double porcentajeDescuento = 10.0; // 10% de descuento
        double montoEsperado = 22500.0;    // 25000 - 2500

        // When
        double montoConDescuento = pagoService.aplicarDescuento(montoInicial, porcentajeDescuento);

        // Then
        assertEquals(montoEsperado, montoConDescuento, "El descuento debe aplicarse correctamente restando el porcentaje indicado al monto inicial");
    }

    @Test
    @DisplayName("Regla de Negocio: Debe calcular el total a pagar sumando el IVA al monto neto")
    void calcularTotalConIvaDebeRetornarSumaCorrecta() {
        // Given
        double montoNeto = 10000.0;
        double totalEsperado = 11900.0; // 10000 + 1900 (IVA del 19%)

        // When
        double totalCalculado = pagoService.calcularTotalConIva(montoNeto);

        // Then
        assertEquals(totalEsperado, totalCalculado, "El total final debe ser la suma exacta del monto neto más el 19% de IVA");
    }
}