package co.analisys.biblioteca.controller;

import co.analisys.biblioteca.model.LibroId;
import co.analisys.biblioteca.model.Prestamo;
import co.analisys.biblioteca.model.PrestamoId;
import co.analisys.biblioteca.model.UsuarioId;
import co.analisys.biblioteca.service.CirculacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/circulacion")
public class CirculacionController {
    @Autowired
    private CirculacionService circulacionService;

    @PostMapping("/prestar")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    public ResponseEntity<Prestamo> prestarLibro(@RequestParam String usuarioId, @RequestParam String libroId) {
        Prestamo prestamo = circulacionService.prestarLibro(new UsuarioId(usuarioId), new LibroId(libroId));
        return ResponseEntity.ok(prestamo);
    }

    @PostMapping("/devolver")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    public ResponseEntity<Prestamo> devolverLibro(@RequestParam String prestamoId) {
        Prestamo prestamo = circulacionService.devolverLibro(new PrestamoId(prestamoId));
        return ResponseEntity.ok(prestamo);
    }

    @Operation(
        summary = "Consultar todos los préstamos",
        description = "Este endpoint permite obtener una lista de todos los prestamos registrados en el sistema." +
        "Es importante que el cliente esté registrado previamente en la base de datos, " +
        "de lo contrario no podrá acceder a la información."
    )
    @GetMapping("/prestamos")
    @PreAuthorize("hasAnyRole('ROLE_LIBRARIAN', 'ROLE_USER')")
    public ResponseEntity<List<Prestamo>> obtenerTodosPrestamos() {
        return ResponseEntity.ok(circulacionService.obtenerTodosPrestamos());
    }

    @GetMapping("/public/status")
    public ResponseEntity<String> getPublicStatus() {
        return ResponseEntity.ok("El servicio de circulación está funcionando correctamente");
    }
}
