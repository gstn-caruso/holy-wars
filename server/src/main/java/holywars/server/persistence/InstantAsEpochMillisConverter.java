package holywars.server.persistence;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.Instant;

@Converter
class InstantAsEpochMillisConverter implements AttributeConverter<Instant, Long> {

    @Override
    public Long convertToDatabaseColumn(Instant attribute) {
        return attribute == null ? null : attribute.toEpochMilli();
    }

    @Override
    public Instant convertToEntityAttribute(Long dbData) {
        return dbData == null ? null : Instant.ofEpochMilli(dbData);
    }
}
