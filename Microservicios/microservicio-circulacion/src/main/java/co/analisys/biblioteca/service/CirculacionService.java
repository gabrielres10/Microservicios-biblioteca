package co.analisys.biblioteca.service;

import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.analisys.biblioteca.client.CatalogoClient;
import co.analisys.biblioteca.client.NotificacionClient;
import co.analisys.biblioteca.dto.NotificacionDTO;
import co.analisys.biblioteca.exception.LibroNoDisponibleException;
import co.analisys.biblioteca.exception.PrestamoNoEncontradoException;
import co.analisys.biblioteca.model.EstadoPrestamo;
import co.analisys.biblioteca.model.FechaDevolucionPrevista;
import co.analisys.biblioteca.model.FechaPrestamo;
import co.analisys.biblioteca.model.LibroId;
import co.analisys.biblioteca.model.Prestamo;
import co.analisys.biblioteca.model.PrestamoId;
import co.analisys.biblioteca.model.UsuarioId;
import co.analisys.biblioteca.repository.PrestamoRepository;
import jakarta.transaction.Transactional;

@Service
public class CirculacionService {
    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private CatalogoClient catalogoClient;

    @Autowired
    private NotificacionClient notificacionClient;

     @Autowired
    private RabbitTemplate rabbitTemplate;

    @Transactional
    public void prestarLibro(UsuarioId usuarioId, LibroId libroId) {
        Boolean libroDisponible = catalogoClient.isLibroDisponible(libroId.getLibroid_value());

        if (libroDisponible != null && libroDisponible) {
            Prestamo prestamo = new Prestamo(
                    new PrestamoId(java.util.UUID.randomUUID().toString()),
                    usuarioId,
                    libroId,
                    new FechaPrestamo(),
                    new FechaDevolucionPrevista(),
                    EstadoPrestamo.ACTIVO
            );
            prestamo = prestamoRepository.save(prestamo);

            // Actualizar disponibilidad
            catalogoClient.actualizarDisponibilidad(libroId.getLibroid_value(), false);
            
            // Enviar notificacion
            //notificacionClient.enviarNotificacion(new NotificacionDTO(usuarioId.getUsuarioid_value(), "Libro prestado: " + libroId.getLibroid_value()));
            NotificacionDTO notificacion = new NotificacionDTO(usuarioId.getUsuarioid_value(), 
            "Libro prestado: " + libroId.getLibroid_value());

            rabbitTemplate.convertAndSend("notificacion.exchange", 
            "notificacion.routingkey",notificacion);

        } else {
            throw new LibroNoDisponibleException(libroId);
        }
    }

    @Transactional
    public void devolverLibro(PrestamoId prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId).orElseThrow(
            () -> new PrestamoNoEncontradoException(prestamoId));
            
        prestamo.setEstado(EstadoPrestamo.DEVUELTO);
        prestamo = prestamoRepository.save(prestamo);

        catalogoClient.actualizarDisponibilidad(prestamo.getLibroId().getLibroid_value(), true);

        notificacionClient.enviarNotificacion(new NotificacionDTO(
            prestamo.getUsuarioId().getUsuarioid_value(), 
            "Libro devuelto: " + prestamo.getLibroId().getLibroid_value()
        ));
    }

    public List<Prestamo> obtenerTodosPrestamos() {
        return prestamoRepository.findAll();
    }
}
