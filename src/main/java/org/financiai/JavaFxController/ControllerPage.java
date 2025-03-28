    package org.financiai.JavaFxController;

    import javafx.event.ActionEvent;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.scene.control.*;
    import javafx.stage.Stage;
    import org.financiai.model.entities.Cliente;
    import org.financiai.model.entities.Financiamento;
    import org.financiai.model.entities.Imovel;
    import org.financiai.model.enums.TipoAmortizacao;
    import org.financiai.model.enums.TipoImovel;

    public class ControllerPage {

        @FXML
        private TextField nomeClienteField;
        @FXML
        private TextField cpfClienteField;
        @FXML
        private TextField rendaClienteField;
        @FXML
        private TextField valorImovelField;
        @FXML
        private TextField valorEntradaField;
        @FXML
        private TextField prazoField;
        @FXML
        private TextField taxaJurosField;
        @FXML
        private ChoiceBox<String> tipoFinanciamentoBox;
        @FXML
        private ChoiceBox<String> tipoImovelBox;
        @FXML
        private Label resultadoLabel;
        @FXML
        private TextArea tabelaParcelasArea;

        @FXML
        public void calcularFinanciamento(ActionEvent event) {
            try {
                // Obter dados do cliente e do financiamento
                String nomeCliente = nomeClienteField.getText();
                String cpfCliente = cpfClienteField.getText();
                double rendaMensal = Double.parseDouble(rendaClienteField.getText());
                double valorImovel = Double.parseDouble(valorImovelField.getText());
                double valorEntrada = Double.parseDouble(valorEntradaField.getText());
                double taxaJurosAnual = Double.parseDouble(taxaJurosField.getText());
                int prazo = Integer.parseInt(prazoField.getText());
                String tipoFinanciamento = tipoFinanciamentoBox.getValue();
                String tipoImovelString = tipoImovelBox.getValue();
                TipoImovel tipoImovel = TipoImovel.valueOf(tipoImovelString.toUpperCase());
                TipoAmortizacao tipoAmortizacao = TipoAmortizacao.valueOf(tipoFinanciamento.toUpperCase());

                Cliente cliente = new Cliente(nomeCliente, cpfCliente, rendaMensal);
                Imovel imovel = new Imovel(tipoImovel, valorImovel);
                double valorFinanciado = valorImovel - valorEntrada;
                Financiamento financiamento = new Financiamento(prazo, taxaJurosAnual, tipoAmortizacao, valorEntrada, valorFinanciado, 0);


                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/financiai/simulacaoView.fxml"));
                Parent root = loader.load();

                ResultPageController resultPageController = loader.getController();
                resultPageController.setDados(cliente, imovel, financiamento);
                resultPageController.calcularFinanciamento(); // Chamar o cálculo

                // Abrir a nova janela
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Simulação de Financiamento");
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
                resultadoLabel.setText("Erro: " + e.getMessage());
            }
        }
    }