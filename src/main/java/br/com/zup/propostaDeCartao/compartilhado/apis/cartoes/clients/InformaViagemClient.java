package br.com.zup.propostaDeCartao.compartilhado.apis.cartoes.clients;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.zup.propostaDeCartao.compartilhado.apis.cartoes.clients.InformaViagemClient.InformaViagemClientFallbackFactory;
import br.com.zup.propostaDeCartao.compartilhado.apis.cartoes.requests.InformaViagemApiRequest;
import br.com.zup.propostaDeCartao.compartilhado.apis.cartoes.responses.InformaViagemApiResponse;
import br.com.zup.propostaDeCartao.compartilhado.exceptions.ApiErroException;

@FeignClient(url = "${apis.cartoes.host}", name = "informaViagem", fallbackFactory = InformaViagemClientFallbackFactory.class)
public interface InformaViagemClient {

	@PostMapping("/api/cartoes/{numeroCartao}/avisos")
	InformaViagemApiResponse informaViagem(@PathVariable("numeroCartao") String numeroCartao, @RequestBody InformaViagemApiRequest request);
	
	@Component
	static class InformaViagemClientFallbackFactory implements FallbackFactory<InformaViagemClient> {
		
		@Override
		public InformaViagemClient create(Throwable cause) {
			throw new ApiErroException(HttpStatus.BAD_REQUEST, "A viagem não foi feita com sucesso!");
		}
		
	}
}
