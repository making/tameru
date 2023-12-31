package lol.maki.tameru.timeflake;

import java.io.IOException;

import am.ik.timeflake.Timeflake;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lol.maki.tameru.event.LogEventMapper;

import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
public class TimeflakeSerializer extends JsonSerializer<Timeflake> {

	@Override
	public void serialize(Timeflake timeflake, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
			throws IOException {
		jsonGenerator.writeString(timeflake.base62());
		jsonGenerator.writeStringField(LogEventMapper.DT_KEY, timeflake.toInstant().toString());
	}

}
