package br.com.zup.propostaDeCartao.proposta.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.zup.propostaDeCartao.compartilhado.apis.solicitacaodeanalise.clients.NovaSolicitacaoClient;
import br.com.zup.propostaDeCartao.compartilhado.apis.solicitacaodeanalise.requests.ResultadoSolicitacaoApiRequest;
import br.com.zup.propostaDeCartao.compartilhado.apis.solicitacaodeanalise.requests.SolicitacaoDeAnaliseApiRequest;
import br.com.zup.propostaDeCartao.compartilhado.apis.solicitacaodeanalise.responses.SolicitacaoDeAnaliseApiResponse;
import br.com.zup.propostaDeCartao.proposta.enums.StatusCartao;
import br.com.zup.propostaDeCartao.proposta.modelo.Proposta;
import br.com.zup.propostaDeCartao.proposta.repositorios.PropostaRepository;
import br.com.zup.propostaDeCartao.proposta.requests.CriaPropostaRequest;
import br.com.zup.propostaDeCartao.proposta.requests.EnderecoRequest;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser
class PropostaControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private PropostaRepository propostaRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private NovaSolicitacaoClient novaSolicitacaoClient;

	private String nomeSolicitacaoSemRestricao = "Lucas";

	private String nomeSolicitacaoComRestricao = "Alex";

	@BeforeEach
	void setUp() throws Exception {
		Mockito.when(
				novaSolicitacaoClient.fazSolicitacaoDeCartao(ArgumentMatchers.any(SolicitacaoDeAnaliseApiRequest.class)))
				.thenAnswer(new Answer<SolicitacaoDeAnaliseApiResponse>() {
					@Override
					public SolicitacaoDeAnaliseApiResponse answer(InvocationOnMock invocation) throws Throwable {
						SolicitacaoDeAnaliseApiRequest request = invocation.getArgument(0);

						if (request.getNome().equals(nomeSolicitacaoSemRestricao))
							return new SolicitacaoDeAnaliseApiResponse(ResultadoSolicitacaoApiRequest.SEM_RESTRICAO);
						else if (request.getNome().equals(nomeSolicitacaoComRestricao))
							return new SolicitacaoDeAnaliseApiResponse(ResultadoSolicitacaoApiRequest.COM_RESTRICAO);
						return null;
					}
				});

		propostaRepository.deleteAll();
	}

	@ParameterizedTest
	@CsvSource({ "27249752006", "272.497.520-06", "56.784.339/0001-80", "56784339000180" })
	@DisplayName("cria deveria inserir proposta e returna 201 quando os dados são válidos")
	void teste1(String documento) throws Exception {
		EnderecoRequest enderecoRequest = new EnderecoRequest("Rua 8", "434", "13230442");
		CriaPropostaRequest criaPropostaRequest = new CriaPropostaRequest(nomeSolicitacaoSemRestricao,
				"lucas@gmail.com", enderecoRequest, documento, new BigDecimal(1000));

		ResultActions result = fazRequisicaoDeCriacao(criaPropostaRequest);

		result.andExpect(status().isCreated());
		assertNotNull(result.andReturn().getResponse().getHeader("Location"));

		long count = propostaRepository.count();
		assertEquals(1L, count);
	}

	@ParameterizedTest
	@CsvSource({ "''", ",", "'    '" })
	@DisplayName("cria não deveria inserir proposta e returna 400 quando o nome está nulo, só espaços ou vazio")
	void teste2(String nome) throws Exception {
		EnderecoRequest enderecoRequest = new EnderecoRequest("Rua 8", "434", "13230442");
		CriaPropostaRequest criaPropostaRequest = new CriaPropostaRequest(nome, "lucas@gmail.com", enderecoRequest,
				"27249752006", new BigDecimal(1000));

		ResultActions result = fazRequisicaoDeCriacao(criaPropostaRequest);
		eErroDeValidacaoRetornando400(result);
	}

	@ParameterizedTest
	@CsvSource({ "''", ",", "'    '", "@gmail.com", "lucas" })
	@DisplayName("cria não deveria inserir proposta e returna 400 quando o email está nulo, só espaços ou vazio")
	void teste3(String email) throws Exception {
		EnderecoRequest enderecoRequest = new EnderecoRequest("Rua 8", "434", "13230442");
		CriaPropostaRequest criaPropostaRequest = new CriaPropostaRequest(nomeSolicitacaoSemRestricao, email,
				enderecoRequest, "27249752006", new BigDecimal(1000));

		ResultActions result = fazRequisicaoDeCriacao(criaPropostaRequest);
		eErroDeValidacaoRetornando400(result);
	}

	@ParameterizedTest
	@CsvSource({ "''", ",", "'    '", "27249752007", "272.497.520-07", "56.784.339/0001-81", "56784339000181" })
	@DisplayName("cria não deveria inserir proposta e returna 400 quando o documento está nulo, só espaços, vazio ou quando cpf/cpnj são inválidos")
	void teste4(String documento) throws Exception {
		EnderecoRequest enderecoRequest = new EnderecoRequest("Rua 8", "434", "13230442");
		CriaPropostaRequest criaPropostaRequest = new CriaPropostaRequest(nomeSolicitacaoSemRestricao,
				"lucas@gmail.com", enderecoRequest, documento, new BigDecimal(1000));

		ResultActions result = fazRequisicaoDeCriacao(criaPropostaRequest);
		eErroDeValidacaoRetornando400(result);
	}

	@Test
	@DisplayName("cria não deveria inserir proposta e returna 400 quando o endereço está nulo")
	void teste5() throws Exception {
		CriaPropostaRequest criaPropostaRequest = new CriaPropostaRequest(nomeSolicitacaoSemRestricao,
				"lucas@gmail.com", null, "27249752006", new BigDecimal(1000));

		ResultActions result = fazRequisicaoDeCriacao(criaPropostaRequest);
		eErroDeValidacaoRetornando400(result);
	}

	@ParameterizedTest
	@CsvSource({ "''", ",", "'    '" })
	@DisplayName("cria não deveria inserir proposta e returna 400 quando o logradouro de um endereço está nulo, só espaços ou vazio")
	void teste6(String logradouro) throws Exception {
		EnderecoRequest enderecoRequest = new EnderecoRequest(logradouro, "434", "13230442");
		CriaPropostaRequest criaPropostaRequest = new CriaPropostaRequest(nomeSolicitacaoSemRestricao,
				"lucas@gmail.com", enderecoRequest, "27249752006", new BigDecimal(1000));

		ResultActions result = fazRequisicaoDeCriacao(criaPropostaRequest);
		eErroDeValidacaoRetornando400(result);
	}

	@ParameterizedTest
	@CsvSource({ "''", ",", "'    '" })
	@DisplayName("cria não deveria inserir proposta e returna 400 quando o número de um endereço está nulo, só espaços ou vazio")
	void teste7(String numero) throws Exception {
		EnderecoRequest enderecoRequest = new EnderecoRequest("Rua 8", numero, "13230442");
		CriaPropostaRequest criaPropostaRequest = new CriaPropostaRequest(nomeSolicitacaoSemRestricao,
				"lucas@gmail.com", enderecoRequest, "27249752006", new BigDecimal(1000));

		ResultActions result = fazRequisicaoDeCriacao(criaPropostaRequest);
		eErroDeValidacaoRetornando400(result);
	}

	@ParameterizedTest
	@CsvSource({ "''", ",", "'    '" })
	@DisplayName("cria não deveria inserir proposta e returna 400 quando o cep de um endereço está nulo, só espaços ou vazio")
	void teste8(String cep) throws Exception {
		EnderecoRequest enderecoRequest = new EnderecoRequest("Rua 8", "767", cep);
		CriaPropostaRequest criaPropostaRequest = new CriaPropostaRequest(nomeSolicitacaoSemRestricao,
				"lucas@gmail.com", enderecoRequest, "27249752006", new BigDecimal(1000));

		ResultActions result = fazRequisicaoDeCriacao(criaPropostaRequest);
		eErroDeValidacaoRetornando400(result);
	}

	@ParameterizedTest
	@CsvSource({ ",", "0", "-1.7", "-35.5" })
	@DisplayName("cria não deveria inserir proposta e returna 400 quando o salário for nulo ou menor que ou igual a zero")
	void teste9(BigDecimal salario) throws Exception {
		EnderecoRequest enderecoRequest = new EnderecoRequest("Rua 8", "767", "13230442");
		CriaPropostaRequest criaPropostaRequest = new CriaPropostaRequest(nomeSolicitacaoSemRestricao,
				"lucas@gmail.com", enderecoRequest, "27249752006", salario);

		ResultActions result = fazRequisicaoDeCriacao(criaPropostaRequest);
		eErroDeValidacaoRetornando400(result);
	}

	@Test
	@DisplayName("cria não deveria inserir proposta e returna 422 quando já existir uma proposta para um mesmo documento")
	void teste10() throws Exception {
		EnderecoRequest enderecoRequest = new EnderecoRequest("Rua 8", "767", "13230442");
		CriaPropostaRequest criaPropostaRequest = new CriaPropostaRequest(nomeSolicitacaoSemRestricao,
				"lucas@gmail.com", enderecoRequest, "27249752006", new BigDecimal(1234.33));

		ResultActions result = fazRequisicaoDeCriacao(criaPropostaRequest);
		result.andExpect(status().isCreated());

		result = fazRequisicaoDeCriacao(criaPropostaRequest);
		result.andExpect(status().isUnprocessableEntity());
		long count = propostaRepository.count();
		assertEquals(1L, count);
	}

	@Test
	@DisplayName("consultaProposta deveria returnar 200 com os dados da proposta quando o ID da proposta informado existir")
	void teste11() throws Exception {
		EnderecoRequest enderecoRequest = new EnderecoRequest("Rua 8", "767", "13230442");
		CriaPropostaRequest criaPropostaRequest = new CriaPropostaRequest(nomeSolicitacaoSemRestricao,
				"lucas@gmail.com", enderecoRequest, "27249752006", new BigDecimal(1234.33));

		Proposta proposta = propostaRepository.save(criaPropostaRequest.toModel());

		ResultActions result = mockMvc.perform(get("/propostas/{id}", proposta.getId())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		result.andExpect(jsonPath("$.nome").value(proposta.getNome()));
		result.andExpect(jsonPath("$.email").value(proposta.getEmail()));
		result.andExpect(jsonPath("$.documento").value(proposta.getDocumento()));
		result.andExpect(jsonPath("$.statusCartao").value(proposta.getStatusCartao()));
		result.andExpect(jsonPath("$.salario").value(proposta.getSalario()));
		result.andExpect(jsonPath("$.endereco.logradouro").value(proposta.getEndereco().getLogradouro()));
		result.andExpect(jsonPath("$.endereco.numero").value(proposta.getEndereco().getNumero()));
		result.andExpect(jsonPath("$.endereco.cep").value(proposta.getEndereco().getCep()));
	}

	@Test
	@DisplayName("consultaProposta deveria returnar 404 quando o ID da proposta informado não existir")
	void teste12() throws Exception {
		ResultActions result = mockMvc.perform(get("/propostas/{id}", 100000).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("cria deveria returnar 422 com mensagem de erro quando a consulta da API de status COM_RESTRICAO")
	void teste13() throws Exception {
		ResultActions result = mockMvc.perform(get("/propostas/{id}", 100000).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("cria deveria inserir proposta com o status de cartão NÃO_ELEGIVEL e returnar 422 quando a API de solicitação retorna COM_RESTRICAO")
	void teste14() throws Exception {
		EnderecoRequest enderecoRequest = new EnderecoRequest("Rua 8", "767", "13230442");
		CriaPropostaRequest criaPropostaRequest = new CriaPropostaRequest(nomeSolicitacaoComRestricao, "alex@gmail.com",
				enderecoRequest, "27249752006", new BigDecimal(1234.33));

		ResultActions result = fazRequisicaoDeCriacao(criaPropostaRequest);
		result.andExpect(status().isUnprocessableEntity());
		long count = propostaRepository.count();
		assertEquals(1L, count);

		Proposta proposta = propostaRepository.findAll().get(0);
		assertEquals(StatusCartao.NAO_ELEGIVEL, proposta.getStatusCartao());
	}

	private void eErroDeValidacaoRetornando400(ResultActions result) throws Exception {
		result.andExpect(status().isBadRequest());

		long count = propostaRepository.count();
		assertEquals(0L, count);
	}

	private ResultActions fazRequisicaoDeCriacao(CriaPropostaRequest criaPropostaRequest) throws Exception {
		return mockMvc.perform(post("/propostas").content(toJson(criaPropostaRequest))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
	}

	private String toJson(Object obj) throws Exception {
		return objectMapper.writeValueAsString(obj);
	}

}
