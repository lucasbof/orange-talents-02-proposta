package br.com.zup.propostaDeCartao.autenticacao.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.zup.propostaDeCartao.autenticacao.requests.LoginRequest;
import br.com.zup.propostaDeCartao.autenticacao.responses.LoginResponse;
import br.com.zup.propostaDeCartao.compartilhado.apis.keycloak.AutenticacaoClient;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {
	
	
	@Value("${keycloak.resource}")
	private String clientId;
	
	@Value("${keycloak.credentials.secret}")
	private String clientSecret;

	@Autowired
	private AutenticacaoClient authenticationClient;

	@PostMapping
    public LoginResponse auth(@RequestBody LoginRequest request) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
        formData.add("grant_type", "password");
        formData.add("username", request.getUsuario());
        formData.add("password", request.getSenha());
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        return authenticationClient.auth(formData);
    }
}
