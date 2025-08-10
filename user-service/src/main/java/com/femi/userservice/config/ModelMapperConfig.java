package com.femi.userservice.config;

import com.femi.userservice.dto.UserDTO;
import com.femi.userservice.model.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);

        mapper.createTypeMap(User.class, UserDTO.class)
                .addMappings(mapping -> mapping.skip(UserDTO::setId))
                .setPostConverter(context -> {
                    User source = context.getSource();
                    UserDTO destination = context.getDestination();
                    destination.setId(source.getId());
                    return destination;
                });

        return mapper;
    }
}