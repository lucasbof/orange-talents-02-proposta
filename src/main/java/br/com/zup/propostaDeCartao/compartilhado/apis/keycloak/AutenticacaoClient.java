package br.com.zup.propostaDeCartao.compartilhado.apis.keycloak;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.zup.propostaDeCartao.autenticacao.responses.LoginResponse;
import br.com.zup.propostaDeCartao.compartilhado.apis.keycloak.AutenticacaoClient.AutenticacaoClientFallbackFactory;
import br.com.zup.propostaDeCartao.compartilhado.exceptions.ApiErroException;
import feign.FeignException;

@FeignClient(name = "auth", url = "${apis.keycloak.autenticacao}", fallbackFactory = AutenticacaoClientFallbackFactory.class)
public interface AutenticacaoClient {

    @PostMapping
    LoginResponse auth(MultiValueMap<String, String> request);
    
    
    @Component
    static class AutenticacaoClientFallbackFactory implements FallbackFactory<AutenticacaoClient> {

		@Override
		public AutenticacaoClient create(Throwable cause) {
			if(cause instanceof FeignException.Unauthorized) {
				throw new ApiErroException(HttpStatus.UNAUTHORIZED, "Login ou senha incorretos!");
			}
			throw new ApiErroException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro na autenticação!");
		}
    	
    }
	
}
