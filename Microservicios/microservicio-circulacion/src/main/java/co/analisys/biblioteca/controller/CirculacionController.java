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

import java.util.List;

@RestController
@RequestMapping("/circulacion")
public class CirculacionController {
    @Autowired
    private CirculacionService circulacionService;

    @PostMapping("/prestar")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    public void prestarLibro(@RequestParam String usuarioId, @RequestParam String libroId) {
        circulacionService.prestarLibro(new UsuarioId(usuarioId), new LibroId(libroId));
    }

    @PostMapping("/devolver")
    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    public void devolverLibro(@RequestParam String prestamoId) {
        circulacionService.devolverLibro(new PrestamoId(prestamoId));
    }

    @Operation(
        summary = "Consultar todos los préstamos",
        description = "Este endpoint permite obtener una lista de todos los prestamos registrados en el sistema." +
        "Es importante que el cliente esté registrado previamente en la base de datos, " +
        "de lo contrario no podrá acceder a la información."
    )
    @GetMapping("/prestamos")
    @PreAuthorize("hasAnyRole('ROLE_LIBRARIAN', 'ROLE_USER')")
    public List<Prestamo> obtenerTodosPrestamos() {
        return circulacionService.obtenerTodosPrestamos();
    }

    @GetMapping("/public/status")
    public String getPublicStatus() {
        return "El servicio de circulación está funcionando correctamente";
    }
}
