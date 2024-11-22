package com.java.EcoDrive.config;

import com.java.EcoDrive.dto.Usuario.UsuarioDTO;
import com.java.EcoDrive.model.Usuario;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Configuração global do ModelMapper
        modelMapper.getConfiguration()
                .setSkipNullEnabled(true) // Ignora propriedades nulas
                .setFieldMatchingEnabled(true) // Permite correspondência por campo
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE); // Acesso a campos privados

        // Configuração personalizada para Usuario -> UsuarioDTO
        modelMapper.addMappings(new PropertyMap<Usuario, UsuarioDTO>() {
            @Override
            protected void configure() {
                // Configuração personalizada (se necessário)
                // Campos como "senha" já são ignorados automaticamente, pois não existem no DTO
            }
        });

        return modelMapper;
    }
}
