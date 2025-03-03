package co.analisys.biblioteca.controller;

import co.analisys.biblioteca.dto.NotificacionDTO;
import co.analisys.biblioteca.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/notificar")
public class NotificacionController {
    @Autowired
    private NotificacionService notificacionService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_NOTIFIER', 'ROLE_LIBRARIAN')")
    public void enviarNotificacion(@RequestBody NotificacionDTO notificacion) {
        notificacionService.enviarNotificacion(notificacion);
    }
}