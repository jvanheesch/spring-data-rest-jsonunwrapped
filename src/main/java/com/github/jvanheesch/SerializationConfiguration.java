package com.github.jvanheesch;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.impl.UnwrappingBeanSerializer;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;
import com.fasterxml.jackson.databind.util.NameTransformer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

import java.io.IOException;

@Configuration
public class SerializationConfiguration implements RepositoryRestConfigurer {
    @Bean
    public SimpleModule customSerializationModule() {
        return new CustomSerializationModule();
    }

    public static class CustomSerializationModule extends SimpleModule {
        @Override
        public void setupModule(SetupContext context) {
            context.addBeanSerializerModifier(new BeanSerializerModifier() {
                @Override
                public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {
                    if (Engine.class.isAssignableFrom(beanDesc.getBeanClass())) {
                        return new EngineSerializer((BeanSerializerBase) serializer, NameTransformer.NOP);
                    }
                    return serializer;
                }
            });

            SimpleSerializers serializers = new SimpleSerializers();
            serializers.addSerializer(SteeringWheel.class, new SteeringWheelSerializer());
            context.addSerializers(serializers);
        }

        public static class EngineSerializer extends UnwrappingBeanSerializer {
            public EngineSerializer(BeanSerializerBase src, NameTransformer transformer) {
                super(src, transformer);
            }

            @Override
            public JsonSerializer<Object> unwrappingSerializer(NameTransformer transformer) {
                return new EngineSerializer(this, transformer);
            }

            @Override
            protected void serializeFields(Object bean, JsonGenerator gen, SerializerProvider provider) throws IOException {
                Engine steeringWheel = (Engine) bean;
                gen.writeFieldName("name");
                gen.writeString(steeringWheel.getName());
            }
        }

        public static class SteeringWheelSerializer extends JsonSerializer<SteeringWheel> {
            @Override
            public void serialize(SteeringWheel engine, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeStartObject();
                gen.writeFieldName("name");
                gen.writeString(engine.getName());
                gen.writeEndObject();
            }
        }
    }
}
