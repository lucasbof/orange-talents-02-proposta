package br.com.zup.propostaDeCartao.compartilhado.apis.cartoes.clients;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.zup.propostaDeCartao.carteira.requests.CarteiraCartaoRequest;
import br.com.zup.propostaDeCartao.compartilhado.apis.cartoes.clients.AssociaCarteiraClient.AssociaCarteiraClientFallbackFactory;
import br.com.zup.propostaDeCartao.compartilhado.apis.cartoes.responses.AssociaCarteiraApiResponse;
import br.com.zup.propostaDeCartao.compartilhado.exceptions.ApiErroException;

@FeignClient(url = "${apis.cartoes.host}", name = "associaCarteira", fallbackFactory = AssociaCarteiraClientFallbackFactory.class)
public interface AssociaCarteiraClient {

	@PostMapping(value = "/api/cartoes/{numeroCartao}/carteiras")
	AssociaCarteiraApiResponse associa(@PathVariable("numeroCartao") String numeroCartao, CarteiraCartaoRequest request);
	
	
	@Component
	static class AssociaCarteiraClientFallbackFactory implements FallbackFactory<AssociaCarteiraClient> {

		@Override
		public AssociaCarteiraClient create(Throwable cause) {
			throw new ApiErroException(HttpStatus.UNPROCESSABLE_ENTITY, "Houve um erro na associação da carteira");
		}


	}
}
