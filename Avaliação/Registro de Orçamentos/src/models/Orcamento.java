package models;

import java.util.Objects;

public class Orcamento {

	private int id;
	private String fornecedor;
	private String produto;
	private double preco;
	private boolean maisBarato;

	public Orcamento(int id, String fornecedor, String produto, double preco, boolean maisBarato) {
		this.id = id;
		this.fornecedor = fornecedor;
		this.produto = produto;
		this.preco = preco;
		this.maisBarato = maisBarato;
	}

	public Orcamento(String linha) {

		try {
			this.id = Integer.parseInt(linha.split(";")[0]);
			this.preco = Double.parseDouble(linha.split(";")[3]);
			this.maisBarato = Boolean.parseBoolean(linha.split(";")[4]);
		} catch (Exception e) {
			System.out.println(e);
		}

		this.fornecedor = linha.split(";")[1];
		this.produto = linha.split(";")[2];
	}

	public Orcamento(int id2) {
	}

	public String getId() {
		return String.valueOf(id);
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}

	public String getProduto() {
		return produto;
	}

	public void setProduto(String produto) {
		this.produto = produto;
	}

	public double getPreco(String a) {
		return preco;
	}

	public String getPreco() {
		return String.format("%.2f", preco);
	}

	public void setPreco(double preco) {
		this.preco = preco;
	}

	public boolean isMaisBarato() {
		return maisBarato;
	}

	public void setMaisBarato(boolean maisBarato) {
		this.maisBarato = maisBarato;
	}

	public String comprar() {
		if (maisBarato) {
			return "Baratíssimo";
		}
		return null;
	}

	@Override
	public int hashCode() {
		return Objects.hash(fornecedor, id, maisBarato, preco, produto);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Orcamento other = (Orcamento) obj;
		return Objects.equals(fornecedor, other.fornecedor) && id == other.id && maisBarato == other.maisBarato
				&& Double.doubleToLongBits(preco) == Double.doubleToLongBits(other.preco)
				&& Objects.equals(produto, other.produto);
	}

	@Override
	public String toString() {
		return id + "\t" + fornecedor + "\t" + produto + "\t" + preco + "\t" + maisBarato;
	}

	public String toCSV() {
		return id + ";" + fornecedor + ";" + produto + ";" + preco + ";" + comprar();
	}

}
