package com.github.jvanheesch;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class SteeringWheelModelProcessor implements RepresentationModelProcessor<EntityModel<SteeringWheel>> {
    @Override
    public EntityModel<SteeringWheel> process(EntityModel<SteeringWheel> model) {
        model.add(new Link("http://www.google.com", "steeringWheelLink"));
        return model;
    }
}
