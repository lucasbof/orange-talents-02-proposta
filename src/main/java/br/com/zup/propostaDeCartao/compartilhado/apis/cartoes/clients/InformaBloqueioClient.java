package br.com.zup.propostaDeCartao.compartilhado.apis.cartoes.clients;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.zup.propostaDeCartao.compartilhado.apis.cartoes.clients.InformaBloqueioClient.InformaBloqueioClientFallbackFactory;
import br.com.zup.propostaDeCartao.compartilhado.apis.cartoes.requests.InformaBloqueioCartaoApiRequest;
import br.com.zup.propostaDeCartao.compartilhado.apis.cartoes.responses.VerificaCartaoExistenteApiResponse;
import br.com.zup.propostaDeCartao.compartilhado.exceptions.ApiErroException;

@FeignClient(url = "${apis.cartoes.host}", name = "informaBloqueio", fallbackFactory = InformaBloqueioClientFallbackFactory.class)
public interface InformaBloqueioClient {

	@PostMapping(value = "/api/cartoes/{numeroCartao}/bloqueios", consumes = MediaType.APPLICATION_JSON_VALUE)
	VerificaCartaoExistenteApiResponse informaBloqueioCartao(@PathVariable("numeroCartao") String numeroCartao, InformaBloqueioCartaoApiRequest request);
	
	
	@Component
	static class InformaBloqueioClientFallbackFactory implements FallbackFactory<InformaBloqueioClient> {

		@Override
		public InformaBloqueioClient create(Throwable cause) {
			throw new ApiErroException(HttpStatus.UNPROCESSABLE_ENTITY, "Houve um erro no bloqueio do cartão");
		}


	}
}
