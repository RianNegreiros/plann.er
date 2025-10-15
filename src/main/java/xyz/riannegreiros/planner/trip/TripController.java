package xyz.riannegreiros.planner.trip;

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
import xyz.riannegreiros.planner.participant.ParticipantCreateResponse;
import xyz.riannegreiros.planner.participant.ParticipantData;
import xyz.riannegreiros.planner.participant.ParticipantRequestPayload;
import xyz.riannegreiros.planner.participant.ParticipantsService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    // Trip endpoints
    @PostMapping
    public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripRequestPayload payload) {
        Trip newTrip = new Trip(payload);
        this.repository.save(newTrip);
        this.participantsService.registerParticipantsToEvent(payload.emails_to_invite(), newTrip);
        return ResponseEntity.ok(new TripCreateResponse(newTrip.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTrip(@PathVariable UUID id) {
        Optional<Trip> tripOptional = this.repository.findById(id);
        return tripOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable UUID id, @RequestBody TripRequestPayload payload) {
        Optional<Trip> tripOptional = this.repository.findById(id);
        if (tripOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Trip trip = tripOptional.get();
        trip.setDestination(payload.destination());
        trip.setStartsAt(LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
        trip.setEndsAt(LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME));
        this.repository.save(trip);
        return ResponseEntity.ok(trip);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Trip> confirmTrip(@PathVariable UUID id) {
        Optional<Trip> tripOptional = this.repository.findById(id);
        if (tripOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Trip trip = tripOptional.get();
        trip.setConfirmed(true);
        this.participantsService.triggerConfirmationEmailToParticipants(id);
        this.repository.save(trip);
        return ResponseEntity.ok(trip);
    }

    // Participant endpoints
    @PostMapping("/{id}/invite")
    public ResponseEntity<ParticipantCreateResponse> inviteParticipant(
        @PathVariable UUID id,
        @RequestBody ParticipantRequestPayload payload) {
        Optional<Trip> tripOptional = this.repository.findById(id);
        if (tripOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Trip trip = tripOptional.get();
        ParticipantCreateResponse response = this.participantsService.registerParticipantToEvent(payload.email(), trip);
        if (trip.isConfirmed()) {
            this.participantsService.triggerConfirmationEmailToParticipant(payload.email());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ParticipantData>> getParticipants(@PathVariable UUID id) {
        List<ParticipantData> participants = participantsService.getAllParticipantsFromTrip(id);
        return ResponseEntity.ok(participants);
    }

    // Activity endpoints
    @PostMapping("/{id}/activities")
    public ResponseEntity<ActivityResponse> createActivity(
        @PathVariable UUID id,
        @RequestBody ActivityRequestPayload payload) {
        Optional<Trip> tripOptional = this.repository.findById(id);
        if (tripOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Trip trip = tripOptional.get();
        ActivityResponse activityResponse = this.activityService.registerActivity(payload, trip);
        return ResponseEntity.ok(activityResponse);
    }

    @GetMapping("/{id}/activities")
    public ResponseEntity<List<ActivityData>> getActivities(@PathVariable UUID id) {
        List<ActivityData> activities = this.activityService.getAllActivitiesFromTripId(id);
        return ResponseEntity.ok(activities);
    }

    // Link endpoints
    @PostMapping("/{id}/links")
    public ResponseEntity<LinkResponse> createLink(
        @PathVariable UUID id,
        @RequestBody LinkRequestPayload payload) {
        Optional<Trip> tripOptional = this.repository.findById(id);
        if (tripOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Trip trip = tripOptional.get();
        LinkResponse linkResponse = this.linkService.registerLink(payload, trip);
        return ResponseEntity.ok(linkResponse);
    }

    @GetMapping("/{id}/links")
    public ResponseEntity<List<LinkData>> getLinks(@PathVariable UUID id) {
        List<LinkData> links = this.linkService.getAllLinksFromTripId(id);
        return ResponseEntity.ok(links);
    }
}
