package registraponto.api.repositories;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import registraponto.api.entities.Firma;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FirmaRepositoryTest {
	
	@Autowired
	private FirmaRepository firmaRepository;
	
	private static final String CNPJ = "51463645000100";

	@Before
	public void setUp() throws Exception {
		Firma firma = new Firma();
		firma.setRazaoSocial("Firma de exemplo");
		firma.setCnpj(CNPJ);
		this.firmaRepository.save(firma);
	}
	
	@After
    public final void tearDown() { 
		this.firmaRepository.deleteAll();
	}

	@Test
	public void testGetPorCnpj() {
		Firma firma = this.firmaRepository.findByCnpj(CNPJ);
		
		assertEquals(CNPJ, firma.getCnpj());
	}

}
