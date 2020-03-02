package com.github.jvanheesch;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class EngineModelProcessor implements RepresentationModelProcessor<EntityModel<Engine>> {
    @Override
    public EntityModel<Engine> process(EntityModel<Engine> model) {
        model.add(new Link("http://www.google.com", "engineLink"));
        return model;
    }
}
