import java.util.Date;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner entrada = new Scanner(System.in);

        Cliente joao = new PessoaFisica("João", "Av. Antonio Carlos, 6627",
                                        new Date(), "111.111.111-11", 36, 'm');
        Cliente lojinha = new PessoaJuridica("Loja R$1,99", "Av. Afonso Pena, 3000",
                                        new Date(), "000.00000.0000/0001", 25, "Comércio");


        Conta cc = new ContaCorrente(1234, joao, 0, 1500);
        Conta cp = new ContaPoupanca(12121, lojinha, 10000, 1500);

        boolean correto = false;
        while(!correto) {
            System.out.println("Digite um valor para deposito");
            double valor = entrada.nextDouble();

            try {
                cc.depositar(valor);
                correto = true;
            } catch (ArithmeticException exception) {
                System.out.println("Erro na realização do depósito");
                System.out.println(exception.getMessage());
            }
        }

    }
}