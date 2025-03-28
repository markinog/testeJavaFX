package org.financiai.JavaFxController;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.financiai.model.entities.Cliente;
import org.financiai.model.entities.Financiamento;
import org.financiai.model.entities.Imovel;
import org.financiai.model.entities.Parcelas;
import org.financiai.model.enums.TipoAmortizacao;
import org.financiai.services.Price;
import org.financiai.services.SAC;

import java.util.ArrayList;
import java.util.List;

public class ResultPageController {

    @FXML private TextArea tabelaParcelasArea;
    @FXML private Label SimulacaoResultLabel;
    private Cliente cliente;
    private Imovel imovel;
    private Financiamento financiamento;

    public void setDados(Cliente cliente, Imovel imovel, Financiamento financiamento) {
        this.cliente = cliente;
        this.imovel = imovel;
        this.financiamento = financiamento;
    }

    // Método alterado para não depender de ActionEvent
    public void calcularFinanciamento() {
        try {
            double taxaJurosMensal = financiamento.getTaxaJuros() / 12 / 100;
            double limiteParcela = cliente.getRendaMensal() * 0.3;

            List<Parcelas> listaParcelas = new ArrayList<>();

            if (financiamento.getTipoAmortizacao() == TipoAmortizacao.PRICE) {
                Price price = new Price();
                List<Double> parcelas = price.calculaParcela(financiamento.getValorFinanciado(), taxaJurosMensal, financiamento.getPrazo());
                List<Double> amortizacoes = price.calculaAmortizacao(financiamento.getValorFinanciado(), taxaJurosMensal, financiamento.getPrazo());

                for (int i = 0; i < parcelas.size(); i++) {
                    listaParcelas.add(new Parcelas(i + 1, parcelas.get(i), amortizacoes.get(i)));

                }

            } else if (financiamento.getTipoAmortizacao() == TipoAmortizacao.SAC) {
                SAC sac = new SAC();
                List<Double> parcelasSac = sac.calculaParcela(financiamento.getValorFinanciado(), taxaJurosMensal, financiamento.getPrazo());
                List<Double> amortizacaoSac = sac.calculaAmortizacao(financiamento.getValorFinanciado(), taxaJurosMensal, financiamento.getPrazo());

                for (int i = 0; i < parcelasSac.size(); i++) {
                    listaParcelas.add(new Parcelas(i + 1, parcelasSac.get(i), amortizacaoSac.get(i)));
                }
            }

            // Validação e exibição
            if (listaParcelas.isEmpty()) {
                tabelaParcelasArea.setText("Nenhum dado calculado.");
                return;
            }

            if (listaParcelas.get(0).getValorParcela() > limiteParcela) {
                tabelaParcelasArea.setText("Financiamento não aprovado! \nA parcela excede 30% da renda.");
                return;
            }

            StringBuilder sb = new StringBuilder("Parcela\tValor Parcela\tAmortização\n");
            for (Parcelas p : listaParcelas) {
                sb.append(String.format("%d\tR$ %.2f\tR$ %.2f%n",
                        p.getNumeroParcela(), p.getValorParcela(), p.getValorAmortizacao()));
            }

            tabelaParcelasArea.setText(sb.toString());
            SimulacaoResultLabel.setText("Financiamento Aprovado!");

        } catch (Exception e) {
            tabelaParcelasArea.setText("Erro ao calcular: " + e.getMessage());
        }
    }
}