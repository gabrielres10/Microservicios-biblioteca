package co.analisys.biblioteca.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import co.analisys.biblioteca.config.FeignConfig;

@FeignClient(name = "catalogo-service", url = "http://localhost:8082", configuration = FeignConfig.class)
public interface CatalogoClient {

    @GetMapping("/libros/{libroId}/disponible")
    public Boolean isLibroDisponible(@PathVariable("libroId") String id);

    @PutMapping("/libros/{libroId}/disponibilidad")
    public void actualizarDisponibilidad(@PathVariable("libroId") String id, @RequestBody Boolean disponible);
}