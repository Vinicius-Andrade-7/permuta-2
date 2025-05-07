
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Desafio 1 - Interpreter Pattern
interface Expressao {

    String interpretar();
}

class CaixaAltaExpressao implements Expressao {

    private final String texto;

    public CaixaAltaExpressao(String texto) {
        this.texto = texto;
    }

    @Override
    public String interpretar() {
        StringBuilder resultado = new StringBuilder();
        for (char c : texto.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                resultado.append((char) (c - 32));
            } else {
                resultado.append(c);
            }
        }
        return resultado.toString();
    }
}

class RepetirExpressao implements Expressao {

    private final int n;
    private final String texto;

    public RepetirExpressao(int n, String texto) {
        this.n = n;
        this.texto = texto;
    }

    @Override
    public String interpretar() {
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < n; i++) {
            resultado.append(texto);
        }
        return resultado.toString();
    }
}

class ComandoInterpreter {

    public String interpretarComando(String comando) {
        if (comando.startsWith("caixa_alta(")) {
            Pattern pattern = Pattern.compile("caixa_alta\\(\"([^\"]*)\"\\)");
            Matcher matcher = pattern.matcher(comando);
            if (matcher.find()) {
                return new CaixaAltaExpressao(matcher.group(1)).interpretar();
            }
        } else if (comando.startsWith("repetir(")) {
            Pattern pattern = Pattern.compile("repetir\\((\\d+), \"([^\"]*)\"\\)");
            Matcher matcher = pattern.matcher(comando);
            if (matcher.find()) {
                int n = Integer.parseInt(matcher.group(1));
                return new RepetirExpressao(n, matcher.group(2)).interpretar();
            }
        }
        return "Comando inválido.";
    }
}

// Desafio 2 - Factory Method Pattern
interface Mensagem {

    String formatar(String nome);
}

class MensagemBoasVindas implements Mensagem {

    @Override
    public String formatar(String nome) {
        return "Bem-vindo, " + nome + "!";
    }
}

class MensagemDespedida implements Mensagem {

    @Override
    public String formatar(String nome) {
        return "Até logo, " + nome + ".";
    }
}

class MensagemAgradecimento implements Mensagem {

    @Override
    public String formatar(String nome) {
        return "Obrigado, " + nome + "!";
    }
}

class MensagemFactory {

    public static Mensagem criar(String tipo) {
        switch (tipo.toLowerCase()) {
            case "boasvindas":
                return new MensagemBoasVindas();
            case "despedida":
                return new MensagemDespedida();
            case "agradecimento":
                return new MensagemAgradecimento();
            default:
                throw new IllegalArgumentException("Tipo de mensagem desconhecido: " + tipo);
        }
    }
}

// Desafio 3 - Adapter Pattern
class SistemaDescontoExterno {

    public double aplicarDescontoPercentual(double valor, double percentual) {
        return valor - (valor * percentual / 100);
    }
}

interface CalculadoraDesconto {

    double calcular(double valor, double percentual);
}

class DescontoAdapter implements CalculadoraDesconto {

    private SistemaDescontoExterno sistemaExterno;

    public DescontoAdapter(SistemaDescontoExterno sistemaExterno) {
        this.sistemaExterno = sistemaExterno;
    }

    @Override
    public double calcular(double valor, double percentual) {
        return sistemaExterno.aplicarDescontoPercentual(valor, percentual);
    }
}

public class Main {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int escolha;
            
            do {
                System.out.println("\nEscolha o desafio para executar:");
                System.out.println("1 - Editor de Texto Simples (Interpreter)");
                System.out.println("2 - Gerador de Mensagens (Factory Method)");
                System.out.println("3 - Sistema de Desconto Externo (Adapter)");
                System.out.println("0 - Sair");
                System.out.print("Digite sua escolha: ");
                escolha = scanner.nextInt();
                scanner.nextLine(); // Consumir a quebra de linha
                
                switch (escolha) {
                    case 1:
                        executarDesafio1(scanner);
                        break;
                    case 2:
                        executarDesafio2(scanner);
                        break;
                    case 3:
                        executarDesafio3(scanner);
                        break;
                    case 0:
                        System.out.println("Saindo...");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } while (escolha != 0);
        }
    }

    private static void executarDesafio1(Scanner scanner) {
        System.out.println("\n--- Desafio 1: Editor de Texto Simples (Interpreter) ---");
        System.out.print("Digite o comando: ");
        String comando = scanner.nextLine();
        ComandoInterpreter interpreter = new ComandoInterpreter();
        String resultado = interpreter.interpretarComando(comando);
        System.out.println("Saída: " + resultado);
    }

    private static void executarDesafio2(Scanner scanner) {
        System.out.println("\n--- Desafio 2: Gerador de Mensagens (Factory Method) ---");
        System.out.print("Digite o tipo de mensagem (boasvindas, despedida, agradecimento): ");
        String tipoMensagem = scanner.nextLine();
        System.out.print("Digite o nome: ");
        String nome = scanner.nextLine();

        try {
            Mensagem mensagem = MensagemFactory.criar(tipoMensagem);
            System.out.println(mensagem.formatar(nome));
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void executarDesafio3(Scanner scanner) {
        System.out.println("\n--- Desafio 3: Sistema de Desconto Externo (Adapter) ---");
        System.out.print("Digite o valor: ");
        double valor = scanner.nextDouble();
        System.out.print("Digite o percentual de desconto: ");
        double percentual = scanner.nextDouble();

        SistemaDescontoExterno sistemaExterno = new SistemaDescontoExterno();
        CalculadoraDesconto adaptador = new DescontoAdapter(sistemaExterno);
        double valorComDesconto = adaptador.calcular(valor, percentual);

        System.out.printf("Valor com desconto: R$%.2f\n", valorComDesconto);
    }
}
