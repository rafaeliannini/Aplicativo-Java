import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.List;

class Perguntas{
    private int id;
    private String pergunta;
    private String resposta;
    private Boolean acertou;

    public Perguntas() {
    }

    public Perguntas(int id, String pergunta, String resposta, Boolean acertou) {
        this.id = id;
        this.pergunta = pergunta;
        this.resposta = resposta;
        this.acertou = acertou;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }
    public void setResposta(String resposta) {
        this.resposta = resposta;
    }
    public void setAcertou(Boolean acertou) {
        this.acertou = acertou;
    }

    public int getId() {
        return id;
    }
    public String getPergunta() {
        return pergunta;
    }
    public String getResposta() {
        return resposta;
    }
    public Boolean getAcertou() {
        return acertou;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Pergunta: " + pergunta + ", Resposta: " + resposta + ", Acertou: " + acertou;
    }
}

public class Principal {
    public static List<Perguntas> lerPerguntasCSV(String caminhoArquivo) {
        List<Perguntas> perguntas = new ArrayList<>();
        String linha;
    
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            // Lê e descarta o cabeçalho
            br.readLine();
    
            // Itera pelas linhas do arquivo
            while ((linha = br.readLine()) != null) {
                List<String> campos = parseLinhaCSV(linha);
    
                // Trata os campos do CSV
                int id = Integer.parseInt(campos.get(0).trim());
                String pergunta = campos.get(1).trim();
                String resposta = campos.get(2).trim();
                Boolean acertou = false;
    
                perguntas.add(new Perguntas(id, pergunta, resposta, acertou));
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo CSV: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Erro ao converter valores numéricos: " + e.getMessage());
        }
    
        return perguntas;
    }
    
    // Método para analisar uma linha CSV
    private static List<String> parseLinhaCSV(String linha) {
        List<String> campos = new ArrayList<>();
        StringBuilder campoAtual = new StringBuilder();
        boolean dentroAspas = false;
    
        for (char c : linha.toCharArray()) {
            if (c == '\"') {
                dentroAspas = !dentroAspas; // Alterna o estado das aspas
            } else if (c == ',' && !dentroAspas) {
                // Adiciona o campo atual à lista e reinicia o builder
                campos.add(campoAtual.toString());
                campoAtual.setLength(0);
            } else {
                campoAtual.append(c);
            }
        }
        campos.add(campoAtual.toString()); // Adiciona o último campo
    
        return campos;
    }    

    public static void main(String[] args) {
        // Wrappers para variáveis mutáveis
        final int[] quantidade = {0};
        final int[] numInicio = {0};
        final int[] numFinal = {0};
        final int[][] numExcluidos = {{}}; // Array para números excluídos
        final int[][] numerosSorteados = {{}};

        String nomeArquivo = "questionario.csv";
        List<Perguntas> listaPerguntas = lerPerguntasCSV(nomeArquivo);

        // Criação do JFrame principal
        JFrame frame = new JFrame("Sorteador de Números");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(740, 493);
        frame.setLayout(null);

        // Calculando o centro do frame
        int centerX = frame.getWidth() / 2;
        int centerY = frame.getHeight() / 2;

        // Adicionando um painel com imagem de fundo
        JLabel background = new JLabel(new ImageIcon("fundo.jpg"));
        background.setBounds(0, 0, frame.getWidth(), frame.getHeight());
        frame.setContentPane(background);
        frame.setLayout(null); // Definindo layout manual para posicionamento

        // Texto para "Escolha a Quantidade de Números"
        JLabel labelQuantidade = new JLabel("Escolha a Quantidade de Números:");
        labelQuantidade.setBounds(centerX - 150, centerY - 210, 300, 20);
        frame.add(labelQuantidade);

        // Campo de texto com botão ao lado para quantidade
        JTextField inputField = new JTextField();
        inputField.setBounds(centerX - 150, centerY - 180, 200, 30);
        JButton inputButton = new JButton("Enviar");
        inputButton.setBounds(centerX + 60, centerY - 180, 80, 30);

        // Adicionando ação ao botão "Enviar"
        inputButton.addActionListener(e -> {
            String quantidadeNumeros = inputField.getText();
            try {
                quantidade[0] = Integer.parseInt(quantidadeNumeros);
                JOptionPane.showMessageDialog(frame, "Quantidade definida: " + quantidade[0]);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Por favor, insira um número válido!");
            }
        });

        // Texto para "Sorteie de [x] a [y]"
        JLabel labelIntervalo = new JLabel("Sorteie de");
        labelIntervalo.setBounds(centerX - 150, centerY - 150, 100, 20);
        frame.add(labelIntervalo);

        // Duas caixas de texto para intervalo
        JTextField textBox1 = new JTextField();
        textBox1.setBounds(centerX - 150, centerY - 130, 80, 30);
        JLabel labelA = new JLabel("a");
        labelA.setBounds(centerX - 55, centerY - 125, 20, 20);
        frame.add(labelA);

        JTextField textBox2 = new JTextField();
        textBox2.setBounds(centerX - 30, centerY - 130, 80, 30);
        JButton readButton = new JButton("Definir Intervalo");
        readButton.setBounds(centerX + 60, centerY - 130, 150, 30);

        // Adicionando ação ao botão "Definir Intervalo"
        readButton.addActionListener(e -> {
            try {
                numInicio[0] = Integer.parseInt(textBox1.getText());
                numFinal[0] = Integer.parseInt(textBox2.getText());
                JOptionPane.showMessageDialog(frame, "Intervalo definido: " + numInicio[0] + " a " + numFinal[0]);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Por favor, insira números válidos!");
            }
        });

        // Texto para "Números a Serem Desconsiderados"
        JLabel labelExcluidos = new JLabel("Números a Serem Desconsiderados:");
        labelExcluidos.setBounds(centerX - 150, centerY - 90, 300, 20);
        frame.add(labelExcluidos);

        // Campo de texto para números a excluir
        JTextField inputField2 = new JTextField();
        inputField2.setBounds(centerX - 150, centerY - 60, 200, 30);
        JButton inputButton2 = new JButton("Excluir Números");
        inputButton2.setBounds(centerX + 60, centerY - 60, 150, 30);

        // Adicionando ação ao botão "Excluir Números"
        inputButton2.addActionListener(e -> {
            String excluirNumeros = inputField2.getText();
            try {
                // Split dos números e conversão para um array de inteiros
                numExcluidos[0] = Arrays.stream(excluirNumeros.split(" "))
                                        .mapToInt(Integer::parseInt)
                                        .toArray();
                JOptionPane.showMessageDialog(frame, "Números excluídos: " + Arrays.toString(numExcluidos[0]));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Por favor, insira números separados por espaços!");
            }
        });

        // Botão "Sortear Números"
        JButton sortearButton = new JButton("Sortear Números");
        sortearButton.setBounds(centerX - 75, centerY, 150, 30);
        frame.add(sortearButton);

        // Label para exibir os números sorteados
        JLabel resultadoLabel = new JLabel("");
        resultadoLabel.setBounds(centerX - 210, centerY-20, 550, 200);
        frame.add(resultadoLabel);

        // Botão para começar os teste
        JButton startButton = new JButton("Começar Teste");
        startButton.setBounds(centerX - 75, centerY+140, 150, 30);

        // Adicionando ação ao botão "Sortear Números"
        sortearButton.addActionListener(e -> {
            // Verifica se os parâmetros estão corretos
            if (quantidade[0] <= 0 || numFinal[0] <= numInicio[0]) {
                JOptionPane.showMessageDialog(frame, "Por favor, defina os parâmetros corretamente.");
                return;
            }
        
            // Cria um conjunto para armazenar os números excluídos
            Set<Integer> excluidosSet = new HashSet<>();
            for (int num : numExcluidos[0]) {
                excluidosSet.add(num);
            }
        
            // Calcula o número total de valores disponíveis no intervalo
            int intervaloDisponivel = (numFinal[0] - numInicio[0] + 1) - excluidosSet.size();
        
            // Verifica se há números suficientes no intervalo para atender à quantidade solicitada
            if (quantidade[0] > intervaloDisponivel) {
                JOptionPane.showMessageDialog(frame, 
                    "Intervalo insuficiente! Reduza a quantidade de números ou ajuste as exclusões.");
                return;
            }
        
            // Gera números aleatórios únicos
            List<Integer> numeros = new ArrayList<>();
            Random random = new Random();
        
            while (numeros.size() < quantidade[0]) {
                int num = random.nextInt(numFinal[0] - numInicio[0] + 1) + numInicio[0];
                if (!excluidosSet.contains(num) && !numeros.contains(num)) {
                    numeros.add(num);
                }
            }
        
            // Embaralha os números para garantir que a ordem seja aleatória
            Collections.shuffle(numeros);
        
            // Converte a lista embaralhada para array
            numerosSorteados[0] = numeros.stream().mapToInt(Integer::intValue).toArray();
        
            // Formata os números para exibição
            StringBuilder resultado = new StringBuilder("<html><div style='font-size: 12px;'> <b>Números sorteados:</b> ");
            int linhaAtual = 0;
        
            // Adiciona os primeiros 12 números ao lado do título
            for (int i = 0; i < Math.min(12, numerosSorteados[0].length); i++) {
                if (i > 0) resultado.append(", ");
                resultado.append(numerosSorteados[0][i]);
                linhaAtual++;
            }
        
            // Adiciona uma quebra de linha
            resultado.append("<br>");
        
            // Adiciona os demais números, 20 por linha
            for (int i = linhaAtual; i < numerosSorteados[0].length; i++) {
                if ((i - linhaAtual) % 20 == 0 && i != linhaAtual) {
                    resultado.append("<br>");
                } else if (i > linhaAtual) {
                    resultado.append(", ");
                }
                resultado.append(numerosSorteados[0][i]);
            }
        
            // Finaliza o texto HTML
            resultado.append("</html>");
        
            // Define o texto no label
            resultadoLabel.setText(resultado.toString());

            // Mostra o botão iniciar
            frame.add(startButton);
        });        

        startButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            final int[] indexPergunta = {0};
            final Runnable[] atualizarPergunta = new Runnable[1];
        
            // Criando os elementos visuais
            JLabel numeroLabel = new JLabel();
            numeroLabel.setBounds(centerX - 100, centerY - 200, 200, 30);
            numeroLabel.setHorizontalAlignment(SwingConstants.CENTER);
            frame.add(numeroLabel);
        
            JLabel perguntaLabel = new JLabel("", SwingConstants.CENTER);
            perguntaLabel.setBounds(centerX - 300, centerY - 150, 600, 80); // Espaço maior para pergunta
            perguntaLabel.setVerticalAlignment(SwingConstants.TOP);
            frame.add(perguntaLabel);
        
            JLabel respostaLabel = new JLabel("", SwingConstants.CENTER);
            respostaLabel.setBounds(centerX - 300, centerY-20, 600, 180); // Espaço maior para resposta
            respostaLabel.setVerticalAlignment(SwingConstants.TOP);
            respostaLabel.setVisible(false);
            frame.add(respostaLabel);
        
            JLabel contadorLabel = new JLabel(); // Contador de perguntas restantes
            contadorLabel.setBounds(centerX - 100, centerY - 250, 200, 30);
            contadorLabel.setHorizontalAlignment(SwingConstants.CENTER);
            frame.add(contadorLabel);
        
            JButton verRespostaButton = new JButton("Ver Resposta");
            verRespostaButton.setBounds(centerX - 75, centerY - 50, 150, 30);
            frame.add(verRespostaButton);
        
            JButton acerteiButton = new JButton("Acertei");
            JButton erreiButton = new JButton("Errei");
        
            // Função para atualizar as perguntas
            atualizarPergunta[0] = () -> {
                if (indexPergunta[0] >= numerosSorteados[0].length) {
                    // Quando todas as perguntas foram respondidas, aparece a tela de revisão
                    frame.getContentPane().removeAll();
        
                    JButton repetirErradasButton = new JButton("Repetir Erradas");
                    repetirErradasButton.setBounds(centerX - 100, centerY - 50, 200, 30);
                    frame.add(repetirErradasButton);
        
                    JButton voltarInicioButton = new JButton("Voltar ao Início");
                    voltarInicioButton.setBounds(centerX - 100, centerY + 50, 200, 30);
                    frame.add(voltarInicioButton);
        
                    // Ação para repetir as perguntas erradas
                    repetirErradasButton.addActionListener(re -> {
                        // Filtra as perguntas erradas
                        List<Integer> erradas = new ArrayList<>();
                        for (int num : numerosSorteados[0]) {
                            if (!listaPerguntas.get(num - 1).getAcertou()) {
                                erradas.add(num);
                            }
                        }
        
                        // Se houver perguntas erradas para repetir
                        if (!erradas.isEmpty()) {
                            // Atualiza os números sorteados com as perguntas erradas
                            numerosSorteados[0] = erradas.stream().mapToInt(i -> i).toArray();
                            indexPergunta[0] = 0;
        
                            // Limpa a tela e reutiliza os componentes
                            frame.getContentPane().removeAll();
        
                            numeroLabel.setText("");
                            perguntaLabel.setText("");
                            respostaLabel.setText("");
                            respostaLabel.setVisible(false);
        
                            frame.add(numeroLabel);
                            frame.add(perguntaLabel);
                            frame.add(respostaLabel);
                            frame.add(verRespostaButton);
                            frame.add(acerteiButton);
                            frame.add(erreiButton);
                            frame.add(contadorLabel);
        
                            // Atualiza a primeira pergunta errada
                            atualizarPergunta[0].run();
        
                        } else {
                            JOptionPane.showMessageDialog(frame, "Nenhuma pergunta errada para repetir!");
                        }
                    });
        
                    // Ação para voltar ao início
                    voltarInicioButton.addActionListener(re -> {
                        frame.dispose(); // Fecha a janela atual
                        main(new String[]{}); // Reinicia o programa
                    });
        
                    frame.repaint();
                    return;
                }
        
                // Atualiza a exibição da pergunta e resposta
                int numeroAtual = numerosSorteados[0][indexPergunta[0]];
                Perguntas perguntaAtual = listaPerguntas.get(numeroAtual - 1);
        
                // Atualiza o contador de perguntas restantes
                contadorLabel.setText("Perguntas restantes: " + (numerosSorteados[0].length - indexPergunta[0]));
        
                // Formatar pergunta
                StringBuilder perguntaBuilder = new StringBuilder("<html><div style='font-size: 14px;'>");
                perguntaBuilder.append("&#8226; ").append(perguntaAtual.getPergunta().replace("/", "<br>&#8226; "));
                perguntaBuilder.append("</div></html>");
                perguntaLabel.setText(perguntaBuilder.toString());
        
                // Formatar resposta
                StringBuilder respostaBuilder = new StringBuilder("<html><div style='font-size: 12px;'><b>Respostas:</b><br>");
                respostaBuilder.append("&#8226; ").append(perguntaAtual.getResposta().replace("/", "<br>&#8226; "));
                respostaBuilder.append("</div></html>");
                respostaLabel.setText(respostaBuilder.toString());
                respostaLabel.setVisible(false);
        
                numeroLabel.setText("Número: " + numeroAtual);
                frame.repaint();
            };
        
            // Ação para ver a resposta
            verRespostaButton.addActionListener(ev -> respostaLabel.setVisible(true));
        
            // Ação para marcar como "acertou"
            acerteiButton.addActionListener(ev -> {
                listaPerguntas.get(numerosSorteados[0][indexPergunta[0]] - 1).setAcertou(true);
                indexPergunta[0]++;
                atualizarPergunta[0].run();
            });
        
            // Ação para marcar como "errou"
            erreiButton.addActionListener(ev -> {
                listaPerguntas.get(numerosSorteados[0][indexPergunta[0]] - 1).setAcertou(false);
                indexPergunta[0]++;
                atualizarPergunta[0].run();
            });
        
            // Posicionamento dos botões
            acerteiButton.setBounds(centerX - 100, centerY + 150, 100, 30);
            erreiButton.setBounds(centerX + 10, centerY + 150, 100, 30);
            frame.add(acerteiButton);
            frame.add(erreiButton);
        
            // Atualiza a primeira pergunta
            atualizarPergunta[0].run();
        });                                 

        // Adicionando os componentes ao frame
        frame.add(inputField);
        frame.add(inputButton);
        frame.add(textBox1);
        frame.add(textBox2);
        frame.add(readButton);
        frame.add(inputField2);
        frame.add(inputButton2);

        frame.setResizable(false);
        frame.setVisible(true);
    }
}
