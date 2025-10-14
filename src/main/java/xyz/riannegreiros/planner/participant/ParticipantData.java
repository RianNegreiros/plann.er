package xyz.riannegreiros.planner.participant;

import java.util.UUID;

public record ParticipantData(UUID uuid,  String name, String email, boolean isConfirmed) {
}
