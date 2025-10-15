package xyz.riannegreiros.planner.trip;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.riannegreiros.planner.activity.ActivityData;
import xyz.riannegreiros.planner.activity.ActivityRequestPayload;
import xyz.riannegreiros.planner.activity.ActivityResponse;
import xyz.riannegreiros.planner.activity.ActivityService;
import xyz.riannegreiros.planner.link.LinkData;
import xyz.riannegreiros.planner.link.LinkRequestPayload;
import xyz.riannegreiros.planner.link.LinkResponse;
import xyz.riannegreiros.planner.link.LinkService;
import xyz.riannegreiros.planner.participant.*;

@RestController
@RequestMapping("/trips")
public class TripController {

  @Autowired
  TripRepository repository;

  @Autowired
  ParticipantsService participantsService;
  
  @Autowired
  ActivityService activityService;
  
  @Autowired
  LinkService linkService;

  @PostMapping
  public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripRequestPayload payload) {
    Trip newTrip = new Trip(payload);

    this.repository.save(newTrip);
    this.participantsService.registerParticipantsToEvent(payload.emails_to_invite(), newTrip);

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
            this.participantsService.triggerConfirmationEmailToParticipants(id);

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
          
          ParticipantCreateResponse participantCreateResponse = this.participantsService.registerParticipantToEvent(payload.email(), trip1);
          
          if (trip1.isConfirmed()) this.participantsService.triggerConfirmationEmailToParticipant(payload.email());
          
          return ResponseEntity.ok(participantCreateResponse);
      }
      
      return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/activities")
    public ResponseEntity<ActivityResponse> createActivity(@PathVariable UUID id, @RequestBody ActivityRequestPayload payload) {
        Optional<Trip> trip = this.repository.findById(id);

        if(trip.isPresent()) {
            Trip trip1 = trip.get();

            ActivityResponse activityResponse = this.activityService.registerActivity(payload, trip1);

            return ResponseEntity.ok(activityResponse);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/links")
    public ResponseEntity<LinkResponse> createLinks(@PathVariable UUID id, @RequestBody LinkRequestPayload payload) {
        Optional<Trip> trip = this.repository.findById(id);

        if(trip.isPresent()) {
            Trip trip1 = trip.get();

            LinkResponse linkResponse = this.linkService.registerLink(payload, trip1);

            return ResponseEntity.ok(linkResponse);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/links")
    public ResponseEntity<List<LinkData>> getAllTripsLinks(@PathVariable UUID id) {
        List<LinkData> linkDataList = this.linkService.getAllLinksFromTripId(id);

        return ResponseEntity.ok(linkDataList);
    }

    @GetMapping("/{id}/activities")
    public ResponseEntity<List<ActivityData>> getAllActivities(@PathVariable UUID id) {
      List<ActivityData> activityResponseList = this.activityService.getAllActivitiesFromTripId(id);
      
      return ResponseEntity.ok(activityResponseList);
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ParticipantData>> getAllParticipantsFromTrip(@PathVariable UUID id) {
      List<ParticipantData> participants = participantsService.getAllParticipantsFromTrip(id);
      return ResponseEntity.ok(participants);
    }
}
