package xyz.riannegreiros.planner.trip;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class ParticipantsService {
  public void registerParticipantsToEvent(List<String> participantsToInvite, UUID id) {}

  public void triggerConfirmationEmailToParticipants(UUID tripId) {}
}
