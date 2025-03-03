package co.analisys.biblioteca.client;

import co.analisys.biblioteca.dto.NotificacionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import co.analisys.biblioteca.config.FeignConfig;

@FeignClient(name = "notificacion-service", url = "http://localhost:8084", configuration = FeignConfig.class)
public interface NotificacionClient {
    @PostMapping("/notificar")
    void enviarNotificacion(@RequestBody NotificacionDTO notificacionDTO);
}


