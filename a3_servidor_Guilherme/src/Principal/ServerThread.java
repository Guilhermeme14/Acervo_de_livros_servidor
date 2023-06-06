package Principal;

import static Principal.Livro.remover;
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
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            while (true) {
                int escolha = in.readInt();
                switch (escolha) {
                    case 1:
                        // Adicionar
                        Livro livro = (Livro) in.readObject();
                        boolean disponivel = in.readBoolean(); // Lê o status de disponibilidade do livro
                        livro.setDisponivel(disponivel); // Define o status de disponibilidade no objeto Livro
                        livroAcervo.add(livro);
                        out.writeObject(livro);
                        out.flush();
                        break;

                    case 2:
                        // Atualizar
                        Livro livroAtualizado = (Livro) in.readObject();
                        boolean atualizado = Livro.foiAtualizado(livroAcervo, livroAtualizado);
                        if (atualizado) {
                            out.writeObject(livroAtualizado);
                        } else {
                            out.writeObject(null);
                        }
                        out.reset(); // Reinicializa o fluxo de saída
                        out.flush(); // Limpa o buffer e garante o envio dos dados

                        break;

                    case 3:
                        // Remover
                        int id = in.readInt();
                        boolean removed = remover(livroAcervo, id);
                        out.writeBoolean(removed);
                        out.flush();
                        break;

                    case 4:
                        // Consultar
                        int idConsulta = in.readInt();
                        Livro livroConsultado = Livro.consultarId(livroAcervo, idConsulta);
                        out.writeObject(livroConsultado);
                        out.flush();
                        break;

                    case 5:
                        // Sair
                        break;
                }

                if (escolha == 5) {
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
