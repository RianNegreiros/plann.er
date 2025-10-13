package xyz.riannegreiros.planner.trip;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


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
    this.service.registerParticipantsToEvent(payload.emails_to_invite(), newTrip.getId());

    return ResponseEntity.ok(new TripCreateResponse(newTrip.getId()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Trip> getTripDetails(@PathVariable UUID id) {
      Optional<Trip> trip = this.repository.findById(id);

      return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }
}
