package br.com.juanbailke.meu_gestor.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ScrapingService {

    public record MetaDados(String titulo, String descricao, String imagemCapa) {}

    public MetaDados extrair(String url){
        try{
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(5000)
                    .get();

            String titulo = doc.title();
            String imagem = doc.select("meta[property=og:image]").attr("content");
            String descricao = doc.select("meta[property=og:description]").attr("content");

            if(descricao.isEmpty())
                descricao = doc.select("meta[name=description]").attr("content");

            return new MetaDados(titulo, descricao, imagem);

        } catch (IOException e) {
            return new MetaDados("Site inacessível", "", "");
        }
    }
}
