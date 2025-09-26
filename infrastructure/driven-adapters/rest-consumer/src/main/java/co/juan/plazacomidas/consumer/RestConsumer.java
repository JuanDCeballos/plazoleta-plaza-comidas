package co.juan.plazacomidas.consumer;

import co.juan.plazacomidas.consumer.dto.ApiResponse;
import co.juan.plazacomidas.consumer.dto.UsuarioResponseDto;
import co.juan.plazacomidas.model.usuario.Usuario;
import co.juan.plazacomidas.model.usuario.gateways.UsuarioGateway;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class RestConsumer implements UsuarioGateway {
    private final String url;
    private final OkHttpClient client;
    private final ObjectMapper mapper;

    public RestConsumer(@Value("${adapter.restconsumer.url}") String url, OkHttpClient client, ObjectMapper mapper) {
        this.url = url;
        this.client = client;
        this.mapper = mapper;
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorId(Long idUsuario) {
        String requestUrl = url + "/api/usuarios/usuario/" + idUsuario;

        Request request = new Request.Builder()
                .url(requestUrl)
                .get()
                .addHeader("Accept", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();

            if (response.code() == 404) {
                return null;
            }
            if (!response.isSuccessful()) {
                throw new IOException("Respuesta inesperada del servicio de usuarios: " + response);
            }

            ApiResponse apiResponse = mapper.readValue(response.body().string(), ApiResponse.class);

            if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                UsuarioResponseDto usuarioDto = apiResponse.getData();

                return Optional.of(Usuario.builder()
                        .idUsuario(usuarioDto.getIdUsuario())
                        .nombre(usuarioDto.getNombre())
                        .apellido(usuarioDto.getApellido())
                        .documentoDeIdentidad(usuarioDto.getDocumentoDeIdentidad())
                        .celular(usuarioDto.getCelular())
                        .fechaNacimiento(usuarioDto.getFechaNacimiento())
                        .correo(usuarioDto.getCorreo())
                        .idRol(usuarioDto.getIdRol())
                        .build());
            } else {
                return null;
            }

        } catch (IOException e) {
            throw new RuntimeException("Error al consumir el servicio de usuarios", e);
        }
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorCorreo(String correo) {
        String requestUrl = url + "/api/usuarios/correo/" + correo;

        Request request = new Request.Builder()
                .url(requestUrl)
                .get()
                .addHeader("Accept", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();

            if (response.code() == 404) {
                return null;
            }
            if (!response.isSuccessful()) {
                throw new IOException("Respuesta inesperada del servicio de usuarios: " + response);
            }

            ApiResponse apiResponse = mapper.readValue(response.body().string(), ApiResponse.class);

            if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                UsuarioResponseDto usuarioDto = apiResponse.getData();

                return Optional.of(Usuario.builder()
                        .idUsuario(usuarioDto.getIdUsuario())
                        .nombre(usuarioDto.getNombre())
                        .apellido(usuarioDto.getApellido())
                        .documentoDeIdentidad(usuarioDto.getDocumentoDeIdentidad())
                        .celular(usuarioDto.getCelular())
                        .fechaNacimiento(usuarioDto.getFechaNacimiento())
                        .correo(usuarioDto.getCorreo())
                        .idRol(usuarioDto.getIdRol())
                        .build());
            } else {
                return null;
            }

        } catch (IOException e) {
            throw new RuntimeException("Error al consumir el servicio de usuarios", e);
        }
    }
}
