package registraponto.api.repositories;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import registraponto.api.entities.Colaborador;
import registraponto.api.entities.Firma;
import registraponto.api.enums.PerfilEnum;
import registraponto.api.utils.PasswordUtils;

import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ColaboradorRepositoryTest {

	@Autowired
	private ColaboradorRepository colaboradorRepository;

	@Autowired
	private FirmaRepository firmaRepository;

	private static final String EMAIL = "email@teste.com";
	private static final String CPF = "63389689338";

	@Before
	public void setUp() throws Exception {
		Firma firma = this.firmaRepository.save(getDadosFirma());
		this.colaboradorRepository.save(getDadosColaborador(firma));
	}

	@After
	public final void tearDown() {
		this.firmaRepository.deleteAll();
	}

	@Test
	public void testeGetColaboradorPorEmail() {
		Colaborador colaborador = this.colaboradorRepository.findByEmail(EMAIL);

		assertEquals(EMAIL, colaborador.getEmail());
	}

	@Test
	public void testGetColaboradorPorCpf() {
		Colaborador colaborador = this.colaboradorRepository.findByCpf(CPF);

		assertEquals(CPF, colaborador.getCpf());
	}

	@Test
	public void testeGetColaboradorPorEmailECpf() {
		Colaborador colaborador = this.colaboradorRepository.findByCpfOrEmail(CPF, EMAIL);

		assertNotNull(colaborador);
	}

	@Test
	public void testeGetColaboradorPorEmailOuCpfParaEmailInvalido() {
		Colaborador colaborador = this.colaboradorRepository.findByCpfOrEmail(CPF, "email@invalido.com");

		assertNotNull(colaborador);
	}

	@Test
	public void testeGetColaboradorPorEmailECpfParaCpfInvalido() {
		Colaborador colaborador = this.colaboradorRepository.findByCpfOrEmail("12345678901", EMAIL);

		assertNotNull(colaborador);
	}

	private Colaborador getDadosColaborador(Firma firma) throws NoSuchAlgorithmException {
		Colaborador colaborador = new Colaborador();
		colaborador.setNome("Fulano de Tal");
		colaborador.setPerfil(PerfilEnum.ROLE_USUARIO);
		colaborador.setSenha(PasswordUtils.gerarBCrypt("123456"));
		colaborador.setCpf(CPF);
		colaborador.setEmail(EMAIL);
		colaborador.setFirma(firma);
		return colaborador;
	}

	private Firma getDadosFirma() {
		Firma firma = new Firma();
		firma.setRazaoSocial("Firma de exemplo");
		firma.setCnpj("51463645000100");
		return firma;
	}

}
