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

    @Operation(
        summary = "Prestar un libro a un usuario",
        description = "Registra el préstamo de un libro a un usuario específico. " +
                "Requiere rol de bibliotecario para ejecutar la operación. " +
                "Verifica la disponibilidad del libro y el estado del usuario antes de realizar el préstamo."
    )
    @PostMapping("/prestar")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    public ResponseEntity<Prestamo> prestarLibro(@RequestParam String usuarioId, @RequestParam String libroId) {
        Prestamo prestamo = circulacionService.prestarLibro(new UsuarioId(usuarioId), new LibroId(libroId));
        return ResponseEntity.ok(prestamo);
    }

    @Operation(
        summary = "Devolver un libro prestado",
        description = "Registra la devolución de un libro previamente prestado. " +
                "Requiere rol de bibliotecario para ejecutar la operación. " +
                "Actualiza el estado del préstamo y la disponibilidad del libro."
    )
    @PostMapping("/devolver")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    public ResponseEntity<Prestamo> devolverLibro(@RequestParam String prestamoId) {
        Prestamo prestamo = circulacionService.devolverLibro(new PrestamoId(prestamoId));
        return ResponseEntity.ok(prestamo);
    }

    @Operation(
        summary = "Consultar todos los préstamos",
        description = "Obtiene una lista de todos los préstamos registrados en el sistema. " +
                "Accesible tanto para bibliotecarios como para usuarios registrados. " +
                "Los préstamos incluyen información del libro, usuario y fechas correspondientes."
    )
    @GetMapping("/prestamos")
    @PreAuthorize("hasAnyRole('ROLE_LIBRARIAN', 'ROLE_USER')")
    public ResponseEntity<List<Prestamo>> obtenerTodosPrestamos() {
        return ResponseEntity.ok(circulacionService.obtenerTodosPrestamos());
    }

    @Operation(
        summary = "Verificar estado del servicio",
        description = "Endpoint público que permite verificar si el servicio de circulación está funcionando correctamente. " +
                "No requiere autenticación."
    )
    @GetMapping("/public/status")
    public ResponseEntity<String> getPublicStatus() {
        return ResponseEntity.ok("El servicio de circulación está funcionando correctamente");
    }
}
