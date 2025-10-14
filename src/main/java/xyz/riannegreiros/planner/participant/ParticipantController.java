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
            Participant participantToUpdate = participant.get();
            participantToUpdate.setConfirmed(true);
            participantToUpdate.setName(payload.name());

            this.repository.save(participantToUpdate);

            return ResponseEntity.ok(participantToUpdate);
        }

        return ResponseEntity.notFound().build();
    }
}
