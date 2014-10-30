package com.example.franciscovictor.vendaslinguicao.library;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VendedorFunctions
{
    private JSONParser jsonParser;
    //URL of the PHP API
    private static String URL = "http://vendaslinguicao.besaba.com/";

    private static String cadastro_tag = "cadastrar";
    private static String login_tag = "login";
    private static String receber_dados_tag = "receber_dados";

    // constructor
    public VendedorFunctions()
    {
        jsonParser = new JSONParser();
    }

    /**
     * Function to Cadastrar
     */
    public JSONObject CadastrarVendedor(String senha, String cpf)
    {
        // Building Parameters
        List params = new ArrayList();
        params.add(new BasicNameValuePair("tag", cadastro_tag));
        params.add(new BasicNameValuePair("senha", senha));
        params.add(new BasicNameValuePair("cpf", cpf));
        JSONObject json = jsonParser.getJSONFromUrl(URL, params);
        return json;
    }

    public JSONObject LogarVendedor(String senha, String cpf)
    {
        // Building Parameters
        List params = new ArrayList();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("senha", senha));
        params.add(new BasicNameValuePair("cpf", cpf));
        JSONObject json = jsonParser.getJSONFromUrl(URL, params);
        return json;
    }

    public JSONObject ReceberInformacoesVendedor(String cpf)
    {
        // Building Parameters
        List params = new ArrayList();
        params.add(new BasicNameValuePair("tag", "receber_dados"));
        params.add(new BasicNameValuePair("cpf", cpf));
        JSONObject json = jsonParser.getJSONFromUrl(URL, params);
        return json;
    }

    public JSONObject AtualizarInformacoesVendedor(String cpf, String descricao, String status, String lati, String longi)
    {
        // Building Parameters
        List params = new ArrayList();
        params.add(new BasicNameValuePair("tag", "atualizar_dados"));
        params.add(new BasicNameValuePair("cpf", cpf));
        params.add(new BasicNameValuePair("descricao", descricao));
        params.add(new BasicNameValuePair("status", status));
        params.add(new BasicNameValuePair("latitude", lati));
        params.add(new BasicNameValuePair("longitude", longi));

        JSONObject json = jsonParser.getJSONFromUrl(URL, params);
        return json;
    }

    public JSONObject AtualizarLocalizacaoVendedor(String cpf, String lati, String longi)
    {
        // Building Parameters
        List params = new ArrayList();
        params.add(new BasicNameValuePair("tag", "atualizar_localizacao"));
        params.add(new BasicNameValuePair("cpf", cpf));
        params.add(new BasicNameValuePair("latitude", lati));
        params.add(new BasicNameValuePair("longitude", longi));

        JSONObject json = jsonParser.getJSONFromUrl(URL, params);
        return json;
    }

    public JSONObject SelecionaVendedoresDisponiveis()
    {
        List params = new ArrayList();
        params.add(new BasicNameValuePair("tag", "vendedores_disponiveis"));

        JSONObject json = jsonParser.getJSONFromUrl(URL, params);
        return json;
    }
}