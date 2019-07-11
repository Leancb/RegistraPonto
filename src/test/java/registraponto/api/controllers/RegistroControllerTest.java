package registraponto.api.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import registraponto.api.dtos.RegistroDto;
import registraponto.api.entities.Colaborador;
import registraponto.api.entities.Registro;
import registraponto.api.enums.TipoEnum;
import registraponto.api.services.ColaboradorService;
import registraponto.api.services.RegistroService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RegistroControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private RegistroService registroService;
	
	@MockBean
	private ColaboradorService colaboradorService;
	
	private static final String URL_BASE = "/api/registros/";
	private static final Long ID_FUNCIONARIO = 1L;
	private static final Long ID_LANCAMENTO = 1L;
	private static final String TIPO = TipoEnum.INICIO_TRABALHO.name();
	private static final Date DATA = new Date();
	
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Test
	@WithMockUser
	public void testeCadRegistro() throws Exception {
		Registro registro = getDadosReg();
		BDDMockito.given(this.colaboradorService.getById(Mockito.anyLong())).willReturn(Optional.of(new Colaborador()));
		BDDMockito.given(this.registroService.persistir(Mockito.any(Registro.class))).willReturn(registro);

		mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
				.content(this.getJsonReqPost())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.id").value(ID_LANCAMENTO))
				.andExpect(jsonPath("$.data.tipo").value(TIPO))
				.andExpect(jsonPath("$.data.data").value(this.dateFormat.format(DATA)))
				.andExpect(jsonPath("$.data.colaboradorId").value(ID_FUNCIONARIO))
				.andExpect(jsonPath("$.errors").isEmpty());
	}
	
	@Test
	@WithMockUser
	public void testeCadRegistroColaboradorIdInvalido() throws Exception {
		BDDMockito.given(this.colaboradorService.getById(Mockito.anyLong())).willReturn(Optional.empty());

		mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
				.content(this.getJsonReqPost())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").value("Funcionário não encontrado. ID inexistente."))
				.andExpect(jsonPath("$.data").isEmpty());
	}
	
	@Test
	@WithMockUser(username = "admin@admin.com", roles = {"ADMIN"})
	public void testeApagaRegistro() throws Exception {
		BDDMockito.given(this.registroService.getById(Mockito.anyLong())).willReturn(Optional.of(new Registro()));

		mvc.perform(MockMvcRequestBuilders.delete(URL_BASE + ID_LANCAMENTO)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser
	public void testeApagarRegAcessoNegado() throws Exception {
		BDDMockito.given(this.registroService.getById(Mockito.anyLong())).willReturn(Optional.of(new Registro()));

		mvc.perform(MockMvcRequestBuilders.delete(URL_BASE + ID_LANCAMENTO)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());
	}

	private String getJsonReqPost() throws JsonProcessingException {
		RegistroDto registroDto = new RegistroDto();
		registroDto.setId(null);
		registroDto.setData(this.dateFormat.format(DATA));
		registroDto.setTipo(TIPO);
		registroDto.setColaboradorId(ID_FUNCIONARIO);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(registroDto);
	}

	private Registro getDadosReg() {
		Registro registro = new Registro();
		registro.setId(ID_LANCAMENTO);
		registro.setData(DATA);
		registro.setTipo(TipoEnum.valueOf(TIPO));
		registro.setColaborador(new Colaborador());
		registro.getColaborador().setId(ID_FUNCIONARIO);
		return registro;
	}	

}
