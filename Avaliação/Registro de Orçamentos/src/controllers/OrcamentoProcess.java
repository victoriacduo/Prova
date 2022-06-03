package controllers;

import java.text.ParseException;
import java.util.ArrayList;

import dao.OrcamentoDAO;
import models.Orcamento;

public class OrcamentoProcess {

	public static ArrayList<Orcamento> orcamentos = new ArrayList<>();
	private static OrcamentoDAO od = new OrcamentoDAO();

	public static void abrir() throws ParseException {
		orcamentos = od.ler();
	}

	public static void salvar() {
		od.escrever(orcamentos);
	}
	
}