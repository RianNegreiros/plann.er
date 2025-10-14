package xyz.riannegreiros.planner.participant;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.riannegreiros.planner.trip.Trip;

import java.util.List;
import java.util.UUID;

public interface ParticipantRepository extends JpaRepository<Participant, UUID> {
    List<Participant> findByTripId(UUID tripId);
}
