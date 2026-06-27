package events;

import java.io.Serializable;
import java.util.UUID;

public record UserRegisteredEvent(UUID userId,
                                  String firstName,
                                  String lastName) implements Serializable {
}
