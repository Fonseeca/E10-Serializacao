import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.ClassNotFoundException;
import java.nio.file.FileAlreadyExistsException;

public abstract class Conta implements ITaxas, Serializable {

    private static final long serialVersionUID = 1L;

    private static int numero;

    private Cliente dono;

    private double saldo;

    protected double limite;

    private List<Operacao> operacoes;

    private static int totalContas = 0;

    public Conta(int numero, Cliente dono, double saldo, double limite) {
        this.numero = numero;
        this.dono = dono;
        this.saldo = saldo;
        this.limite = limite;

        this.operacoes = new ArrayList<>();

        Conta.totalContas++;
    }

    public boolean sacar(double valor) {
        if (valor >= 0 && valor <= this.limite) {
            this.saldo -= valor;
            this.operacoes.add(new OperacaoSaque(valor));
            return true;
        }

        return false;
    }

    public void depositar(double valor) throws ArithmeticException {
        if (valor < 0)
            throw new ArithmeticException("Erro. Valor negativo depositado.");

        this.saldo += valor;
        this.operacoes.add(new OperacaoDeposito(valor));
    }

    public boolean transferir(Conta destino, double valor) {
        boolean valorSacado = this.sacar(valor);
        if (valorSacado) {
            destino.depositar(valor);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return dono.toString() + '\n' +
                "---\n" +
                "numero=" + numero + '\n' +
                "saldo=" + saldo + '\n' +
                "limite=" + limite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o instanceof Conta) {
            Conta conta = (Conta) o;
            return numero == conta.numero;
        }
        return false;
    }

    public void imprimirExtrato(int modo) {
        // realiza a cópia para não perder a ordem original da lista
        List<Operacao> operacoesParaExtrato = new ArrayList<>(this.operacoes);

        // Ordena de modo == 1, caso contrário mantém ordem original
        if (modo == 1) {
            Collections.sort(operacoesParaExtrato);
        }

        System.out.println("======= Extrato Conta " + this.numero + "======");
        for(Operacao atual : operacoesParaExtrato) {
            System.out.println(atual);
        }
        System.out.println("====================");
    }

    public void imprimirExtratoTaxas() {
        System.out.println("=== Extrato de Taxas ===");
        System.out.printf("Manutenção:\t%.2f\n", this.calcularTaxas());

        double totalTaxas = this.calcularTaxas();
        for (Operacao atual : this.operacoes) {
            totalTaxas += atual.calcularTaxas();
            System.out.printf("%c:\t%.2f\n", atual.getTipo(), atual.calcularTaxas());
        }

        System.out.printf("Total:\t%.2f\n", totalTaxas);
    }

    public static int getNumero() {
        return numero;
    }

    public Cliente getDono() {
        return dono;
    }

    public double getSaldo() {
        return saldo;
    }

    public double getLimite() {
        return limite;
    }

    public static int getTotalContas() {
        return Conta.totalContas;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setDono(Cliente dono) {
        this.dono = dono;
    }

    public abstract void setLimite(double limite);

    public static void escreveFile(){
        String nomeFile = String.valueOf(Conta.getNumero()) + String.valueOf(Conta.getTotalContas() + ".ser");

        FileInputStream fileIn = new FileInputStream(nomeFile);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        Conta conta = (Conta) in.readObject();
        in.close();
        fileIn.close();
    }

    public static void leFile(int numero, int totalContas){
        try{
            String nomeFile = String.valueOf(numero + totalContas + ".ser");

            FileOutputStream fileOut = new FileOutputStream(nomeFile);
            ObjectOutputStream Out = new ObjectOutputStream(fileOut);
            Conta conta = (Conta) Out.writeObject();
            out.close();
            fileOut.close();
        }catch(FileNotFoundException e){
            System.out.println("Erro ao ler arquivo");
        }
        
    }
}
