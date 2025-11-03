package com.example.demo.Controllers;

import java.time.LocalDate;
import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Models.ReservacionesModel;
import com.example.demo.Models.ReservacionesModel.EstadoReserva;
import com.example.demo.Services.ReservacionesService;

@RestController
@RequestMapping("/reservaciones")
public class ReservacionesController {
    @Autowired    
    private ReservacionesService reservacionesService;

    //Metodo para obtener todas las reservaciones
    @GetMapping()
    public List<ReservacionesModel> getAllReservaciones(){
        return reservacionesService.getAllReservaciones();
    }

    //Metodo para obtener reservacion por id
    @GetMapping("/{id}")
    public ResponseEntity<ReservacionesModel> getReservacionById(@PathVariable Long id){
        Optional<ReservacionesModel> reservacion = reservacionesService.getReservacionById(id);
        return reservacion.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /* 
    //Create reservacion
    @PostMapping
    public ReservacionesModel createReservacion(@RequestBody ReservacionesModel reservacion){
        return reservacionesService.saveReservacion(reservacion);
    }
    */ 

    //Metodo para actualizar reservacion
    @PutMapping("/{id}")
    public ResponseEntity<ReservacionesModel> updateReservacion(@PathVariable Long id, @RequestBody ReservacionesModel reservacion){
        Optional<ReservacionesModel> existingReservacion = reservacionesService.getReservacionById(id);

        if (existingReservacion.isPresent()) {
            ReservacionesModel reservacionToUpdate = existingReservacion.get();
            reservacionToUpdate.setFechaEntrada(reservacion.getFechaEntrada());
            reservacionToUpdate.setFechaSalida(reservacion.getFechaSalida());
            reservacionToUpdate.setNumeroHuespedes(reservacion.getNumeroHuespedes());
            reservacionToUpdate.setPrecioTotal(reservacion.getPrecioTotal());
            reservacionToUpdate.setEstado(reservacion.getEstado());
            reservacionToUpdate.setNotas(reservacion.getNotas());
            reservacionToUpdate.setCodigoReserva(reservacion.getCodigoReserva());
           

            ReservacionesModel updatedReservacion = reservacionesService.saveReservacion(reservacionToUpdate);
            return ResponseEntity.ok(updatedReservacion);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Delete reservacion
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservacion(@PathVariable Long id){
        Optional<ReservacionesModel> existingReservacion = reservacionesService.getReservacionById(id);

        if (existingReservacion.isPresent()) {
            reservacionesService.deleteReservacion(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
   
    // Endpoint para crear una nueva reserva con lógica de negocio
    @PostMapping
    public ResponseEntity<ReservacionesModel> crearReserva(@RequestBody ReservacionesModel reserva) {
        
        try {
            ReservacionesModel nuevaReserva = reservacionesService.reservarApartamento(reserva);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaReserva);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Endpoint para cancelar una reserva
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<ReservacionesModel> cancelarReserva(@PathVariable Long id) {
        
        Optional<ReservacionesModel> reservaCancelada = reservacionesService.cancelarReserva(id);
        
        return reservaCancelada.map(ResponseEntity::ok) // Devuelve 200 OK con el objeto cancelado
                               .orElseGet(() -> ResponseEntity.notFound().build()); // Devuelve 404 Not Found
    }

    //Metodo para obtener reservas por usuario
    @GetMapping("/usuario/{clienteId}")
    public ResponseEntity<List<ReservacionesModel>> getReservasByUsuario(@PathVariable Long clienteId) {
        
        List<ReservacionesModel> reservas = reservacionesService.getReservasByUsuarioId(clienteId);
        
        if (reservas.isEmpty()) {
            // Retorna 404 Not Found si el cliente no existe o no tiene reservas
            return ResponseEntity.notFound().build();
        }
        
        // Retorna 200 OK con la lista de reservas (que puede estar vacía si el cliente existe)
        return ResponseEntity.ok(reservas); 
    }

    //Metodo para obtener reservas por fecha
    //URL: GET http://localhost:10101/reservaciones/fecha?fecha=YYYY-MM-DD
    @GetMapping("/fecha")
    public ResponseEntity<List<ReservacionesModel>> getReservasByFecha(@RequestParam LocalDate fecha) {
        
        List<ReservacionesModel> reservas = reservacionesService.getReservasByFecha(fecha);
        
        return ResponseEntity.ok(reservas); 
    }

    //Metodo para obtener reservas por estado
    //URL: GET http://localhost:10101/reservaciones/estado?estado=ESTADO

    @GetMapping("/estado")
    public ResponseEntity<List<ReservacionesModel>> getReservasByEstado(@RequestParam EstadoReserva estado) {
        
        List<ReservacionesModel> reservas = reservacionesService.getReservasByEstado(estado);
        
        // Retorna 200 OK con la lista de reservas encontradas (puede estar vacía).
        return ResponseEntity.ok(reservas); 
    }

    //Método para obtener reservaciones por usuario y estado
    //http://localhost:10101/reservaciones/usuario/5/estado?estado=PENDIENTE
    @GetMapping("/usuario/{clienteId}/estado")
    public ResponseEntity<List<ReservacionesModel>> getReservasByUsuarioAndEstado(
            @PathVariable Long clienteId, 
            @RequestParam EstadoReserva estado) {
        
        List<ReservacionesModel> reservas = reservacionesService.getReservasByUsuarioAndEstado(clienteId, estado);
        
        // Retorna 200 OK con la lista de reservas encontradas (puede estar vacía).
        return ResponseEntity.ok(reservas); 
    }

    //Método para obtener reservaciones por propiedad y estado
    //http://localhost:10101/reservaciones/propiedad/3/estado?estado=CONFIRMADA
    @GetMapping("/propiedad/{propiedadId}/estado")
    public ResponseEntity<List<ReservacionesModel>> getReservasByPropiedadAndEstado(
            @PathVariable Long propiedadId, 
            @RequestParam EstadoReserva estado) {
        
        List<ReservacionesModel> reservas = reservacionesService.getReservasByPropiedadAndEstado(propiedadId, estado);
        
        // Retorna 200 OK con la lista de reservas encontradas (puede estar vacía si la propiedad existe).
        return ResponseEntity.ok(reservas); 
    }
    
    //Método para obtener reservaciones por usuario y fecha
    //http://localhost:10101/reservaciones/usuario/5/fecha?fecha=2024-07-15
    @GetMapping("/usuario/{clienteId}/fecha")
    public ResponseEntity<List<ReservacionesModel>> getReservasByUsuarioAndFecha(
            @PathVariable Long clienteId, 
            @RequestParam LocalDate fecha) {
        
        List<ReservacionesModel> reservas = reservacionesService.getReservasByUsuarioAndFecha(clienteId, fecha);
        
        // Retorna 200 OK con la lista de reservas encontradas (puede estar vacía).
        return ResponseEntity.ok(reservas); 
    }

    //Metodo para obtener reservaciones por propiedad y fecha
    //http://localhost:10101/reservaciones/propiedad/3/fecha?fecha=2024-07-15
    @GetMapping("/propiedad/{propiedadId}/fecha")
    public ResponseEntity<List<ReservacionesModel>> getReservasByPropiedadAndFecha(
            @PathVariable Long propiedadId, 
            @RequestParam LocalDate fecha) {
        
        List<ReservacionesModel> reservas = reservacionesService.getReservasByPropiedadAndFecha(propiedadId, fecha);
        
        // Retorna 200 OK con la lista de reservas encontradas (puede estar vacía).
        return ResponseEntity.ok(reservas); 
    }

    //Metodo para cancelar todas las reservaciones por usuario
    //http://localhost:10101/reservaciones/usuario/5/cancelar
    @PutMapping("/usuario/{clienteId}/cancelar")
    public ResponseEntity<List<ReservacionesModel>> cancelarReservasDeUsuario(@PathVariable Long clienteId) {
        
        try {
            List<ReservacionesModel> reservasCanceladas = 
                reservacionesService.cancelarTodasLasReservasDeUsuario(clienteId);
            
            if (reservasCanceladas.isEmpty()) {
                // Si el usuario existe pero no tiene reservas, retorna OK con lista vacía
                return ResponseEntity.ok(reservasCanceladas); 
            }
            
            return ResponseEntity.ok(reservasCanceladas); // 200 OK con la lista de reservas canceladas
            
        } catch (RuntimeException e) {
            // Captura la excepción si el usuario no fue encontrado
            if (e.getMessage().contains("Usuario no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            throw e; // Relanza otros errores
        }
    }

    //Metodo para cancelar todas las reservaciones por propiedad
    //http://localhost:10101/reservaciones/propiedad/3/cancelar
    @PutMapping("/propiedad/{propiedadId}/cancelar")
    public ResponseEntity<List<ReservacionesModel>> cancelarReservasDePropiedad(@PathVariable Long propiedadId) {
        
        try {
            List<ReservacionesModel> reservasCanceladas = 
                reservacionesService.cancelarTodasLasReservasDePropiedad(propiedadId);
            
            if (reservasCanceladas.isEmpty()) {
                // Si la propiedad existe pero no tiene reservas activas, retorna OK con lista vacía
                return ResponseEntity.ok(reservasCanceladas); 
            }
            
            return ResponseEntity.ok(reservasCanceladas); // 200 OK con la lista de reservas canceladas
            
        } catch (RuntimeException e) {
            // Captura la excepción si la propiedad no fue encontrada
            if (e.getMessage().contains("Propiedad no encontrada")) {
                return ResponseEntity.notFound().build();
            }
            throw e; // Relanza otros errores
        }
    }

    //Metodo para cancelar todas las reservaciones por fecha
    //URL: PUT http://localhost:10101/reservaciones/cancelar-por-fecha?fecha=YYYY-MM-DD
    @PutMapping("/cancelar-por-fecha")
    public ResponseEntity<List<ReservacionesModel>> cancelarReservasPorFecha(@RequestParam LocalDate fecha) {
        
        List<ReservacionesModel> reservasCanceladas = 
             reservacionesService.cancelarReservasPorFecha(fecha);
        
        if (reservasCanceladas.isEmpty()) {
            // No se encontraron reservas activas para cancelar en esa fecha.
            return ResponseEntity.ok(reservasCanceladas); 
        }
        
        return ResponseEntity.ok(reservasCanceladas); // 200 OK con la lista de reservas que se cancelaron
    }

}
