package br.com.zup.proposta.novaProposta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PropostaControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private PropostaRepository propostaRepository;
	
	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() throws Exception {
		propostaRepository.deleteAll();
	}

	@ParameterizedTest
	@CsvSource({"27249752006", "272.497.520-06", "56.784.339/0001-80", "56784339000180"})
	@DisplayName("cria deveria inserir proposta e returnar 201 quando os dados são válidos")
	void teste1(String documento) throws Exception {
		EnderecoRequest enderecoRequest = new EnderecoRequest("Rua 8", "434", "13230442");
		CriaPropostaRequest criaPropostaRequest = new CriaPropostaRequest("Lucas", "lucas@gmail.com", enderecoRequest,
				documento, new BigDecimal(1000));
		
		ResultActions result = fazRequisicao(criaPropostaRequest);
		
		result.andExpect(status().isCreated());
		assertNotNull(result.andReturn().getResponse().getHeader("Location"));
		
		long count = propostaRepository.count();
		assertEquals(1L, count);
	}
	
	@ParameterizedTest
	@CsvSource({"''", ",", "'    '"})
	@DisplayName("cria não deveria inserir proposta e returnar 400 quando o nome está nulo, só espaços ou vazio")
	void teste2(String nome) throws Exception {
		EnderecoRequest enderecoRequest = new EnderecoRequest("Rua 8", "434", "13230442");
		CriaPropostaRequest criaPropostaRequest = new CriaPropostaRequest(nome, "lucas@gmail.com", enderecoRequest,
				"27249752006", new BigDecimal(1000));
		
		ResultActions result = fazRequisicao(criaPropostaRequest);
		eErroDeValidacao(result);
	}
	
	@ParameterizedTest
	@CsvSource({"''", ",", "'    '", "@gmail.com", "lucas"})
	@DisplayName("cria não deveria inserir proposta e returnar 400 quando o email está nulo, só espaços ou vazio")
	void teste3(String email) throws Exception {
		EnderecoRequest enderecoRequest = new EnderecoRequest("Rua 8", "434", "13230442");
		CriaPropostaRequest criaPropostaRequest = new CriaPropostaRequest("Lucas", email, enderecoRequest,
				"27249752006", new BigDecimal(1000));
		
		ResultActions result = fazRequisicao(criaPropostaRequest);
		eErroDeValidacao(result);
	}
	
	
	@ParameterizedTest
	@CsvSource({"''", ",", "'    '", "27249752007", "272.497.520-07", "56.784.339/0001-81", "56784339000181"})
	@DisplayName("cria não deveria inserir proposta e returnar 400 quando o documento está nulo, só espaços, vazio ou quando cpf/cpnj são inválidos")
	void teste4(String documento) throws Exception {
		EnderecoRequest enderecoRequest = new EnderecoRequest("Rua 8", "434", "13230442");
		CriaPropostaRequest criaPropostaRequest = new CriaPropostaRequest("Lucas", "lucas@gmail.com" , enderecoRequest,
				documento, new BigDecimal(1000));
		
		ResultActions result = fazRequisicao(criaPropostaRequest);
		eErroDeValidacao(result);
	}
	
	@Test
	@DisplayName("cria não deveria inserir proposta e returnar 400 quando o endereço está nulo")
	void teste5() throws Exception {
		CriaPropostaRequest criaPropostaRequest = new CriaPropostaRequest("Lucas", "lucas@gmail.com" , null,
				"27249752006", new BigDecimal(1000));
		
		ResultActions result = fazRequisicao(criaPropostaRequest);
		eErroDeValidacao(result);
	}
	
	@ParameterizedTest
	@CsvSource({"''", ",", "'    '"})
	@DisplayName("cria não deveria inserir proposta e returnar 400 quando o logradouro de um endereço está nulo, só espaços ou vazio")
	void teste6(String logradouro) throws Exception {
		EnderecoRequest enderecoRequest = new EnderecoRequest(logradouro, "434", "13230442");
		CriaPropostaRequest criaPropostaRequest = new CriaPropostaRequest("Lucas", "lucas@gmail.com" , enderecoRequest,
				"27249752006", new BigDecimal(1000));
		
		ResultActions result = fazRequisicao(criaPropostaRequest);
		eErroDeValidacao(result);
	}
	
	@ParameterizedTest
	@CsvSource({"''", ",", "'    '"})
	@DisplayName("cria não deveria inserir proposta e returnar 400 quando o número de um endereço está nulo, só espaços ou vazio")
	void teste7(String numero) throws Exception {
		EnderecoRequest enderecoRequest = new EnderecoRequest("Rua 8", numero, "13230442");
		CriaPropostaRequest criaPropostaRequest = new CriaPropostaRequest("Lucas", "lucas@gmail.com" , enderecoRequest,
				"27249752006", new BigDecimal(1000));
		
		ResultActions result = fazRequisicao(criaPropostaRequest);
		eErroDeValidacao(result);
	}
	
	
	@ParameterizedTest
	@CsvSource({"''", ",", "'    '"})
	@DisplayName("cria não deveria inserir proposta e returnar 400 quando o cep de um endereço está nulo, só espaços ou vazio")
	void teste8(String cep) throws Exception {
		EnderecoRequest enderecoRequest = new EnderecoRequest("Rua 8", "767", cep);
		CriaPropostaRequest criaPropostaRequest = new CriaPropostaRequest("Lucas", "lucas@gmail.com" , enderecoRequest,
				"27249752006", new BigDecimal(1000));
		
		ResultActions result = fazRequisicao(criaPropostaRequest);
		eErroDeValidacao(result);
	}
	
	@ParameterizedTest
	@CsvSource({",", "0", "-1.7", "-35.5"})
	@DisplayName("cria não deveria inserir proposta e returnar 400 quando o salário for nulo ou menor que ou igual a zero")
	void teste9(BigDecimal salario) throws Exception {
		EnderecoRequest enderecoRequest = new EnderecoRequest("Rua 8", "767", "13230442");
		CriaPropostaRequest criaPropostaRequest = new CriaPropostaRequest("Lucas", "lucas@gmail.com" , enderecoRequest,
				"27249752006", salario);
		
		ResultActions result = fazRequisicao(criaPropostaRequest);
		eErroDeValidacao(result);
	}
	
	
	private void eErroDeValidacao(ResultActions result) throws Exception {
		result.andExpect(status().isBadRequest());
		
		long count = propostaRepository.count();
		assertEquals(0L, count);
	}
	
	private ResultActions fazRequisicao(CriaPropostaRequest criaPropostaRequest) throws Exception {
		return mockMvc.perform(post("/propostas")
						.content(toJson(criaPropostaRequest))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
	}
	
	
	private String toJson(Object obj) throws Exception {
		return objectMapper.writeValueAsString(obj);
	}

}
