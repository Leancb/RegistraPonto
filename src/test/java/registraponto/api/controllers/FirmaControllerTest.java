package registraponto.api.controllers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import registraponto.api.entities.Firma;
import registraponto.api.services.FirmaService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class FirmaControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private FirmaService firmaService;

	private static final String BUSCAR_EMPRESA_CNPJ_URL = "/api/firmas/cnpj/";
	private static final Long ID = Long.valueOf(1);
	private static final String CNPJ = "51463645000100";
	private static final String RAZAO_SOCIAL = "Firma XYZ";

	@Test
	@WithMockUser
	public void testGetFirmaCnpjInvalido() throws Exception {
		BDDMockito.given(this.firmaService.getByCnpj(Mockito.anyString())).willReturn(Optional.empty());

		mvc.perform(MockMvcRequestBuilders.get(BUSCAR_EMPRESA_CNPJ_URL + CNPJ).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors").value("Firma não encontrada para o CNPJ " + CNPJ));
	}

	@Test
	@WithMockUser
	public void testGetFirmaCnpjValido() throws Exception {
		BDDMockito.given(this.firmaService.getByCnpj(Mockito.anyString()))
				.willReturn(Optional.of(this.getDadosFirma()));

		mvc.perform(MockMvcRequestBuilders.get(BUSCAR_EMPRESA_CNPJ_URL + CNPJ)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.id").value(ID))
				.andExpect(jsonPath("$.data.razaoSocial", equalTo(RAZAO_SOCIAL)))
				.andExpect(jsonPath("$.data.cnpj", equalTo(CNPJ)))
				.andExpect(jsonPath("$.errors").isEmpty());
	}

	private Firma getDadosFirma() {
		Firma firma = new Firma();
		firma.setId(ID);
		firma.setRazaoSocial(RAZAO_SOCIAL);
		firma.setCnpj(CNPJ);
		return firma;
	}

}
