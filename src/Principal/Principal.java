package Principal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Principal {

    public static void main(String[] args) {

        // Cria o Array em que os livros serão colocados
        List<Livro> livroAcervo = new ArrayList<>();

        try {
            // Inicia o server na porta 4444 e aguarda pelo cliente
            ServerSocket servidor = new ServerSocket(4444);
            System.out.println("Servidor iniciado.");
            System.out.println("Aguardando cliente.");

            while (true) {
                // Aceita e faz conecção com o cliente
                Socket socket = servidor.accept();
                System.out.println("Cliente " + socket.getInetAddress().getHostAddress() + " conectado.");

                // Começa thread para lidar com cliente(s)
                ServerThread serverThread = new ServerThread(socket, livroAcervo);
                serverThread.start();

            }

        } catch (IOException e) {
            System.out.println("Exceção: " + e.getMessage());
        }
    }
}
