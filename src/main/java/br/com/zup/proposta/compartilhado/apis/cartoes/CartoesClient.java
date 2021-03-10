package br.com.zup.proposta.compartilhado.apis.cartoes;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "http://localhost:8888/", name = "cartoes")
public interface CartoesClient {

	@GetMapping(value = "/api/cartoes", consumes = MediaType.APPLICATION_JSON_VALUE)
	CartaoResponse verificaCartaoExistente(@RequestParam("idProposta") String idProposta);
}
