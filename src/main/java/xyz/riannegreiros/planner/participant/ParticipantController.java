package xyz.riannegreiros.planner.participant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/participants")
public class ParticipantController {
    
    @Autowired
    ParticipantRepository repository;

    @PostMapping("/{id}")
    public ResponseEntity<Participant> confirmParticipant(@PathVariable UUID id, @RequestBody ParticipantRequestPayload payload) {
        Optional<Participant> participant = this.repository.findById(id);

        if (participant.isPresent()) {
            Participant participantCreated = participant.get();
            participantCreated.setConfirmed(true);
            participantCreated.setName(payload.name());
            participantCreated.setEmail(participantCreated.getEmail());

            this.repository.save(participantCreated);

            return ResponseEntity.ok(participantCreated);
        }

        return ResponseEntity.notFound().build();
    }
}
