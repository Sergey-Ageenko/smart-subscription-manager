package events;

import java.io.Serializable;
import java.util.UUID;

public record UserRegisteredEvent(
        UUID eventId,
        UUID userId,
        String firstName,
        String lastName) implements Serializable {
}
