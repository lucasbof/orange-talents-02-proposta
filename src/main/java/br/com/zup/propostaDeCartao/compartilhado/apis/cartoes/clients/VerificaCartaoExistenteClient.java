package br.com.zup.propostaDeCartao.compartilhado.apis.cartoes.clients;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.zup.propostaDeCartao.compartilhado.apis.cartoes.clients.VerificaCartaoExistenteClient.VerificaCartaoFallbackFactory;
import br.com.zup.propostaDeCartao.compartilhado.apis.cartoes.responses.VerificaCartaoExistenteApiResponse;

@FeignClient(url = "${apis.cartoes.host}", name = "verificaCartaoExistente", fallbackFactory = VerificaCartaoFallbackFactory.class)
public interface VerificaCartaoExistenteClient {

	@GetMapping(value = "/api/cartoes", consumes = MediaType.APPLICATION_JSON_VALUE)
	VerificaCartaoExistenteApiResponse verificaCartaoExistente(@RequestParam("idProposta") String idProposta);

	@Component
	static class VerificaCartaoFallbackFactory implements FallbackFactory<VerificaCartaoExistenteClient> {

		@Override
		public VerificaCartaoExistenteClient create(Throwable cause) {
			throw new IllegalStateException("Erro na verificação de cartão");
		}

	}

}
