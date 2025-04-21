package pj.gob.pe.security.service.externals.impl;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import pj.gob.pe.security.configuration.ConfigProperties;
import pj.gob.pe.security.exception.ModeloNotFoundException;
import pj.gob.pe.security.service.externals.SIJService;
import pj.gob.pe.security.utils.beans.DataUsuarioDTO;

import java.util.HashMap;
import java.util.Map;

@Service
public class SIJServiceImpl implements SIJService {

    private final RestClient restClient;
    private final ConfigProperties properties;

    public SIJServiceImpl(RestClient.Builder builder, ConfigProperties properties) {
        this.restClient = builder.baseUrl(properties.getUrlConsultaSIJ()).build();
        this.properties = properties;
    }

    @Override
    public DataUsuarioDTO getDatosUser(String username, String password) {

        String pathConsulta = properties.getConsultaUsuarioPath();

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("username", username);
        requestBody.put("password", password);

        return restClient.post()
                .uri(pathConsulta)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody) // Enviar objeto como JSON
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new ModeloNotFoundException("Usuario No encontrado");
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new RuntimeException("Error del servidor, Comunicarse con el administrador");
                })
                .body(DataUsuarioDTO.class); // Se convierte automáticamente el JSON a la clase Java
    }
}
