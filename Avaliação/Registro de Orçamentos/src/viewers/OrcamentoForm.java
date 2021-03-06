package viewers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import java.awt.Color;

import controllers.OrcamentoProcess;
import models.Orcamento;

public class OrcamentoForm extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel painel;
	private JLabel id, fornecedor, produto, preco;
	private JTextField tfId, tfFornecedor, tfProduto, tfPreco;
	private JTable table;
	private DefaultTableModel tableModel;
	private JScrollPane rolagem;
	private JButton create, read, update, delete;
	private int autoId = Integer
			.parseInt(OrcamentoProcess.orcamentos.get(OrcamentoProcess.orcamentos.size() - 1).getId()) + 1;

	private final Locale BRASIL = new Locale("pt", "BR");
	private DecimalFormat df = new DecimalFormat("#.00");

	public OrcamentoForm() {

		setTitle("Cadastro de Produtos");
		setBounds(150, 180, 430, 390);
		painel = new JPanel();
		painel.setBackground(new Color(189, 150, 147));
		setContentPane(painel);
		setLayout(null);

		id = new JLabel("ID:");
		id.setBounds(15, 15, 100, 20);
		painel.add(id);
		tfId = new JTextField(String.format("%d", autoId));
		tfId.setEditable(false);
		tfId.setBounds(120, 15, 120, 20);
		painel.add(tfId);

		fornecedor = new JLabel("Fornecedor:");
		fornecedor.setBounds(15, 45, 100, 20);
		painel.add(fornecedor);
		tfFornecedor = new JTextField();
		tfFornecedor.setBounds(120, 45, 120, 20);
		painel.add(tfFornecedor);

		produto = new JLabel("Produto:");
		produto.setBounds(15, 75, 100, 20);
		painel.add(produto);
		tfProduto = new JTextField();
		tfProduto.setBounds(120, 75, 120, 20);
		painel.add(tfProduto);

		preco = new JLabel("Pre?o:");
		preco.setBounds(15, 105, 100, 20);
		painel.add(preco);
		tfPreco = new JTextField();
		tfPreco.setBounds(120, 105, 120, 20);
		painel.add(tfPreco);

		table = new JTable();
		tableModel = new DefaultTableModel();
		tableModel.addColumn("ID");
		tableModel.addColumn("Fornecedor");
		tableModel.addColumn("Produto");
		tableModel.addColumn("Pre?o");
		tableModel.addColumn("Mais Barato");
		if (OrcamentoProcess.orcamentos.size() != 0) {
			preencherTabela();
		}
		table = new JTable(tableModel);
		table.setEnabled(false);
		rolagem = new JScrollPane(table);
		rolagem.setBounds(5, 160, 400, 180);
		painel.add(rolagem);

		create = new JButton("Cadastrar");
		read = new JButton("Buscar");
		update = new JButton("Atualizar");
		delete = new JButton("Excluir");
		create.setBounds(280, 15, 100, 20);
		read.setBounds(280, 40, 100, 20);
		update.setBounds(280, 65, 100, 20);
		delete.setBounds(280, 90, 100, 20);
		update.setEnabled(false);
		delete.setEnabled(false);
		painel.add(create);
		painel.add(read);
		painel.add(update);
		painel.add(delete);

		delete.setEnabled(false);
		update.setEnabled(false);

		create.addActionListener(this);
		read.addActionListener(this);
		update.addActionListener(this);
		delete.addActionListener(this);

	}

	private void preencherTabela() {
		int totLinhas = tableModel.getRowCount();
		if (tableModel.getRowCount() > 0) {
			for (int i = 0; i < totLinhas; i++) {
				tableModel.removeRow(0);
			}
		}
		for (Orcamento o : OrcamentoProcess.orcamentos) {
			tableModel.addRow(new String[] { o.getId(), o.getFornecedor(), o.getProduto(), o.getPreco(), o.comprar() });
		}
	}

	private void cadastrar() {
		if (tfId.getText().length() != 0 && tfFornecedor.getText().length() != 0 && tfProduto.getText().length() != 0
				&& tfPreco.getText().length() != 0) {

			df.setCurrency(Currency.getInstance(BRASIL));
			double preco = 0;
			boolean comprar = true;
			int id = 0;
			try {
				preco = Double.parseDouble(df.parse(tfPreco.getText()).toString());
				id = Integer.parseInt(tfId.getText().toString());
			} catch (ParseException e) {
				System.out.println(e);
			}
			
			

			for (int i = 0; i < OrcamentoProcess.orcamentos.size(); i++) {
				if (OrcamentoProcess.orcamentos.get(i).getProduto().contains(tfProduto.getText())) {
					if (OrcamentoProcess.orcamentos.get(i).getPreco("a") > preco) {
						OrcamentoProcess.orcamentos.get(i).setMaisBarato(false);
						comprar = true;
					}
				} else {
					comprar = true;
				}

			}

			OrcamentoProcess.orcamentos
					.add(new Orcamento(id, tfFornecedor.getText(), tfProduto.getText(), preco, comprar));
			OrcamentoProcess.salvar();
			preencherTabela();
			limparCampos();
			tfId.setText(""+ (Integer.parseInt(OrcamentoProcess.orcamentos.get(OrcamentoProcess.orcamentos.size() - 1).getId()) + 1));
		} else {
			JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
		}
	}

	private void limparCampos() {
		tfFornecedor.setText(null);
		tfProduto.setText(null);
		tfPreco.setText(null);
	}

	private void alterar() {
		int id = Integer.parseInt(tfId.getText());
		boolean comprar = true;
		int indice = -1;
		for (Orcamento orcamento : OrcamentoProcess.orcamentos) {
			if (Integer.parseInt(orcamento.getId()) == id) {
				indice = OrcamentoProcess.orcamentos.indexOf(orcamento);
			}
		}
		if (tfId.getText().length() != 0 && tfFornecedor.getText().length() != 0 && tfProduto.getText().length() != 0
				&& tfPreco.getText().length() != 0) {

			df.setCurrency(Currency.getInstance(BRASIL));
			double preco = 0;
			try {
				preco = Double.parseDouble(df.parse(tfPreco.getText()).toString());
				id = Integer.parseInt(tfId.getText().toString());
			} catch (ParseException e) {
				System.out.println(e);
			}

			for (int i = 0; i < OrcamentoProcess.orcamentos.size(); i++) {
				if (OrcamentoProcess.orcamentos.get(i).getProduto().contains(tfProduto.getText())) {
					if (OrcamentoProcess.orcamentos.get(i).getPreco("a") > preco) {
						OrcamentoProcess.orcamentos.get(i).setMaisBarato(false);
						comprar = true;
					}
				} else {
					comprar = true;
				}

			}

			OrcamentoProcess.orcamentos.set(indice,
					new Orcamento(id, tfFornecedor.getText(), tfProduto.getText(), preco, comprar));

			limparCampos();
		} else {
			JOptionPane.showMessageDialog(this, "Favor preencher todos os campos.");
		}
		create.setEnabled(true);
		update.setEnabled(false);
		delete.setEnabled(false);
		tfId.setText(String.format("%d",
				Integer.parseInt(OrcamentoProcess.orcamentos.get(OrcamentoProcess.orcamentos.size() - 1).getId()) + 1));
		OrcamentoProcess.salvar();
		preencherTabela();
	}

	private void buscar() {
		String entrada = JOptionPane.showInputDialog(this, "Digite o ID do equipamento:");

		boolean isNumeric = true;
		if (entrada != null && !entrada.equals("")) {
			for (int i = 0; i < entrada.length(); i++) {
				if (!Character.isDigit(entrada.charAt(i))) {
					isNumeric = false;
				}
			}
		} else {
			isNumeric = false;
		}

		if (isNumeric) {
			int id = Integer.parseInt(entrada);
			for (Orcamento o : OrcamentoProcess.orcamentos) {
				if (id == Integer.parseInt(o.getId())) {
					tfId.setText("" + id);
					tfFornecedor.setText(o.getFornecedor());
					tfProduto.setText(o.getProduto());
					tfPreco.setText(o.getPreco());

					create.setEnabled(false);
					update.setEnabled(true);
					delete.setEnabled(true);
				}
			}
		}

	}

	private void excluir() {
		int id = Integer.parseInt(tfId.getText());
		int indice = -1;
		for (Orcamento orcamento : OrcamentoProcess.orcamentos) {
			if (Integer.parseInt(orcamento.getId()) == id) {
				indice = OrcamentoProcess.orcamentos.indexOf(orcamento);
			}
		}
		OrcamentoProcess.orcamentos.remove(indice);
		preencherTabela();
		limparCampos();
		create.setEnabled(true);
		update.setEnabled(false);
		delete.setEnabled(false);
		tfId.setText(String.format("%d",
				Integer.parseInt(OrcamentoProcess.orcamentos.get(OrcamentoProcess.orcamentos.size() - 1).getId()) + 1));
		OrcamentoProcess.salvar();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == create) {
			cadastrar();
		}
		if (e.getSource() == read) {
			buscar();
		}
		if (e.getSource() == update) {
			alterar();
		}
		if (e.getSource() == delete) {
			excluir();
		}
	}

	public static void main(String[] args) throws ParseException {

		OrcamentoProcess.abrir();
		OrcamentoForm tela = new OrcamentoForm();
		tela.setVisible(true);

	}

}