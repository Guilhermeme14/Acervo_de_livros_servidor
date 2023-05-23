package Principal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Principal {

    public static void main(String[] args) {
        try {
            ServerSocket servidor = new ServerSocket(4444);
            System.out.println("Servidor iniciado.");
            System.out.println("Aguardando cliente.");

            while (true) {
                Socket socket = servidor.accept();
                System.out.println("Cliente " + socket.getInetAddress().getHostAddress() + " conectado.");
                ObjectInputStream obj = new ObjectInputStream(socket.getInputStream());

                Livro livro = (Livro) obj.readObject();

                System.out.println("Livro");
                System.out.println("Título: " + livro.getTitulo());
                System.out.println("Autor: " + livro.getAutor());
                System.out.println("Editora: " + livro.getEditora());
                System.out.println("Ano de publicação: " + livro.getAno());
                System.out.println("Coleção: " + livro.getColecao());
                System.out.println("Assunto: " + livro.getAssunto());
                System.out.println("Sinopse: " + livro.getSinopse());
                System.out.println("Idioma: " + livro.getIdioma());
                System.out.println("Disponível: " + livro.isDisponivel());

            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
