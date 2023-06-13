package Principal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ServerThread extends Thread {

    private final Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private final List<Livro> livroAcervo;

    public ServerThread(Socket socket, List<Livro> livroAcervo) {
        this.socket = socket;
        this.livroAcervo = livroAcervo;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            while (true) {
                int escolha = in.readInt();
                System.out.println("Processo escolhido: " + escolha);
                switch (escolha) {
                    case 1 -> {
                        // Adicionar
                        Livro livro = (Livro) in.readObject();
                        boolean disponivel = in.readBoolean();
                        livro.setDisponivel(disponivel);
                        livroAcervo.add(livro);
                        out.writeObject(livro);
                        out.flush();
                    }

                    case 2 -> {
                        // Remover
                        int id = in.readInt();
                        boolean removed = Livro.remover(livroAcervo, id);
                        out.writeBoolean(removed);
                        out.flush();
                    }

                    case 3 -> {
                        // Consultar
                        int idConsulta = in.readInt();
                        if (in.available() > 0) {
                            Livro livroConsultado = (Livro) in.readObject();
                            out.writeObject(livroConsultado);
                            out.flush();
                        } else {
                            // Não há objetos disponíveis para leitura
                            out.writeObject(null);
                            out.flush();
                        }
                        break;
                    }

                    case 4 -> {
                    }
                }
                // Sair

                if (escolha == 4) {
                    // Fecha os fluxos de entrada e saída de dados
                    out.close();
                    in.close();
                    socket.close();
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Exceção: " + e.getMessage());
        }
    }

}
