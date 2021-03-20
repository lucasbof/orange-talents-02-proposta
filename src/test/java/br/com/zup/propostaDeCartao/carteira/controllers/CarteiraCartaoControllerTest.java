package br.com.zup.propostaDeCartao.carteira.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.zup.propostaDeCartao.cartao.modelo.Cartao;
import br.com.zup.propostaDeCartao.carteira.enums.TiposCarteiraEnum;
import br.com.zup.propostaDeCartao.carteira.modelo.CarteiraCartao;
import br.com.zup.propostaDeCartao.carteira.requests.CarteiraCartaoRequest;
import br.com.zup.propostaDeCartao.compartilhado.apis.cartoes.clients.AssociaCarteiraClient;
import br.com.zup.propostaDeCartao.compartilhado.apis.cartoes.responses.AssociaCarteiraApiResponse;
import br.com.zup.propostaDeCartao.proposta.modelo.Endereco;
import br.com.zup.propostaDeCartao.proposta.modelo.Proposta;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@WithMockUser
class CarteiraCartaoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private AssociaCarteiraClient carteiraClient;

	@PersistenceContext
	private EntityManager manager;

	private CarteiraCartaoRequest request;

	private String jsonBody;

	@BeforeEach
	void setUp() throws Exception {
		when(carteiraClient.associa(eq("5209-1622-1164-9999"), any()))
		.thenReturn(new AssociaCarteiraApiResponse("ASSOCIADA"));
		
		request = new CarteiraCartaoRequest("email@email.com", TiposCarteiraEnum.PAYPAL);
		jsonBody = objectMapper.writeValueAsString(request);
	}

	@Test
	@DisplayName("deveria retornar 201 quando tudo estiver Ok")
	void teste1() throws Exception {
		Endereco endereco = new Endereco("Rua dos Testes", "3500", "25015301");
		Proposta proposta = new Proposta("Lucas", "lucas@gmail.com", endereco, "hdX7KhLixySiWxDGMIxZ9g==",
				new BigDecimal(1500));

		manager.persist(proposta);

		Cartao cartao = new Cartao("5209-1622-1164-6666", "Lucas", LocalDateTime.now(), proposta);

		manager.persist(cartao);

		mockMvc.perform(
				post("/carteiras/{cartaoId}", cartao.getId())
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated())
		.andExpect(header().exists("Location"));

		CarteiraCartao result = manager.find(CarteiraCartao.class, 1L);
		assertNotNull(result);
		assertEquals(request.getEmail(), ReflectionTestUtils.getField(result, "email"));
		assertEquals(request.getCarteira(), result.getTipoCarteira());
	}

	@ParameterizedTest
	@CsvSource({ ", PAYPAL", "'', PAYPAL", "'   ', PAYPAL", "@email.com, PAYPAL", "email@email.com," })
	@DisplayName("deveria retornar 400 quando alguns dado informado for inválido")
	void teste2(String email, TiposCarteiraEnum tipoCarteira) throws Exception {
		request = new CarteiraCartaoRequest(email, tipoCarteira);
		jsonBody = objectMapper.writeValueAsString(request);

		Endereco endereco = new Endereco("Rua dos Testes", "3500", "25015301");
		Proposta proposta = new Proposta("Lucas", "lucas@gmail.com", endereco, "hdX7KhLixySiWxDGMIxZ9g==",
				new BigDecimal(1500));

		manager.persist(proposta);

		Cartao cartao = new Cartao("5209-1622-1164-6666", "Lucas", LocalDateTime.now(), proposta);

		manager.persist(cartao);

		mockMvc.perform(
				post("/carteiras/{cartaoId}", cartao.getId())
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest());

		CarteiraCartao result = manager.find(CarteiraCartao.class, 1L);
		assertNull(result);
	}

	@Test
	@DisplayName("deveria retornar 404 quando cartão não for encontrado")
	void teste3() throws Exception {		
		mockMvc.perform(post("/carteiras/{cartaoId}", 1000L)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());

		CarteiraCartao result = manager.find(CarteiraCartao.class, 1000L);
		assertNull(result);
	}
	

    @Test
    @DisplayName("deveria retornar 422 quando já existir uma carteira associada a um cartão")
    @WithMockUser
    void teste4() throws Exception {
    	Endereco endereco = new Endereco("Rua dos Testes", "3500", "25015301");
		Proposta proposta = new Proposta("Lucas", "lucas@gmail.com", endereco, "hdX7KhLixySiWxDGMIxZ9g==",
				new BigDecimal(1500));

		manager.persist(proposta);

		Cartao cartao = new Cartao("5209-1622-1164-6666", "Lucas", LocalDateTime.now(), proposta);

		manager.persist(cartao);
		
		CarteiraCartao carteira = new CarteiraCartao(TiposCarteiraEnum.PAYPAL, "lucas@gmail.com", cartao);
		
		manager.persist(carteira);
		
        mockMvc.perform(post("/carteiras/{cartaoId}", cartao.getId())
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnprocessableEntity());
    }

}
