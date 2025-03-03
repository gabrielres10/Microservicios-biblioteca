package co.analisys.biblioteca.controller;

import co.analisys.biblioteca.model.Libro;
import co.analisys.biblioteca.model.LibroId;
import co.analisys.biblioteca.service.CatalogoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/libros")
public class CatalogoController {
    private final CatalogoService catalogoService;

    @Autowired
    public CatalogoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    @Operation(
        summary = "Obtener información de un libro",
        description = "Recupera los detalles completos de un libro específico por su ID. " +
                "Accesible para bibliotecarios y usuarios registrados."
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_LIBRARIAN', 'ROLE_USER')")
    public Libro obtenerLibro(@PathVariable String id) {
        return catalogoService.obtenerLibro(new LibroId(id));
    }

    @Operation(
        summary = "Verificar disponibilidad de un libro",
        description = "Comprueba si un libro específico está disponible para préstamo. " +
                "Retorna verdadero si el libro existe y está disponible, falso en caso contrario."
    )
    @GetMapping("/{id}/disponible")
    @PreAuthorize("hasAnyRole('ROLE_LIBRARIAN', 'ROLE_USER')")
    public boolean isLibroDisponible(@PathVariable String id) {
        Libro libro = catalogoService.obtenerLibro(new LibroId(id));
        return libro != null && libro.isDisponible();
    }

    @Operation(
        summary = "Actualizar disponibilidad de un libro",
        description = "Modifica el estado de disponibilidad de un libro. " +
                "Solo puede ser ejecutado por administradores del catálogo o bibliotecarios."
    )
    @PutMapping("/{id}/disponibilidad")
    @PreAuthorize("hasAnyRole('ROLE_CATALOG_ADMIN','ROLE_LIBRARIAN')")
    public void actualizarDisponibilidad(@PathVariable String id, @RequestBody boolean disponible) {
        catalogoService.actualizarDisponibilidad(new LibroId(id), disponible);
    }

    @Operation(
        summary = "Buscar libros por criterio",
        description = "Permite buscar libros en el catálogo según un criterio específico. " +
                "La búsqueda puede incluir título, autor, ISBN u otros campos relevantes."
    )
    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('ROLE_LIBRARIAN', 'ROLE_USER')")
    public List<Libro> buscarLibros(@RequestParam String criterio) {
        return catalogoService.buscarLibros(criterio);
    }
}
