public class OperacaoSaque extends Operacao {

    public OperacaoSaque(double valor) {
        super('s', valor);
    }

    @Override
    public double calcularTaxas() {
        return 0.05;
    }
}
