package registraponto.api.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import registraponto.api.entities.Colaborador;
import registraponto.api.repositories.ColaboradorRepository;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ColaboradorServiceTest {

	@MockBean
	private ColaboradorRepository colaboradorRepository;

	@Autowired
	private ColaboradorService colaboradorService;

	@Before
	public void setUp() throws Exception {
		BDDMockito.given(this.colaboradorRepository.save(Mockito.any(Colaborador.class))).willReturn(new Colaborador());
		BDDMockito.given(this.colaboradorRepository.getOne(Mockito.anyLong())).willReturn(new Colaborador());
		BDDMockito.given(this.colaboradorRepository.findByEmail(Mockito.anyString())).willReturn(new Colaborador());
		BDDMockito.given(this.colaboradorRepository.findByCpf(Mockito.anyString())).willReturn(new Colaborador());
	}

	@Test
	public void testPersistirColaborador() {
		Colaborador colaborador = this.colaboradorService.persistir(new Colaborador());

		assertNotNull(colaborador);
	}

	@Test
	public void testGetColaboradorPorId() {
		Optional<Colaborador> colaborador = this.colaboradorService.getById(1L);

		assertTrue(colaborador.isPresent());
	}

	@Test
	public void testeGetColaboradorPorEmail() {
		Optional<Colaborador> colaborador = this.colaboradorService.getByEmail("email@email.com");

		assertTrue(colaborador.isPresent());
	}

	@Test
	public void testGetColaboradorPorCpf() {
		Optional<Colaborador> colaborador = this.colaboradorService.getByCpf("24291173474");

		assertTrue(colaborador.isPresent());
	}

}
