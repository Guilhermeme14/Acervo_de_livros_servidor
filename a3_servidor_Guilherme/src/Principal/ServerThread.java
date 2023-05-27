package Principal;

import static Principal.Livro.remover;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;

public class ServerThread extends Thread {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private List<Livro> livroAcervo;

    public ServerThread(Socket socket, List<Livro> livroAcervo) {
        this.socket = socket;
        this.livroAcervo = livroAcervo;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Fluxo de entrada e saída de dados 
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                while (true) {
                    // Recebe o processo que o cliente escolheu (1-5)
                    int escolha = in.readInt();
                    switch (escolha) {
                        case 1 -> {
                            // Adicionar
                            Livro livro = (Livro) in.readObject(); // Recebe o livro do cliente
                            livroAcervo.add(livro); // Adiciona livro ao array
                            out.writeObject(livro); // Envia o livro de volta para o cliente
                            out.flush();

                        }
                        case 2 -> {
                            // Atualizar
                            Livro livroAtualizado = (Livro) in.readObject(); // Recebe as informações atualizadas do livro
                            boolean atualizado = Livro.foiAtualizado(livroAcervo, livroAtualizado);
                            if (atualizado) {
                                out.writeObject(livroAtualizado); // Envia o livro atualizado
                            } else {
                                out.writeObject(null);
                            }
                            out.flush();

                        }
                        case 3 -> {
                            // Remover
                            int id = in.readInt();
                            boolean removed = remover(livroAcervo, id);
                            out.writeBoolean(removed); // Indica se o livro foi removido com sucesso
                            out.flush();
                        }
                        case 4 -> {
                            // Consultar
                            int id = in.readInt(); // Recebe o ID do livro que será consultado
                            Livro livroConsultado = Livro.consultarId(livroAcervo, id); // Pesquisa o livro no array pelo ID
                            out.writeObject(livroConsultado); // Envia o livro consultado para o cliente
                            out.flush();

                        }
                        case 5 -> {
                            // Sair
                            break;
                        }
                    }
                }

            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Exceção: " + e.getMessage());
            }
            try {
                // Fecha os fluxos de entrada e saída de dados
                out.close();
                in.close();
                socket.close();
            } catch (IOException e) {
                System.out.println("Exceção ao fechar os fluxos: " + e.getMessage());
            }
        }
    }
}    
