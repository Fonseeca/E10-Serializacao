public class OperacaoDeposito extends Operacao {

    public OperacaoDeposito(double valor) {
        super('d', valor);
    }

    @Override
    public double calcularTaxas() {
        return 0f;
    }
}
