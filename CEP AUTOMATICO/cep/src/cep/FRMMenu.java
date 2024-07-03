package cep;

import java.awt.EventQueue;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.SystemColor;
import java.net.URL;
import java.util.Iterator;
import java.awt.Cursor;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FRMMenu extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtCep;
	private JTextField txtEndereco;
	private JTextField txtBairro;
	private JTextField txtCidade;
	private JComboBox cbxUf;
	private JLabel lblStatus;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FRMMenu frame = new FRMMenu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FRMMenu() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblCep = new JLabel("CEP");
		lblCep.setBounds(32, 29, 43, 14);
		contentPane.add(lblCep);
		
		txtCep = new JTextField();
		txtCep.setBounds(69, 26, 130, 20);
		contentPane.add(txtCep);
		txtCep.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Endereço");
		lblNewLabel.setBounds(10, 69, 65, 14);
		contentPane.add(lblNewLabel);
		
		txtEndereco = new JTextField();
		txtEndereco.setBounds(69, 66, 250, 20);
		contentPane.add(txtEndereco);
		txtEndereco.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Bairro");
		lblNewLabel_1.setBounds(20, 109, 36, 14);
		contentPane.add(lblNewLabel_1);
		
		txtBairro = new JTextField();
		txtBairro.setBounds(69, 106, 250, 20);
		contentPane.add(txtBairro);
		txtBairro.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Cidade");
		lblNewLabel_2.setBounds(20, 150, 43, 14);
		contentPane.add(lblNewLabel_2);
		
		txtCidade = new JTextField();
		txtCidade.setBounds(69, 147, 250, 20);
		contentPane.add(txtCidade);
		txtCidade.setColumns(10);
		
		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buscarCep();
			}
		});
		btnBuscar.setBounds(335, 25, 89, 23);
		contentPane.add(btnBuscar);
		
		JButton btnLimpar = new JButton("Limpar");
		btnLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				limpar();
			}
		});
		btnLimpar.setBounds(68, 195, 89, 23);
		contentPane.add(btnLimpar);
		
		lblStatus = new JLabel("");
		lblStatus.setIcon(new ImageIcon(FRMMenu.class.getResource("/imagens/check.png")));
		lblStatus.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblStatus.setBorder(null);
		lblStatus.setBackground(SystemColor.control);
		lblStatus.setBounds(224, 11, 65, 32);
		contentPane.add(lblStatus);
		
		JLabel lblNewLabel_3 = new JLabel("UF");
		lblNewLabel_3.setBounds(329, 150, 36, 14);
		contentPane.add(lblNewLabel_3);
		
		cbxUf = new JComboBox();
		cbxUf.setModel(new DefaultComboBoxModel(new String[] {"AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"}));
		cbxUf.setBounds(353, 146, 71, 22);
		contentPane.add(cbxUf);
	}
	public void buscarCep() {
        String cep = txtCep.getText().trim();
        if (cep.isEmpty() || cep.length() != 8 || !cep.matches("\\d{8}")) {
            JOptionPane.showMessageDialog(null, "CEP inválido! Informe um CEP válido com 8 dígitos.", "Erro", JOptionPane.ERROR_MESSAGE);
            limparCampos();
            return;
        }

        try {
            URL url = new URL("http://cep.republicavirtual.com.br/web_cep.php?cep=" + cep + "&formato=xml");
            SAXReader xml = new SAXReader();
            Document documento = xml.read(url);
            Element root = documento.getRootElement();
            
            String resultado = null;
            String logradouro = "";
            String tipoLogradouro = "";

            for (Iterator<Element> it = root.elementIterator(); it.hasNext();) {
                Element element = it.next();
                String nomeElemento = element.getQualifiedName();
                String valorElemento = element.getText();

                switch (nomeElemento) {
                    case "cidade":
                        txtCidade.setText(valorElemento);
                        break;
                    case "bairro":
                        txtBairro.setText(valorElemento);
                        break;
                    case "uf":
                        cbxUf.setSelectedItem(valorElemento);
                        break;
                    case "tipoLogradouro":
                        tipoLogradouro = valorElemento;
                        break;
                    case "logradouro":
                        logradouro = valorElemento;
                        break;
                    case "resultado":
                        resultado = valorElemento;
                        break;
                }
            }

            if (resultado != null && resultado.equals("0")) {
                JOptionPane.showMessageDialog(null, "CEP não encontrado.", "Aviso", JOptionPane.WARNING_MESSAGE);
                limparCampos();
            } else {
                txtEndereco.setText(tipoLogradouro + " " + logradouro);
                lblStatus.setIcon(new ImageIcon(getClass().getResource("/imagens/check.png")));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar o CEP: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void limpar() {
        limparCampos();
        lblStatus.setIcon(null);
    }

    private void limparCampos() {
        txtCep.setText("");
        txtEndereco.setText("");
        txtBairro.setText("");
        txtCidade.setText("");
        cbxUf.setSelectedIndex(0);
        txtCep.requestFocus();
    }
}
