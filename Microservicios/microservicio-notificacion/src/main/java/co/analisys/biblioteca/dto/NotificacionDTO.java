package co.analisys.biblioteca.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionDTO implements Serializable{
    private String usuarioId;
    private String mensaje;
}
