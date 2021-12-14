import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;


public class ClientHandler implements Runnable {
    private Server server;
    private Socket socket;
    DataInputStream in;
    DataOutputStream out;
    private String nick;
    private String login;
    private static final Logger logger = Logger.getLogger(ClientHandler.class.getName());


    public String getLogin() {
        return login;
    }

    public String getNick() {
        return nick;
    }

    public ClientHandler(Server server, Socket socket) {

        try {
            this.server = server;
            this.socket = socket;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            logger.setLevel(Level.ALL);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {

            socket.setSoTimeout(120000);
            while (true) {
                String str = in.readUTF();
                if (str.equals("/end")) {
                    sendMsg("/end");
                    logger.log(Level.INFO,"Отключаемся");
                    throw new RuntimeException("отключаемся");

                }
                if (str.startsWith("/reg ")) {
                    String[] token = str.split(" ");
                    boolean b = server.getAuthService()
                            .registration(token[1], token[2], token[3]);
                    if (!b) {
                        sendMsg("Ошибка: с этим логином уже Зарегистированы.");
                    } else {
                        sendMsg("Регистрация прошла успешно.");
                    }
                }

                if (str.startsWith("/auth ")) {
                    String[] token = str.split(" ");
                    String newNick = server.getAuthService()
                            .getNicknameByLoginAndPassword(token[1], token[2]);
                    if (newNick != null) {
                        if (!server.isLoginAuthorized(token[1])) {
                            login = token[1];
                            sendMsg("/authok " + newNick);
                            nick = newNick;
                            server.subscribe(this);

                            logger.log(Level.INFO,"Клиент " + nick + " подключился");
                            socket.setSoTimeout(0);
                            break;
                        } else {
                            sendMsg("С этим логином уже авторизовались");
                        }
                    } else {
                        sendMsg("Неверный логин / пароль");
                    }
                }

            }

            while (true) {
                String str = in.readUTF();
                if (str.startsWith("/")) {
                    if (str.equals("/end")) {
                        sendMsg("/end");
                        break;
                    }
                    if (str.startsWith("/w ")) {
                        String[] token = str.split(" ", 3);
                        server.privateMsg(this, token[1], token[2]);
                    }

                    if (str.startsWith("/chnick ")) {
                        String[] token = str.split(" ", 2);
                        if (token[1].contains(" ")) {
                            sendMsg("Ник не может содержать пробелов");
                            continue;
                        }
                        if (server.getAuthService().changeNick(this.nick, token[1])) {
                            sendMsg("/yournickis " + token[1]);
                            sendMsg("Ваш ник изменен на " + token[1]);
                            this.nick = token[1];
                            server.broadcastClientlist();
                        } else {
                            sendMsg("Не удалось изменить ник. Ник " + token[1] + " уже существует");
                        }
                    }
                } else {
                    server.broadcastMsg(nick, str);
                }
            }
        } catch (RuntimeException e) {
            logger.log(Level.INFO,"Bye");
        } catch (SocketTimeoutException e) {
            sendMsg("/end");
            logger.log(Level.INFO,"Bye time out");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            server.unsubscribe(this);
            logger.log(Level.INFO,"Клиент отключился");
        }

    }
}
