package Principal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
        try {
            // Fluxo de entrada e saída de dados 
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            while (true) {
                int escolha = in.readInt(); // Recebe o processo que o cliente escolheu (1-5)
                System.out.println("Processo escolhido: " + escolha);
                switch (escolha) {
                    case 1 -> {
                        // Adicionar
                        Livro livro = (Livro) in.readObject(); // Recebe o livro do cliente
                        livroAcervo.add(livro); // Adiciona livro ao array
                        out.writeObject(livro); // Envia o livro de volta para o cliente
                        out.flush();
                        break;
                    }
                    case 3 -> {
                        // Remover
                        Livro livroRemover = (Livro) in.readObject();
                        int id = in.readInt(); // Recebe o ID do livro  que será deletado
                        boolean removido = Livro.remover(livroAcervo, id);
                        livroAcervo.remove(livroRemover);
                        if (removido) {
                            out.writeObject(removido);
                        } else {
                            out.writeObject(null);
                        }
                        break;
                    }
                    case 4 -> {
                        // Consultar
                        int id = in.readInt(); // Recebe o ID do livro que será consultado
                        Livro livroConsultado = Livro.consultarId(livroAcervo, id); // Pesquisa o livro no array pelo ID
                        out.writeObject(livroConsultado); // Envia o livro consultado para o cliente
                        out.flush();
                        break;
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

    }

}
