package registraponto.api.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

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

import registraponto.api.entities.Firma;
import registraponto.api.repositories.FirmaRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FirmaServiceTest {

	@MockBean
	private FirmaRepository firmaRepository;

	@Autowired
	private FirmaService firmaService;

	private static final String CNPJ = "51463645000100";

	@Before
	public void setUp() throws Exception {
		BDDMockito.given(this.firmaRepository.findByCnpj(Mockito.anyString())).willReturn(new Firma());
		BDDMockito.given(this.firmaRepository.save(Mockito.any(Firma.class))).willReturn(new Firma());
	}

	@Test
	public void testGetFirmaPorCnpj() {
		Optional<Firma> firma = this.firmaService.getByCnpj(CNPJ);

		assertTrue(firma.isPresent());
	}
	
	@Test
	public void testPersistirFirma() {
		Firma firma = this.firmaService.persistir(new Firma());

		assertNotNull(firma);
	}

}
