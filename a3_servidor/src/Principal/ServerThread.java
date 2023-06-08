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
                        boolean disponivel = in.readBoolean();
                        livro.setDisponivel(disponivel);
                        livroAcervo.add(livro);
                        out.writeObject(livro);
                        out.flush();
                        break;

                    case 2:
                        // Remover
                        int id = in.readInt();
                        boolean removed = remover(livroAcervo, id);
                        out.writeBoolean(removed);
                        out.flush();
                        break;

                    case 3:
                        // Consultar
                        int idConsulta = in.readInt();
                        Livro livroConsultado = Livro.consultarId(livroAcervo, idConsulta);
                        out.writeObject(livroConsultado);
                        out.flush();
                        break;

                    case 4:
                        // Sair
                        break;
                }

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
