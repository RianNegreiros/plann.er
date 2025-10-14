package xyz.riannegreiros.planner.trip;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.riannegreiros.planner.participant.*;

@RestController
@RequestMapping("/trips")
public class TripController {

  @Autowired
  TripRepository repository;

  @Autowired
  ParticipantsService service;

  @PostMapping
  public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripRequestPayload payload) {
    Trip newTrip = new Trip(payload);

    this.repository.save(newTrip);
    this.service.registerParticipantsToEvent(payload.emails_to_invite(), newTrip);

    return ResponseEntity.ok(new TripCreateResponse(newTrip.getId()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Trip> getTripDetails(@PathVariable UUID id) {
      Optional<Trip> trip = this.repository.findById(id);

      return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTripDetails(@PathVariable UUID id, @RequestBody TripRequestPayload payload) {
        Optional<Trip> trip = this.repository.findById(id);
        
        if (trip.isPresent()) {
            Trip tripUpdated = trip.get();
            tripUpdated.setDestination(payload.destination());
            tripUpdated.setStartsAt(LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
            tripUpdated.setEndsAt(LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME));
            
            this.repository.save(tripUpdated);
            
            return ResponseEntity.ok(tripUpdated);
        }

        return ResponseEntity.notFound().build();
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable UUID id) {
        Optional<Trip> trip = this.repository.findById(id);

        if (trip.isPresent()) {
            Trip tripUpdated = trip.get();
            tripUpdated.setConfirmed(true);
            this.service.triggerConfirmationEmailToParticipants(id);

            this.repository.save(tripUpdated);

            return ResponseEntity.ok(tripUpdated);
        }

        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/{id}/invite")
    public ResponseEntity<ParticipantCreateResponse> inviteParticipant(@PathVariable UUID id, @RequestBody ParticipantRequestPayload payload) {
      Optional<Trip> trip = this.repository.findById(id);
      
      if(trip.isPresent()) {
          Trip trip1 = trip.get();
          
          ParticipantCreateResponse participantCreateResponse = this.service.registerParticipantToEvent(payload.email(), trip1);
          
          if (trip1.isConfirmed()) this.service.triggerConfirmationEmailToParticipant(payload.email());
          
          return ResponseEntity.ok(participantCreateResponse);
      }
      
      return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ParticipantData>> getAllParticipantsFromTrip(@PathVariable UUID id) {
      List<ParticipantData> participants = service.getAllParticipantsFromTrip(id);
      return ResponseEntity.ok(participants);
    }
}
