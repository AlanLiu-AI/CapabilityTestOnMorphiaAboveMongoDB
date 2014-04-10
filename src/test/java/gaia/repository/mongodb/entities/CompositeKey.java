package gaia.repository.mongodb.entities;

import com.google.common.base.Objects;
import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class CompositeKey {

    private String idPart1;
    private String idPart2;

    public CompositeKey() {
    }

    public CompositeKey(final String idPart1, final String idPart2) {
        this.idPart1 = idPart1;
        this.idPart2 = idPart2;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idPart1, idPart2);
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        final CompositeKey myId = (CompositeKey) other;
        return Objects.equal(idPart1, myId.idPart1)
                && Objects.equal(idPart2, myId.idPart2);
    }

}
