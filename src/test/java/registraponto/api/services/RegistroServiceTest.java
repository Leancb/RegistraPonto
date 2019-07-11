package registraponto.api.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import registraponto.api.entities.Registro;
import registraponto.api.repositories.RegistroRepository;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class RegistroServiceTest {

	@MockBean
	private RegistroRepository registroRepository;

	@Autowired
	private RegistroService registroService;

	@Before
	public void setUp() throws Exception {
		BDDMockito
				.given(this.registroRepository.findByColaboradorId(Mockito.anyLong(), Mockito.any(PageRequest.class)))
				.willReturn(new PageImpl<Registro>(new ArrayList<Registro>()));
		BDDMockito.given(this.registroRepository.getOne(Mockito.anyLong())).willReturn(new Registro());
		BDDMockito.given(this.registroRepository.save(Mockito.any(Registro.class))).willReturn(new Registro());
	}

	@Test
	public void testGetRegistroPorColaboradorId() {
		Page<Registro> registro = this.registroService.getByColaboradorId(1L, new PageRequest(0, 10));

		assertNotNull(registro);
	}

	@Test
	public void testGetRegistroPorId() {
		Optional<Registro> registro = this.registroService.getById(1L);

		assertTrue(registro.isPresent());
	}

	@Test
	public void testPersistirRegistro() {
		Registro registro = this.registroService.persistir(new Registro());

		assertNotNull(registro);
	}

}
