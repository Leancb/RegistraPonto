package teste.teste.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import teste.teste.model.TesteApiModelCreators;
import teste.teste.utils.GsonUtils;

public class ApiServiceCreators {


    private static final Logger LOG = LoggerFactory.getLogger(ApiServiceCreators.class);
    private static final String MSG_EXCEPTION = "Exception :: ";

    public static TesteApiModelCreators processarApiCreators() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            TesteApiModelCreators testeApiModelCreators = GsonUtils.stringToObject(restTemplate.getForObject("https://gateway.marvel.com:443/v1/public/creators?ts=1&apikey=96c88fa24ca0e6c5fe37beb77459ddcd&hash=88fb09ca3e362e36e3f6dd10f8132ecd", String.class), TesteApiModelCreators.class);

            if (testeApiModelCreators.getStatus() != null) {
                LOG.info("Resposta recebida");
            } else {
                LOG.info("Um erro ocorreu");
                return null;
            }

            return testeApiModelCreators;
        } catch (Exception e) {
            LOG.error(MSG_EXCEPTION, e);
            throw e;
        }


    }
}