package br.com.zup.propostaDeCartao.compartilhado.apis.cartoes.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.zup.propostaDeCartao.compartilhado.apis.cartoes.responses.CartaoApiResponse;

@FeignClient(url = "${apis.cartoes.host}", name = "cartoes")
public interface CartoesClient {

	@GetMapping(value = "/api/cartoes", consumes = MediaType.APPLICATION_JSON_VALUE)
	CartaoApiResponse verificaCartaoExistente(@RequestParam("idProposta") String idProposta);
}
