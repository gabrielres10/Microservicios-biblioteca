package co.analisys.biblioteca.controller;

import co.analisys.biblioteca.dto.NotificacionDTO;
import co.analisys.biblioteca.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/notificar")
public class NotificacionController {
    @Autowired
    private NotificacionService notificacionService;

    @Operation(
        summary = "Enviar notificación",
        description = "Envía una notificación a los usuarios del sistema. " +
                "Puede ser utilizado para avisos de vencimiento, confirmaciones de préstamo o devolución. " +
                "Solo accesible para notificadores y bibliotecarios."
    )
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_NOTIFIER', 'ROLE_LIBRARIAN')")
    public void enviarNotificacion(@RequestBody NotificacionDTO notificacion) {
        notificacionService.enviarNotificacion(notificacion);
    }
}