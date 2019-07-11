import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import teste.teste.TesteApplication;
import teste.teste.model.*;

import static teste.teste.service.ApiServiceCaracters.processarApiCaracters;
import static teste.teste.service.ApiServiceComics.processarApiComics;
import static teste.teste.service.ApiServiceCreators.processarApiCreators;
import static teste.teste.service.ApiServiceEvents.processarApiEvents;
import static teste.teste.service.ApiServiceSeries.processarApiSeries;
import static teste.teste.service.ApiServiceStories.processarApiStories;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TesteApplication.class)
@ActiveProfiles("test")
public class TesteService {





    @Test
    public void validaRespostaNotNullCaracters() {
        TesteApiModelCaracters testeApiModelCaracters = processarApiCaracters();

        Assert.assertNotNull(testeApiModelCaracters);
    }

    @Test
    public void validaStatus200Caracters() {
        TesteApiModelCaracters testeApiModelCaracters =  processarApiCaracters();

        Assert.assertEquals(testeApiModelCaracters.getCode(), "200");
    }

    @Test
    public void validaRespostaNotNullComics() {
        TesteApiModelComics testeApiModelComics = processarApiComics();

        Assert.assertNotNull(testeApiModelComics);
    }

    @Test
    public void validaStatus200Comics() {
        TesteApiModelComics testeApiModelComics =  processarApiComics();

        Assert.assertEquals(testeApiModelComics.getCode(), "200");
    }

    @Test
    public void validaRespostaNotNullCreators() {
        TesteApiModelCreators testeApiModelCreators = processarApiCreators();

        Assert.assertNotNull(testeApiModelCreators);
    }

    @Test
    public void validaStatus200Creators() {
        TesteApiModelCreators testeApiModelCreators =  processarApiCreators();

        Assert.assertEquals(testeApiModelCreators.getCode(), "200");
    }


    @Test
    public void validaRespostaNotNullEvents() {
        TesteApiModelEvents testeApiModelEvents = processarApiEvents();

        Assert.assertNotNull(testeApiModelEvents);
    }

    @Test
    public void validaStatus200Events() {
        TesteApiModelEvents testeApiModelEvents =  processarApiEvents();

        Assert.assertEquals(testeApiModelEvents.getCode(), "200");
    }


    @Test
    public void validaRespostaNotNullSeries() {
        TesteApiModelSeries testeApiModelSeries = processarApiSeries();

        Assert.assertNotNull(testeApiModelSeries);
    }

    @Test
    public void validaStatus200Series() {
        TesteApiModelSeries testeApiModelSeries =  processarApiSeries();

        Assert.assertEquals(testeApiModelSeries.getCode(), "200");
    }


    @Test
    public void validaRespostaNotNullStories() {
        TesteApiModelStories testeApiModelStories = processarApiStories();

        Assert.assertNotNull(testeApiModelStories);
    }

    @Test
    public void validaStatus200Stories() {
        TesteApiModelStories testeApiModelStories =  processarApiStories();

        Assert.assertEquals(testeApiModelStories.getCode(), "200");
    }
/*
    @Test( dataProvider = "data-provider", dataProviderClass = DataProviderClass.class)
    public void testMethod(String data)
    {
        System.out.println("Data is: " + data);
    }
*/
    @Test
    public void validaStatusOkCaracters() {
        TesteApiModelCaracters testeApiModelCaracters =  processarApiCaracters();

        Assert.assertEquals(testeApiModelCaracters.getStatus(), "Ok");
    }
    
    [![Build Status](https://travis-ci.org/Leancb/RegistraPonto.svg?branch=master)](https://travis-ci.org/Leancb/RegistraPonto)
}
